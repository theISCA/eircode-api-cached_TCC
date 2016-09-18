package com.fexco.service.eircode;

import java.net.URI;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class EircodeMockClient implements EircodeClient {

	@Value("${eircode.url}")
	private String eircodeUrl;

	@Override
	public String address(@NotBlank String apiKey, @NotBlank String eircodeOrAddressFrag,
			 String format, MediaType acceptFormat, Integer lines, Integer page,
			 String include, String exclude, String addtags, String identifier,
			 String callback) {


		StringBuilder builder = new StringBuilder();
		builder.append(eircodeUrl);
		if (format != null)      builder.append("format=" + format);
		if (acceptFormat!= null) builder.append("acceptFormat=" + acceptFormat);
		if (lines != null)       builder.append("lines=" + lines);
		if (page != null)        builder.append("page=" + page);
		if (include != null)     builder.append("include=" + include);
		if (exclude != null)     builder.append("exclude=" + exclude);
		if (addtags != null)     builder.append("addtags=" + addtags);
		if (identifier != null)	 builder.append("identifier=" + identifier);
		if (callback != null)    builder.append("identifier=" + identifier);

		// TODO: Verify if it is present on the cache


	    return builder.toString();

	}

	@Override
	public URI generateAddressURI(String baseUrl, String apiKey, String eircodeOrAddressFrag, String format,
			Integer lines, Integer page, String include, String exclude, String addtags, String identifier,
			String callback) {
		// TODO Auto-generated method stub
		return null;
	}

}
