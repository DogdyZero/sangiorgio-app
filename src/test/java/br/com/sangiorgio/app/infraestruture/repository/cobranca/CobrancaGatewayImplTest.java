package br.com.sangiorgio.app.infraestruture.repository.cobranca;

import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.domain.Cobranca;
import br.com.sangiorgio.app.domain.RegraNegocioException;
import br.com.sangiorgio.app.domain.StatusPagamento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CobrancaGatewayImplTest {
	@Mock
	private CobrancaEntityRepository cobrancaEntityRepository;
	@InjectMocks
	private CobrancaGatewayImpl cobrancaGateway;
	private AutoCloseable openMocks;
	private CobrancaEntity cobrancaEntity;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);

		this.cobrancaEntity = CobrancaEntity.builder()
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
	void deveReturnarListaCobrancas() {
		when(cobrancaEntityRepository.findAll()).thenReturn(Collections.singletonList(cobrancaEntity));
		List<CobrancaDTO> listaCobranca = cobrancaGateway.pesquisarCobrancas();
		assertEquals(1, listaCobranca.size());
		verify(cobrancaEntityRepository, times(1)).findAll();
	}

	@Test
	void deveReturnarCobranca() {
		when(cobrancaEntityRepository.findByCodigo(any())).thenReturn(Optional.of(cobrancaEntity));
		Optional<CobrancaDTO> optional = cobrancaGateway.pesquisarPorCodigo("XPTO");
		optional.ifPresent(cobranca -> {
			assertEquals("XPTO", cobranca.getCodigo());
		});
		verify(cobrancaEntityRepository, times(1)).findByCodigo(any());
	}

	@Test
	void deveCadastrarComSucesso() {
		var cobranca = new Cobranca("XPTO", StatusPagamento.PENDENTE, 25d, 0d, "Douglas");
		when(cobrancaEntityRepository.save(any())).thenReturn(cobrancaEntity);

		CobrancaDTO resultado = cobrancaGateway.cadastrar(cobranca);
		assertEquals("XPTO", resultado.getCodigo());
		verify(cobrancaEntityRepository, times(1)).save(any(CobrancaEntity.class));
	}

	@Test
	void deveAtualizarComSucesso() {
		var cobranca = new Cobranca("XPTO", StatusPagamento.PENDENTE, 25d, 0d, "Douglas");
		when(cobrancaEntityRepository.findByCodigo(any())).thenReturn(Optional.of(cobrancaEntity));
		when(cobrancaEntityRepository.save(any())).thenReturn(cobrancaEntity);

		cobrancaGateway.atualizarValorPagoEStatus(cobranca);
		verify(cobrancaEntityRepository, times(1)).findByCodigo(any());
		verify(cobrancaEntityRepository, times(1)).save(any(CobrancaEntity.class));
	}

	@Test
	void deveAtualizarComSucesso_deveLancarErroQuandoCodigoNaoEncontrado() {
		var cobranca = new Cobranca("XPTO", StatusPagamento.PENDENTE, 25d, 0d, "Douglas");
		when(cobrancaEntityRepository.findByCodigo(any())).thenReturn(Optional.empty());

		var exception = assertThrows(RegraNegocioException.class, () -> {
			cobrancaGateway.atualizarValorPagoEStatus(cobranca);
		});
		assertEquals("Código da cobrança XPTO, não localizado!", exception.getMessage());
		verify(cobrancaEntityRepository, never()).save(any(CobrancaEntity.class));
	}
}
