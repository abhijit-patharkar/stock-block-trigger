package com.block.trigger.service;

import com.block.trigger.model.Trigger;

public interface TriggerService {
	
	/**
	 * Create a trigger point
	 * 
	 * @param instrumentToken
	 * @param lastTradedQuantity
	 */
	public String create(String instrumentToken, double lastTradedQuantity);
	
	/**
	 * Fetch trigger by instrument token
	 * 
	 * @param instrumentToken
	 * @return
	 */
	//public Trigger fetchByInstrumentToken(String instrumentToken);
	
	
}
