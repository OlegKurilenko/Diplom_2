package ru.yandex.practicum.stellarburgers.api;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.stellarburgers.api.model.CreateUserResponse;
import ru.yandex.practicum.stellarburgers.api.model.User;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.practicum.stellarburgers.api.model.User.getRandomUser;

public class UserEditTest {
    User user;
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
    @DisplayName("Проверка изменения имени пользователя")
    public void userEditNameTest() {

        //Создаем пользователя
        Response responseCreate = userClient.createUser(user);
        CreateUserResponse createUserResponse = responseCreate.as(CreateUserResponse.class);
        token = responseCreate.body().jsonPath().getString("accessToken");
        assertEquals(SC_OK, responseCreate.statusCode());
        assertTrue(createUserResponse.success);

        //Изменяем имя пользователя
        user.setName("NewUserName");
        Response responseEditOk = userClient.editUser(user, token);
        assertEquals(SC_OK, responseEditOk.statusCode());
        assertEquals("NewUserName", responseEditOk.body().jsonPath().getString("user.name"));
    }

    @Test
    @DisplayName("Проверка изменения логина пользователя")
    public void userEditEmailTest() {

        //Создаем пользователя
        Response responseCreate = userClient.createUser(user);
        CreateUserResponse createUserResponse = responseCreate.as(CreateUserResponse.class);
        token = responseCreate.body().jsonPath().getString("accessToken");
        assertEquals(SC_OK, responseCreate.statusCode());
        assertTrue(createUserResponse.success);

        //Изменяем логин пользователя
        user.setEmail("newtestemail@yandex.ru");
        Response responseEditOk = userClient.editUser(user, token);
        assertEquals(SC_OK, responseEditOk.statusCode());
        assertEquals("newtestemail@yandex.ru", responseEditOk.body().jsonPath().getString("user.email"));
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя без авторизации")
    public void userEditNameWithoutAuthTest() {

        //Создаем пользователя
        Response responseCreate = userClient.createUser(user);
        CreateUserResponse createUserResponse = responseCreate.as(CreateUserResponse.class);
        assertEquals(SC_OK, responseCreate.statusCode());
        assertTrue(createUserResponse.success);

        //Изменяем имя пользователя
        user.setName("NewUserName");
        Response responseEditOk = userClient.editUser(user);
        assertEquals(SC_UNAUTHORIZED, responseEditOk.statusCode());
        assertEquals("You should be authorised", responseEditOk.body().jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Проверка изменения логина пользователя без авторизации")
    public void userEditEmailWithoutAuthTest() {

        //Создаем пользователя
        Response responseCreate = userClient.createUser(user);
        CreateUserResponse createUserResponse = responseCreate.as(CreateUserResponse.class);
        assertEquals(SC_OK, responseCreate.statusCode());
        assertTrue(createUserResponse.success);

        //Изменяем логин пользователя
        user.setEmail("newtestemail@yandex.ru");
        Response responseEditOk = userClient.editUser(user);
        assertEquals(SC_UNAUTHORIZED, responseEditOk.statusCode());
        assertEquals("You should be authorised", responseEditOk.body().jsonPath().getString("message"));
    }
}
