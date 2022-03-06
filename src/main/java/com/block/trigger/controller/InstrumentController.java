package com.block.trigger.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.block.trigger.service.InstrumentService;
import com.rainmatter.kitehttp.exceptions.KiteException;

@RestController
@RequestMapping(value = "/instrument")
public class InstrumentController {

	// Logger
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private InstrumentService instrumentService;

	@GetMapping(value = "")
	public void sync(HttpServletResponse response) throws KiteException, IOException {
		instrumentService.pull();
	}

	@GetMapping(value = "/subscribe/all")
	public void subscribeAll() {
		instrumentService.subscribe();
	}

}
