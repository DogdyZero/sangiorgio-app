package br.com.sangiorgio.app.infraestruture.repository.cobranca;

import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.domain.Cobranca;

public class CobrancaMapper {
	public static CobrancaDTO toDTO(CobrancaEntity cobranca) {
		return CobrancaDTO.builder()
				.id(cobranca.getId())
				.codigo(cobranca.getCodigo())
				.valor(cobranca.getValor())
				.status(cobranca.getStatus())
				.pago(cobranca.getPago())
				.cliente(cobranca.getCliente())
				.build();
	}

	public static CobrancaEntity toEntity(Cobranca cobranca) {
		return CobrancaEntity.builder()
				.codigo(cobranca.getCodigo())
				.valor(cobranca.getValor())
				.status(cobranca.getStatus())
				.pago(cobranca.getPago())
				.cliente(cobranca.getCliente())
				.build();
	}
}
