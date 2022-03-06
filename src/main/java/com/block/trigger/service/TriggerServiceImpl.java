package com.block.trigger.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.block.trigger.model.Trigger;

@Service
public class TriggerServiceImpl implements TriggerService {

	// Logger
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String create(String instrumentToken, double lastTradedQuantity) {
		logger.info("Create a trigger point for instrumentToken: " + instrumentToken + ", and lastTradedQuantity: "
				+ lastTradedQuantity);
		return null;
	}

}
