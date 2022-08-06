package ru.yandex.practicum.stellarburgers.api;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellarburgers.api.model.Order;
import ru.yandex.practicum.stellarburgers.api.model.User;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;
import static ru.yandex.practicum.stellarburgers.api.model.User.getRandomUser;

public class GetOrderTest {
    User user;
    Order order;
    String token;
    OrderClient orderClient;
    UserClient userClient;
    GetOrderClient getOrderClient;

    @Before
    public void setup() {
        user = getRandomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        Response responseCreateUser = userClient.createUser(user);
        token = responseCreateUser.body().jsonPath().getString("accessToken");
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa6d"});
        Response responseCreateOrder = orderClient.createOrderWithAuth(order, token);
    }

    @After
    public void delete() {
        if (token != null) {
            userClient.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Проверка получения списка заказов без авторизации")
    public void getOrderWithoutAuthTest() {
        getOrderClient = new GetOrderClient();
        Response getOrderWithoutAuthResponse = getOrderClient.getOrdersWithoutAuth();
        assertFalse(getOrderWithoutAuthResponse.body().jsonPath().getBoolean("success"));
        assertEquals(SC_UNAUTHORIZED, getOrderWithoutAuthResponse.statusCode());
        assertEquals("You should be authorised", getOrderWithoutAuthResponse.body().jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Проверка получения списка заказов с авторизацией")
    public void getOrderWithAuthTest() {
        getOrderClient = new GetOrderClient();
        Response getOrderWithAuthResponse = getOrderClient.getOrdersWithAuth(token);
        assertTrue(getOrderWithAuthResponse.body().jsonPath().getBoolean("success"));
        assertEquals(SC_OK, getOrderWithAuthResponse.statusCode());
    }
}
