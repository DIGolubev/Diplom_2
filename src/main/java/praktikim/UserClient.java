package praktikim;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


/**
 * Для использования API
 */
public class UserClient extends RestClient {
    public final String REGISTER_USER_PATH = BASE_URL + "/auth/register";
    public final String LOGIN_USER_PATH = BASE_URL + "/auth/login";
    public final String INFO_USER_PATH = BASE_URL + "/auth/user";

    @Step("Создание пользователя")
    public Response create(User user) {
        return given()
                .spec(getBaseSpec())
                .log()
                .all()
                .body(user)
                .post(REGISTER_USER_PATH);
    }

    @Step("Логин пользователя")
    public Response login(UserCredentials userCredentials) {
        return given()
                .spec(getBaseSpec())
                .log()
                .all()
                .body(userCredentials)
                .post(LOGIN_USER_PATH);
    }

    @Step("Удаление пользователя")
    public void delete(User user) {
        given()
                .spec(getBaseSpec())
                .log()
                .all()
                .body(user)
                .delete(INFO_USER_PATH);
    }

    @Step("Изменение данных авторизированного пользователя")
    public Response changeDataWithLogin(Object changesUser, String accessUserToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessUserToken)
                .log()
                .all()
                .body(changesUser)
                .when()
                .patch(INFO_USER_PATH);
    }

    @Step("Изменение данных неавторизованного пользователя")
    public Response changeDataWithoutLogin(Object changesUser) {
        return given()
                .spec(getBaseSpec())
                .log()
                .all()
                .body(changesUser)
                .when()
                .patch(INFO_USER_PATH);
    }
}
