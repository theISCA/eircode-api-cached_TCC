package com.fexco.address.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;

public interface PostcoderAddressClient {

	String address(@NotNull String apiKey,
			@NotNull String country,
			String eircodeOrAddressFrag,
			String format,
			List<MediaType> acceptFormat,
			Integer lines,
			Integer page,
			String include,
			String exclude,
			String addtags,
			String identifier,
			String callback,
			Boolean postcodeonly);

}
