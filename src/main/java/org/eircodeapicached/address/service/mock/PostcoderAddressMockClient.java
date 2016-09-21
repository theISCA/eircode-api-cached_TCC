package org.eircodeapicached.address.service.mock;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.eircodeapicached.address.service.PostcoderAddressClient;
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

	private static String COUNTRY_UK = "uk";
	private static String COUNTRY_IE = "ie";

	private static String FORMAT_XML = "xml";

	@Value("${postcoder.address.path}")
	private String addressLookupPath;

	@Override
	public String address(@NotNull String apiKey, @NotNull String country,
			 String eircodeOrAddressFrag, String format, List<MediaType> acceptFormat,
			 Integer lines, Integer page, String include, String exclude,
			 String addtags, String identifier, String callback, Boolean postcodeonly) {

		if (COUNTRY_IE.contains(country)) {
			if ("D02X285".equals(eircodeOrAddressFrag)) {
				if (FORMAT_XML.equals(format)) {
					return "<Addresses xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n  <Address>\r\n    <addressline1>Department of Communications, Energy and Natural Resources</addressline1>\r\n    <addressline2>Adelaide Road</addressline2>\r\n    <summaryline>Department of Communications, Energy and Natural Resources, Adelaide Road, Dublin 2, D02 X285</summaryline>\r\n    <organisation>Department of Communications, Energy and Natural Resources</organisation>\r\n    <street>Adelaide Road</street>\r\n    <posttown>Dublin 2</posttown>\r\n    <county>Dublin</county>\r\n    <postcode>D02 X285</postcode>\r\n  </Address>\r\n</Addresses>";
				} else {
					return "[\r\n  {\r\n    \"addressline1\":\"Department of Communications, Energy and Natural Resources\",\r\n    \"addressline2\":\"Adelaide Road\",\r\n    \"summaryline\":\"Department of Communications, Energy and Natural Resources, Adelaide Road, Dublin 2, D02 X285\",\r\n    \"organisation\":\"Department of Communications, Energy and Natural Resources\",\r\n    \"street\":\"Adelaide Road\",\r\n    \"posttown\":\"Dublin 2\",\r\n    \"county\":\"Dublin\",\r\n    \"postcode\":\"D02 X285\"\r\n  }\r\n]";
				}
			}
		} else if (COUNTRY_UK.contains(country)) {
			if ("NR147PZ".equals(eircodeOrAddressFrag)) {
				if (FORMAT_XML.equals(format)) {
					return "<Addresses xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n  <Address>\r\n    <summaryline>\r\n    Allies Computing Ltd, Manor Farm Barns, Fox Road, Framingham Pigot, Norwich, Norfolk, NR14 7PZ\r\n    </summaryline>\r\n    <organisation>Allies Computing Ltd</organisation>\r\n    <buildingname>Manor Farm Barns</buildingname>\r\n    <premise>Manor Farm Barns</premise>\r\n    <street>Fox Road</street>\r\n    <dependentlocality>Framingham Pigot</dependentlocality>\r\n    <posttown>Norwich</posttown>\r\n    <county>Norfolk</county>\r\n    <postcode>NR14 7PZ</postcode>\r\n  </Address>\r\n</Addresses>";
				} else {
					return "[\r\n  {\r\n    \"summaryline\":\"Allies Computing Ltd, Manor Farm Barns, Fox Road, Framingham Pigot, Norwich, Norfolk, NR14 7PZ\",\r\n    \"organisation\":\"Allies Computing Ltd\",\r\n    \"buildingname\":\"Manor Farm Barns\",\r\n    \"premise\":\"Manor Farm Barns\",\r\n    \"street\":\"Fox Road\",\r\n    \"dependentlocality\":\"Framingham Pigot\",\r\n    \"posttown\":\"Norwich\",\r\n    \"county\":\"Norfolk\",\r\n    \"postcode\":\"NR14 7PZ\"\r\n  }\r\n]";
				}
			}
			if ("manor farm barns".equals(eircodeOrAddressFrag)) {
				if (FORMAT_XML.equals(format)) {
					return "<Addresses xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n  <Address>\r\n    <summaryline>\r\n    Allies Computing Ltd, Manor Farm Barns, Fox Road, Framingham Pigot, Norwich, Norfolk, NR14 7PZ\r\n    </summaryline>\r\n    <organisation>Allies Computing Ltd</organisation>\r\n    <buildingname>Manor Farm Barns</buildingname>\r\n    <premise>Manor Farm Barns</premise>\r\n    <street>Fox Road</street>\r\n    <dependentlocality>Framingham Pigot</dependentlocality>\r\n    <posttown>Norwich</posttown>\r\n    <county>Norfolk</county>\r\n    <postcode>NR14 7PZ</postcode>\r\n  </Address>\r\n</Addresses>";
				} else {
					return "[\r\n  {\r\n    \"addressline1\":\"Allies Computing Ltd\",\r\n    \"addressline2\":\"Manor Farm Barns, Fox Road\",\r\n    \"addressline3\":\"Framingham Pigot\",\r\n    \"summaryline\":\"Allies Computing Ltd, Manor Farm Barns, Fox Road, Framingham Pigot, Norwich, Norfolk, NR14 7PZ\",\r\n    \"organisation\":\"Allies Computing Ltd\",\r\n    \"buildingname\":\"Manor Farm Barns\",\r\n    \"premise\":\"Manor Farm Barns\",\r\n    \"street\":\"Fox Road\",\r\n    \"dependentlocality\":\"Framingham Pigot\",\r\n    \"posttown\":\"Norwich\",\r\n    \"county\":\"Norfolk\",\r\n    \"postcode\":\"NR14 7PZ\"\r\n  }\r\n]";
				}
			}
		}

	    return "empty mock response";
	}
}
