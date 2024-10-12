package br.com.sangiorgio.app.infraestruture.configuration;

import br.com.sangiorgio.app.domain.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(RegraNegocioException.class)
	protected ResponseEntity<Object> handleRegraNegocioException(RegraNegocioException ex) {
		HttpStatus badRequest = HttpStatus.BAD_REQUEST;
		Map<String, String> errorMessage = Map.of("statusCode", Integer.toString(badRequest.value()), "errorMessage",
				ex.getMessage());
		return ResponseEntity.status(badRequest)
				.body(errorMessage);
	}
}
