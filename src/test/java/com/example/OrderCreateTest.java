package com.example;

import com.example.client.OrderClient;
import com.example.model.Order;
import com.example.utils.DataGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.apache.http.HttpStatus.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@Epic("Управление заказами")
@Feature("Создание заказа")
@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> colors;
    private int track;

    public OrderCreateTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвет(а): {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {null}
        });
    }

    @After
    public void tearDown() {
        if (track != 0) {
            new OrderClient().cancelOrder(track);
        }
    }

    @Test
    @Story("Создание с разными цветами")
    @DisplayName("Создание заказа с разными цветами")
    @Description("Проверяем, что заказ создается при указании одного, двух или отсутствии цветов")
    public void createOrderWithVariousColors() {
        OrderClient orderClient = new OrderClient();
        Order order = DataGenerator.randomOrder(colors);

        Response response = orderClient.create(order);

        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());

        track = response.then().extract().jsonPath().getInt("track");
    }
}