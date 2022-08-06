package ru.yandex.practicum.stellarburgers.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApiClient {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    public static RequestSpecification getReqSpec() {
        return new RequestSpecBuilder().log(LogDetail.ALL)
                .addFilter(new AllureRestAssured())
                .setContentType(ContentType.JSON).build();
    }
}
