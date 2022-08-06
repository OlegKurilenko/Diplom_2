package ru.yandex.practicum.stellarburgers.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.stellarburgers.api.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseApiClient {

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .spec(getReqSpec())
                .body(order)
                .when()
                .post(BASE_URL + "/api/orders")
                .then().log().all().extract().response();
    }

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(Order order, String token) {
        return given()
                .header("Authorization", token)
                .spec(getReqSpec())
                .body(order)
                .when()
                .post(BASE_URL + "/api/orders")
                .then().log().all().extract().response();
    }

}
