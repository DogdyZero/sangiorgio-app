package br.com.sangiorgio.app.infraestruture.api;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
	public static String asJsonString(final Object obj) throws Exception {
		return new ObjectMapper().writeValueAsString(obj);
	}
}
