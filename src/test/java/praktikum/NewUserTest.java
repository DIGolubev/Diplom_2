package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikim.User;
import praktikim.UserClient;

import static org.junit.Assert.assertEquals;

@Story("Создание пользователя")
public class NewUserTest {
    private UserClient userClient;
    private User user;
    private Response response;
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
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
    @DisplayName("Создать уникального пользователя")
    @Description("Пользователя можно создать, успешный запрос возвращает код ответа 200")
    public void userCreateSuccess() {
        response = userClient.create(user);
        boolean isUserCreated = true;
        int expectedCodResponse = 200;

        assertEquals("", expectedCodResponse, response.statusCode());
        assertEquals("Пользователь не создан", isUserCreated, response.then().extract().path("success"));
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    @Description("Пользователя создать нельзя")
    public void twoIdenticalUsersNotCreate() {
        response = userClient.create(user);
        Response responseCreateIdenticalUsers = userClient
                .create(new User(user.getEmail(), user.getPassword(), user.getName()));
        String messageIdenticalUsers = "User already exists";

        assertEquals("Созданы два одинаковых курьера", messageIdenticalUsers,
                responseCreateIdenticalUsers.then().extract().path("message"));
    }

    @Test
    @DisplayName("Создание пользователя и не заполнить одно из обязательных полей")
    @Description("Если создать пользователя без почты, возвращается ошибка")
    public void userWithoutRequiredField() {
        user = new User("",
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
        response = userClient.create(user);
        String userWithoutEmail = "Email, password and name are required fields";

        assertEquals("Создан курьер без почты",
                userWithoutEmail, response.then().extract().path("message"));
    }
}
