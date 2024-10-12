package br.com.sangiorgio.app.application.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface QueueGateway {
	void sendQueue(Object object) throws JsonProcessingException;
}
