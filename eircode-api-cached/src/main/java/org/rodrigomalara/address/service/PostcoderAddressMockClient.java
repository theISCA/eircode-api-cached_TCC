package org.rodrigomalara.address.service;

import java.util.List;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Just echoes back relevant path, request parameters and request headers received
 * Allows to check if parameters are being received and managed correctly.
 *
 * Useful for testing REST Webservice without having to call Postcoder service.
 *
 * @author Rodrigo
 */
@Component("addressMockClient")
@Validated
public class PostcoderAddressMockClient implements PostcoderAddressClient {

	@Value("${postcoder.address.path}")
	private String addressLookupPath;

	@Override
	public String address(@NotNull String apiKey, @NotNull String country,
			 String eircodeOrAddressFrag, String format, List<MediaType> acceptFormat,
			 Integer lines, Integer page, String include, String exclude,
			 String addtags, String identifier, String callback, Boolean postcodeonly) {

		String url = addressLookupPath.replaceAll(Pattern.quote("{api-key}"), apiKey);
		url = url.replaceAll(Pattern.quote("{country}"), country);
		url = url.replaceAll(Pattern.quote("{eircode-or-address-fragment}"), eircodeOrAddressFrag);

		StringBuilder builder = new StringBuilder();
		builder.append(url + "?a=a");
		if (format != null)       builder.append("&format=" + format);
		if (acceptFormat!= null)  builder.append("&acceptFormat=" + acceptFormat);
		if (lines != null)        builder.append("&lines=" + lines);
		if (page != null)         builder.append("&page=" + page);
		if (include != null)      builder.append("&include=" + include);
		if (exclude != null)      builder.append("&exclude=" + exclude);
		if (addtags != null)      builder.append("&addtags=" + addtags);
		if (identifier != null)	  builder.append("&identifier=" + identifier);
		if (callback != null)     builder.append("&callback=" + callback);
		if (postcodeonly != null) builder.append("&postcodeonly=" + postcodeonly);

	    return builder.toString();
	}
}
