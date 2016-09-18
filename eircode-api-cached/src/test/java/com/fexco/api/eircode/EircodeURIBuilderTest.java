package com.fexco.api.eircode;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.net.URI;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fexco.service.eircode.EircodeClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EircodeURIBuilderTest {

	@Value("${eircode.address.url}")
	private String eircodeAddressUrl;

	@Value("${eircode.addressgeo.url}")
	private String eircodeAddressGeoUrl;

	@Value("${eircode.position.url}")
	private String eircodePositionUrl;

	@Value("${eircode.rgeo.url}")
	private String eircodeRGeoUrl;

	@Autowired
	EircodeClient eircodeRestClient;

	@Test
	public void testAddressUrlParameterPassing() throws Exception {

		URI uri = eircodeRestClient.generateAddressURI(eircodeAddressUrl, "apiKey",
				"eircodeOrAddressFrag", "format", 1,
				0, "include", "exclude", "addtags", "identifier", "callback");
		String url = uri.toString();

		assertThat(url, containsString("apiKey"));
		assertThat(url, containsString("eircodeOrAddressFrag"));
		assertThat(url, containsString("format=format"));
		assertThat(url, containsString("lines=1"));
		assertThat(url, containsString("page=0"));
		assertThat(url, containsString("include=include"));
		assertThat(url, containsString("exclude=exclude"));
		assertThat(url, containsString("addtags=addtags"));
		assertThat(url, containsString("identifier=identifier"));
		assertThat(url, containsString("callback=callback"));
	}

	@Test
	public void testAddressBaseUrlMissing() throws Exception {
		assertThatThrownBy(() -> eircodeRestClient.generateAddressURI(
				null, "apikey", "eircodeOrAddressFrag", "format", 1,
				0, "include", "exclude", "addtags", "identifier", "callback"))
        .isInstanceOf(ConstraintViolationException.class);
	}

	@Test
	public void testAddressApiKeyMissing() throws Exception {
		assertThatThrownBy(() -> eircodeRestClient.generateAddressURI(
				eircodeAddressUrl, null, "eircodeOrAddressFrag", "format", 1,
				0, "include", "exclude", "addtags", "identifier", "callback"))
        .isInstanceOf(ConstraintViolationException.class);
	}
}
