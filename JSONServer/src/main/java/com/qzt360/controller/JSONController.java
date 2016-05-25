package com.qzt360.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class JSONController {
	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "this is json server.";
	}

	@RequestMapping("/ss")
	@ResponseBody
	ResponseEntity<?> getSS() {
		Map<String, String> listKeys = new HashMap<String, String>();
		for (int i = 0; i < 100; i++) {
			listKeys.put("key" + i, "value" + i);
		}
		String json = listKeys.toString();
		return new ResponseEntity<String>(json, HttpStatus.OK);
	}

	public static void main(String[] args) {
		SpringApplication.run(JSONController.class, args);
	}
}
