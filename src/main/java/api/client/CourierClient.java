package api.client;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String COURIER_PTH ="/api/v1/courier";
    private static final String loginCourierPth = "/api/v1/courier/login";

    @Step("Get response after successfully attempt to create courier")
    public static ValidatableResponse getSuccessfulCourierCreationResponse(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .post(COURIER_PTH)
                .then();
    }
    @Step("Get response after successfully attempt to courier login")
    public static ValidatableResponse getSuccessfulCourierLoginResponse(Object credentials) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(credentials)
                .post(loginCourierPth)
                .then();
    }

    @Step("Delete created user")
    public static ValidatableResponse deleteCreatedUser(Object credentials) {

        Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .body(credentials)
                .post(loginCourierPth);

        Integer courierId = resp.then().extract().path("id");

        return given()
                .header("Content-type", "application/json")
                .and()
                .delete(COURIER_PTH + "/" + courierId)
                .then();
    }
}
