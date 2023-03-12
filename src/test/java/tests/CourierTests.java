package tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import model.CreateCourierModel;
import model.LoginCourierModel;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static helpers.ErrorMessages.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

@RunWith(JUnitParamsRunner.class)
public class CourierTests extends TestBase {

    @Test
    @DisplayName("Successful creation of a courier")
    public void createCourierWithSuccessResultReturnsCreatedStatusCode() {
        // Arrange
        CreateCourierModel courierShouldBeCreated = CreateCourierModel.createFakeCourier();

        // Action
       Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierShouldBeCreated)
           .post(createCourierPth);

        // Assert
         resp.then()
              .body("ok", equalTo(true))
            .statusCode(HttpStatus.SC_CREATED);


    }

    @Test
    @DisplayName("It is impossible to create two couriers with the same data")
    public void cannotCreateTwoIdenticalCourierReturnsConflictStatusCode() {
        // Arrange
        CreateCourierModel courierShouldBeCreated = CreateCourierModel.createFakeCourier();
        Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierShouldBeCreated)
                .post(createCourierPth);

        // Action
        Response resp2 = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierShouldBeCreated)
                .post(createCourierPth);

        // Assert
        resp2
                .then()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("message", equalTo(USER_ALREADY_CREATED_ERROR))
                .body("code", equalTo(HttpStatus.SC_CONFLICT));


    }

    @Test
    @DisplayName("Inability to create a courier with an incomplete set of data")
    @Parameters({", , SomeFirstName",
            "SomeLogin,,",
            ",TestTestTest123,"})
    public void cannotCreateCourierIfThereIsInsufficientDataReturnsBadRequest(
            String login, String password, String firstName) {

        // Arrange
        CreateCourierModel courierShouldBeCreated = CreateCourierModel
                .builder()
                .firstName(firstName)
                .login(login)
                .password(password)
                .build();

         // Action
        Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierShouldBeCreated)
                .post(createCourierPth);

        // Assert
        resp.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo(NOT_ENOUGH_DATA_TO_COURIER_CREATION_ERROR))
                .body("code", equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    @DisplayName("Impossibility to create a courier with the same login")
    public void cannotCreateCourierIfCourierWithTheSameLoginAlreadyExistReturnsConflictStatusCode() {

        // Arrange
        CreateCourierModel courierShouldBeCreated = CreateCourierModel.createFakeCourier();

         given()
                .header("Content-type", "application/json")
                .and()
                .body(courierShouldBeCreated)
                .post(createCourierPth);
        courierShouldBeCreated.setPassword("Test123Test!");
        courierShouldBeCreated.setFirstName("Василий");

        // Action
        Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierShouldBeCreated)
                .post(createCourierPth);

        // Assert
        resp.then()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("message", equalTo(USER_ALREADY_CREATED_ERROR))
                .body("code", equalTo(HttpStatus.SC_CONFLICT));

    }

    @Test
    @DisplayName("Checking the courier's ability to log in to the site")
    public void courierCanSignedInWithCorrectParametersReturnsSuccessStatusCode() {
        // Arrange
        CreateCourierModel createCourierModel = CreateCourierModel.createFakeCourier();
        sendPostRequestCourier(createCourierModel);
        LoginCourierModel credentials = LoginCourierModel
                .builder()
                .login(createCourierModel.getLogin())
                .password(createCourierModel.getPassword())
                .build();

        // Action
        Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .body(credentials)
                .post(loginCourierPth);

        // Assert
        resp.then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue())
                .body("id", greaterThan(0));
    }

    @Test
    @DisplayName("Checking that you can't log in with an invalid username-password pair")
    @Parameters({"SomeLogin, ", ",Some123!Password"})
    public void courierCantSignedInWithIncorrectParametersReturnsNotFoundStatusCode(String login, String password) {
        // Arrange
        CreateCourierModel createCourierModel = CreateCourierModel.createFakeCourier();
        sendPostRequestCourier(createCourierModel);
        LoginCourierModel credentials = LoginCourierModel
                .builder()
                .login(login.isEmpty() ? createCourierModel.getLogin() : login)
                .password(password.isEmpty() ? createCourierModel.getPassword() : password)
                .build();

        // Action
        Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .body(credentials)
                .post(loginCourierPth);

        // Assert
        resp.then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo(COUPLE_LOGIN_PASSWORD_NOT_EXISTS_ERROR));
    }

    @Test
    @DisplayName("Checking the inability to log in the site if there is not enough data")
    @Parameters({"SomeLogin, ", ",Some123!Password"})
    public void courierCantSignedInWithEmptyLoginOrPasswordReturnsNotFoundStatusCode(String login, String password) {
        // Arrange
        CreateCourierModel createCourierModel = CreateCourierModel.createFakeCourier();
        sendPostRequestCourier(createCourierModel);
        LoginCourierModel credentials = LoginCourierModel
                .builder()
                .login(login.isEmpty() ? login : createCourierModel.getLogin())
                .password(password.isEmpty() ? password : createCourierModel.getPassword())
                .build();

        // Action
        Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .body(credentials)
                .post(loginCourierPth);

        // Assert
        resp.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo(NOT_ENOUGH_DATA_TO_SIGN_IN_ERROR));
    }

    @Test
    @DisplayName("Checking the inability to log in the site if courier account does not exist")
    public void courierCantSignedIfHisAccountNotExistReturnsNotFoundStatusCode() {
        // Arrange
        CreateCourierModel createCourierModel = CreateCourierModel.createFakeCourier();
        LoginCourierModel credentials = LoginCourierModel
                .builder()
                .login( createCourierModel.getLogin())
                .password(createCourierModel.getPassword())
                .build();

        // Action
        Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .body(credentials)
                .post(loginCourierPth);

        // Assert
        resp.then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", equalTo(COUPLE_LOGIN_PASSWORD_NOT_EXISTS_ERROR));
    }

    @Step("Send request to /api/v1/courier to create new courier")
    public Response sendPostRequestCourier(CreateCourierModel courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .post(createCourierPth);
    }
}
