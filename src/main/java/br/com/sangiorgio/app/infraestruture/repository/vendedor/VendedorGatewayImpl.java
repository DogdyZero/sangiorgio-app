package br.com.sangiorgio.app.infraestruture.repository.vendedor;

import br.com.sangiorgio.app.application.dto.VendedorDTO;
import br.com.sangiorgio.app.application.gateway.VendedorGateway;
import br.com.sangiorgio.app.domain.Vendedor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VendedorGatewayImpl implements VendedorGateway {
	private final VendedorEntityRepository vendedorEntityRepository;

	@Override
	public List<VendedorDTO> pesquisarVendedores() {
		return vendedorEntityRepository.findAll()
				.stream()
				.map(VendedorMapper::toDTO)
				.toList();
	}

	@Override
	public Optional<VendedorDTO> pesquisarPorCodigo(String codigo) {
		return vendedorEntityRepository.findByCodigo(codigo)
				.map(VendedorMapper::toDTO);
	}

	@Override
	public boolean nomeJaExiste(String nome) {
		return vendedorEntityRepository.findByNome(nome)
				.isPresent();
	}

	@Override
	public VendedorDTO cadastrar(Vendedor vendedor) {
		VendedorEntity entity = VendedorMapper.toEntity(vendedor);
		vendedorEntityRepository.save(entity);
		return VendedorMapper.toDTO(entity);
	}
}
