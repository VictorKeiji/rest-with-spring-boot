package br.com.victorkk.restwithspringboot.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.victorkk.restwithspringboot.configs.TestConfigs;
import br.com.victorkk.restwithspringboot.integrationtests.controller.withyaml.mapper.YMLMapper;
import br.com.victorkk.restwithspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.victorkk.restwithspringboot.integrationtests.vo.AccountCredentialsTestVO;
import br.com.victorkk.restwithspringboot.integrationtests.vo.BookTestVO;
import br.com.victorkk.restwithspringboot.integrationtests.vo.TokenTestVO;
import br.com.victorkk.restwithspringboot.integrationtests.vo.pagedmodels.PagedModelBook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;

    private static YMLMapper objectMapper;

    private static BookTestVO book;

    @BeforeAll
    public static void setup() {
        objectMapper = new YMLMapper();
        book = new BookTestVO();
    }

    @Test
    @Order(1)
    public void authorization() {
        AccountCredentialsTestVO user = new AccountCredentialsTestVO();
        user.setUsername("victor");
        user.setPassword("admin123");

        var token =
                given()
                        .config(
                                RestAssuredConfig
                                        .config()
                                        .encoderConfig(EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                        .basePath("/auth/signin")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .accept(TestConfigs.CONTENT_TYPE_YML)
                        .body(user, objectMapper)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenTestVO.class, objectMapper)
                        .getAccessToken();

        specification =
                new RequestSpecBuilder()
                        .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token)
                        .setBasePath("/api/book/v1")
                        .setPort(TestConfigs.SERVER_PORT)
                        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                        .build();
    }

    @Test
    @Order(2)
    public void testCreate() throws JsonMappingException, JsonProcessingException {

        mockBook();

        book = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .body(book, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookTestVO.class, objectMapper);

        assertNotNull(book.getBookId());
        assertNotNull(book.getTitle());
        assertNotNull(book.getAuthor());
        assertNotNull(book.getPrice());
        assertTrue(book.getBookId() > 0);
        assertEquals("Docker Deep Dive", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(55.99, book.getPrice());
    }

    @Test
    @Order(3)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {

        book.setTitle("Docker Deep Dive - Updated");

        BookTestVO bookUpdated = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .body(book, objectMapper)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookTestVO.class, objectMapper);

        assertNotNull(bookUpdated.getBookId());
        assertNotNull(bookUpdated.getTitle());
        assertNotNull(bookUpdated.getAuthor());
        assertNotNull(bookUpdated.getPrice());
        assertEquals(bookUpdated.getBookId(), book.getBookId());
        assertEquals("Docker Deep Dive - Updated", bookUpdated.getTitle());
        assertEquals("Nigel Poulton", bookUpdated.getAuthor());
        assertEquals(55.99, bookUpdated.getPrice());
    }

    @Test
    @Order(4)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        var foundBook = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", book.getBookId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookTestVO.class, objectMapper);

        assertNotNull(foundBook.getBookId());
        assertNotNull(foundBook.getTitle());
        assertNotNull(foundBook.getAuthor());
        assertNotNull(foundBook.getPrice());
        assertEquals(foundBook.getBookId(), book.getBookId());
        assertEquals("Docker Deep Dive - Updated", foundBook.getTitle());
        assertEquals("Nigel Poulton", foundBook.getAuthor());
        assertEquals(55.99, foundBook.getPrice());
    }

    @Test
    @Order(5)
    public void testDelete() {
        given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", book.getBookId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {
        var response = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .queryParams("page", 1 , "size", 5, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelBook.class, objectMapper);


        List<BookTestVO> books = response.getContent();

        BookTestVO foundBookOne = books.get(0);

        assertNotNull(foundBookOne.getBookId());
        assertNotNull(foundBookOne.getTitle());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getPrice());
        assertTrue(foundBookOne.getBookId() > 0);
        assertEquals("Refactoring", foundBookOne.getTitle());
        assertEquals("Martin Fowler e Kent Beck", foundBookOne.getAuthor());
        assertEquals(88.00, foundBookOne.getPrice());

        BookTestVO foundBookFive = books.get(4);

        assertNotNull(foundBookFive.getBookId());
        assertNotNull(foundBookFive.getTitle());
        assertNotNull(foundBookFive.getAuthor());
        assertNotNull(foundBookFive.getPrice());
        assertTrue(foundBookFive.getBookId() > 0);
        assertEquals("O poder dos quietos", foundBookFive.getTitle());
        assertEquals("Susan Cain", foundBookFive.getAuthor());
        assertEquals(123.0, foundBookFive.getPrice());
    }

    private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(Double.valueOf(55.99));
        book.setLaunchDate(new Date());
    }
}