package com.fexco.api.eircode;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;

import com.fexco.service.eircode.EircodeRestClient;

@RunWith(SpringRunner.class)
public class TestEircodeURIBuilder {

	@Value("${eircode.address.url}")
	private String eircodeAddressUrl;

	@Value("${eircode.addressgeo.url}")
	private String eircodeAddressGeoUrl;

	@Value("${eircode.position.url}")
	private String eircodePositionUrl;

	@Value("${eircode.rgeo.url}")
	private String eircodeRGeoUrl;

	@Autowired
	EircodeRestClient eircodeRestClient;


	@Test
	public void testAddressUrlParameterPassing() throws Exception {

		URI uri = eircodeRestClient.generateAddressURI(eircodeAddressUrl, "apiKey",
				"eircodeOrAddressFrag", "format", 1,
				0, "include", "exclude", "addtags", "identifier", "callback");
		String url = uri.toString();

		assertThat(url, containsString("/apiKey/"));
		assertThat(url, containsString("/eircodeOrAddressFrag"));
		assertThat(url, containsString("format=format"));
		assertThat(url, containsString("lines=1"));
		assertThat(url, containsString("page=0"));
		assertThat(url, containsString("include=include"));
		assertThat(url, containsString("exclude=exclude"));
		assertThat(url, containsString("addtags=addtags"));
		assertThat(url, containsString("identifier=identifier"));
		assertThat(url, containsString("callback=callback"));


	}

	@Test(expected=Exception.class)
	public void testAddressBaseUrlMissing() throws Exception {
		eircodeRestClient.generateAddressURI(eircodeAddressUrl, null,
				"eircodeOrAddressFrag", "format", 1,
				0, "include", "exclude", "addtags", "identifier", "callback");
	}

	@Test
	public void testApiKeyMissing() throws Exception {
		assertThatThrownBy(() -> eircodeRestClient.generateAddressURI(
				eircodeAddressUrl, null, "eircodeOrAddressFrag", "format", 1,
				0, "include", "exclude", "addtags", "identifier", "callback"))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Runtime exception occurred")
        .hasNoCause();

		;
	}

	@Test
	public void testAddressPathVariableMissing() throws Exception {

	}

	@Test
	public void testAddressGeoPathVariableMissing() throws Exception {

	}

	@Test
	public void testPositionPathVariableMissing() throws Exception {

	}

	@Test
	public void testRGeoPathVariableMissing() throws Exception {

	}
}
