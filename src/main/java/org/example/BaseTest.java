package org.example;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class BaseTest {

    @Test
    public void checkBaseGetTest() {

        RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

        given()
                .param("user", "123")
                .when()
                .get("https://a5a6ff47-6f34-4d17-9ad7-0c3b87ac07b5.mock.pstmn.io/auth/getToken")
                .then()
                .log()
                .body();

        //given().when().get("https://a5a6ff47-6f34-4d17-9ad7-0c3b87ac07b5.mock.pstmn.io/auth/getToken?user=123");

        Response response = given()
                .param("user", "123")
                .when()
                .get("https://a5a6ff47-6f34-4d17-9ad7-0c3b87ac07b5.mock.pstmn.io/auth/getToken");

        System.out.println(response.asString());

        response
                .then()
                .statusCode(200);

    }

    @Test
    public void checkBasePostTest() {
        String userId = "123456";
        String json = "{\n" +
                " \"id\": " + userId + "" +
                "\n }";

        Response resp = given()
                .baseUri("https://a5a6ff47-6f34-4d17-9ad7-0c3b87ac07b5.mock.pstmn.io/api/v1/client/orders")
                .header("Content-Type", "application/json")
                .body(json)
                .post();

        //System.out.println(resp.body().asString());

        resp
                .then()
                .body("orders.id[0]", equalTo(100500))
                .body("orders.name[1]", equalTo("eat"));
    }

    @Test
    public void checkJsonSchema() {

        String userId = "123456";
        String json = "{\n" +
                " \"id\": " + userId + "" +
                "\n }";

        ValidatableResponse validatableResponse = given()
                .baseUri("https://a5a6ff47-6f34-4d17-9ad7-0c3b87ac07b5.mock.pstmn.io/api/v1/client/orders")
                .header("Content-Type", "application/json")
                .body(json)
                .post()
                .then();

        validatableResponse
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schema.json"));

    }
}
