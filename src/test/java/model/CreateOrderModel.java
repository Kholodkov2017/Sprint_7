package model;

import com.github.javafaker.Faker;
import helpers.ScooterColorEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateOrderModel {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private String deliveryDate;
    private int rentTime;
    private String comment;
    private String[] color;

    public static CreateOrderModel createOrderModel(ScooterColorEnum... colors) {
        String[] colorData = Arrays.stream(colors).map(Enum::name).toArray(String[]::new);
        Faker faker = new Faker(new Locale("ru-RU"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return CreateOrderModel
                .builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .address(faker.address().fullAddress())
                .metroStation("Метро Белорусская")
                .phone(faker.phoneNumber().phoneNumber())
                .deliveryDate(sdf.format(faker.date().future(1, TimeUnit.DAYS)))
                .rentTime(faker.random().nextInt(1, 10))
                .comment(faker.lorem().characters(30))
                .color(colorData)
                .build();
    }
}
