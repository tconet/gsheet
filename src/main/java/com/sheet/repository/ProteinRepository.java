package com.sheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sheet.domain.Protein;

public interface ProteinRepository extends JpaRepository<Protein, Long> {

	Protein findOneByNameAndGram(String name, int gram);
}
