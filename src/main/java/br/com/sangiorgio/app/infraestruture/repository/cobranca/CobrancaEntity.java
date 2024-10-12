package br.com.sangiorgio.app.infraestruture.repository.cobranca;

import br.com.sangiorgio.app.domain.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Document
public class CobrancaEntity {
	@Id
	private String id;
	private String codigo;
	private StatusPagamento status;
	private Double valor;
	private Double pago;
	private String cliente;
}
