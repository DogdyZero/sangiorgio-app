package br.com.sangiorgio.app.infraestruture.configuration;

import br.com.sangiorgio.app.application.gateway.VendedorGateway;
import br.com.sangiorgio.app.application.usecase.vendedor.VendedorUsecase;
import br.com.sangiorgio.app.application.usecase.vendedor.VendedorUsecaseImpl;
import br.com.sangiorgio.app.infraestruture.repository.vendedor.VendedorEntityRepository;
import br.com.sangiorgio.app.infraestruture.repository.vendedor.VendedorGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VendedorConfiguration {

	@Bean
	public VendedorUsecase vendedorUsecase(VendedorGateway vendedorGateway) {
		return new VendedorUsecaseImpl(vendedorGateway);
	}

	@Bean
	public VendedorGateway vendedorGateway(VendedorEntityRepository vendedorEntityRepository) {
		return new VendedorGatewayImpl(vendedorEntityRepository);
	}
}
