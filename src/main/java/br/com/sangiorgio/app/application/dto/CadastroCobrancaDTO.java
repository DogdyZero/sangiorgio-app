package br.com.sangiorgio.app.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CadastroCobrancaDTO {
	private Double valor;
	private String cliente;
}
