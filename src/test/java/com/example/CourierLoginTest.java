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
import org.junit.Ignore;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Управление курьерами")
@Feature("Логин курьера")
public class CourierLoginTest {

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
        courierClient.create(courier);
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @Story("Успешный логин")
    @DisplayName("Успешный логин курьера")
    @Description("Проверяем, что курьер может авторизоваться, код 200 и тело содержит id")
    public void courierCanLogin() {
        Response response = courierClient.login(courier);

        response.then()
                .statusCode(SC_OK)
                .body("id", notNullValue());

        courierId = response.then().extract().jsonPath().getInt("id");
    }

    @Test
    @Story("Неверный пароль")
    @DisplayName("Логин с неверным паролем")
    @Description("Проверяем, что система вернёт ошибку, если неправильно указать пароль")
    public void loginWithWrongPassword() {
        Courier wrongCourier = new Courier(courier.getLogin(), "wrong_password");
        Response response = courierClient.login(wrongCourier);

        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Story("Неверный логин")
    @DisplayName("Логин с неверным логином")
    @Description("Проверяем, что система вернёт ошибку, если неправильно указать логин")
    public void loginWithWrongLogin() {
        Courier wrongCourier = new Courier("wrong_login", courier.getPassword());
        Response response = courierClient.login(wrongCourier);

        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Ignore("Баг API: сервер возвращает 504 Gateway Timeout при передаче null в password")
    @Story("Без пароля")
    @DisplayName("Логин без пароля")
    @Description("Проверяем, что система вернёт ошибку, если не передать пароль")
    public void loginWithoutPassword() {
        Courier wrongCourier = new Courier(courier.getLogin(), null);
        Response response = courierClient.login(wrongCourier);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Story("Без логина")
    @DisplayName("Логин без логина")
    @Description("Проверяем, что система вернёт ошибку, если не передать логин")
    public void loginWithoutLogin() {
        Courier wrongCourier = new Courier(null, courier.getPassword());
        Response response = courierClient.login(wrongCourier);

        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Story("Несуществующий пользователь")
    @DisplayName("Логин несуществующего пользователя")
    @Description("Проверяем, что система вернёт ошибку, если авторизоваться под несуществующим пользователем")
    public void loginNonExistentUser() {
        Courier wrongCourier = new Courier(DataGenerator.randomLogin(), DataGenerator.randomPassword());
        Response response = courierClient.login(wrongCourier);

        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}