package br.com.sangiorgio.app.application.gateway;

import br.com.sangiorgio.app.application.dto.VendedorDTO;
import br.com.sangiorgio.app.domain.Vendedor;

import java.util.List;
import java.util.Optional;

public interface VendedorGateway {
	List<VendedorDTO> pesquisarVendedores();

	Optional<VendedorDTO> pesquisarPorCodigo(String codigo);

	boolean nomeJaExiste(String nome);

	VendedorDTO cadastrar(Vendedor vendedor);
}
