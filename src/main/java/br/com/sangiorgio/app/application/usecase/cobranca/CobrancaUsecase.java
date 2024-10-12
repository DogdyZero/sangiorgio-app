package br.com.sangiorgio.app.application.usecase.cobranca;

import br.com.sangiorgio.app.application.dto.CadastroCobrancaDTO;
import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.application.dto.PagamentoQueueDTO;

import java.util.List;

public interface CobrancaUsecase {
	List<CobrancaDTO> pesquisarCobrancas();

	CobrancaDTO cadastrarCobranca(CadastroCobrancaDTO cadastroCobrancaDTO);

	void atualizarStatusEValorPago(PagamentoQueueDTO cobranca);
}
