package ru.yandex.samokat.courier;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class CourierLogInPositiveTests {

    CourierService courierService = new CourierService();
    CourierData courierData = new CourierData();

    public String genString() {
        Faker faker = new Faker();
        return faker.name().lastName();
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void tearDown() {
        courierService.deleteCourier(courierData);
    }

    @Test
    @DisplayName("Проверка авторизации пользователя с корректными логином и паролем")
    public void courierLogIn() {
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService
                .courierLogIn(courierData)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("id", notNullValue());
    }

}