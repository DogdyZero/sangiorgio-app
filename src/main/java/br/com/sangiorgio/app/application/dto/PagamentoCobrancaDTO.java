package br.com.sangiorgio.app.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagamentoCobrancaDTO {
	private String codigo;
	private Double valorPago;
}
