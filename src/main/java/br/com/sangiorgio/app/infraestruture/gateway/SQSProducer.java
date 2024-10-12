package br.com.sangiorgio.app.infraestruture.gateway;

import br.com.sangiorgio.app.application.gateway.QueueGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public class SQSProducer implements QueueGateway {
	private final SqsTemplate template;
	@Value("${cloud.aws.end-point.uri}")
	private String sqsFila;

	@Override
	public void sendQueue(Object object) throws JsonProcessingException {
		var mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.writer()
				.withDefaultPrettyPrinter();
		var queueMessage = mapper.writeValueAsString(object);

		template.send(sqsFila, queueMessage);
	}
}