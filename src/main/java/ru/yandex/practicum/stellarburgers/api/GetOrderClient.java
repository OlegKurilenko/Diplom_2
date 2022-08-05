package ru.yandex.practicum.stellarburgers.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetOrderClient extends BaseApiClient {

    @Step("Получение списка заказов без авторизации")
    public Response getOrdersWithoutAuth() {
        return given()
                .spec(getReqSpec())
                .get(BASE_URL + "/api/orders");
    }

    @Step("Получение списка заказов с авторизацией")
    public Response getOrdersWithAuth(String token) {
        return given()
                .header("Authorization", token)
                .spec(getReqSpec())
                .get(BASE_URL + "/api/orders");
    }
}
