package com.block.trigger.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

/**
 * 
 * Model class for Instrument
 */
public class Instr {

	@Id
	private String id;

	private String exchange;

	private long exchangeToken;

	private String expiry;

	private long instrumentToken;

	private String instrumentType;

	private double lastPrice;

	private int lotSize;

	private String name;

	private String segment;

	private String strike;

	private double tickSize;

	private String tradingsymbol;

	private double previousClose;

	private long previousVolume;

	private double previousVolumeAverage;

	private double avgPerMin;

	private double triggerValue;

	private List<PreviousVolumeHistory> previousVolumeHistory;

	public double getAvgPerMin() {
		return avgPerMin;
	}

	public void setAvgPerMin(double avgPerMin) {
		this.avgPerMin = avgPerMin;
	}

	public double getTriggerValue() {
		return triggerValue;
	}

	public void setTriggerValue(double triggerValue) {
		this.triggerValue = triggerValue;
	}

	public List<PreviousVolumeHistory> getPreviousVolumeHistory() {
		if (previousVolumeHistory == null) {
			previousVolumeHistory = new ArrayList<PreviousVolumeHistory>();
		}
		return previousVolumeHistory;
	}

	public void setPreviousVolumeHistory(List<PreviousVolumeHistory> previousVolumeHistory) {
		this.previousVolumeHistory = previousVolumeHistory;
	}

	// Getter and setter methods
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public long getExchangeToken() {
		return exchangeToken;
	}

	public void setExchangeToken(long exchangeToken) {
		this.exchangeToken = exchangeToken;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	public long getInstrumentToken() {
		return instrumentToken;
	}

	public void setInstrumentToken(long instrumentToken) {
		this.instrumentToken = instrumentToken;
	}

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

	public double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public int getLotSize() {
		return lotSize;
	}

	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public double getPreviousVolumeAverage() {
		return previousVolumeAverage;
	}

	public void setPreviousVolumeAverage(double previousVolumeAverage) {
		this.previousVolumeAverage = previousVolumeAverage;
	}

	public String getStrike() {
		return strike;
	}

	public void setStrike(String strike) {
		this.strike = strike;
	}

	public double getTickSize() {
		return tickSize;
	}

	public void setTickSize(double tickSize) {
		this.tickSize = tickSize;
	}

	public double getPreviousClose() {
		return previousClose;
	}

	public void setPreviousClose(double close) {
		this.previousClose = close;
	}

	public String getTradingsymbol() {
		return tradingsymbol;
	}

	public void setTradingsymbol(String tradingsymbol) {
		this.tradingsymbol = tradingsymbol;
	}

	public long getPreviousVolume() {
		return previousVolume;
	}

	public void setPreviousVolume(long previousVolume) {
		this.previousVolume = previousVolume;
	}
}
