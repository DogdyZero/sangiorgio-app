package br.com.sangiorgio.app.infraestruture.api;

import br.com.sangiorgio.app.application.dto.CadastroVendedorDTO;
import br.com.sangiorgio.app.application.dto.VendedorDTO;
import br.com.sangiorgio.app.application.usecase.vendedor.VendedorUsecase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("vendedores")
@AllArgsConstructor
public class VendedorController {
	private final VendedorUsecase vendedorUsecase;

	@PostMapping
	public ResponseEntity<VendedorDTO> adicionar(@RequestBody CadastroVendedorDTO vendedorDTO) {
		var resposta = vendedorUsecase.cadastrarVendedor(vendedorDTO);
		return new ResponseEntity<>(resposta, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<VendedorDTO>> pesquisarLista() {
		return ResponseEntity.ok(vendedorUsecase.pesquisarVendedores());
	}
}
