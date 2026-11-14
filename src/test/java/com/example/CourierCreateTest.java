package com.example;

import com.example.client.CourierClient;
import com.example.model.Courier;
import com.example.utils.DataGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

@Epic("Управление курьерами")
@Feature("Создание курьера")
public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier(
                DataGenerator.randomLogin(),
                DataGenerator.randomPassword(),
                DataGenerator.randomFirstName()
        );
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @Story("Успешное создание курьера")
    @DisplayName("Создание курьера")
    @Description("Проверяем, что курьера можно создать, код ответа 201, тело ok: true")
    public void createCourierSuccessfully() {
        Response response = courierClient.create(courier);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = courierClient.login(courier)
                .then()
                .extract()
                .jsonPath()
                .getInt("id");
    }

    @Test
    @Story("Создание дубликата курьера")
    @DisplayName("Создание дубликата курьера")
    @Description("Проверяем, что нельзя создать двух одинаковых курьеров, код ответа 409")
    public void createDuplicateCourier() {
        courierClient.create(courier);

        Response response = courierClient.create(courier);

        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}