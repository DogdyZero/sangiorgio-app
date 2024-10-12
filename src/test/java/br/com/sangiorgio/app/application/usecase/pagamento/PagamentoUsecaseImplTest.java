package br.com.sangiorgio.app.application.usecase.pagamento;

import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.application.dto.PagamentoCobrancaDTO;
import br.com.sangiorgio.app.application.dto.PagamentoDTO;
import br.com.sangiorgio.app.application.dto.PagamentoQueueDTO;
import br.com.sangiorgio.app.application.dto.VendedorDTO;
import br.com.sangiorgio.app.application.gateway.CobrancaGateway;
import br.com.sangiorgio.app.application.gateway.QueueGateway;
import br.com.sangiorgio.app.application.gateway.VendedorGateway;
import br.com.sangiorgio.app.domain.RegraNegocioException;
import br.com.sangiorgio.app.domain.StatusPagamento;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PagamentoUsecaseImplTest {
	@Mock
	private CobrancaGateway cobrancaGateway;
	@Mock
	private VendedorGateway vendedorGateway;
	@Mock
	private QueueGateway queueGateway;
	@InjectMocks
	private PagamentoUsecaseImpl pagamentoUsecase;
	private AutoCloseable openMocks;
	private PagamentoDTO pagamentoDTO;
	private VendedorDTO vendedorDTO;
	private CobrancaDTO cobrancaDTO;
	private PagamentoQueueDTO pagamentoQueueDTO;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);

		var pagamentoCobranca = PagamentoCobrancaDTO.builder()
				.codigo("XPTO")
				.valorPago(100d)
				.build();

		this.pagamentoDTO = PagamentoDTO.builder()
				.codigoVendedor("XPTO")
				.listaPagamentos(Collections.singletonList(pagamentoCobranca))
				.build();
		this.vendedorDTO = VendedorDTO.builder()
				.id("123123")
				.nome("Douglas")
				.codigo("XPTO")
				.build();

		this.cobrancaDTO = CobrancaDTO.builder()
				.id("123123")
				.valor(25d)
				.cliente("Douglas")
				.status(StatusPagamento.PENDENTE)
				.codigo("XPTO")
				.build();
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void deveCadastrarUmPagamento() throws JsonProcessingException {
		when(vendedorGateway.pesquisarPorCodigo(any())).thenReturn(Optional.of(vendedorDTO));
		when(cobrancaGateway.pesquisarPorCodigo(any())).thenReturn(Optional.of(cobrancaDTO));
		doNothing().when(queueGateway)
				.sendQueue(any(Object.class));

		assertDoesNotThrow(() -> {
			pagamentoUsecase.efetuarPagamento(pagamentoDTO);
		});

		verify(vendedorGateway, times(1)).pesquisarPorCodigo(any());
		verify(cobrancaGateway, times(1)).pesquisarPorCodigo(any());
		verify(queueGateway, times(1)).sendQueue(any(Object.class));

	}

	@Test
	void deveCadastrarUmPagamento_lancarExceptionQuandoCodigoVendedorForVazio() throws JsonProcessingException {
		pagamentoDTO.setCodigoVendedor(" ");
		var exception = assertThrows(RegraNegocioException.class, () -> {
			pagamentoUsecase.efetuarPagamento(pagamentoDTO);
		});
		verify(vendedorGateway, never()).pesquisarPorCodigo(any());
		verify(cobrancaGateway, never()).pesquisarPorCodigo(any());
		verify(queueGateway, never()).sendQueue(any(Object.class));

		assertEquals("O código do vendedor não deve ser vazio!", exception.getMessage());
	}

	@Test
	void deveCadastrarUmPagamento_lancarExceptionQuandoCodigoVendedorNaoForLocalizado() throws JsonProcessingException {
		when(vendedorGateway.pesquisarPorCodigo(any())).thenReturn(Optional.empty());

		var exception = assertThrows(RegraNegocioException.class, () -> {
			pagamentoUsecase.efetuarPagamento(pagamentoDTO);
		});
		verify(vendedorGateway, times(1)).pesquisarPorCodigo(any());
		verify(cobrancaGateway, never()).pesquisarPorCodigo(any());
		verify(queueGateway, never()).sendQueue(any(Object.class));

		assertEquals("O código do vendedor 'XPTO' não foi localizado", exception.getMessage());
	}

	@Test
	void deveCadastrarUmPagamento_lancarExceptionQuandoCodigoCodigoDaCobrancaoForVazio() throws JsonProcessingException {
//		pagamentoDTO.getListaPagamentos()
//				.getFirst().setCodigo(" ");
		pagamentoDTO.getListaPagamentos().iterator().next().setCodigo(" ");
		when(vendedorGateway.pesquisarPorCodigo(any())).thenReturn(Optional.of(vendedorDTO));
		when(cobrancaGateway.pesquisarPorCodigo(any())).thenReturn(Optional.empty());

		var exception = assertThrows(RegraNegocioException.class, () -> {
			pagamentoUsecase.efetuarPagamento(pagamentoDTO);
		});
		verify(vendedorGateway, times(1)).pesquisarPorCodigo(any());
		verify(cobrancaGateway, never()).pesquisarPorCodigo(any());
		verify(queueGateway, never()).sendQueue(any(Object.class));

		assertEquals("O código da cobrança não deve ser vazio!", exception.getMessage());
	}

	@Test
	void deveCadastrarUmPagamento_lancarExceptionQuandoCodigoCodigoNaoForEncontrado() throws JsonProcessingException {
		when(vendedorGateway.pesquisarPorCodigo(any())).thenReturn(Optional.of(vendedorDTO));
		when(cobrancaGateway.pesquisarPorCodigo(any())).thenReturn(Optional.empty());

		var exception = assertThrows(RegraNegocioException.class, () -> {
			pagamentoUsecase.efetuarPagamento(pagamentoDTO);
		});
		verify(vendedorGateway, times(1)).pesquisarPorCodigo(any());
		verify(cobrancaGateway, times(1)).pesquisarPorCodigo(any());
		verify(queueGateway, never()).sendQueue(any(Object.class));

		assertEquals("O código do cobrança 'XPTO' não foi localizado!", exception.getMessage());
	}

	@Test
	void deveCadastrarUmPagamento_lancarExceptionQuandoHouverErroComSQS() throws JsonProcessingException {
		when(vendedorGateway.pesquisarPorCodigo(any())).thenReturn(Optional.of(vendedorDTO));
		when(cobrancaGateway.pesquisarPorCodigo(any())).thenReturn(Optional.of(cobrancaDTO));
		doThrow(JsonProcessingException.class).when(queueGateway)
				.sendQueue(any(Object.class));

		var exception = assertThrows(RegraNegocioException.class, () -> {
			pagamentoUsecase.efetuarPagamento(pagamentoDTO);
		});

		verify(vendedorGateway, times(1)).pesquisarPorCodigo(any());
		verify(cobrancaGateway, times(1)).pesquisarPorCodigo(any());
		verify(queueGateway, times(1)).sendQueue(any(Object.class));

		assertEquals("Ocorreu um erro inexperado na solicitação!", exception.getMessage());

	}
}
