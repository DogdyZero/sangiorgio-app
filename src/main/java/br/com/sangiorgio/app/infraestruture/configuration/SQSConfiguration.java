package br.com.sangiorgio.app.infraestruture.configuration;

import br.com.sangiorgio.app.application.gateway.QueueGateway;
import br.com.sangiorgio.app.application.usecase.cobranca.CobrancaUsecase;
import br.com.sangiorgio.app.application.usecase.pagamento.PagamentoUsecase;
import br.com.sangiorgio.app.infraestruture.gateway.SQSConsumer;
import br.com.sangiorgio.app.infraestruture.gateway.SQSProducer;
import io.awspring.cloud.sqs.config.SqsBootstrapConfiguration;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Import(SqsBootstrapConfiguration.class)
@Configuration
public class SQSConfiguration {

	@Bean
	public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
		return SqsMessageListenerContainerFactory.builder()
				.sqsAsyncClient(sqsAsyncClient())
				.build();
	}

	@Bean
	public SqsAsyncClient sqsAsyncClient() {
		return SqsAsyncClient.builder()
				.build();
	}

	@Bean
	public SQSConsumer sqsConsumer(CobrancaUsecase cobrancaUsecase) {
		return new SQSConsumer(cobrancaUsecase);
	}

	@Bean
	public QueueGateway queueGateway(SqsTemplate sqsTemplate) {
		return new SQSProducer(sqsTemplate);
	}
	@Bean
	public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient){
		return SqsTemplate.builder()
				.sqsAsyncClient(sqsAsyncClient)
				.build();
	}
}
