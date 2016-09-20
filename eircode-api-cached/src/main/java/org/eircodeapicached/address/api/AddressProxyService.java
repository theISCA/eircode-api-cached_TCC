package org.eircodeapicached.address.api;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.eircodeapicached.address.service.PostcoderAddressClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * http://ws.postcoder.com/pcw/[api-key]/address/ie/[eircode-or-address-fragment]
 */
@Controller
public class AddressProxyService {

	private static final String REQ_PARAM_FORMAT_JSON = "json";
	private static final String REQ_PARAM_FORMAT_XML = "xml";

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name="addressRestCachedClient")
	private PostcoderAddressClient addressClient;

	@Resource(name="addressMockClient")
	private PostcoderAddressClient mockClient;

	@RequestMapping(path="/pcw/{apiKey}/address/{country}/{eircodeOrAddressFrag}",
			method=RequestMethod.GET)
	public ResponseEntity<String> address(
			@PathVariable String apiKey,
			@PathVariable String country,
			@PathVariable String eircodeOrAddressFrag,
			@RequestParam(required=false) String format,
			@RequestHeader(name="Accept", required=false, defaultValue="application/json")
			  List<MediaType> acceptFormat,
			@RequestParam(required=false) Integer lines,
			@RequestParam(required=false) Integer page,
			@RequestParam(required=false) String include,
			@RequestParam(required=false) String exclude,
			@RequestParam(required=false) String addtags,
			@RequestParam(required=false) String identifier,
			@RequestParam(required=false) String callback,
			@RequestParam(required=false) Boolean postcodeonly,
			@RequestParam(required=false) Boolean mock) {

		log.debug("New request received");
		log.debug("[api-key] {} [country] {} [code/addr] {}", apiKey, country, eircodeOrAddressFrag);

		MediaType responseContentType;
		if (REQ_PARAM_FORMAT_JSON.equals(format)) {
	      responseContentType = MediaType.APPLICATION_JSON;
		} else if (REQ_PARAM_FORMAT_XML.equals(format) ||
				checkAcceptHeader(acceptFormat, MediaType.APPLICATION_XML)) {
			responseContentType = MediaType.APPLICATION_XML;
		} else {
			responseContentType = MediaType.APPLICATION_JSON;
			log.debug("Defaulting response Content-Type");
		}

		log.debug("Response Content-Type header: {}", responseContentType.toString());

		String response;
		if (!Boolean.TRUE.equals(mock)) {
			response = addressClient.address(apiKey, country, eircodeOrAddressFrag,
				format, acceptFormat, lines, page, include, exclude,
				addtags, identifier, callback, postcodeonly);
		} else {
			log.debug("Request parameter mock=true detected. Using mock client to render response.");
			response = mockClient.address(apiKey, country, eircodeOrAddressFrag,
					format, acceptFormat, lines, page, include, exclude,
					addtags, identifier, callback, postcodeonly);
		}

		final HttpHeaders httpHeaders= new HttpHeaders();
		httpHeaders.setContentType(responseContentType);
		return new ResponseEntity<String>(response, httpHeaders, HttpStatus.OK);
	}

	private boolean checkAcceptHeader(@NotNull List<MediaType> mediaTypes, MediaType mt) {
		for (MediaType mediaType : mediaTypes) {
			if (mt.equals(mediaType)) {
				return true;
			}
		}
		return false;
	}

}
