package br.com.sangiorgio.app.infraestruture.api;

import br.com.sangiorgio.app.application.dto.PagamentoDTO;
import br.com.sangiorgio.app.application.usecase.pagamento.PagamentoUsecase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PagamentoControllerTest {
	@Mock
	private PagamentoUsecase pagamentoUsecase;
	private MockMvc mockMvc;
	private AutoCloseable openMocks;
	private static final String BASE_URL = "/pagamentos";

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);

		var controller = new PagamentoController(pagamentoUsecase);

		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);

		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setControllerAdvice(new GlobalExceptionHandler())
				.addFilters(filter)
				.build();
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void deveEnviarPedidoParaPagamento() throws Exception {
		var dto = PagamentoDTO.builder()
				.build();

		doNothing().when(pagamentoUsecase)
				.efetuarPagamento(any(PagamentoDTO.class));

		mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
						.content(JsonHelper.asJsonString(dto)))
				.andExpect(status().isOk());
	}
}
