package br.com.sangiorgio.app.application.usecase.cobranca;

import br.com.sangiorgio.app.application.dto.CadastroCobrancaDTO;
import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.application.dto.PagamentoQueueDTO;
import br.com.sangiorgio.app.application.gateway.CobrancaGateway;
import br.com.sangiorgio.app.application.usecase.shared.GeradorCodigo;
import br.com.sangiorgio.app.domain.Cobranca;
import br.com.sangiorgio.app.domain.RegraNegocioException;
import br.com.sangiorgio.app.domain.StatusPagamento;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CobrancaUsecaseImpl implements CobrancaUsecase {
	private final CobrancaGateway cobrancaGateway;

	@Override
	public List<CobrancaDTO> pesquisarCobrancas() {
		return cobrancaGateway.pesquisarCobrancas();
	}

	@Override
	public CobrancaDTO cadastrarCobranca(CadastroCobrancaDTO cadastroCobrancaDTO) {
		if (StringUtils.containsWhitespace(cadastroCobrancaDTO.getCliente()))
			throw new RegraNegocioException("O cliente deve ser informado!");

		if (Objects.isNull(cadastroCobrancaDTO.getValor()))
			throw new RegraNegocioException("O valor da cobrança deve ser informado!");

		String codigoGerado = GeradorCodigo.gerar("gerarCobranca");
		Cobranca cobranca = new Cobranca(codigoGerado, StatusPagamento.PENDENTE, cadastroCobrancaDTO.getValor(), 0d,
				cadastroCobrancaDTO.getCliente());

		return cobrancaGateway.cadastrar(cobranca);
	}

	@Override
	public void atualizarStatusEValorPago(PagamentoQueueDTO dto) {
		if (dto.statusPagamento()
				.equals(StatusPagamento.TOTAL))
			throw new RegraNegocioException("O pagamento total já foi feito!");

		if (dto.valorPago() <= 0)
			throw new RegraNegocioException("Valor informado não deve ser zerado ou negativo!");

		var valorPago = dto.valorDivida() - (dto.valorDivida() - dto.valorPago());
		var novoStatus = getNovoStatus(dto);
		var cobranca = new Cobranca(dto.codigo(), novoStatus, dto.valorDivida(), valorPago, null);
		cobrancaGateway.atualizarValorPagoEStatus(cobranca);
	}

	private StatusPagamento getNovoStatus(PagamentoQueueDTO dto) {
		if (dto.valorDivida()
				.equals(dto.valorPago())) {
			return StatusPagamento.TOTAL;
		} else if (dto.valorDivida() > dto.valorPago()) {
			return StatusPagamento.PARCIAL;
		} else {
			return StatusPagamento.EXCEDENTE;
		}
	}
}
