package ru.yandex.samokat.courier;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreationNegativeTests {
    CourierService courierService = new CourierService();

    public String genString() {
        Faker faker = new Faker();
        return faker.name().lastName();
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Создание нового пользователя с логином, который уже занят")
    public void createNewCourierWithExistingLogin() {  // Падает, т.к. текст ошибки отличается от докуентации: "Этот логин уже используется. Попробуйте другой."
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService
                .createCourier(courierData)
                .then()
                .assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание нового пользователя без указания пароля")
    public void createCourierWithoutPassword() {
        CourierData courierData = new CourierData(genString(), null, genString());
        courierService
                .createCourier(courierData)
                .then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание нового пользователя без указания логина")
    public void createCourierWithoutLogin() {
        CourierData courierData = new CourierData(null, genString(), genString());
        courierService
                .createCourier(courierData)
                .then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка возврата ошибки при создании пользователя с неуникальным логином")
    public void createCourierWithExistingLogin() { // Падает, т.к. текст ошибки отличается от докуентации: "Этот логин уже используется. Попробуйте другой."
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService
                .createCourier(new CourierData(courierData.getLogin(), genString(), genString()))
                .then()
                .assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }

}