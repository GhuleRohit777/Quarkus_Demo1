package org.acme.hibernate.orm.panache;







import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class JokeResourceTest {

    @Test
    public void getJokesList() {
        // Perform the GET request and validate the response
        given()
                .when()
                .get("/jokes?count=10")
                .then()
                .statusCode(200)
                .body(notNullValue())
                .body("size()", is(10)); // Validate that we get 10 jokes in the response
    }

    @Test
    public void getJokesListBadRequest() {
        // Perform the GET request with an invalid count and validate the response
        given()
                .when()
                .get("/jokes?count=-1")
                .then()
                .statusCode(400) // Expecting a Bad Request response
                .body(notNullValue()) // Validate that there is a response body
                .body("error", is("Count must be a positive number.")); // Optional: Add specific error message check if available
    }
}
