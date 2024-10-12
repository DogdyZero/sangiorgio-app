package br.com.sangiorgio.app.infraestruture.gateway;

import br.com.sangiorgio.app.application.dto.PagamentoQueueDTO;
import br.com.sangiorgio.app.application.usecase.cobranca.CobrancaUsecase;
import br.com.sangiorgio.app.domain.StatusPagamento;
import br.com.sangiorgio.app.infraestruture.repository.cobranca.CobrancaEntity;
import br.com.sangiorgio.app.infraestruture.repository.cobranca.CobrancaGatewayImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SQSConsumerTest {
	@Mock
	private CobrancaUsecase cobrancaUsecase;

	@InjectMocks
	private SQSConsumer sqsConsumer;
	private AutoCloseable openMocks;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void deveExecutarConsumer() throws JsonProcessingException {
		var object = new PagamentoQueueDTO("XPTO", StatusPagamento.PENDENTE, 100d, 100d);
		doNothing().when(cobrancaUsecase)
				.atualizarStatusEValorPago(any(PagamentoQueueDTO.class));
		String value = new ObjectMapper().writeValueAsString(object);
		sqsConsumer.listen(value);

		verify(cobrancaUsecase, times(1)).atualizarStatusEValorPago(any(PagamentoQueueDTO.class));
	}
}
