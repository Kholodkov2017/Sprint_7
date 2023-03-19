package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import model.CreateCourierModel;
import model.LoginCourierModel;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import static api.client.CourierClient.getSuccessfulCourierCreationResponse;
import static api.client.CourierClient.getSuccessfulCourierLoginResponse;
import static helpers.ErrorMessages.*;
import static org.hamcrest.Matchers.*;

@RunWith(JUnitParamsRunner.class)
public class CourierTests extends TestBase {

    @Test
    @DisplayName("Successful creation of a courier")
    public void createCourierWithSuccessResultReturnsCreatedStatusCode() {
        CreateCourierModel courierShouldBeCreated = CreateCourierModel.createFakeCourier();
        getSuccessfulCourierCreationResponse(courierShouldBeCreated)
                .body("ok", is(true))
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    @DisplayName("It is impossible to create two couriers with the same data")
    public void cannotCreateTwoIdenticalCourierReturnsConflictStatusCode() {
        CreateCourierModel courierShouldBeCreated = CreateCourierModel.createFakeCourier();
        getSuccessfulCourierCreationResponse(courierShouldBeCreated);
        getSuccessfulCourierCreationResponse(courierShouldBeCreated)
               .statusCode(HttpStatus.SC_CONFLICT)
                .body("message", is(USER_ALREADY_CREATED_ERROR))
                .body("code", is(HttpStatus.SC_CONFLICT));
    }

    @Test
    @DisplayName("Inability to create a courier with an incomplete set of data")
    @Parameters({", , SomeFirstName",
            "SomeLogin,,",
            ",TestTestTest123,"})
    public void cannotCreateCourierIfThereIsInsufficientDataReturnsBadRequest(
            String login, String password, String firstName) {
        CreateCourierModel courierShouldBeCreated = CreateCourierModel
                .builder()
                .firstName(firstName)
                .login(login)
                .password(password)
                .build();
        getSuccessfulCourierCreationResponse(courierShouldBeCreated)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is(NOT_ENOUGH_DATA_TO_COURIER_CREATION_ERROR))
                .body("code", is(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    @DisplayName("Impossibility to create a courier with the same login")
    @Parameters({"Василий, Test123Test!"})
    public void cannotCreateCourierIfCourierWithTheSameLoginAlreadyExistReturnsConflictStatusCode(String firstName,
                                                                                                  String password) {
        CreateCourierModel courierShouldBeCreated = CreateCourierModel.createFakeCourier();
        getSuccessfulCourierCreationResponse(courierShouldBeCreated);
        courierShouldBeCreated.setPassword(password);
        courierShouldBeCreated.setFirstName(firstName);
        getSuccessfulCourierCreationResponse(courierShouldBeCreated)
            .statusCode(HttpStatus.SC_CONFLICT)
            .body("message", is(USER_ALREADY_CREATED_ERROR))
            .body("code", is(HttpStatus.SC_CONFLICT));
    }

    @Test
    @DisplayName("Checking the courier's ability to log in to the site")
    public void courierCanSignedInWithCorrectParametersReturnsSuccessStatusCode() {
        CreateCourierModel createCourierModel = CreateCourierModel.createFakeCourier();
        getSuccessfulCourierCreationResponse(createCourierModel);
        LoginCourierModel credentials = LoginCourierModel
                .builder()
                .login(createCourierModel.getLogin())
                .password(createCourierModel.getPassword())
                .build();
        getSuccessfulCourierLoginResponse(credentials)
                .statusCode(HttpStatus.SC_OK)
                .body("id", notNullValue())
                .body("id", greaterThan(0));
    }

    @Test
    @DisplayName("Checking that you can't log in with an invalid username-password pair")
    @Parameters({"SomeLogin, ", ",Some123!Password"})
    public void courierCantSignedInWithIncorrectParametersReturnsNotFoundStatusCode(String login, String password) {
        CreateCourierModel createCourierModel = CreateCourierModel.createFakeCourier();
        getSuccessfulCourierCreationResponse(createCourierModel);
        LoginCourierModel credentials = LoginCourierModel
                .builder()
                .login(login.isEmpty() ? createCourierModel.getLogin() : login)
                .password(password.isEmpty() ? createCourierModel.getPassword() : password)
                .build();
        getSuccessfulCourierLoginResponse(credentials)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", is(COUPLE_LOGIN_PASSWORD_NOT_EXISTS_ERROR));
    }

    @Test
    @DisplayName("Checking the inability to log in the site if there is not enough data")
    @Parameters({"SomeLogin, ", ",Some123!Password"})
    public void courierCantSignedInWithEmptyLoginOrPasswordReturnsNotFoundStatusCode(String login, String password) {
        CreateCourierModel createCourierModel = CreateCourierModel.createFakeCourier();
        getSuccessfulCourierCreationResponse(createCourierModel);
        LoginCourierModel credentials = LoginCourierModel
                .builder()
                .login(login.isEmpty() ? login : createCourierModel.getLogin())
                .password(password.isEmpty() ? password : createCourierModel.getPassword())
                .build();
        getSuccessfulCourierLoginResponse(credentials)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is(NOT_ENOUGH_DATA_TO_SIGN_IN_ERROR));
    }

    @Test
    @DisplayName("Checking the inability to log in the site if courier account does not exist")
    public void courierCantSignedIfHisAccountNotExistReturnsNotFoundStatusCode() {
        CreateCourierModel createCourierModel = CreateCourierModel.createFakeCourier();
        LoginCourierModel credentials = LoginCourierModel
                .builder()
                .login(createCourierModel.getLogin())
                .password(createCourierModel.getPassword())
                .build();
        getSuccessfulCourierLoginResponse(credentials)
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", is(COUPLE_LOGIN_PASSWORD_NOT_EXISTS_ERROR));
    }
}
