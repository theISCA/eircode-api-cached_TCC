package com.fexco.service.eircode;

import java.net.URI;

import org.springframework.http.MediaType;

public interface EircodeClient {

	String address(String apiKey,
			String eircodeOrAddressFrag,
			String format,
			MediaType acceptFormat,
			Integer lines,
			Integer page,
			String include,
			String exclude,
			String addtags,
			String identifier,
			String callback);

	URI generateAddressURI(String baseUrl, String apiKey, String eircodeOrAddressFrag,
			String format, Integer lines, Integer page, String include, String exclude,
			String addtags, String identifier, String callback);

}
