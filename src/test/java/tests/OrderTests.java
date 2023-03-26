package tests;

import helpers.ScooterColorEnum;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import model.CreateOrderModel;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Arrays;

import static api.client.OrdersClient.makeSuccessfulScooterOrder;
import static api.client.OrdersClient.successfullyCreateOrder;
import static org.hamcrest.Matchers.*;

@RunWith(JUnitParamsRunner.class)
public class OrderTests  extends TestBase {

    @Test
    @DisplayName("Ability to order a scooter")
    @Description("The method checks the possibility of ordering a scooter of different colors and" +
            " the content of the response track number and status code 201")
    @Parameters({"BLACK, Метро Белорусская", "BLACK;GRAY, Метро Белорусская", ",Метро Белорусская"})
    public void canCreateOrderWithSpecificColorReturnsTrackAndCreatedStatusCode(String colorsInput,
                                                                                String metroStation) {
        ScooterColorEnum[] colors = new ScooterColorEnum[0];
        if (!colorsInput.isBlank()) {
            colors = Arrays.stream(colorsInput.split(";"))
                    .map(ScooterColorEnum::valueOf).toArray(ScooterColorEnum[]::new);
        }
        CreateOrderModel order = CreateOrderModel.createOrderModel(metroStation, colors);
        makeSuccessfulScooterOrder(order)
                .statusCode(HttpStatus.SC_CREATED)
                .body("track", greaterThan(0));
    }

    @Test
    @DisplayName("Checking to receive an order list")
    @Description("Checking whether the order list can be received, checking that the response is not an empty list")
    public void canGetOrderListReturnsSuccessStatusCode() {
        // Action
        successfullyCreateOrder()
                .statusCode(HttpStatus.SC_OK)
                .body("orders.size()", greaterThan(0))
                .body("orders[0]", hasKey("id"));
    }
}
