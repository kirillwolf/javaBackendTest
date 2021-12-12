package com.gbhw3;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import java.sql.Statement;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImgurApiTests {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = ImgurApiParameters.API_URL + "/" + ImgurApiParameters.API_VERSION;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @DisplayName("Test for obtaining information about a picture")
    @Test
    @Order(1)
    void testImage() {
        // https://api.imgur.com/3/image/{{imageHash}}
        String imageHash = "BGlPTjV";
        String url = "image/" + imageHash;

        given().when()
                .auth()
                .oauth2(ImgurApiParameters.TOKEN)
                .header("Authorization", ImgurApiParameters.CLIENT_ID)
                .expect()
                .statusCode(is(200))
                .body("success", is(true))
                .body("data.id", is(imageHash))
                .when()
                .get(url);
    }

    @DisplayName("Image upload test from image link")
    @Test
    @Order(2)
    void testPostImageFromUrl() {
        // https://api.imgur.com/3/upload
        String url = "upload";
        String albumHash = "10914481/";
        String imgUrl = "https://imgur.com/a/BGlPTjV";
        String imgTitle = "The discomfort";
        String imgDescription = "cat and cactus";
        String imgName = "when-you-beg-for-a-treat-and-they-give-it-to-you.jpg";

        given().when()
                .auth()
                .oauth2(ImgurApiParameters.TOKEN)
                .header("Authorization", ImgurApiParameters.CLIENT_ID)
                .formParam("image", imgUrl)
                .formParam("album", albumHash)
                .formParam("type", "url")
                .formParam("name", imgName)
                .formParam("title", imgTitle)
                .formParam("description", imgDescription)
                .expect()
                .log().all()
                .statusCode(is(200))
                .body("success", is(true))
                .body("data.name", is(imgName))
                .body("data.title", is(imgTitle))
                .body("data.description", is(imgDescription))
                .when()
                .post(url);
    }

    @DisplayName("Test for loading a gif from a link to a gif")
    @Test
    @Order(3)
    void testPostImageGifFromUrl() {
        // https://api.imgur.com/3/upload
        String url = "upload";
        String albumHash = "Q2FHfrf";
        String gifUrl = "https://www.eatliver.com/hilarious-gif-animations/";
        String gifTitle = "internet";
        String gifDescription = "what now?";
        String gifName = "funny-gifs2.gif";

        given().when()
                .auth()
                .oauth2(ImgurApiParameters.TOKEN)
                .log().all()
                .formParam("image", gifUrl)
                .formParam("album", albumHash)
                .formParam("type", "url")
                .formParam("name", gifName)
                .formParam("title", gifTitle)
                .formParam("description", gifDescription)
                .expect()
                .log().all()
                .statusCode(is(200))
                .body("success", is(true))
                .body("data.name", is(gifName))
                .body("data.title", is(gifTitle))
                .body("data.description", is(gifDescription))
                .when()
                .post(url);
    }

    @DisplayName("Video download test from video link")
    @Test
    @Order(4)
    void testPostImageVideoFromUrl() {
        // https://api.imgur.com/3/upload
        String url = "upload";
        String albumHash = "Q2FHfrf";
        String videoUrl = ("https://www.hahaha.com/en/videos/huge-fight-errupted");
        String videoTitle = "hahah.com";
        String videoDescription = "hahaha";
        String videoName = "huge-fight-errupted.mp4";

        given().when()
                .auth()
                .oauth2(ImgurApiParameters.TOKEN)
                .log().all()
                .formParam("image", videoUrl)
                .formParam("album", albumHash)
                .formParam("type", "url")
                .formParam("name", videoName)
                .formParam("title", videoTitle)
                .formParam("description", videoDescription)
                .expect()
                .log().all()
                .statusCode(is(200))
                .body("success", is(true))
                .body("data.name", is(videoName))
                .body("data.title", is(videoTitle))
                .body("data.description", is(videoDescription))
                .when()
                .post(url);
    }

    @DisplayName("Тeats updates of the title and description of the image with authorization byClient-ID")
    @Test
    @Order(5)
    void testPostUpdateImageInformationUnAuthed() {
        // https://api.imgur.com/3/image/{{imageDeleteHash}}
        String albumHash = "Q2FHfrf";
        String urlForDeleteHash = "album/" + albumHash + "/images";
        String imageDeleteHash = given().when()
                .auth()
                .oauth2(ImgurApiParameters.TOKEN)
                .when()
                .get(urlForDeleteHash)
                .then().statusCode(200)
                .extract()
                .path("data[2].deletehash");
        String url = "image/" + imageDeleteHash;
        String imgTitle = "funy face";
        String imgDescription = "funy face";

        given().when()
                .log().all()
                .header("Authorization", ImgurApiParameters.CLIENT_ID)
                .formParam("title", imgTitle)
                .formParam("description", imgDescription)
                .expect()
                .log().all()
                .statusCode(is(200))
                .body("success", is(true))
                .body("data", is(true))
                .when()
                .post(url);
    }

    @DisplayName("Image title and description update test with authorization using accessToken")
    @Test
    @Order(6)
    void testUpdateImageInformationAuthed() {
        // https://api.imgur.com/3/image/{{imageHash}}
        String albumHash = "Q2FHfrf";
        String urlForImageHash = "album/" + albumHash + "/images";
        String imageHash = given().when()
                .auth()
                .oauth2(ImgurApiParameters.TOKEN)
                .when()
                .get(urlForImageHash)
                .then().statusCode(200)
                .extract()
                .path("data[2].id");
        String url = "image/" + imageHash;
        String imgTitle = "The discomfort";
        String imgDescription = "funy face";

        given().when()
                .auth()
                .oauth2(ImgurApiParameters.TOKEN)
                .header("Authorization", ImgurApiParameters.CLIENT_ID)
                .formParam("title", imgTitle)
                .formParam("description", imgDescription)
                .expect()
                .log().all()
                .statusCode(is(200))
                .body("success", is(true))
                .body("data", is(true))
                .when()
                .post(url);
    }

    @DisplayName("Test of adding an image to favorites")
    @Test
    @Order(7)
    void testFavoriteAnImage() {
        // https://api.imgur.com/3/image/{{imageHash}}/favorite
        String albumHash = "Q2FHfrf";
        String urlForImageHash = "album/" + albumHash + "/images";
        String imageHash = given().when()
                .auth()
                .oauth2(ImgurApiParameters.TOKEN)
                .when()
                .get(urlForImageHash)
                .then().statusCode(200)
                .extract()
                .path("data[2].id");
        String url = "image/" + imageHash + "/favorite";

        given().when()
                .auth()
                .oauth2(ImgurApiParameters.TOKEN)
                .expect()
                .log().all()
                .statusCode(is(200))
                .body("success", is(true))
                .body("data", is("favorited"))
                .when()
                .post(url);
    }
