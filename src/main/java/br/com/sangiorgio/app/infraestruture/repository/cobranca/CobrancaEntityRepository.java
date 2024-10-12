package br.com.sangiorgio.app.infraestruture.repository.cobranca;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CobrancaEntityRepository extends MongoRepository<CobrancaEntity, String> {
	Optional<CobrancaEntity> findByCodigo(String codigo);
}
