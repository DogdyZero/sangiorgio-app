package br.com.sangiorgio.app.infraestruture.repository.vendedor;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VendedorEntityRepository extends MongoRepository<VendedorEntity, String> {
	Optional<VendedorEntity> findByCodigo(String codigo);

	Optional<VendedorEntity> findByNome(String nome);
}
