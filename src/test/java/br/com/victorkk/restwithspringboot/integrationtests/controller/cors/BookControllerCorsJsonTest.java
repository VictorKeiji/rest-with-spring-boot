package br.com.victorkk.restwithspringboot.integrationtests.controller.cors;

import br.com.victorkk.restwithspringboot.configs.TestConfigs;
import br.com.victorkk.restwithspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.victorkk.restwithspringboot.integrationtests.vo.AccountCredentialsTestVO;
import br.com.victorkk.restwithspringboot.integrationtests.vo.BookTestVO;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerCorsJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static BookTestVO book;

	@BeforeAll
	public static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		book = new BookTestVO();
	}

	@Test
	@Order(0)
	public void authorization() {
		AccountCredentialsTestVO user = new AccountCredentialsTestVO("victor", "admin123");

		var accessToken = given()
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
				.when().post()
				.then().statusCode(200).extract().body().as(TokenTestVO.class)
				.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws IOException {
		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
					.body(book)
				.when().post()
				.then().statusCode(200).extract().body().asString();

		BookTestVO persistedBook = objectMapper.readValue(content, BookTestVO.class);
		book = persistedBook;

		assertNotNull(persistedBook);

		assertNotNull(persistedBook.getBookId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());

		assertTrue(persistedBook.getBookId() > 0);
		assertEquals("William Shakespeare", persistedBook.getAuthor());
		assertEquals(45.0, persistedBook.getPrice());
		assertEquals("Hamlet", persistedBook.getTitle());
	}

	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() throws IOException {
		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, "https://stackoverflow.com/")
					.body(book)
				.when().post()
				.then().statusCode(403).extract().body().asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(3)
	public void testFindById() throws IOException {
		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
					.pathParam("bookId", book.getBookId())
				.when().get("{bookId}")
				.then().statusCode(200).extract().body().asString();

		BookTestVO persistedBook = objectMapper.readValue(content, BookTestVO.class);
		book = persistedBook;

		assertNotNull(persistedBook);

		assertNotNull(persistedBook.getBookId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());

		assertTrue(persistedBook.getBookId() > 0);
		assertEquals("William Shakespeare", persistedBook.getAuthor());
		assertEquals(45.0, persistedBook.getPrice());
		assertEquals("Hamlet", persistedBook.getTitle());
	}

	@Test
	@Order(4)
	public void testFindByIdWithWrongOrigin() throws IOException {
		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, "https://stackoverflow.com/")
					.pathParam("bookId", book.getBookId())
				.when().get("{bookId}")
				.then().statusCode(403).extract().body().asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	private void mockBook()  {
		book.setAuthor("William Shakespeare");
		book.setPrice(45.0);
		book.setTitle("Hamlet");
	}
}
