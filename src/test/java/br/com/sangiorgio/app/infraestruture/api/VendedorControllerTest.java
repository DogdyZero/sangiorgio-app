package br.com.sangiorgio.app.infraestruture.api;

import br.com.sangiorgio.app.application.dto.CadastroVendedorDTO;
import br.com.sangiorgio.app.application.dto.VendedorDTO;
import br.com.sangiorgio.app.application.usecase.vendedor.VendedorUsecase;
import br.com.sangiorgio.app.infraestruture.configuration.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VendedorControllerTest {
	@Mock
	private VendedorUsecase vendedorUsecase;
	private MockMvc mockMvc;
	private AutoCloseable openMocks;
	private static final String BASE_URL = "/vendedores";
	private VendedorDTO vendedorDTO;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);

		var controller = new VendedorController(vendedorUsecase);

		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);

		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setControllerAdvice(new GlobalExceptionHandler())
				.addFilters(filter)
				.build();
		this.vendedorDTO = VendedorDTO.builder()
				.id("12312321")
				.codigo("XPTO")
				.nome("Douglas")
				.build();
		;
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void deveCriarUmVendedor() throws Exception {
		var cadastroVendedorDTO = CadastroVendedorDTO.builder()
				.nome("Douglas")
				.build();
		when(vendedorUsecase.cadastrarVendedor(any(CadastroVendedorDTO.class))).thenReturn(vendedorDTO);

		mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
						.content(JsonHelper.asJsonString(cadastroVendedorDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value("12312321"))
				.andExpect(jsonPath("$.codigo").value("XPTO"))
				.andExpect(jsonPath("$.nome").value("Douglas"));
	}

	@Test
	void deveRetornarUmaListaVendedores() throws Exception {
		when(vendedorUsecase.pesquisarVendedores()).thenReturn(Collections.singletonList(vendedorDTO));

		mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id").value("12312321"))
				.andExpect(jsonPath("$.[0].codigo").value("XPTO"))
				.andExpect(jsonPath("$.[0].nome").value("Douglas"));
	}
}
