package br.com.sangiorgio.app.infraestruture.api;

import br.com.sangiorgio.app.application.dto.CadastroCobrancaDTO;
import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.application.usecase.cobranca.CobrancaUsecase;
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
@RequestMapping("cobrancas")
@AllArgsConstructor
public class CobrancaController {
	private final CobrancaUsecase cobrancaUsecase;

	@PostMapping
	public ResponseEntity<CobrancaDTO> adicionar(@RequestBody CadastroCobrancaDTO cobrancaDTO) {
		var resposta = cobrancaUsecase.cadastrarCobranca(cobrancaDTO);
		return new ResponseEntity<>(resposta, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<CobrancaDTO>> pesquisarLista() {
		return ResponseEntity.ok(cobrancaUsecase.pesquisarCobrancas());
	}
}
