package com.i2iacademy.testautomation.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostsApiTest {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    void getSinglePostReturnsExpectedFields() {
        given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", notNullValue())
                .body("title", notNullValue());
    }

    @Test
    void getSinglePostRespondsWithinAcceptableTime() {
        Response response = given()
                .when()
                .get("/posts/1");

        assertEquals(200, response.statusCode());
        assertTrue(response.getTime() < 5000, "Response time should be under 5000 ms");
    }

    @Test
    void createPostReturnsCreatedStatusWithEchoedBody() {
        String requestBody = """
            {
                "title": "i2i academy test automation",
                "body": "created via rest-assured",
                "userId": 10
            }
            """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("title", equalTo("i2i academy test automation"))
                .body("userId", equalTo(10));
    }
}