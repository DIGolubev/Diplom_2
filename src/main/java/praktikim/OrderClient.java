package praktikim;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    public final String INGREDIENTS_PATH = BASE_URL + "/ingredients";
    public final String ORDER_PATH = BASE_URL + "/orders";

    @Step("Получение ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH);
    }

    @Step("Создание заказа")
    public Response createOrder(HashMap ingredients, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .and()
                .body(ingredients)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Получение заказа авторизированного пользователя")
    public Response getOrderWithLogin(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH);
    }

    @Step("Получение заказа неавторизованного пользователя")
    public Response getOrderWithoutLogin() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH);
    }
}
