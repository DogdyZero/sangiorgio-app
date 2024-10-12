package br.com.sangiorgio.app.infraestruture.configuration;

import br.com.sangiorgio.app.application.gateway.CobrancaGateway;
import br.com.sangiorgio.app.application.gateway.QueueGateway;
import br.com.sangiorgio.app.application.gateway.VendedorGateway;
import br.com.sangiorgio.app.application.usecase.pagamento.PagamentoUsecase;
import br.com.sangiorgio.app.application.usecase.pagamento.PagamentoUsecaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoConfiguration {
	@Bean
	public PagamentoUsecase pagamentoUsecase(CobrancaGateway cobrancaGateway, VendedorGateway vendedorGateway,
			QueueGateway queueGateway) {
		return new PagamentoUsecaseImpl(cobrancaGateway, vendedorGateway, queueGateway);
	}
}
