package br.com.sangiorgio.app.infraestruture.gateway;

import br.com.sangiorgio.app.application.dto.PagamentoQueueDTO;
import br.com.sangiorgio.app.application.usecase.cobranca.CobrancaUsecase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SQSConsumer {
	private final CobrancaUsecase cobrancaUsecase;

	@SqsListener("${cloud.aws.end-point.uri}")
	public void listen(String message) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		PagamentoQueueDTO dto = mapper.readValue(message, PagamentoQueueDTO.class);
		cobrancaUsecase.atualizarStatusEValorPago(dto);
	}


}
