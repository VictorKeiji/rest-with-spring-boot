package br.com.victorkk.restwithspringboot.integrationtests.controller.withyaml;

import br.com.victorkk.restwithspringboot.configs.TestConfigs;
import br.com.victorkk.restwithspringboot.integrationtests.controller.withyaml.mapper.YMLMapper;
import br.com.victorkk.restwithspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.victorkk.restwithspringboot.integrationtests.vo.AccountCredentialsTestVO;
import br.com.victorkk.restwithspringboot.integrationtests.vo.PersonTestVO;
import br.com.victorkk.restwithspringboot.integrationtests.vo.TokenTestVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static YMLMapper objectMapper;
	private static PersonTestVO person;

	@BeforeAll
	public static void setUp() {
		objectMapper = new YMLMapper();
		person = new PersonTestVO();
	}

	@Test
	@Order(0)
	public void authorization() {
		AccountCredentialsTestVO user = new AccountCredentialsTestVO("victor", "admin123");

		var accessToken = given()
				.config(RestAssuredConfig.config().encoderConfig(
						EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(user, objectMapper)
				.when().post()
				.then().statusCode(200).extract().body().as(TokenTestVO.class, objectMapper)
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

		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(
						EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(person, objectMapper)
				.when().post()
				.then().statusCode(200).extract().body().as(PersonTestVO.class, objectMapper);

		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getPersonId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());

		assertTrue(persistedPerson.getPersonId() > 0);
		assertEquals("Willian", persistedPerson.getFirstName());
		assertEquals("DubGod", persistedPerson.getLastName());
		assertEquals("Brasil", persistedPerson.getAddress());
		assertEquals("M", persistedPerson.getGender());
	}

	@Test
	@Order(2)
	public void testUpdate() throws IOException {
		person.setLastName("do Bigode");

		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(
						EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(person, objectMapper)
				.when().post()
				.then().statusCode(200).extract().body().as(PersonTestVO.class, objectMapper);

		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getPersonId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());

		assertEquals(person.getPersonId(), persistedPerson.getPersonId());
		assertEquals("Willian", persistedPerson.getFirstName());
		assertEquals("do Bigode", persistedPerson.getLastName());
		assertEquals("Brasil", persistedPerson.getAddress());
		assertEquals("M", persistedPerson.getGender());
	}

	@Test
	@Order(3)
	public void testDisableById() throws IOException {

		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(
						EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getPersonId())
				.when().patch("{id}")
				.then().statusCode(200).extract().body().as(PersonTestVO.class, objectMapper);

		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getPersonId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertEquals(person.getPersonId(), persistedPerson.getPersonId());
		assertEquals("Willian", persistedPerson.getFirstName());
		assertEquals("do Bigode", persistedPerson.getLastName());
		assertEquals("Brasil", persistedPerson.getAddress());
		assertEquals("M", persistedPerson.getGender());
	}

	@Test
	@Order(4)
	public void testFindById() throws IOException {
		mockPerson();

		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(
						EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getPersonId())
				.when().get("{id}")
				.then().statusCode(200).extract().body().as(PersonTestVO.class, objectMapper);

		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getPersonId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertEquals(person.getPersonId(), persistedPerson.getPersonId());
		assertEquals("Willian", persistedPerson.getFirstName());
		assertEquals("do Bigode", persistedPerson.getLastName());
		assertEquals("Brasil", persistedPerson.getAddress());
		assertEquals("M", persistedPerson.getGender());
	}

	@Test
	@Order(5)
	public void testDelete() {

		given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(
						EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getPersonId())
				.when().delete("{id}")
				.then().statusCode(204);
	}

	@Test
	@Order(6)
	public void testFindAll() throws IOException {
		mockPerson();

		var content = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(
						EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when().get()
				.then().statusCode(200).extract().body().as(PersonTestVO[].class, objectMapper);

		List<PersonTestVO> people = Arrays.asList(content);

		PersonTestVO personOne = people.get(0);

		assertNotNull(personOne.getPersonId());
		assertNotNull(personOne.getFirstName());
		assertNotNull(personOne.getLastName());
		assertNotNull(personOne.getAddress());
		assertNotNull(personOne.getGender());
		assertTrue(personOne.getEnabled());

		assertEquals(1, personOne.getPersonId());
		assertEquals("João", personOne.getFirstName());
		assertEquals("Paulo", personOne.getLastName());
		assertEquals("Brasil", personOne.getAddress());
		assertEquals("M", personOne.getGender());

		PersonTestVO personSix = people.get(5);

		assertNotNull(personSix.getPersonId());
		assertNotNull(personSix.getFirstName());
		assertNotNull(personSix.getLastName());
		assertNotNull(personSix.getAddress());
		assertNotNull(personSix.getGender());
		assertTrue(personSix.getEnabled());

		assertEquals(6, personSix.getPersonId());
		assertEquals("Joaquim", personSix.getFirstName());
		assertEquals("Henrique", personSix.getLastName());
		assertEquals("Brasil", personSix.getAddress());
		assertEquals("M", personSix.getGender());
	}

	@Test
	@Order(7)
	public void testFindAllWithoutToken() {
		mockPerson();

		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given().spec(specificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.when().get()
				.then().statusCode(403);
	}

	private void mockPerson() {
		person.setFirstName("Willian");
		person.setLastName("DubGod");
		person.setAddress("Brasil");
		person.setGender("M");
		person.setEnabled(true);
	}
}
