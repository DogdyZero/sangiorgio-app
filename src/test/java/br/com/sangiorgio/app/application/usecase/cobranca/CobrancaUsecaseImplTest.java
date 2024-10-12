package br.com.sangiorgio.app.application.usecase.cobranca;

import br.com.sangiorgio.app.application.dto.CadastroCobrancaDTO;
import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.application.dto.PagamentoQueueDTO;
import br.com.sangiorgio.app.application.gateway.CobrancaGateway;
import br.com.sangiorgio.app.domain.Cobranca;
import br.com.sangiorgio.app.domain.RegraNegocioException;
import br.com.sangiorgio.app.domain.StatusPagamento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CobrancaUsecaseImplTest {
	@Mock
	private CobrancaGateway cobrancaGateway;
	@InjectMocks
	private CobrancaUsecaseImpl cobrancaUsecase;
	private AutoCloseable openMocks;
	private CobrancaDTO cobrancaDTO;
	private Cobranca cobranca;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);

		this.cobrancaDTO = CobrancaDTO.builder()
				.id("123123")
				.valor(25d)
				.cliente("Douglas")
				.status(StatusPagamento.PENDENTE)
				.codigo("XPTO")
				.build();
		cobranca = new Cobranca("XPTO", StatusPagamento.PENDENTE, 25d, 0d, "Douglas");
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void deveRetornarListaCobranca() {
		when(cobrancaGateway.pesquisarCobrancas()).thenReturn(Collections.singletonList(cobrancaDTO));
		List<CobrancaDTO> listaCobranca = cobrancaUsecase.pesquisarCobrancas();
		assertEquals(1, listaCobranca.size());
		verify(cobrancaGateway, times(1)).pesquisarCobrancas();
	}

	@Nested
	class CadastrarCobranca {
		@Test
		void deveCadastrarUmCobranca() {
			var cadastroDto = CadastroCobrancaDTO.builder()
					.valor(25d)
					.cliente("Douglas")
					.build();
			when(cobrancaGateway.cadastrar(any(Cobranca.class))).thenReturn(cobrancaDTO);
			CobrancaDTO resposta = cobrancaUsecase.cadastrarCobranca(cadastroDto);
			assertEquals(cobrancaDTO.getCodigo(), resposta.getCodigo());
			verify(cobrancaGateway, times(1)).cadastrar(any(Cobranca.class));
		}

		@Test
		void deveCadastrarUmCobranca_lancaErroQuandoNomeForVazio() {
			var cadastroDto = CadastroCobrancaDTO.builder()
					.valor(25d)
					.cliente(" ")
					.build();
			when(cobrancaGateway.cadastrar(any(Cobranca.class))).thenReturn(cobrancaDTO);
			var exception = assertThrows(RegraNegocioException.class, () -> {
				cobrancaUsecase.cadastrarCobranca(cadastroDto);
			});
			assertEquals("O cliente deve ser informado!", exception.getMessage());
			verify(cobrancaGateway, never()).cadastrar(any(Cobranca.class));
		}

		@Test
		void deveCadastrarUmCobranca_lancaErroQuandoValorForNulo() {
			var cadastroDto = CadastroCobrancaDTO.builder()
					.cliente("Douglas")
					.build();
			when(cobrancaGateway.cadastrar(any(Cobranca.class))).thenReturn(cobrancaDTO);
			var exception = assertThrows(RegraNegocioException.class, () -> {
				cobrancaUsecase.cadastrarCobranca(cadastroDto);
			});
			assertEquals("O valor da cobrança deve ser informado!", exception.getMessage());
			verify(cobrancaGateway, never()).cadastrar(any(Cobranca.class));
		}
	}

	@Nested
	class AtualizarCobranca {
		@Test
		void deveAtualizarCobranca_ValidarSeEstaStatusTotal() {
			var pagamentoDto = new PagamentoQueueDTO("XPTO", StatusPagamento.PENDENTE, 100d, 100d);
			doNothing().when(cobrancaGateway)
					.atualizarValorPagoEStatus(any(Cobranca.class));
			ArgumentCaptor<Cobranca> cobrancaCaptor = ArgumentCaptor.forClass(Cobranca.class);

			assertDoesNotThrow(() -> {
				cobrancaUsecase.atualizarStatusEValorPago(pagamentoDto);
			});
			verify(cobrancaGateway).atualizarValorPagoEStatus(cobrancaCaptor.capture());

			var statusAtual = cobrancaCaptor.getValue().getStatus();
			assertEquals(StatusPagamento.TOTAL, statusAtual);
		}

		@Test
		void deveAtualizarCobranca_ValidarSeEstaStatusParcial() {
			var pagamentoDto = new PagamentoQueueDTO("XPTO", StatusPagamento.PENDENTE, 100d, 25d);
			doNothing().when(cobrancaGateway)
					.atualizarValorPagoEStatus(any(Cobranca.class));
			ArgumentCaptor<Cobranca> cobrancaCaptor = ArgumentCaptor.forClass(Cobranca.class);

			assertDoesNotThrow(() -> {
				cobrancaUsecase.atualizarStatusEValorPago(pagamentoDto);
			});
			verify(cobrancaGateway).atualizarValorPagoEStatus(cobrancaCaptor.capture());

			var statusAtual = cobrancaCaptor.getValue().getStatus();
			assertEquals(StatusPagamento.PARCIAL, statusAtual);
		}

		@Test
		void deveAtualizarCobranca_ValidarSeEstaStatusExcedente() {
			var pagamentoDto = new PagamentoQueueDTO("XPTO", StatusPagamento.PENDENTE, 100d, 125d);
			doNothing().when(cobrancaGateway)
					.atualizarValorPagoEStatus(any(Cobranca.class));
			ArgumentCaptor<Cobranca> cobrancaCaptor = ArgumentCaptor.forClass(Cobranca.class);

			assertDoesNotThrow(() -> {
				cobrancaUsecase.atualizarStatusEValorPago(pagamentoDto);
			});
			verify(cobrancaGateway).atualizarValorPagoEStatus(cobrancaCaptor.capture());

			var statusAtual = cobrancaCaptor.getValue().getStatus();
			assertEquals(StatusPagamento.EXCEDENTE, statusAtual);
		}

		@Test
		void deveAtualizarCobranca_lancarExceptionQuandoStatusJaEstiverComoTotal() {
			var pagamentoDto = new PagamentoQueueDTO("XPTO", StatusPagamento.TOTAL, 100d, 100d);
			doNothing().when(cobrancaGateway)
					.atualizarValorPagoEStatus(any(Cobranca.class));

			var exception =  assertThrows(RegraNegocioException.class, () -> {
				cobrancaUsecase.atualizarStatusEValorPago(pagamentoDto);
			});
			assertEquals("O pagamento total já foi feito!", exception.getMessage());
			verify(cobrancaGateway, never()).atualizarValorPagoEStatus(any(Cobranca.class));
		}

		@Test
		void deveAtualizarCobranca_lancarExceptionQuandoValorPagoForNegativo() {
			var pagamentoDto = new PagamentoQueueDTO("XPTO", StatusPagamento.PENDENTE, 100d, 0d);
			doNothing().when(cobrancaGateway)
					.atualizarValorPagoEStatus(any(Cobranca.class));

			var exception =  assertThrows(RegraNegocioException.class, () -> {
				cobrancaUsecase.atualizarStatusEValorPago(pagamentoDto);
			});
			assertEquals("Valor informado não deve ser zerado ou negativo!", exception.getMessage());
			verify(cobrancaGateway, never()).atualizarValorPagoEStatus(any(Cobranca.class));
		}
	}
}
