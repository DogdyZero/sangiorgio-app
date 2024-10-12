package br.com.sangiorgio.app.application.usecase.vendedor;

import br.com.sangiorgio.app.application.dto.CadastroVendedorDTO;
import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.application.dto.VendedorDTO;
import br.com.sangiorgio.app.application.gateway.VendedorGateway;
import br.com.sangiorgio.app.domain.Cobranca;
import br.com.sangiorgio.app.domain.RegraNegocioException;
import br.com.sangiorgio.app.domain.StatusPagamento;
import br.com.sangiorgio.app.domain.Vendedor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VendedorUsecaseImplTest {
	@Mock
	private VendedorGateway vendedorGateway;
	@InjectMocks
	private VendedorUsecaseImpl vendedorUsecase;
	private AutoCloseable openMocks;
	private VendedorDTO vendedorDTO;
	private Cobranca cobranca;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);

		this.vendedorDTO = VendedorDTO.builder()
				.id("123123")
				.nome("Douglas")
				.codigo("XPTO")
				.build();
		cobranca = new Cobranca("XPTO", StatusPagamento.PENDENTE, 25d, 0d, "Douglas");
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void deveCadastrarVendedor() {
		CadastroVendedorDTO cadastroVendedorDTO = CadastroVendedorDTO.builder()
				.nome("Douglas")
				.build();
		when(vendedorGateway.nomeJaExiste(any())).thenReturn(false);
		when(vendedorGateway.cadastrar(any(Vendedor.class))).thenReturn(vendedorDTO);

		assertDoesNotThrow(() -> {
			vendedorUsecase.cadastrarVendedor(cadastroVendedorDTO);
		});

		verify(vendedorGateway).nomeJaExiste(any());
		verify(vendedorGateway).cadastrar(any(Vendedor.class));
	}

	@Test
	void deveCadastrarVendedor_lancarExceptionQuandoVendedorJaExistir() {
		CadastroVendedorDTO cadastroVendedorDTO = CadastroVendedorDTO.builder()
				.nome("Douglas")
				.build();
		when(vendedorGateway.nomeJaExiste(any())).thenReturn(true);
		when(vendedorGateway.cadastrar(any(Vendedor.class))).thenReturn(vendedorDTO);

		var exception = assertThrows(RegraNegocioException.class, () -> {
			vendedorUsecase.cadastrarVendedor(cadastroVendedorDTO);
		});

		verify(vendedorGateway).nomeJaExiste(any());
		verify(vendedorGateway, never()).cadastrar(any(Vendedor.class));

		assertEquals("J́á existe o nome Douglas no banco de dados", exception.getMessage());
	}

	@Test
	void devePesquisarVendedores() {
		when(vendedorGateway.pesquisarVendedores()).thenReturn(Collections.singletonList(vendedorDTO));
		List<VendedorDTO> listaVendedores = vendedorUsecase.pesquisarVendedores();
		verify(vendedorGateway, times(1)).pesquisarVendedores();
		assertEquals(1, listaVendedores.size());
	}
}
