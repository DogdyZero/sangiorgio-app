package br.com.sangiorgio.app.infraestruture.repository.vendedor;

import br.com.sangiorgio.app.application.dto.VendedorDTO;
import br.com.sangiorgio.app.domain.Vendedor;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class VendedorGatewayImplTest {
	@Mock
	private VendedorEntityRepository vendedorEntityRepository;
	@InjectMocks
	private VendedorGatewayImpl vendedorGateway;
	private AutoCloseable openMocks;
	private VendedorEntity vendedorEntity;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);

		this.vendedorEntity = VendedorEntity.builder()
				.id("123123")
				.nome("Douglas")
				.codigo("XPTO")
				.build();
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void deveReturnarListaCobrancas() {
		when(vendedorEntityRepository.findAll()).thenReturn(Collections.singletonList(vendedorEntity));
		List<VendedorDTO> listaVendedores = vendedorGateway.pesquisarVendedores();
		assertEquals(1, listaVendedores.size());
		verify(vendedorEntityRepository, times(1)).findAll();
	}

	@Test
	void deveRetornarUmVendedor() {
		when(vendedorEntityRepository.save(any(VendedorEntity.class))).thenReturn(vendedorEntity);
		Optional<VendedorDTO> vendedorDTO = vendedorGateway.pesquisarPorCodigo("XPTO");
		vendedorDTO.ifPresent(vendedor -> {
			assertEquals("XPTO", vendedor.getCodigo());
		});
		verify(vendedorEntityRepository, times(1)).findByCodigo(any());
	}

	@Test
	void deveCadastrarUmVendedor() {
		var vendedor = new Vendedor("XPTO", "Douglas");
		when(vendedorEntityRepository.save(any(VendedorEntity.class))).thenReturn(vendedorEntity);
		VendedorDTO vendedorDTO = vendedorGateway.cadastrar(vendedor);
		assertEquals("XPTO", vendedorDTO.getCodigo());
		verify(vendedorEntityRepository, times(1)).save(any(VendedorEntity.class));
	}

	@Test
	void deveRetornarQuandoUsuarioExisteOuNao() {
		when(vendedorEntityRepository.findByNome(any())).thenReturn(Optional.of(vendedorEntity));
		boolean existe = vendedorGateway.nomeJaExiste("Douglas");
		assertTrue(existe);
		verify(vendedorEntityRepository, times(1)).findByNome(any());
	}
}
