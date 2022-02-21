package com.sheet.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.sheet.domain.Product;
import com.sheet.util.SheetsServiceUtil;

/**
 * <p>
 * WE must use the following report on 3LM system - 'Entrada de Produtos Sintético' 
 * as the base for must updated product's values. 
 * http://localhost:8080/services/h2-console
 * @author Tiago Oliveira
 */
@Service
public class SheetService {

	private static Sheets sheetsService;
	private static List<Product> products;
	
	/**
	 * this id can be replaced with your spreadsheet id
	 * otherwise be advised that multiple people may run this test and update the
	 * public spreadsheet
	 */
	private static final String SPREADSHEET_ID = "1BXP9jjMkaKEH6cG5iU9aRUGF2ueSvL3Jt-0lbkZ0uS0";
	
	/**
	 * <p>
	 * Updates all product values based.
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public void UpdateProductsValue() throws IOException, GeneralSecurityException {
		
		setup(); loadProducts(); loadNewValues(); updateValues();
	}
	
	/**
	 * <p>
	 * Sets up the API credentials and returns the sheet service instance.
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	private void setup() throws GeneralSecurityException, IOException {
		if (sheetsService != null )
			return;
		sheetsService = SheetsServiceUtil.getSheetsService();
	}
	
	/**
	 * <p>
	 * Loads the list of products available to update the price.
	 * We're going to find those products into the DADOS tab from google sheet.
	 * All we gone do here is just load all values into a list of String.
	 * @throws IOException
	 */
	private void loadProducts() throws IOException {

		// Initialize the list of products
		products = new ArrayList<>();
		// Set data range.
		String range = "DADOS!A1:B70";
		
		// Call google sheet API to load the values.
		int index = 1;
		ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
		List<List<Object>> data = response.getValues();
		for (@SuppressWarnings("rawtypes")
		List row : data) {
			products.add(Product.create((String)row.get(0), null, "DADOS!B"+index++));
		}
	}
	
	/**
	 * <p>
	 * Search for new products values to be updated. In this case, we'll read 
	 * the VALUES tab of the sheet that holds all updated values. 
	 * 
	 * Only the products existent into PRODUCTS COLLECTION must be collected.
	 * OBS: We'll use the LAST PRICE as the NEW VALUE to be updated.
	 * 
	 * @throws IOException
	 */
	private void loadNewValues() throws IOException {

		// Set data range.
		String range = "VALUES!C1:K200";
		
		// Call google sheet API to load the values.
		ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
		List<List<Object>> data = response.getValues();
		for (@SuppressWarnings("rawtypes")
		List row : data) {
			if ( row.isEmpty() )
				continue;
			String name = (String)row.get(0);
			if ( products.contains(new Product(name)) ) {
				// GET THE LAST PRICE
				String price = (String)row.get(8);
				price = price.replace("R$", "").trim();
				Product prod = Product.create(name, price);
				prod.updatePrice(products);
			}
		}
	}
	
	/**
	 * <p>
	 * Updates the DATA tab sheet with all new values.
	 * @throws IOException
	 */
	private void updateValues() throws IOException {
		
		ValueRange newValue;
		for (Product product : products) {
			if ( product.getPrice() == null )
				continue;
			newValue = new ValueRange().setValues(Arrays.asList(Arrays.asList(product.getPrice())));
			sheetsService.spreadsheets()
				.values()
				.update(SPREADSHEET_ID, product.getCell(), newValue)
				.setValueInputOption("RAW")
				.execute();
		}
	}
}
