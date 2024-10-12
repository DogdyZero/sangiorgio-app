package br.com.sangiorgio.app.application.usecase.vendedor;

import br.com.sangiorgio.app.application.dto.CadastroVendedorDTO;
import br.com.sangiorgio.app.application.dto.VendedorDTO;

import java.util.List;

public interface VendedorUsecase {
	List<VendedorDTO> pesquisarVendedores();

	VendedorDTO cadastrarVendedor(CadastroVendedorDTO cadastroVendedorDTO);
}
