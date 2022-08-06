package ru.yandex.practicum.stellarburgers.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.stellarburgers.api.model.User;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;

public class UserClient extends BaseApiClient {

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .spec(getReqSpec())
                .body(user)
                .when()
                .post(BASE_URL + "/api/auth/register");
    }

    @Step("Логин пользователя")
    public Response loginUser(User user, String token) {
        return given()
                .header("Authorization", token)
                .spec(getReqSpec())
                .body(user)
                .when()
                .post(BASE_URL + "/api/auth/login");
    }

    @Step("Обновление данных пользователя без авторизации")
    public Response editUser(User user) {
        return given()
                .spec(getReqSpec())
                .body(user)
                .when()
                .patch(BASE_URL + "/api/auth/user");
    }

    @Step("Обновление данных пользователя с авторизацией")
    public Response editUser(User user, String token) {
        return given()
                .header("Authorization", token)
                .spec(getReqSpec())
                .body(user)
                .when()
                .patch(BASE_URL + "/api/auth/user");
    }

    @Step("Удаление пользователя")
    public Boolean deleteUser(String token) {
        return given()
                .spec(getReqSpec())
                .header("Authorization", token)
                .when()
                .delete(BASE_URL + "/api/auth/user")
                .then().log().all()
                .assertThat()
                .statusCode(SC_ACCEPTED)
                .extract()
                .path("ok");
    }
}
