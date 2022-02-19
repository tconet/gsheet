package com.sheet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sheet.domain.Protein;

@RestController
public class SalesController {

	@GetMapping("/vendas")
	public Protein sales(
			@RequestParam("proteina") String protein,
			@RequestParam("gramatura") int gram) {
		System.out.println(protein);
		System.out.println(gram);
		return new Protein("Frango", 150, 10);
	}
}
