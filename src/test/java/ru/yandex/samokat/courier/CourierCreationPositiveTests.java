package ru.yandex.samokat.courier;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CourierCreationPositiveTests {
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
    @DisplayName("Создание нового пользователя с уникальным логином")
    public void createNewCourier() {
        CourierData courierData = new CourierData(genString(), genString(), genString());
        courierService
                .createCourier(courierData)
                .then()
                .statusCode(201)
                .and()
                .assertThat()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Проверка статус кода при создании нового пользователя")
    public void checkCourierSuccessCreationStatus() {
        CourierData courierData = new CourierData();
        courierService
                .createCourier(courierData)
                .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    @DisplayName("Проверка тела ответа при создании нового пользователя")
    public void checkCourierSuccessCreationValue() {
        CourierData courierData = new CourierData();
        courierService
                .createCourier(courierData)
                .then().assertThat()
                .body("ok", equalTo(true));
    }


}