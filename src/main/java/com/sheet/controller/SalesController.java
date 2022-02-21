package com.sheet.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sheet.domain.Protein;
import com.sheet.dto.ProteinDTO;
import com.sheet.repository.ProteinRepository;
import com.sheet.services.SalesService;
import com.sheet.services.SheetService;

@RestController
public class SalesController {
	
	/**
	 * @see {@link SheetService}
	 */
	private SheetService sheetService;
	private SalesService salesService;
	private ProteinRepository proteinRepository;
	private ModelMapper mapper;
	
	/**
	 * <p>
	 * Default constructor. Inject all necessary dependencies.
	 * @param sheetService
	 */
	public SalesController(
			SheetService sheetService,
			SalesService salesService,
			ProteinRepository proteinRepository,
			ModelMapper mapper) {
		this.sheetService = sheetService;
		this.salesService = salesService;
		this.proteinRepository = proteinRepository;
		this.mapper = mapper;
	}

	@GetMapping("/sale")
	public ResponseEntity<ProteinDTO> sale(
			@RequestParam("name") String name,
			@RequestParam("gram") int gram) {
		
		name = name.toUpperCase();
		name = StringUtils.stripAccents(name);
		Protein protein = proteinRepository.findOneByNameAndGram(name, gram);
		
		if ( protein == null )
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<ProteinDTO>
						(ProteinDTO.convertToDto(protein, mapper), HttpStatus.OK);
	}
	
	@PostMapping("/updatePrice")
	public void updateProductsPrice() throws IOException, GeneralSecurityException {
		this.sheetService.UpdateProductsValue();
	}
	
	@GetMapping("/sales")
    @ResponseBody
    public ResponseEntity<List<ProteinDTO>> getSales() {
		List<Protein> proteins =  proteinRepository.findAll();
		
		return new ResponseEntity
						<List<ProteinDTO>>
							(ProteinDTO.convertToDto(proteins, mapper), HttpStatus.OK); 
	}
	
	@PostMapping("/updateSalesFile")
	public ResponseEntity<String> updateSalesFile(@RequestParam("file") MultipartFile file) throws IOException, GeneralSecurityException {

		ArrayList<String> sales = new ArrayList<>();
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	        	System.out.println(line);
	        	sales.add(line);
	        }
	    }
	    salesService.updateSales(sales);
		return ResponseEntity.ok("Sales Updated!");
	}
	
	

}
