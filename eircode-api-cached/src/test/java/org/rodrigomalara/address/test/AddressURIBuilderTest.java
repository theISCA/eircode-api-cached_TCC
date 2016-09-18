package org.rodrigomalara.address.test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.net.URI;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.rodrigomalara.address.service.AddressURIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressURIBuilderTest {

	@Value("${postcoder.address.host}")
	private String addressLookupHost;

	@Value("${postcoder.address.path}")
	private String addressLookupPath;

	@Autowired
	private AddressURIBuilder uriBuilder;

	@Test
	public void testAddressUrlParameterPassing() throws Exception {

		URI uri = uriBuilder.generateAddressURIFromUrl(addressLookupHost + addressLookupPath, "apiKey", "ie",
				"eircodeOrAddressFrag", "format", 1,
				0, "include", "exclude", "addtags", "identifier", "callback", true);
		String url = uri.toString();

		assertThat(url, containsString("apiKey"));
		assertThat(url, containsString("ie"));
		assertThat(url, containsString("eircodeOrAddressFrag"));
		assertThat(url, containsString("format=format"));
		assertThat(url, containsString("lines=1"));
		assertThat(url, containsString("page=0"));
		assertThat(url, containsString("include=include"));
		assertThat(url, containsString("exclude=exclude"));
		assertThat(url, containsString("addtags=addtags"));
		assertThat(url, containsString("identifier=identifier"));
		assertThat(url, containsString("callback=callback"));
		assertThat(url, containsString("postcodeonly=true"));
	}

	@Test
	public void testAddressUrlNoEirCode() throws Exception {

		URI uri = uriBuilder.generateAddressURIFromUrl(addressLookupHost + addressLookupPath, "apiKey", "ie",
				null, null, null, null, null, null, null, null, null, null);
		String url = uri.toString();

		assertThat(url, containsString("apiKey"));
		assertThat(url, containsString("ie"));
	}

	@Test
	public void testAddressUrlNoParameterPassing() throws Exception {

		URI uri = uriBuilder.generateAddressURIFromUrl(addressLookupHost + addressLookupPath, "apiKey", "ie",
				"eircodeOrAddressFrag", null, null, null, null, null, null, null, null, null);
		String url = uri.toString();

		assertThat(url, containsString("apiKey"));
		assertThat(url, containsString("ie"));
		assertThat(url, containsString("eircodeOrAddressFrag"));
	}

	@Test
	public void testAddressBaseUrlMissing() throws Exception {
		assertThatThrownBy(() -> uriBuilder.generateAddressURIFromUrl(
				null, "apikey", "ie", "eircodeOrAddressFrag", "format", 1,
				0, "include", "exclude", "addtags", "identifier", "callback", true))
        .isInstanceOf(ConstraintViolationException.class);
	}

	@Test
	public void testAddressApiKeyMissing() throws Exception {
		assertThatThrownBy(() -> uriBuilder.generateAddressURIFromUrl(
				addressLookupHost + addressLookupPath, null, "ie", "eircodeOrAddressFrag", "format", 1,
				0, "include", "exclude", "addtags", "identifier", "callback", true))
        .isInstanceOf(ConstraintViolationException.class);
	}

	@Test
	public void testAddressCountryMissing() throws Exception {
		assertThatThrownBy(() -> uriBuilder.generateAddressURIFromUrl(
				addressLookupHost + addressLookupPath, "apikey", null, "eircodeOrAddressFrag", "format", 1,
				0, "include", "exclude", "addtags", "identifier", "callback", true))
        .isInstanceOf(ConstraintViolationException.class);
	}
}
