package br.com.sangiorgio.app.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagamentoDTO {
	private String codigoVendedor;
	private List<PagamentoCobrancaDTO> listaPagamentos;
}
