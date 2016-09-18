package com.fexco.api.eircode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fexco.service.eircode.EircodeClient;

/**
 * http://ws.postcoder.com/pcw/[api-key]/address/ie/[eircode-or-address-fragment]  addtags=w3w
 */
@Controller
public class EircodeProxyService {

	private static final String REQ_PARAM_FORMAT_JSON = "json";
	private static final String REQ_PARAM_FORMAT_XML = "xml";

	@Autowired
	private EircodeClient eircodeClient;

	@RequestMapping(path="/pcw/{apiKey}/address/ie/{eircodeOrAddressFrag}",
			method=RequestMethod.GET)
	@ResponseBody
	public String address(
			@PathVariable String apiKey,
			@PathVariable String eircodeOrAddressFrag,
			@RequestParam(required=false) String format,
			@RequestHeader(name="Accept", required=false) MediaType acceptFormat,
			@RequestParam(required=false) Integer lines,
			@RequestParam(required=false) Integer page,
			@RequestParam(required=false) String include,
			@RequestParam(required=false) String exclude,
			@RequestParam(required=false) String addtags,
			@RequestParam(required=false) String identifier,
			@RequestParam(required=false) String callback) {

		// Figuring out the response type to set Content-Type header
		final HttpHeaders httpHeaders= new HttpHeaders();
		if (format == null || REQ_PARAM_FORMAT_JSON.equals(format)) {
	      httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		} else if (REQ_PARAM_FORMAT_XML.equals(format) ||
				MediaType.APPLICATION_XML.equals(acceptFormat)) {
		}

		return eircodeClient.address(apiKey, eircodeOrAddressFrag,
				format, acceptFormat, lines, page, include, exclude,
				addtags, identifier, callback);

	}

}
