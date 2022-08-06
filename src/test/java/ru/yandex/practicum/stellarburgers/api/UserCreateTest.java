package ru.yandex.practicum.stellarburgers.api;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellarburgers.api.model.CreateUserResponse;
import ru.yandex.practicum.stellarburgers.api.model.User;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.practicum.stellarburgers.api.model.User.getRandomUser;
import static ru.yandex.practicum.stellarburgers.api.model.User.getRandomUserWithoutName;

public class UserCreateTest {
    User user;
    User userWithoutName;
    String token;
    UserClient userClient = new UserClient();

    @Before
    public void setup() {
        user = getRandomUser();
    }

    @After
    public void delete() {
        if (token != null) {
            userClient.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    public void userCreateTest() {

        //Создаем пользователя
        Response responseCreate = userClient.createUser(user);
        CreateUserResponse createUserResponse = responseCreate.as(CreateUserResponse.class);
        token = responseCreate.body().jsonPath().getString("accessToken");
        assertEquals(SC_OK, responseCreate.statusCode());
        assertTrue(createUserResponse.success);
    }

    @Test
    @DisplayName("Проверка создания пользователя который уже зарегистрирован")
    public void userDoubleCreateTest() {

        //Создаем пользователя
        Response responseCreate = userClient.createUser(user);
        CreateUserResponse createUserResponse = responseCreate.as(CreateUserResponse.class);
        token = responseCreate.body().jsonPath().getString("accessToken");
        assertEquals(SC_OK, responseCreate.statusCode());
        assertTrue(createUserResponse.success);

        //Пытаемся создать пользователя еще раз с теми же данными
        Response responseCreate2 = userClient.createUser(user);
        assertEquals(SC_FORBIDDEN, responseCreate2.statusCode());
        assertEquals("User already exists", responseCreate2.body().jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без имени")
    public void userCreateTestWithoutName() {
        userWithoutName = getRandomUserWithoutName();

        //Создаем пользователя
        Response responseCreate = userClient.createUser(userWithoutName);
        assertEquals(SC_FORBIDDEN, responseCreate.statusCode());
        assertEquals("Email, password and name are required fields", responseCreate.body().jsonPath().getString("message"));
    }
}
