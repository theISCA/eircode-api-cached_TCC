package org.eircodeapicached.address.service;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.cache.annotation.CacheResult;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

@Component("addressRestCachedClient")
@Validated
public class PostcoderAddressRestCachedClient implements PostcoderAddressClient {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${postcoder.address.host}")
	private String addressLookupHost;

	@Value("${postcoder.address.path}")
	private String addressLookupPath;

	@Autowired
	private AddressURIBuilder uriBuilder;

	@Override
	@CacheResult(cacheName="addressCache")
	public String address(@NotNull String apiKey, @NotNull String country, String eircodeOrAddressFrag,
			 String format, List<MediaType> acceptFormat, Integer lines, Integer page,
			 String include, String exclude, String addtags, String identifier,
			 String callback, Boolean postcodeonly) {

		log.debug("About to generate REST service URI");
		URI uri = uriBuilder.generateAddressURIFromUrl(addressLookupHost + addressLookupPath,
				apiKey, country, eircodeOrAddressFrag, format, lines, page,
				include, exclude, addtags, identifier, callback, postcodeonly);
		log.debug("REST service URI generated");

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		if (acceptFormat != null) {
		  log.debug("Setting Accept HTTP header before invoking Postcoder service");
		  headers.setAccept(acceptFormat);
		}

		HttpEntity<?> entity = new HttpEntity<>(headers);
		log.debug("About to invoke Postcoder service...");

		Date before = new Date();
		HttpEntity<String> response = restTemplate.exchange(
				uri, HttpMethod.GET, entity, String.class);
		Date after = new Date();

		String responseBody = response.getBody();
		log.trace(">> {}", responseBody);
		log.debug("Service invoked in {} ms", after.getTime() - before.getTime());

	    return responseBody;
	}
}
