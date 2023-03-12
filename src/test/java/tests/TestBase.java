package tests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.LoginCourierModel;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static helpers.Constants.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestBase {
    protected final String createOrderPth ="/api/v1/orders";
    protected final String getOrderListPth ="/api/v1/orders";
    protected final String createCourierPth ="/api/v1/courier";
    protected String loginCourierPth = "/api/v1/courier/login";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void down() {
        deleteCreatedUser();
    }
    @Step
    public void deleteCreatedUser() {
        LoginCourierModel credentials = LoginCourierModel
                .builder()
                .login(DEFAULT_COURIER_LOGIN)
                .password(DEFAULT_COURIER_PASSWORD)
                .build();

        Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .body(credentials)
                .post(loginCourierPth);

        Integer courierId = resp.then().extract().path("id");

        given()
            .header("Content-type", "application/json")
            .and()
            .delete(createCourierPth + "/" + courierId);
    }
}
