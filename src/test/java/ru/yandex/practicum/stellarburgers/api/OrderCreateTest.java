package ru.yandex.practicum.stellarburgers.api;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellarburgers.api.model.Order;
import ru.yandex.practicum.stellarburgers.api.model.User;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static ru.yandex.practicum.stellarburgers.api.model.User.getRandomUser;

public class OrderCreateTest {
    User user;
    Order order;
    String token;
    OrderClient orderClient;
    UserClient userClient;

    @Before
    public void setup() {
        user = getRandomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        Response responseCreateUser = userClient.createUser(user);
        token = responseCreateUser.body().jsonPath().getString("accessToken");
    }

    @After
    public void delete() {
        if (token != null) {
            userClient.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Проверка создания заказа без авторизации")
    public void orderCreateTestWithoutAuth() {
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa6d"});
        Response responseCreate = orderClient.createOrderWithoutAuth(order);
        assertEquals(SC_OK, responseCreate.statusCode());
        assertTrue(responseCreate.body().jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией")
    public void orderCreateTestWithAuth() {
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa6d"});
        Response responseCreateOrder = orderClient.createOrderWithAuth(order, token);
        assertEquals(SC_OK, responseCreateOrder.statusCode());
        assertTrue(responseCreateOrder.body().jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Проверка создания заказа без ингредиентов")
    public void orderCreateTestWithoutIngr() {
        order = new Order(new String[]{});
        Response responseCreateOrder = orderClient.createOrderWithAuth(order, token);
        assertEquals(SC_BAD_REQUEST, responseCreateOrder.statusCode());
        assertFalse(responseCreateOrder.body().jsonPath().getBoolean("success"));
        assertEquals("Ingredient ids must be provided", responseCreateOrder.body().jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Проверка создания заказа с неверным хешем ингридиентов")
    public void orderCreateTestWithWrongIngr() {
        order = new Order(new String[]{"Test61c0c5a71d1f82001bdaaa6c", "Test61c0c5a71d1f82001bdaaa6d"});
        Response responseCreateOrder = orderClient.createOrderWithoutAuth(order);
        assertEquals(SC_INTERNAL_SERVER_ERROR, responseCreateOrder.statusCode());
    }
}
