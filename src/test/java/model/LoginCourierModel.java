package model;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginCourierModel {
    private String login;
    private String password;

    public static LoginCourierModel createFakeCredentials() {
        Faker faker = new Faker();
        return LoginCourierModel
                .builder()
                .login(faker.name().username())
                .password(faker.internet().password(6, 20))
                .build();
    }
}
