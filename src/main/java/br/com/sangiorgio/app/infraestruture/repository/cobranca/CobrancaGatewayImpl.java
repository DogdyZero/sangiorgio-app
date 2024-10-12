package br.com.sangiorgio.app.infraestruture.repository.cobranca;

import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.application.gateway.CobrancaGateway;
import br.com.sangiorgio.app.domain.Cobranca;
import br.com.sangiorgio.app.domain.RegraNegocioException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CobrancaGatewayImpl implements CobrancaGateway {
	private final CobrancaEntityRepository cobrancaEntityRepository;

	@Override
	public List<CobrancaDTO> pesquisarCobrancas() {
		return cobrancaEntityRepository.findAll()
				.stream()
				.map(CobrancaMapper::toDTO)
				.toList();
	}

	@Override
	public Optional<CobrancaDTO> pesquisarPorCodigo(String codigo) {
		return cobrancaEntityRepository.findByCodigo(codigo)
				.map(CobrancaMapper::toDTO);
	}

	@Override
	public CobrancaDTO cadastrar(Cobranca cobranca) {
		CobrancaEntity entity = CobrancaMapper.toEntity(cobranca);
		cobrancaEntityRepository.save(entity);
		return CobrancaMapper.toDTO(entity);
	}

	@Override
	public void atualizarValorPagoEStatus(Cobranca cobranca) {
		CobrancaEntity entity = cobrancaEntityRepository.findByCodigo(cobranca.getCodigo())
				.orElseThrow(() -> new RegraNegocioException(
						String.format("Código da cobrança %s, não localizado!", cobranca.getCodigo())));
		entity.setPago(cobranca.getPago());
		entity.setStatus(cobranca.getStatus());
		cobrancaEntityRepository.save(entity);
	}

}
