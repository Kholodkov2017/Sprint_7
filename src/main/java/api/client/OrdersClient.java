package api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrdersClient {
    private static final String ORDER_PTH ="/api/v1/orders";

    @Step("Get response after successfully attempt to create order")
    public static ValidatableResponse makeSuccessfulScooterOrder(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .post(ORDER_PTH)
                .then();
    }

    @Step("Get response after successfully attempt to get order list")
    public static ValidatableResponse successfullyCreateOrder() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .get(ORDER_PTH)
                .then();
    }
}
