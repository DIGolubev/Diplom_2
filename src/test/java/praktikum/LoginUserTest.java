package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikim.User;
import praktikim.UserClient;
import praktikim.UserCredentials;

import static org.junit.Assert.assertEquals;

public class LoginUserTest {
    private UserClient userClient;
    private User user;
    private Response responseLogin;
    private final String messageInvalidLogin = "email or password are incorrect";
    private final int expectedErrorCodResponse = 401;
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        accessToken = userClient.create(user).path("accessToken");
        if (accessToken != null) {
            userClient.delete(user);
            System.out.println("User deleted");
        } else {
            System.out.println("No user to delete");
        }
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Пользователь может авторизоваться, успешный запрос возвращает код ответа 200")
    public void userLoginSuccess() {
        responseLogin = userClient.login(UserCredentials.from(user));
        int expectedCodResponse = 200;

        assertEquals("Код ответа не соответствует 200", expectedCodResponse, responseLogin.statusCode());
    }

    @Test
    @DisplayName("Логин пользователя с неверной почтой")
    @Description("Если неправильный логин, запрос возвращает ошибку")
    public void userInvalidLogin() {
        user = new User(user.getEmail() + "1", user.getPassword());
        responseLogin = userClient.login(UserCredentials.from(user));

        assertEquals("Ошибка. Авторизация с неверным логином", messageInvalidLogin,
                responseLogin.then().extract().path("message"));
        assertEquals("Ошибка. неверный код ответа", expectedErrorCodResponse, responseLogin.statusCode());
    }

    @Test
    @DisplayName("Логин пользователя с неверным паролем")
    @Description("Если неправильный пароль, запрос возвращает ошибку")
    public void userInvalidPassword() {
        user = new User(user.getEmail(), user.getPassword() + 1);
        responseLogin = userClient.login(UserCredentials.from(user));
        assertEquals("Ошибка. Авторизация с неверным паролем", messageInvalidLogin,
                responseLogin.then().extract().path("message"));
        assertEquals("Ошибка. неверный код ответа", expectedErrorCodResponse, responseLogin.statusCode());
    }
}
