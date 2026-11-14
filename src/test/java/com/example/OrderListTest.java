package com.example;

import com.example.client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

@Epic("Управление заказами")
@Feature("Список заказов")
public class OrderListTest {

    @Test
    @Story("Успешное получение списка")
    @DisplayName("Получение списка заказов")
    @Description("Проверяем, что в тело ответа возвращается список заказов")
    public void getOrderList() {
        OrderClient orderClient = new OrderClient();

        Response response = orderClient.getOrderList();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}