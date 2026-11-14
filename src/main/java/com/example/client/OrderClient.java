package com.example.client;

import com.example.utils.Constants;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {

    static {
        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.socket.timeout", 15000)
                        .setParam("http.connection.timeout", 15000));
    }

    @Step("Создание заказа")
    public Response create(Object order) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(Constants.BASE_URL)
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Получение списка заказов")
    public Response getOrderList() {
        return given()
                .baseUri(Constants.BASE_URL)
                .queryParam("limit", 10)
                .when()
                .get("/api/v1/orders");
    }

    @Step("Получение заказа по треку")
    public Response getOrderByTrack(int track) {
        return given()
                .baseUri(Constants.BASE_URL)
                .queryParam("t", track)
                .when()
                .get("/api/v1/orders/track");
    }

    @Step("Принятие заказа курьером")
    public Response acceptOrder(int orderId, int courierId) {
        return given()
                .baseUri(Constants.BASE_URL)
                .queryParam("courierId", courierId)
                .when()
                .put("/api/v1/orders/accept/" + orderId);
    }

    @Step("Отмена заказа по треку")
    public Response cancelOrder(int track) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(Constants.BASE_URL)
                .body("{\"track\": " + track + "}")
                .when()
                .put("/api/v1/orders/cancel");
    }
}