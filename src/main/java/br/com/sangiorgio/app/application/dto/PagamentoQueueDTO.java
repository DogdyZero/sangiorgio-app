package br.com.sangiorgio.app.application.dto;

import br.com.sangiorgio.app.domain.StatusPagamento;

public record PagamentoQueueDTO(String codigo, StatusPagamento statusPagamento, Double valorDivida, Double valorPago) {
}
