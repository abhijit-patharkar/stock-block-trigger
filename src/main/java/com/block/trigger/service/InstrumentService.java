package com.block.trigger.service;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;

import com.block.trigger.model.Instr;
import com.rainmatter.kitehttp.exceptions.KiteException;

/**
 * Interface for all operations related to instruments
 * 
 * @author sachin
 */
public interface InstrumentService {

	/**
	 * Pull instruments from kite
	 * 
	 * @throws KiteException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void pull() throws JSONException, IOException, KiteException;
	
	/**
	 * Based on the data added in the DB for instruments we initialize the Avg Per Min Map per instrument
	 * 
	 */
	public void initAvgPerMinMap();
	
	/**
	 * Based on the data added in the DB for instrument we initialize the Trigger Value Map per instrument
	 * 
	 */
	public void initTriggerValueMap();
	
	/**
	 * Initialize instrument token map
	 * 
	 */
	public void initInstrumentTokenMap();
	
	/**
	 * Init all in memory maps required for calculation
	 * 
	 */
	public void initAllInstrumentData();
	
	/**
	 * Fetch instrument trigger value map
	 * 
	 * @return
	 */
	public Map<Long, Double> getTriggerValueMap();
	
	/**
	 * Fetch avg per min map for instrument
	 * 
	 * @return
	 */
	public Map<Long, Double> getAvgPerMinMap();
	
	/**
	 * Get instrument token map per instrument
	 * 
	 * @return
	 */
	public Map<Long, Instr> getInstrumentTokenMap();

	/**
	 * Open web socket for all the instruments
	 * 
	 */
	public void subscribe();
}
