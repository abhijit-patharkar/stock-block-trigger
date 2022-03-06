package com.block.trigger.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.block.trigger.dao.InstrumentRepository;
import com.block.trigger.model.Instr;
import com.block.trigger.model.PreviousVolumeHistory;
import com.neovisionaries.ws.client.WebSocketException;
import com.rainmatter.kitehttp.exceptions.KiteException;
import com.rainmatter.models.Instrument;
import com.rainmatter.models.OHLC;
import com.rainmatter.models.Quote;

/**
 * InstrumentServiceImpl class to implement all services related to
 * InstrumentService
 * 
 * @author sachin
 */
@Service
public class InstrumentServiceImpl implements InstrumentService {

	// Logger
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private KiteService kiteService;

	@Autowired
	private InstrumentRepository instrumentRepository;

	private Map<Long, Double> instrumentAvgPerMin = new HashMap<Long, Double>();

	private Map<Long, Double> instrumentTriggerPoint = new HashMap<Long, Double>();

	private Map<Long, Instr> instrumentTokenMap = new HashMap<Long, Instr>();

	@PostConstruct
	public void InstrumentServiceImplPostConstruct() {
		this.initAllInstrumentData();
	}

	/**
	 * Pull instruments from kite
	 * 
	 * @throws KiteException
	 * @throws IOException
	 * @throws JSONException
	 */
	@Override
	@Scheduled(cron = "0 1 * * * ?")
	public void pull() {

		logger.info("Sync Instrument Job Started at: " + new Date());
		// Getting list of instruments from Kite
		List<Instrument> instruments = new ArrayList<Instrument>();

		try {
			instruments = kiteService.fetchInstrumentList();
		} catch (KiteException ex) {
			logger.error("Kite Exception in fetching the instrument list", ex);
		} catch (IOException iex) {
			logger.error("IO Exception in fetching the instrument list", iex);
		}

		if (instruments != null) {
			logger.info("Total Instruments size: " + instruments.size());
			instruments = instruments.subList(0, 20);

			for (Instrument instrument : instruments) {
				logger.info("Updating Instrument: " + instrument.getName());
				String tradingSymbol = instrument.getTradingsymbol();
				if (tradingSymbol.contains("-")) {

					tradingSymbol = tradingSymbol.substring(0, tradingSymbol.indexOf("-"));
				}

				Instr instr = new Instr();
				instr.setExchange(instrument.getExchange());
				instr.setExchangeToken(instrument.getExchange_token());
				instr.setExpiry(instrument.getExpiry());
				instr.setInstrumentToken(instrument.getInstrument_token());
				instr.setInstrumentType(instrument.getInstrument_type());
				instr.setLastPrice(instrument.getLast_price());
				instr.setLotSize(instrument.getLot_size());
				instr.setName(instrument.getName());
				instr.setSegment(instrument.getSegment());
				instr.setStrike(instrument.getStrike());
				instr.setTickSize(instrument.getTick_size());
				instr.setTradingsymbol(tradingSymbol);

				try {
					if (instr.getExchange() != null && instr.getTradingsymbol() != null) {
						Thread.sleep(1000);
						Quote quote = kiteService.getQuote(instr.getExchange().trim(), tradingSymbol.trim());

						if (quote != null) {
							List<PreviousVolumeHistory> volumeHistory = instr.getPreviousVolumeHistory();

							PreviousVolumeHistory previousVolumeHistory = new PreviousVolumeHistory();

							if (volumeHistory.size() < 10) {
								previousVolumeHistory.setVolume(Long.parseLong(quote.volume));
								previousVolumeHistory.setCreatedDate(new Date());
								volumeHistory.add(previousVolumeHistory);
							} else {
								if (volumeHistory.get(volumeHistory.size() - 1).getVolume() != Long
										.parseLong(quote.volume)) {
									volumeHistory.remove(0);
									previousVolumeHistory.setVolume(Long.parseLong(quote.volume));
									previousVolumeHistory.setCreatedDate(new Date());
									volumeHistory.add(previousVolumeHistory);
								}
							}

							double dayAvg = 0;
							double sum = 0;

							for (PreviousVolumeHistory vol : volumeHistory) {
								sum += vol.getVolume();
							}
							dayAvg = sum / volumeHistory.size();

							double avgPerMin;
							double triggerValue;
							avgPerMin = dayAvg / 1440;
							triggerValue = avgPerMin * 40;

							instr.setPreviousVolumeAverage(dayAvg);
							instr.setPreviousVolumeHistory(volumeHistory);
							instr.setAvgPerMin(avgPerMin);
							instr.setTriggerValue(triggerValue);
							instr.setPreviousVolume(Long.parseLong(quote.volume));

							logger.info("Day Average: " + dayAvg + ", Average Per Min: " + avgPerMin
									+ ", Trigger Value: " + triggerValue);

							OHLC ohlc = quote.ohlc;
							if (ohlc != null) {
								instr.setPreviousClose(ohlc.close);
							}
						}
					}
				} catch (KiteException ex) {
					logger.error("Could not set the previous day close for instrument name: " + instrument.getName(),
							ex);
				} catch (JSONException jex) {
					logger.error("Could not set the previous day close for instrument name: " + instrument.getName(),
							jex);
				} catch (Exception e) {
					logger.error("Could not set the previous day close for instrument name: " + instrument.getName(),
							e);
				}

				// Check if we have existing instrument with the InstrumentToken
				Instr existingInstr = instrumentRepository.findByInstrumentToken(instr.getInstrumentToken());
				if (existingInstr == null) {
					instrumentRepository.insert(instr);
				} else {
					existingInstr.setExpiry(instr.getExpiry());
					existingInstr.setLastPrice(instr.getLastPrice());
					existingInstr.setLotSize(instr.getLotSize());
					existingInstr.setStrike(instr.getStrike());
					existingInstr.setTickSize(instr.getTickSize());
					existingInstr.setPreviousClose(instr.getPreviousClose());
					existingInstr.setPreviousVolume(instr.getPreviousVolume());
					existingInstr.setTradingsymbol(instr.getTradingsymbol());
					existingInstr.setAvgPerMin(instr.getAvgPerMin());
					existingInstr.setTriggerValue(instr.getTriggerValue());
					existingInstr.setPreviousVolumeHistory(instr.getPreviousVolumeHistory());
					existingInstr.setPreviousVolumeAverage(instr.getPreviousVolumeAverage());
					existingInstr.setAvgPerMin(instr.getAvgPerMin());
					existingInstr.setTriggerValue(instr.getTriggerValue());
					existingInstr.setPreviousVolume(instr.getPreviousVolume());
					instrumentRepository.save(existingInstr);
				}
			}
		}
		logger.info("Sync Instrument Job Completed at: " + new Date());

		// Re-initialize the instrument in memory maps
		this.initAllInstrumentData();
	}

