package br.com.sangiorgio.app.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Cobranca {
	private String codigo;
	private StatusPagamento status;
	private Double valor;
	private Double pago;
	private String cliente;
}
