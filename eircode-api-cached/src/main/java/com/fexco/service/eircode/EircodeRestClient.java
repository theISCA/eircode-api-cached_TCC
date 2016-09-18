package com.fexco.service.eircode;

import java.net.URI;
import java.util.Arrays;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Validated
public class EircodeRestClient implements EircodeClient {

	@Value("${eircode.address.url}")
	private String eircodeAddressUrl;

	@Value("${eircode.addressgeo.url}")
	private String eircodeAddressGeoUrl;

	@Value("${eircode.position.url}")
	private String eircodePositionUrl;

	@Value("${eircode.rgeo.url}")
	private String eircodeRGeoUrl;

	@Override
	public String address(@NotBlank String apiKey, @NotBlank String eircodeOrAddressFrag,
			 String format, MediaType acceptFormat, Integer lines, Integer page,
			 String include, String exclude, String addtags, String identifier,
			 String callback) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		if (acceptFormat != null) {
		  headers.setAccept(Arrays.asList(acceptFormat));
		}

		URI uri = generateAddressURI(eircodeAddressUrl, apiKey, eircodeOrAddressFrag,
				format, lines, page, include, exclude, addtags, identifier,
				callback);

		// TODO: Verify if it is present on the cache

		HttpEntity<?> entity = new HttpEntity<>(headers);

		HttpEntity<String> response = restTemplate.exchange(
				uri, HttpMethod.GET, entity, String.class);

	    return response.getBody();

	}

	public URI generateAddressURI(@NotBlank String baseUrl, @NotBlank String apiKey, @NotBlank String eircodeOrAddressFrag,
			String format, Integer lines, Integer page, String include, String exclude,
			String addtags, String identifier, String callback) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
		builder.buildAndExpand(apiKey, eircodeOrAddressFrag);
		if (format != null)     builder.queryParam("format", format);
		if (lines != null)      builder.queryParam("lines", lines);
		if (page != null)       builder.queryParam("page", page);
		if (include != null)    builder.queryParam("include", include);
		if (exclude != null)    builder.queryParam("exclude", exclude);
		if (addtags != null)    builder.queryParam("addtags", addtags);
		if (identifier != null)	builder.queryParam("identifier", identifier);
		if (callback != null)   builder.queryParam("identifier", identifier);

		URI uri = builder.build().encode().toUri();
		return uri;
	}

}
