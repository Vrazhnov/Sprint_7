package ru.yandex.samokat.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
public class OrderCreationTest {

    private final List<String> color;

    public OrderCreationTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Проверка создания заказа. Тестовые данные: {0}")
    public static Object[][] getDataForOrder() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("GREY", "BLACK")},
                {List.of()}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Проверка создания заказа")
    public void createOrder() {
        OrderCreateData orderCreateData = new OrderCreateData();
        orderCreateData.setColor(color);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(orderCreateData)
                        .when()
                        .post("/api/v1/orders");
        response.then()
                .assertThat()
                .statusCode(201)
                .and()
                .assertThat().body("track", is(notNullValue()));
    }
}