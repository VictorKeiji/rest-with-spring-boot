package br.com.victorkk.restwithspringboot.integrationtests.controller.withxml;

import br.com.victorkk.data.vo.v1.security.TokenVO;
import br.com.victorkk.restwithspringboot.configs.TestConfigs;
import br.com.victorkk.restwithspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.victorkk.restwithspringboot.integrationtests.vo.AccountCredentialsTestVO;
import br.com.victorkk.restwithspringboot.integrationtests.vo.PersonTestVO;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static XmlMapper objectMapper;
	private static PersonTestVO person;

	@BeforeAll
	public static void setUp() {
		objectMapper = new XmlMapper();
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
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.body(user)
				.when().post()
				.then().statusCode(200).extract().body().as(TokenVO.class)
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
	public void testCreate() throws JsonProcessingException {
		mockPerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
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
		assertTrue(persistedPerson.getEnabled());

		assertTrue(persistedPerson.getPersonId() > 0);
		assertEquals("Willian", persistedPerson.getFirstName());
		assertEquals("DubGod", persistedPerson.getLastName());
		assertEquals("Brasil", persistedPerson.getAddress());
		assertEquals("M", persistedPerson.getGender());
	}

	@Test
	@Order(2)
	public void testUpdate() throws JsonProcessingException {
		person.setLastName("do Bigode");

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
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

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("id", person.getPersonId())
				.when().patch("{id}")
				.then().statusCode(200).extract().body().asString();

		PersonTestVO persistedPerson = objectMapper.readValue(content, PersonTestVO.class);
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
	public void testFindById() throws JsonProcessingException {
		mockPerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
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
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.pathParam("id", person.getPersonId())
				.when().delete("{id}")
				.then().statusCode(204);
	}

	@Test
	@Order(6)
	public void testFindAll() throws JsonProcessingException {
		mockPerson();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.when().get()
				.then().statusCode(200).extract().body().asString();

		List<PersonTestVO> people = objectMapper.readValue(content, new TypeReference<List<PersonTestVO>>() {});

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
				.contentType(TestConfigs.CONTENT_TYPE_XML)
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
