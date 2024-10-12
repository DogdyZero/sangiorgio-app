package br.com.sangiorgio.app.application.usecase.pagamento;

import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.application.dto.PagamentoCobrancaDTO;
import br.com.sangiorgio.app.application.dto.PagamentoDTO;
import br.com.sangiorgio.app.application.dto.PagamentoQueueDTO;
import br.com.sangiorgio.app.application.gateway.CobrancaGateway;
import br.com.sangiorgio.app.application.gateway.QueueGateway;
import br.com.sangiorgio.app.application.gateway.VendedorGateway;
import br.com.sangiorgio.app.domain.RegraNegocioException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.utils.StringUtils;

@AllArgsConstructor
@Slf4j
public class PagamentoUsecaseImpl implements PagamentoUsecase {
	private final CobrancaGateway cobrancaGateway;
	private final VendedorGateway vendedorGateway;
	private final QueueGateway queueGateway;

	@Override
	public void efetuarPagamento(PagamentoDTO pagamentoDTO) {
		if (StringUtils.isBlank(pagamentoDTO.getCodigoVendedor()))
			throw new RegraNegocioException("O código do vendedor não deve ser vazio!");

		vendedorGateway.pesquisarPorCodigo(pagamentoDTO.getCodigoVendedor())
				.orElseThrow(() -> new RegraNegocioException(
						String.format("O código do vendedor '%s' não foi localizado",
								pagamentoDTO.getCodigoVendedor())));

		for (PagamentoCobrancaDTO pagamentoCobrancaDTO : pagamentoDTO.getListaPagamentos()) {

			if (StringUtils.isBlank(pagamentoCobrancaDTO.getCodigo()))
				throw new RegraNegocioException("O código da cobrança não deve ser vazio!");

			CobrancaDTO cobrancaDTO = cobrancaGateway.pesquisarPorCodigo(pagamentoCobrancaDTO.getCodigo())
					.orElseThrow(() -> new RegraNegocioException(
							String.format("O código do cobrança '%s' não foi localizado!",
									pagamentoCobrancaDTO.getCodigo())));

			PagamentoQueueDTO pagamentoQueueDTO = new PagamentoQueueDTO(cobrancaDTO.getCodigo(),
					cobrancaDTO.getStatus(), cobrancaDTO.getValor(), pagamentoCobrancaDTO.getValorPago());
			try {
				queueGateway.sendQueue(pagamentoQueueDTO);
			} catch (JsonProcessingException e) {
				log.error(e.getMessage());
				throw new RegraNegocioException("Ocorreu um erro inexperado na solicitação!");
			}
		}
	}
}

