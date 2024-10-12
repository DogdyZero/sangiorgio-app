package br.com.sangiorgio.app.infraestruture.repository.vendedor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

@Document
@Getter
@Service
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class VendedorEntity {
	@Id
	private String id;
	private String codigo;
	private String nome;
}
