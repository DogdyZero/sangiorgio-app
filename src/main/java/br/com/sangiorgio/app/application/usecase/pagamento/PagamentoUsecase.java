package br.com.sangiorgio.app.application.usecase.pagamento;

import br.com.sangiorgio.app.application.dto.PagamentoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PagamentoUsecase {
	void efetuarPagamento(PagamentoDTO pagamentoDTO);
}
