package tests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.LoginCourierModel;
import org.junit.After;
import org.junit.Before;

import static api.client.CourierClient.deleteCreatedUser;
import static helpers.Constants.*;
import static io.restassured.RestAssured.given;

public class TestBase {


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void down() {
        LoginCourierModel credentials = LoginCourierModel
                .builder()
                .login(DEFAULT_COURIER_LOGIN)
                .password(DEFAULT_COURIER_PASSWORD)
                .build();
        deleteCreatedUser(credentials);
    }
}
