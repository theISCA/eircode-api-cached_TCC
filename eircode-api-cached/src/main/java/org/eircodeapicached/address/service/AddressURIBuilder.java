package org.eircodeapicached.address.service;

import java.net.URI;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

@Validated
public class AddressURIBuilder {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public URI generateAddressURIFromUrl(@NotNull String baseUrl, @NotNull String apiKey, @
			NotNull String country, String eircodeOrAddressFrag,
			String format, Integer lines, Integer page, String include, String exclude,
			String addtags, String identifier, String callback, Boolean postcodeonly) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

		URI result = generate(apiKey, country, eircodeOrAddressFrag, format, lines, page, include, exclude, addtags,
				identifier, callback, postcodeonly, builder);

		log.trace(">> {}", result.toString());
		return result;
	}

	public URI generateAddressURIFromPath(@NotNull String baseUrl, @NotNull String apiKey, @
			NotNull String country, String eircodeOrAddressFrag,
			String format, Integer lines, Integer page, String include, String exclude,
			String addtags, String identifier, String callback, Boolean postcodeonly) {

		UriComponentsBuilder builder = UriComponentsBuilder.fromPath(baseUrl);

		return generate(apiKey, country, eircodeOrAddressFrag, format, lines, page, include, exclude, addtags,
				identifier, callback, postcodeonly, builder);
	}

	private URI generate(String apiKey, String country, String eircodeOrAddressFrag, String format, Integer lines,
			Integer page, String include, String exclude, String addtags, String identifier, String callback,
			Boolean postcodeonly, UriComponentsBuilder builder) {
		if (format != null)     builder.queryParam("format", format);
		if (lines != null)      builder.queryParam("lines", lines);
		if (page != null)       builder.queryParam("page", page);
		if (include != null)    builder.queryParam("include", include);
		if (exclude != null)    builder.queryParam("exclude", exclude);
		if (addtags != null)    builder.queryParam("addtags", addtags);
		if (identifier != null)	builder.queryParam("identifier", identifier);
		if (callback != null)   builder.queryParam("callback", callback);
		if (postcodeonly != null && "ie".equals(country))   builder.queryParam("postcodeonly", postcodeonly);

		URI uri = builder.buildAndExpand(apiKey, country, eircodeOrAddressFrag).encode().toUri();
		return uri;
	}
}
