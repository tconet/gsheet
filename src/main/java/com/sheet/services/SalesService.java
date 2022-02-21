package com.sheet.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sheet.domain.Protein;
import com.sheet.repository.ProteinRepository;

@Service
public class SalesService {

	private ProteinRepository proteinRepository;

	
	/**
	 * <p>
	 * Default constructor. Inject all necessary dependency
	 * @param proteinRepository
	 */
	public SalesService(ProteinRepository proteinRepository) {
		this.proteinRepository = proteinRepository;
	}
	
	
	/**
	 * <p>
	 * Read the sales variable where each line must have the following pattern.
	 * The protein's name, following by "-" character and the protein grams 
	 * following by "," and the sales quantity.
	 * FRANGO-150G,10
	 * @param sales
	 */
	public void updateSales(ArrayList<String> sales) {
		
		// Before update sales values, we must first 
		// reset all values to zero.
		resetSales();
		
		/**
		 * Sales variable must be in the following format.
		 * Example: FRANGO-150G,10 
		 */
		for (String sale : sales) {
			
			String[] line = sale.split(",");
			String quant = line[1];
			String content = line[0];
			String name =  content.split("-")[0];
			String vgram =  content.split("-")[1];
			vgram = vgram.substring(0, vgram.length()-1);
			
			int gram = Integer.parseInt(vgram);
			Protein protein = proteinRepository.findOneByNameAndGram(name, gram);
			if ( protein != null ) {
				protein.setQuant( Integer.parseInt(quant) );
				proteinRepository.save(protein);
			}
		}
	}
	
	/**
	 * <p>
	 * Just set all sales values to zero.
	 */
	private void resetSales() {
		
		List<Protein> proteins = proteinRepository.findAll();
		for (Protein protein : proteins) {
			protein.setQuant(0);
			proteinRepository.save(protein);
		}
	}
}
