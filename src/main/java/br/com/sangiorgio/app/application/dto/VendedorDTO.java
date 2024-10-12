package br.com.sangiorgio.app.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VendedorDTO {
	private String id;
	private String codigo;
	private String nome;
}
