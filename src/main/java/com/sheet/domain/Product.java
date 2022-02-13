package com.sheet.domain;

import java.util.List;

import lombok.Data;

@Data
public class Product {

	private String name;
	private String price;
	private String cell;

	public Product(String name) {
		this.name = name;
	}
	
	public Product(String name, String price) {
		this.name = name;
		this.price = price;
	}

	public Product(String name, String price, String cell) {
		this.name = name;
		this.price = price;
		this.cell = cell;
	}

	public static Product create(String name, String price) {
		return new Product(name, price);
	}

	public static Product create(String name, String price, String cell) {
		return new Product(name, price, cell);
	}
	
	public void updatePrice(List<Product> products) {
		
		for (Product product : products) {
			if ( product.equals(this) ) {
				product.setPrice(this.getPrice());
				return;
			}
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Product))
			return false;
		
		Product product = (Product) obj;
		return this.name.equals(product.getName());
	}
	
	 @Override
     public int hashCode() {
         return super.hashCode();
     }
}
