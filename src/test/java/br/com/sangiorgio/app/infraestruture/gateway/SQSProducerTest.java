package br.com.sangiorgio.app.infraestruture.gateway;

import br.com.sangiorgio.app.application.dto.PagamentoQueueDTO;
import br.com.sangiorgio.app.domain.StatusPagamento;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SQSProducerTest {

	@Mock
	private SqsTemplate sqsTemplate; // Assumindo que você tem uma classe SqsTemplate

	@InjectMocks
	private SQSProducer sqsProducer;
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
	void deveExecutarProducer() throws JsonProcessingException {
		var object = new PagamentoQueueDTO("XPTO", StatusPagamento.PENDENTE, 100d, 100d);
		SQSProducer spyService = spy(sqsProducer);
		when(sqsTemplate.send(any(), any())).thenAnswer(InvocationOnMock::getArguments);

		ObjectMapper objectMapper = new ObjectMapper();
		String expectedJson = objectMapper.writeValueAsString(object);

		sqsProducer.sendQueue(object);
		verify(sqsTemplate).send(any(), eq(expectedJson));

	}
}
