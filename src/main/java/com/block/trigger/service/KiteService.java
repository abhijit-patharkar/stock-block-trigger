package com.block.trigger.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.neovisionaries.ws.client.WebSocketException;
import com.rainmatter.kitehttp.exceptions.KiteException;
import com.rainmatter.models.Instrument;
import com.rainmatter.models.Quote;

/**
 * Interface for all operations related to kite
 * 
 * @author sachin
 */
public interface KiteService {

	/**
	 * Get login url from kite
	 */
	public String getLoginUrl();

	/**
	 * login
	 * 
	 * @param requestToken
	 * @throws KiteException
	 * @throws JSONException
	 */
	public boolean login(String requestToken) throws JSONException, KiteException;

	/**
	 * Fetch instruments from kite
	 * 
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 * @throws KiteException
	 */
	public List<Instrument> fetchInstrumentList() throws JSONException, IOException, KiteException;

	/**
	 * Get market quotes
	 * 
	 * @param exchange
	 * @param tradingSymbol
	 * @throws KiteException
	 * @throws JSONException
	 * @throws Exception 
	 */
	public Quote getQuote(String exchange, String tradingSymbol) throws JSONException, KiteException, Exception;

	/**
	 * Subscribing to web socket (All instruments)
	 * 
	 */
	public void subscribe(ArrayList<Long> tokenList) throws IOException, WebSocketException;
}
