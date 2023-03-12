package tests;

import helpers.ScooterColorEnum;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import model.CreateOrderModel;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(JUnitParamsRunner.class)
public class OrderTests extends TestBase {

    @Test
    @DisplayName("Ability to order a scooter")
    @Description("The method checks the possibility of ordering a scooter of different colors and" +
            " the content of the response track number and status code 201")
    @Parameters({"BLACK", "BLACK;GRAY", ""})
    public void canCreateOrderWithSpecificColorReturnsTrackAndCreatedStatusCode(String colorsInput) {
        ScooterColorEnum[] colors = new ScooterColorEnum[0];
        // Arrange
        if (!colorsInput.isBlank()) {
            colors = Arrays.stream(colorsInput.split(";"))
                    .map(ScooterColorEnum::valueOf).toArray(ScooterColorEnum[]::new);
        }
        CreateOrderModel order = CreateOrderModel.createOrderModel(colors);

        // Action
        Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .post(getOrderListPth);

        // Assert
        resp.then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", greaterThan(0));
    }

    @Test
    @DisplayName("Checking to receive an order list")
    @Description("Checking whether the order list can be received, checking that the response is not an empty list")
    public void canGetOrderListReturnsSuccessStatusCode() {
        // Action
        Response resp = given()
                .header("Content-type", "application/json")
                .and()
                .get(createOrderPth);

        // Assert
        resp.then()
                .statusCode(HttpStatus.SC_OK)
                .body("orders.size()", greaterThan(0))
                .body("orders[0]", hasKey("id"));
    }
}
