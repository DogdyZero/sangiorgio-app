package br.com.sangiorgio.app.infraestruture.api;

import br.com.sangiorgio.app.application.dto.CadastroCobrancaDTO;
import br.com.sangiorgio.app.application.dto.CadastroVendedorDTO;
import br.com.sangiorgio.app.application.dto.CobrancaDTO;
import br.com.sangiorgio.app.application.usecase.cobranca.CobrancaUsecase;
import br.com.sangiorgio.app.domain.StatusPagamento;
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

class CobrancaControllerTest {
	@Mock
	private CobrancaUsecase cobrancaUsecase;
	private MockMvc mockMvc;
	private AutoCloseable openMocks;
	private static final String BASE_URL = "/cobrancas";
	private CobrancaDTO cobrancaDTO;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);

		var controller = new CobrancaController(cobrancaUsecase);

		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);

		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setControllerAdvice(new GlobalExceptionHandler())
				.addFilters(filter)
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
	void deveCriarUmVendedor() throws Exception {
		var cadastroCobrancaDTO = CadastroCobrancaDTO.builder()
				.cliente("Douglas")
				.valor(25d)
				.build();
		when(cobrancaUsecase.cadastrarCobranca(any(CadastroCobrancaDTO.class))).thenReturn(cobrancaDTO);

		mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
						.content(JsonHelper.asJsonString(cadastroCobrancaDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value("123123"))
				.andExpect(jsonPath("$.valor").value("25.0"))
				.andExpect(jsonPath("$.cliente").value("Douglas"))
				.andExpect(jsonPath("$.status").value("PENDENTE"))
				.andExpect(jsonPath("$.codigo").value("XPTO"));
	}

	@Test
	void deveRetornarUmaListaVendedores() throws Exception {
		when(cobrancaUsecase.pesquisarCobrancas()).thenReturn(Collections.singletonList(cobrancaDTO));

		mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id").value("123123"))
				.andExpect(jsonPath("$.[0].valor").value("25.0"))
				.andExpect(jsonPath("$.[0].cliente").value("Douglas"))
				.andExpect(jsonPath("$.[0].status").value("PENDENTE"))
				.andExpect(jsonPath("$.[0].codigo").value("XPTO"));

	}
}
