package com.sheet.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.sheet.domain.Protein;

import lombok.Data;

@Data
public class ProteinDTO {

	private String name;
	private int gram;
	private int quant;

	
	public static List<ProteinDTO> convertToDto(List<Protein> proteins, ModelMapper mapper) {
		
		List<ProteinDTO> dtos = new ArrayList<ProteinDTO>();
		for (Protein protein : proteins) {
			dtos.add(convertToDto(protein, mapper));
		}
	    return dtos;
	}
	
	public static ProteinDTO convertToDto(Protein protein, ModelMapper mapper) {
		ProteinDTO proteinDto = mapper.map(protein, ProteinDTO.class);
	    return proteinDto;
	}
}
