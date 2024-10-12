package br.com.sangiorgio.app.application.dto;

import br.com.sangiorgio.app.domain.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CobrancaDTO {
	private String id;
	private String codigo;
	private StatusPagamento status;
	private Double valor;
	private Double pago;
	private String cliente;
}
