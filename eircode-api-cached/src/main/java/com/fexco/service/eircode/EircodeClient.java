package com.fexco.service.eircode;

import java.net.URI;

import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;

public interface EircodeClient {

	String address(@NotNull String apiKey,
			@NotNull String eircodeOrAddressFrag,
			String format,
			MediaType acceptFormat,
			Integer lines,
			Integer page,
			String include,
			String exclude,
			String addtags,
			String identifier,
			String callback);

	URI generateAddressURI(@NotNull String baseUrl, @NotNull String apiKey,
			@NotNull String eircodeOrAddressFrag, String format, Integer lines,
			Integer page, String include, String exclude, String addtags,
			String identifier, String callback);

}
