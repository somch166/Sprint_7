package com.example.client;

import com.example.model.Courier;
import com.example.utils.Constants;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {

    static {
        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.socket.timeout", 15000)
                        .setParam("http.connection.timeout", 15000));
    }

    @Step("Создание курьера")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(Constants.BASE_URL)
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логин курьера")
    public Response login(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(Constants.BASE_URL)
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удаление курьера с id = {id}")
    public Response delete(int id) {
        return given()
                .baseUri(Constants.BASE_URL)
                .when()
                .delete("/api/v1/courier/" + id);
    }
}