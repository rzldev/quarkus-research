package org.rzldev.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieResourceRestAssuredTest {

    @Test
    @Order(1)
    void getAll() {
        given()
                .when()
                .get("/movies")
                .then()
                .body("size()", equalTo(2))
                .body("id", hasItems(1, 2))
                .body("title", hasItems("Iron Man", "Garfield"))
                .body("description", hasItems("Iron Man description", "Garfield description"))
                .body("director", hasItems("Jon Favreau", "Peter Hewitt"))
                .body("country", hasItem("USA"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getMovieById() {
        given()
                .when()
                .get("/movies/2")
                .then()
                .body("id", equalTo(2))
                .body("title", equalTo("Garfield"))
                .body("description", equalTo("Garfield description"))
                .body("director", equalTo("Peter Hewitt"))
                .body("country", equalTo("USA"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getMovieByIdKO() {
        given()
                .when()
                .get("/movies/1000")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(3)
    void getMovieByTitle() {
        given()
                .when()
                .get("/movies/title/Gundala")
                .then()
                .body("title", equalTo("Gundala"))
                .body("description", equalTo("Gundala description"))
                .body("director", equalTo("Joko Anwar"))
                .body("country", equalTo("Indonesia"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(3)
    void getMoviesByTitleKO() {
        given()
                .when()
                .get("/movies/title/i3urroq")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(3)
    void getMovieByCountry() {
        given()
                .when()
                .get("/movies/country/Indonesia")
                .then()
                .body("title", hasItem("Gundala"))
                .body("description", hasItem("Gundala description"))
                .body("director", hasItem("Joko Anwar"))
                .body("country", hasItem("Indonesia"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(3)
    void getMovieByCountryKO() {
        given()
                .when()
                .get("/movies/country/Pluto")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(2)
    void createMovie() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("title", "Gundala");
        jsonObject.put("description", "Gundala description");
        jsonObject.put("director", "Joko Anwar");
        jsonObject.put("country", "Indonesia");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString())
                .when()
                .post("/movies")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(4)
    void updateMovie() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("title", "Gundala 2");
        jsonObject.put("description", "Gundala 2 description");
        jsonObject.put("director", "Joko Anwar");
        jsonObject.put("country", "Indonesia");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString())
                .when()
                .put("/movies/3")
                .then()
                .body("id", equalTo(3))
                .body("title", equalTo("Gundala 2"))
                .body("description", equalTo("Gundala 2 description"))
                .body("country", equalTo("Indonesia"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(5)
    void deleteMovie() {
        given()
                .when()
                .delete("/movies/3")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        given()
                .when()
                .delete("/movies/3")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(6)
    void deleteMovieKO() {
        given()
                .when()
                .delete("/movies/3000")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}