package com.sheet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sheet.domain.Protein;

@RestController
public class SalesController {

	@GetMapping("/sales")
	public Protein sales() {
		return new Protein("Frango", 150, 10);
	}
}
