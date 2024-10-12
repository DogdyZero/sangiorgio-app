package br.com.sangiorgio.app.application.gateway;

import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.domain.Cobranca;

import java.util.List;
import java.util.Optional;

public interface CobrancaGateway {
	List<CobrancaDTO> pesquisarCobrancas();

	Optional<CobrancaDTO> pesquisarPorCodigo(String codigo);

	CobrancaDTO cadastrar(Cobranca cobranca);

	void atualizarValorPagoEStatus(Cobranca cobranca);
}
