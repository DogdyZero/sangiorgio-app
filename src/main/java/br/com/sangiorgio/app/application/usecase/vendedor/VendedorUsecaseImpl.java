package br.com.sangiorgio.app.application.usecase.vendedor;

import br.com.sangiorgio.app.application.dto.CadastroVendedorDTO;
import br.com.sangiorgio.app.application.dto.VendedorDTO;
import br.com.sangiorgio.app.application.gateway.VendedorGateway;
import br.com.sangiorgio.app.application.usecase.shared.GeradorCodigo;
import br.com.sangiorgio.app.domain.RegraNegocioException;
import br.com.sangiorgio.app.domain.Vendedor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class VendedorUsecaseImpl implements VendedorUsecase {

	private final VendedorGateway vendedorGateway;

	public List<VendedorDTO> pesquisarVendedores() {
		return vendedorGateway.pesquisarVendedores();
	}

	@Override
	public VendedorDTO cadastrarVendedor(CadastroVendedorDTO cadastroVendedorDTO) {
		boolean existe = vendedorGateway.nomeJaExiste(cadastroVendedorDTO.getNome());
		if (existe)
			throw new RegraNegocioException(
					String.format("J́á existe o nome %s no banco de dados", cadastroVendedorDTO.getNome()));

		String codigoGerado = GeradorCodigo.gerar(cadastroVendedorDTO.getNome());
		Vendedor vendedor = new Vendedor(codigoGerado, cadastroVendedorDTO.getNome());

		return vendedorGateway.cadastrar(vendedor);
	}
}
