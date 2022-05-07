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

public class ChangeUserTest {
    private UserClient userClient;
    private User user;
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(user);
            System.out.println("User deleted");
        } else {
            System.out.println("No user to delete");
        }
    }

    @Test
    @DisplayName("Изменение данных пользователя: с авторизацией")
    @Description("Можно изменить поля email и name")
    public void editUserDataWithLogin() {
        accessToken = userClient.create(user).path("accessToken");
        userClient.login(UserCredentials.from(user));
        User newUser = User.getRandomUser();
        Response changedData = userClient.changeDataWithLogin(newUser, accessToken);

        assertEquals("Статус код", 200, changedData.statusCode());
        assertEquals("Почта пользователя не изменилась",
                newUser.getEmail().toLowerCase(), changedData.path("user.email"));
        assertEquals("Имя пользователя не изменилось",
                newUser.getName(), changedData.path("user.name"));
    }

    @Test
    @DisplayName("Изменение данных пользователя: без авторизации")
    @Description("Нельзя изменить поля email и name")
    public void userChangeWithoutLogin() {
        userClient.login(UserCredentials.from(user));
        User newUser = User.getRandomUser();
        Response changedData = userClient.changeDataWithoutLogin(newUser);

        assertEquals("Статус код не соответствует 401", 401, changedData.statusCode());
    }
}
