package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class HealthController {

	private final JdbcTemplate jdbc;

	public HealthController(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@GetMapping("/api/health")
	public Map<String, String> health() {
		Map<String, String> result = new LinkedHashMap<>();
		result.put("api", "UP");
		try {
			jdbc.queryForObject("SELECT 1", Integer.class);
			result.put("database", "UP");
		} catch (Exception e) {
			result.put("database", "DOWN");
		}
		return result;
	}
}