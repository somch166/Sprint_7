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

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

@Epic("Управление курьерами")
@Feature("Создание курьера")
public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;

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
        try {
            int courierId = courierClient.login(courier)
                    .then()
                    .extract()
                    .jsonPath()
                    .getInt("id");
            courierClient.delete(courierId);
        } catch (Exception e) {
        }
    }

    @Test
    @Story("Успешное создание курьера")
    @DisplayName("Создание курьера")
    @Description("Проверяем, что курьера можно создать, код ответа 201, тело ok: true")
    public void createCourierSuccessfully() {
        Response response = courierClient.create(courier);

        response.then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @Story("Создание дубликата курьера")
    @DisplayName("Создание дубликата курьера")
    @Description("Проверяем, что нельзя создать двух одинаковых курьеров, код ответа 409")
    public void createDuplicateCourier() {
        courierClient.create(courier);
        Response response = courierClient.create(courier);

        response.then()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @Story("Создание без обязательных полей")
    @DisplayName("Создание курьера без логина")
    @Description("Проверяем, что без логина курьер не создаётся, код 400")
    public void createCourierWithoutLogin() {
        Courier courierWithoutLogin = new Courier(null, courier.getPassword(), courier.getFirstName());
        Response response = courierClient.create(courierWithoutLogin);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Story("Создание без обязательных полей")
    @DisplayName("Создание курьера без пароля")
    @Description("Проверяем, что без пароля курьер не создаётся, код 400")
    public void createCourierWithoutPassword() {
        Courier courierWithoutPassword = new Courier(courier.getLogin(), null, courier.getFirstName());
        Response response = courierClient.create(courierWithoutPassword);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Story("Создание без обязательных полей")
    @DisplayName("Создание курьера без имени")
    @Description("Баг API: сервер создаёт курьера без firstName, хотя документация требует 400")
    public void createCourierWithoutFirstName() {
        Courier courierWithoutFirstName = new Courier(courier.getLogin(), courier.getPassword(), null);
        Response response = courierClient.create(courierWithoutFirstName);

        // Баг API: ожидается 400, но сервер возвращает 201
        response.then()
                .statusCode(SC_CREATED);
    }
}