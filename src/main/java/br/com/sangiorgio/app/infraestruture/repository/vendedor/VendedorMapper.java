package br.com.sangiorgio.app.infraestruture.repository.vendedor;

import br.com.sangiorgio.app.application.dto.VendedorDTO;
import br.com.sangiorgio.app.domain.Vendedor;

public class VendedorMapper {
	public static VendedorDTO toDTO(VendedorEntity vendedor) {
		return VendedorDTO.builder()
				.id(vendedor.getId())
				.nome(vendedor.getNome())
				.codigo(vendedor.getCodigo())
				.build();
	}

	public static VendedorEntity toEntity(Vendedor vendedor){
		return VendedorEntity.builder()
				.nome(vendedor.getNome())
				.codigo(vendedor.getCodigo())
				.build();
	}
}
