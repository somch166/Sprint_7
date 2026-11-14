package com.example.utils;

import com.example.model.Order;

import java.util.List;
import java.util.Random;

public class DataGenerator {
    private static final Random random = new Random();

    public static String randomLogin() {
        return "courier_" + random.nextInt(1_000_000);
    }

    public static String randomPassword() {
        return "pass_" + random.nextInt(1_000_000);
    }

    public static String randomFirstName() {
        return "name_" + random.nextInt(1_000_000);
    }

    public static Order randomOrder(List<String> colors) {
        return new Order(
                "Naruto_" + random.nextInt(1000),
                "Uchiha_" + random.nextInt(1000),
                "Konoha, " + random.nextInt(100) + " apt.",
                random.nextInt(10) + 1,
                "+7 800 355 35 35",
                5,
                "2024-11-20",
                "Saske, come back to Konoha",
                colors
        );
    }
}