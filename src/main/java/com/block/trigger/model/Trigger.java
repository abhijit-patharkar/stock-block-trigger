package com.block.trigger.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class Trigger {
	
	@Id
	private String id;
	
	private long instrumentToken;
	
	private String tradingSymbol;
	
	private Date createdDate;
	
	private Date updatedDate;
	
	private double lastTradedQuantityWhenTriggered;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getInstrumentToken() {
		return instrumentToken;
	}

	public void setInstrumentToken(long instrumentToken) {
		this.instrumentToken = instrumentToken;
	}

	public String getTradingSymbol() {
		return tradingSymbol;
	}

	public void setTradingSymbol(String tradingSymbol) {
		this.tradingSymbol = tradingSymbol;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public double getLastTradedQuantityWhenTriggered() {
		return lastTradedQuantityWhenTriggered;
	}

	public void setLastTradedQuantityWhenTriggered(double lastTradedQuantityWhenTriggered) {
		this.lastTradedQuantityWhenTriggered = lastTradedQuantityWhenTriggered;
	}
	
	
	
}
