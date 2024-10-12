package br.com.sangiorgio.app.infraestruture.configuration;

import br.com.sangiorgio.app.application.gateway.CobrancaGateway;
import br.com.sangiorgio.app.application.usecase.cobranca.CobrancaUsecase;
import br.com.sangiorgio.app.application.usecase.cobranca.CobrancaUsecaseImpl;
import br.com.sangiorgio.app.infraestruture.repository.cobranca.CobrancaEntityRepository;
import br.com.sangiorgio.app.infraestruture.repository.cobranca.CobrancaGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CobrancaConfiguration {
	@Bean
	public CobrancaUsecase cobrancaUsecase(CobrancaGateway cobrancaGateway) {
		return new CobrancaUsecaseImpl(cobrancaGateway);
	}

	@Bean
	public CobrancaGateway cobrancaGateway(CobrancaEntityRepository cobrancaEntityRepository) {
		return new CobrancaGatewayImpl(cobrancaEntityRepository);
	}
}
