package model;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static helpers.Constants.DEFAULT_COURIER_LOGIN;
import static helpers.Constants.DEFAULT_COURIER_PASSWORD;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateCourierModel {
    private String login;
    private String password;
    private String firstName;


    public static CreateCourierModel createFakeCourier() {
        Faker faker = new Faker();
        return builder()
                .firstName(faker.name().firstName())
                .login(DEFAULT_COURIER_LOGIN)
                .password(DEFAULT_COURIER_PASSWORD)
                .build();
    }
}
