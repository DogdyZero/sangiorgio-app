package br.com.sangiorgio.app.infraestruture.api;

import br.com.sangiorgio.app.application.dto.PagamentoDTO;
import br.com.sangiorgio.app.application.usecase.pagamento.PagamentoUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pagamentos")
@RequiredArgsConstructor
public class PagamentoController {
	private final PagamentoUsecase pagamentoUsecase;

	@PostMapping
	public ResponseEntity<?> efetuarPagamento(@RequestBody PagamentoDTO pagamentoDTO){
		pagamentoUsecase.efetuarPagamento(pagamentoDTO);
		return ResponseEntity.ok().build();
	}
}
