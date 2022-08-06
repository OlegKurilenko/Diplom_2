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

public class UserLoginTest {
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
    @DisplayName("Проверка успешной авторизации существующего пользователя")
    public void userLoginTest() {

        //Создаем пользователя
        Response responseCreate = userClient.createUser(user);
        CreateUserResponse createUserResponse = responseCreate.as(CreateUserResponse.class);
        token = responseCreate.body().jsonPath().getString("accessToken");
        assertEquals(SC_OK, responseCreate.statusCode());
        assertTrue(createUserResponse.success);

        //Логин пользователя
        Response responseLoginOk = userClient.loginUser(user, token);
        assertEquals(SC_OK, responseLoginOk.statusCode());
    }

    @Test
    @DisplayName("Проверка авторизации пользователя с неверным паролем")
    public void userLoginTestWithWrongPassword() {

        //Создаем пользователя
        Response responseCreate = userClient.createUser(user);
        CreateUserResponse createUserResponse = responseCreate.as(CreateUserResponse.class);
        token = responseCreate.body().jsonPath().getString("accessToken");
        assertEquals(SC_OK, responseCreate.statusCode());
        assertTrue(createUserResponse.success);

        //Логин пользователя
        user.setPassword("testPassword");
        Response responseLoginBad = userClient.loginUser(user, token);
        assertEquals(SC_UNAUTHORIZED, responseLoginBad.statusCode());
        assertEquals("email or password are incorrect", responseLoginBad.body().jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Проверка авторизации пользователя с неверным логином")
    public void userLoginTestWithWrongLogin() {

        //Создаем пользователя
        Response responseCreate = userClient.createUser(user);
        CreateUserResponse createUserResponse = responseCreate.as(CreateUserResponse.class);
        token = responseCreate.body().jsonPath().getString("accessToken");
        assertEquals(SC_OK, responseCreate.statusCode());
        assertTrue(createUserResponse.success);

        //Логин пользователя
        user.setEmail("testEmail");
        Response responseLoginBad = userClient.loginUser(user, token);
        assertEquals(SC_UNAUTHORIZED, responseLoginBad.statusCode());
        assertEquals("email or password are incorrect", responseLoginBad.body().jsonPath().getString("message"));
    }

}
