package com.block.trigger.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.block.trigger.service.KiteService;
import com.rainmatter.kitehttp.exceptions.KiteException;

@RestController
@RequestMapping(value = "/login")
public class LoginController {
	
	// Logger
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private KiteService kiteService;
	
	@GetMapping(value = "")
	public void login(HttpServletResponse response) throws IOException {
		logger.info("Login api..");
		String url = kiteService.getLoginUrl();
		response.sendRedirect(url);
	}
	
	@GetMapping(value = "/callback")
	public Map<String, Boolean> kiteCallback(@RequestParam(value = "request_token") String requestToken) throws JSONException, KiteException {
		logger.info("Login callback api with request token : "+requestToken);
		boolean loggedIn = kiteService.login(requestToken);
		Map<String, Boolean> map = new HashMap<>();
		map.put("LoggedIn", loggedIn);
		return map;
	}
}
