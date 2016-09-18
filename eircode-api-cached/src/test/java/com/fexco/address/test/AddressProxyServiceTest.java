package com.fexco.address.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fexco.address.service.AddressURIBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AddressProxyServiceTest {

	@Value("${postcoder.address.path}")
	private String addressLookupPath;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AddressURIBuilder uriBuilder;

	@Test
	public void testAddressUrlParameterPassing() throws Exception {

		URI uri = uriBuilder.generateAddressURIFromPath(addressLookupPath, "123", "ie", "eircodeOrAddressFrag",
				"json", 1, 0, "include", "exclude", "addtags", "identifier", "callback", true);

		this.mockMvc.perform(get(uri.toString() + "&mock=true"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(header().string("Content-type", "application/json"))
		.andExpect(content().string(containsString("123")))
		.andExpect(content().string(containsString("ie")))
		.andExpect(content().string(containsString("eircodeOrAddressFrag")))
		.andExpect(content().string(containsString("format=json")))
		.andExpect(content().string(containsString("lines=1")))
		.andExpect(content().string(containsString("page=0")))
		.andExpect(content().string(containsString("include=include")))
		.andExpect(content().string(containsString("exclude=exclude")))
		.andExpect(content().string(containsString("addtags=addtags")))
		.andExpect(content().string(containsString("identifier=identifier")))
		.andExpect(content().string(containsString("callback=callback")))
		.andExpect(content().string(containsString("postcodeonly=true")));
	}

	@Test
	public void testAcceptParameterJSON() throws Exception {

		URI uri = uriBuilder.generateAddressURIFromPath(addressLookupPath, "123", "ie", "eircodeOrAddressFrag",
				"json", null, null, null, null, null, null, null, null );

		this.mockMvc.perform(get(uri.toString() + "&mock=true"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(header().string("Content-type", "application/json"))
		.andExpect(content().string(containsString("123")))
		.andExpect(content().string(containsString("ie")))
		.andExpect(content().string(containsString("eircodeOrAddressFrag")))
		.andExpect(content().string(containsString("format=json")));
	}

	@Test
	public void testAcceptParameterXML() throws Exception {

		URI uri = uriBuilder.generateAddressURIFromPath(addressLookupPath, "123", "ie", "eircodeOrAddressFrag",
				"xml", null, null, null, null, null, null, null, null );

		this.mockMvc.perform(get(uri.toString() + "&mock=true"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(header().string("Content-type", "application/xml"))
		.andExpect(content().string(containsString("123")))
		.andExpect(content().string(containsString("ie")))
		.andExpect(content().string(containsString("eircodeOrAddressFrag")))
		.andExpect(content().string(containsString("format=xml")));
	}

	@Test
	public void testAcceptHeaderXML() throws Exception {

		URI uri = uriBuilder.generateAddressURIFromPath(addressLookupPath, "123", "ie", "eircodeOrAddressFrag",
				null, null, null, null, null, null, null, null, null );

		this.mockMvc.perform(get(uri.toString() + "?mock=true").header("Accept", "application/xml"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string("Content-type", "application/xml"))
			.andExpect(content().string(containsString("123")))
			.andExpect(content().string(containsString("ie")))
			.andExpect(content().string(containsString("eircodeOrAddressFrag")));
	}

	/**
	 * Test retrieving a valid Eircode using the AddressProxyService.
	 * Assumes only that the Eircode doesn't change and that there at least one result available.
	 *
	 * Should work the same as invoking the endpoint below:
	 * http://ws.postcoder.com/pcw/PCW45-12345-12345-1234X/address/ie/D02X285?lines=3&format=json
	 *
	 * @throws Exception
	 */
	@Test
	public void testValidEircodeQueryJSON() throws Exception {
		String serviceUrl = "/pcw/PCW45-12345-12345-1234X/address/ie/D02X285?lines=3&format=json";

		this.mockMvc.perform(get(serviceUrl))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string("Content-type", "application/json"))
			.andExpect(jsonPath("$[0].postcode", equalTo("D02 X285")));

	}

	/**
	 * Test retrieving a valid Eircode using the AddressProxyService.
	 * Assumes only that the Eircode doesn't change and that there at least one result available.
	 *
	 * Should work the same as invoking the endpoint below:
	 * http://ws.postcoder.com/pcw/PCW45-12345-12345-1234X/address/ie/D02X285?lines=3&format=xml
	 *
	 * @throws Exception
	 */
	@Test
	public void testValidEircodeQueryXML() throws Exception {
		String serviceUrl = "/pcw/PCW45-12345-12345-1234X/address/ie/D02X285?lines=3&format=xml";

		this.mockMvc.perform(get(serviceUrl))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string("Content-type", "application/xml"))
			.andExpect(content().string(containsString("<postcode>D02 X285</postcode>")));
	}

	/**
	 * Test retrieving a valid Eircode using the AddressProxyService.
	 * Assumes only that the Eircode doesn't change and that there at least one result available.
	 *
	 * Should work the same as invoking the endpoint below:
	 * http://ws.postcoder.com/pcw/PCW45-12345-12345-1234X/address/uk/NR147PZ?format=xml
	 *
	 * @throws Exception
	 */
	@Test
	public void testValidPostcodeQueryXML() throws Exception {
		String serviceUrl = "/pcw/PCW45-12345-12345-1234X/address/uk/NR147PZ?format=xml";

		this.mockMvc.perform(get(serviceUrl))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string("Content-type", "application/xml"))
			.andExpect(content().string(containsString("<postcode>NR14 7PZ</postcode>")));

	}

	/**
	 * Test retrieving a valid UK postcode using the AddressProxyService.
	 * Assumes only that the Eircode doesn't change and that there at least one result available.
	 *
	 * Should work the same as invoking the endpoint below:
	 * http://ws.postcoder.com/pcw/PCW45-12345-12345-1234X/address/uk/manor%20farm%20barns?format=xml
	 * @throws Exception
	 */
	@Test
	public void testValidLocationQueryXML() throws Exception {
		String serviceUrl = "/pcw/PCW45-12345-12345-1234X/address/uk/manor%20farm%20barns?format=xml";

		this.mockMvc.perform(get(serviceUrl))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string("Content-type", "application/xml"))
			.andExpect(content().string(containsString("<buildingname>Manor Farm Barns</buildingname>")));

	}

	/**
	 * Test retrieving a valid UK postcode using the AddressProxyService.
	 * Assumes only that the Eircode doesn't change and that there at least one result available.
	 *
	 * Should work the same as invoking the endpoint below:
	 * http://ws.postcoder.com/pcw/PCW45-12345-12345-1234X/address/uk/NR147PZ?format=json&lines=3
	 *
	 * @throws Exception
	 */
	@Test
	public void testValidPostcodeQueryJSON() throws Exception {
		String serviceUrl = "/pcw/PCW45-12345-12345-1234X/address/uk/NR147PZ?format=json&lines=3";

		this.mockMvc.perform(get(serviceUrl))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(header().string("Content-type", "application/json"))
			.andExpect(jsonPath("$[0].postcode", equalTo("NR14 7PZ")));

	}
}
