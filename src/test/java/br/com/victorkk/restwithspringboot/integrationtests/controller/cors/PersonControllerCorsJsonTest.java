package br.com.victorkk.restwithspringboot.integrationtests.controller.cors;

import br.com.victorkk.restwithspringboot.configs.TestConfigs;
import br.com.victorkk.restwithspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.victorkk.restwithspringboot.integrationtests.vo.AccountCredentialsTestVO;
import br.com.victorkk.restwithspringboot.integrationtests.vo.PersonTestVO;
import br.com.victorkk.restwithspringboot.integrationtests.vo.TokenTestVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerCorsJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static PersonTestVO person;

	@BeforeAll
	public static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonTestVO();
	}

	@Test
	@Order(0)
	public void authorization() {
		AccountCredentialsTestVO user = new AccountCredentialsTestVO("victor", "admin123");

		var accessToken = given()
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.body(user)
				.when().post()
				.then().statusCode(200).extract().body().as(TokenTestVO.class)
				.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws IOException {
		mockPerson();

		var content = given().spec(specification)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
				.contentType(TestConfigs.CONTENT_TYPE_XML)
					.body(person)
				.when().post()
				.then().statusCode(200).extract().body().asString();

		PersonTestVO persistedPerson = objectMapper.readValue(content, PersonTestVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getPersonId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getPersonId() > 0);
		assertEquals("Willian", persistedPerson.getFirstName());
		assertEquals("DubGod", persistedPerson.getLastName());
		assertEquals("Brasil", persistedPerson.getAddress());
		assertEquals("M", persistedPerson.getGender());
	}

	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() {
		mockPerson();

		var content = given().spec(specification)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, "https://stackoverflow.com/")
				.contentType(TestConfigs.CONTENT_TYPE_XML)
					.body(person)
				.when().post()
				.then().statusCode(403).extract().body().asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(3)
	public void testFindById() throws IOException {
		mockPerson();

		var content = given().spec(specification)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("id", person.getPersonId())
				.when().get("{id}")
				.then().statusCode(200).extract().body().asString();

		PersonTestVO persistedPerson = objectMapper.readValue(content, PersonTestVO.class);
		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getPersonId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getPersonId() > 0);
		assertEquals("Willian", persistedPerson.getFirstName());
		assertEquals("DubGod", persistedPerson.getLastName());
		assertEquals("Brasil", persistedPerson.getAddress());
		assertEquals("M", persistedPerson.getGender());
	}

	@Test
	@Order(4)
	public void testFindByIdWithWrongOrigin() {
		mockPerson();

		var content = given().spec(specification)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, "https://stackoverflow.com/")
				.contentType(TestConfigs.CONTENT_TYPE_XML)
					.pathParam("id", person.getPersonId())
				.when().get("{id}")
				.then().statusCode(403).extract().body().asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	private void mockPerson() {
		person.setFirstName("Willian");
		person.setLastName("DubGod");
		person.setAddress("Brasil");
		person.setGender("M");
		person.setEnabled(true);
	}
}
