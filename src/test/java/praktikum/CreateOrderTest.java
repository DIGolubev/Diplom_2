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

public class CreateOrderTest {
    UserClient userClient;
    String accessToken;
    HashMap<String, List> orderHash;
    List<String> ingredients = new ArrayList<>();
    OrderClient orderClient;
    User user;

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
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Можно создать заказ. Успешный запрос возвращает код ответа 200")
    public void createOrderWithIngredientsAndLogin() {
        Response createOrder = orderClient.createOrder(orderHash, accessToken);

        assertEquals("Код ответа не соответствует 200", 200, createOrder.statusCode());
        assertEquals("Заказ не создан", true, createOrder.path("success"));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами без авторизации")
    @Description("Можно создать заказ. Успешный запрос возвращает код ответа 200")
    public void createOrderWithoutLogin() {
        Response createOrder = orderClient.createOrder(orderHash, "");

        assertEquals("Код ответа не соответствует 200", 200, createOrder.statusCode());
        assertEquals("Заказ не создан", true, createOrder.path("success"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Заказ не создать. Запрос возвращает код ответа 400")
    public void createOrderWithoutIngredients() {
        HashMap<String, List> nullOrderHash = new HashMap<>();
        Response createOrder = orderClient.createOrder(nullOrderHash, accessToken);

        assertEquals("Код ответа не соответствует 400", 400, createOrder.statusCode());
        assertEquals("Неверное сообщение",
                "Ingredient ids must be provided", createOrder.path("message"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Заказ не создать. Запрос возвращает код ответа 500")
    public void createOrderWithIncorrectHash() {
        String invalidHashIngredients = "5";
        List<String> invalidIngredients = new ArrayList<>();
        HashMap<String, List> invalidOrderHash = new HashMap<>();
        invalidIngredients.add(invalidHashIngredients);
        invalidOrderHash.put("ingredients", invalidIngredients);
        Response createOrder = orderClient.createOrder(invalidOrderHash, accessToken);

        assertEquals("Код ответа не соответствует 500", 500, createOrder.statusCode());
    }
}
