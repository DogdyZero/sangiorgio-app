package br.com.sangiorgio.app.domain;

public class RegraNegocioException extends RuntimeException {

	public RegraNegocioException(String nome) {
		super(nome);
	}
}
