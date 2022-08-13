package data;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class APIHelper {
    private static final RequestSpecification reqSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void login(DataHelper.AuthInfo login) {
        Gson gson = new Gson();
        String logon = gson.toJson(login);
        given()
                .spec(reqSpec)
                .body(logon)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    public static String verification(DataHelper.VerificationCode verification) {
        Gson gson = new Gson();
        String verify = gson.toJson(verification);
        return given()
                .spec(reqSpec)
                .body(verify)
                .when()
                .post("/api/auth/verification")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

    }

    public static List<DataHelper.Cards> getCards(String token) {
        reqSpec.header("Authorization", "Bearer " + token);
        return Arrays.asList(given()
                .spec(reqSpec)
                .when()
                .get("/api/cards")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getBody()
                .as(DataHelper.Cards[].class));
    }

    public static void transfer(DataHelper.Transfers transfers, String token) {
        Gson gson = new Gson();
        String transfer = gson.toJson(transfers);
        reqSpec.header("Authorization", "Bearer " + token);
        given()
                .spec(reqSpec)
                .body(transfer)
                .when()
                .post("/api/transfer")
                .then()
                .statusCode(200);

    }
}