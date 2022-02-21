package com.sheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sheet.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
