package br.com.victorkk.restwithspringboot.integrationtests.controller.withyaml;


import br.com.victorkk.restwithspringboot.configs.TestConfigs;
import br.com.victorkk.restwithspringboot.integrationtests.controller.withyaml.mapper.YMLMapper;
import br.com.victorkk.restwithspringboot.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.victorkk.restwithspringboot.integrationtests.vo.AccountCredentialsTestVO;
import br.com.victorkk.restwithspringboot.integrationtests.vo.TokenTestVO;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static YMLMapper objectMapper;
    private static TokenTestVO tokenVO;

    @BeforeAll
    public static void setup() {
        objectMapper = new YMLMapper();
    }

    @Test
    @Order(1)
    public void testSignIn() {
        AccountCredentialsTestVO user = new AccountCredentialsTestVO("victor", "admin123");

        tokenVO = given()
                .config(RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(user, objectMapper)
                .when().post()
                .then().statusCode(200).extract().body().as(TokenTestVO.class, objectMapper);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(2)
    public void testRefresh() {
        AccountCredentialsTestVO user = new AccountCredentialsTestVO("victor", "admin123");

        var newTokenVO = given()
                .config(RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("username", tokenVO.getUsername())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when().put("{username}")
                .then().statusCode(200).extract().body().as(TokenTestVO.class, objectMapper);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }
}
