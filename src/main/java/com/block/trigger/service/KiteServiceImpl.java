package com.block.trigger.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.neovisionaries.ws.client.WebSocketException;
import com.rainmatter.kiteconnect.KiteConnect;
import com.rainmatter.kitehttp.SessionExpiryHook;
import com.rainmatter.kitehttp.exceptions.KiteException;
import com.rainmatter.models.IndicesQuote;
import com.rainmatter.models.Instrument;
import com.rainmatter.models.Quote;
import com.rainmatter.models.Tick;
import com.rainmatter.models.UserModel;
import com.rainmatter.ticker.KiteTicker;
import com.rainmatter.ticker.OnConnect;
import com.rainmatter.ticker.OnDisconnect;
import com.rainmatter.ticker.OnTick;

/**
 * KiteServiceImpl class to implement all services related to KiteService
 * 
 * @author sachin
 */
@Service
public class KiteServiceImpl implements KiteService {

	// Logger
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${kite.api.key}")
	private String kiteApiKey;

	@Value("${kite.api.secret}")
	private String kiteApiSecret;

	@Value("${kite.api.userId}")
	private String kiteApiUserId;
	
	@Autowired
	private InstrumentService instrumentService;

	private KiteConnect kiteSdk = null;

	@PostConstruct
	public void initIt() throws Exception {

		logger.info("Kite Connect object initialized..");

		// Initialize Kiteconnect using apiKey.
		kiteSdk = new KiteConnect(kiteApiKey);

		// Set userId.
		kiteSdk.setUserId(kiteApiUserId);
	}

	/**
	 * Get login url from kite
	 */
	public String getLoginUrl() {

		logger.info("Get login Url");
		String url = kiteSdk.getLoginUrl();
		return url;
	}

	/**
	 * login
	 * 
	 * @throws KiteException
	 * @throws JSONException
	 */
	public boolean login(String requestToken) throws JSONException, KiteException {
		logger.info("Login into kite with requestToken: " + requestToken + " and kiteApiSecret: " + kiteApiSecret);

		boolean loggedIn = false;

		// Getting accessToken
		UserModel userModel = kiteSdk.requestAccessToken(requestToken, kiteApiSecret);

		logger.info("Login into kite successful with accessToken: " + userModel.accessToken + " and publicToken: "
				+ userModel.publicToken);

		// Set request token and public token which are obtained from login process.
		kiteSdk.setAccessToken(userModel.accessToken);
		kiteSdk.setPublicToken(userModel.publicToken);

		if (userModel != null) {
			loggedIn = true;
		}
		// Set session expiry callback.
		kiteSdk.registerHook(new SessionExpiryHook() {
			@Override
			public void sessionExpired() {
				logger.info("session expired");
			}
		});
		
		// Start instrument token subscription
		instrumentService.subscribe();
		
		return loggedIn;
	}

	/**
	 * Fetch instruments from kite
	 * 
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 * @throws KiteException
	 */
	@Override
	public List<Instrument> fetchInstrumentList() throws JSONException, IOException, KiteException {

		logger.info("Fetching all NSE instruments from kite..");
		List<Instrument> instruments = kiteSdk.getInstruments("NSE");
		return instruments;
	}

	/**
	 * Get market quotes
	 * 
	 * @param exchange
	 * @param tradingSymbol
	 * @throws KiteException
	 * @throws JSONException
	 */
	@Override
	public Quote getQuote(String exchange, String tradingSymbol) throws JSONException, KiteException,Exception{

		logger.info("Getting market quotes from kite for exchange: " + exchange + " and tradingSymbol: " + tradingSymbol);
		Quote quote = kiteSdk.getQuote(exchange, tradingSymbol);
		
		return quote;
	}

	@Override
	public void subscribe(ArrayList<Long> tokenList) throws IOException, WebSocketException {

		KiteTicker tickerProvider = new KiteTicker(kiteSdk);
		tickerProvider.setOnConnectedListener(new OnConnect() {

			@Override
			public void onConnected() {
				try {
					tickerProvider.subscribe(tokenList);
				} catch (KiteException ex) {
					logger.error("Kite Exception", ex);
				} catch (WebSocketException wEx) {
					logger.error("Web Socket Exception", wEx);
				} catch (IOException iEx) {
					logger.error("IO Exception Exception", iEx);
				}
			}
		});

		tickerProvider.setOnDisconnectedListener(new OnDisconnect() {
			@Override
			public void onDisconnected() {
				// your code goes here
			}
		});

		tickerProvider.setOnTickerArrivalListener(new OnTick() {

			@Override
			public void onTick(ArrayList<Tick> ticks) {
				//System.out.println(ticks.size());
				//logger.info("Ticks Size: " + ticks.size());
				for (Tick tick : ticks) {
					long instrumentToken = tick.getToken();
					double lastTradedQuantity = tick.getLastTradedQuantity();
					if(lastTradedQuantity >= instrumentService.getTriggerValueMap().get(instrumentToken)) {
						logger.info("Triggered for instrumentToken: " + tick.getToken() + " with last traded quantity: " + tick.getLastTradedQuantity());
						// Create a trigger point entry in the DB
						
					}
					
				}
			}
		});

		// Connects to ticker server for getting live quotes.
		tickerProvider.connect();

		// Disconnect from ticker server.
		// tickerProvider.disconnect();

	}
}