	@Override
	public void subscribe() {
		try {
			logger.info("Listening job for all instruments started (This job will be running continuously)");
			List<Instr> instrList = instrumentRepository.findAll();
			ArrayList<Long> instrumentTokenList = new ArrayList<Long>();
			for (Instr instr : instrList) {
				instrumentTokenList.add(instr.getInstrumentToken());
			}
			kiteService.subscribe(instrumentTokenList);
		} catch (WebSocketException wex) {
			logger.error("Web socket exception", wex);
		} catch (IOException iex) {
			logger.error("IO exception", iex);
		}
	}

	@Override
	public void initAvgPerMinMap() {
		logger.info("Updating the Avg Per Min Map for instruments");

		// Fetch all instruments
		List<Instr> instrList = instrumentRepository.findAll();
		if (instrList != null) {
			for (Instr instr : instrList) {
				instrumentAvgPerMin.put(instr.getInstrumentToken(), instr.getAvgPerMin());
			}
		}
	}

	@Override
	public void initTriggerValueMap() {
		logger.info("Updating the Trigger Value Map for instruments");

		// Fetch all instruments
		List<Instr> instrList = instrumentRepository.findAll();
		if (instrList != null) {
			for (Instr instr : instrList) {
				instrumentTriggerPoint.put(instr.getInstrumentToken(), instr.getTriggerValue());
			}
		}
	}

	@Override
	public void initInstrumentTokenMap() {
		logger.info("Updating the Instrument Token Map");

		// Fetch all instruments
		List<Instr> instrList = instrumentRepository.findAll();
		if (instrList != null) {
			for (Instr instr : instrList) {
				instrumentTokenMap.put(instr.getInstrumentToken(), instr);
			}
		}
	}

	@Override
	public void initAllInstrumentData() {
		logger.info("Initializing all instrument data");

		this.initInstrumentTokenMap();
		this.initTriggerValueMap();
		this.initAvgPerMinMap();

	}

	public Map<Long, Double> getTriggerValueMap() {
		return instrumentTriggerPoint;
	}

	public Map<Long, Double> getAvgPerMinMap() {
		return instrumentAvgPerMin;
	}

	public Map<Long, Instr> getInstrumentTokenMap() {
		return instrumentTokenMap;
	}
}
