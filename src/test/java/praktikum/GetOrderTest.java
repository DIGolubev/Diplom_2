package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikim.OrderClient;
import praktikim.User;
import praktikim.UserClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GetOrderTest {
    UserClient userClient;
    String accessToken;

    HashMap<String, List> orderHash;
    List<String> ingredients = new ArrayList<>();
    OrderClient orderClient;
    User user;
    Response userOrder;

    @Before
    public void setUp() {
        user = User.getRandomUser();
        userClient = new UserClient();
        accessToken = userClient.create(user).path("accessToken");

        orderHash = new HashMap<>();
        orderClient = new OrderClient();
        List<String> idOfIngredients = orderClient.getIngredients().path("data._id");

        ingredients.add(idOfIngredients.get(1));
        orderHash.put("ingredients", idOfIngredients);
        orderClient.createOrder(orderHash, accessToken);
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
    @DisplayName("Получение заказа авторизированного пользователя")
    @Description("Заказ можно получить, успешный запрос возвращает код ответа 200")
    public void getOrdersLoginUserTest() {
        userOrder = orderClient.getOrderWithLogin(accessToken);

        assertEquals("Код ответа не соответствует 200", 200, userOrder.statusCode());
        assertEquals("Заказ не получен", true, userOrder.path("success"));
    }

    @Test
    @DisplayName("Получение заказа неавторизованного пользователя")
    @Description("Заказ нельзя получить, успешный запрос возвращает код ответа 401")
    public void getOrdersWithNoLoginUserTest() {
        userOrder = orderClient.getOrderWithoutLogin();

        assertEquals("Код ответа не соответствует 401", 401, userOrder.statusCode());
        assertEquals("You should be authorised", userOrder.path("message"));
    }
}
