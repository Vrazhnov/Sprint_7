package ru.yandex.samokat.courier;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierLogInNegativeTests {

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
    @DisplayName("Проверка авторизации пользователя без указания и логина и пароля")
    public void courierLogInWithoutData() {
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService
                .courierLogIn(new CourierLoginData(null, null))
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка авторизации пользователя без указания логина")
    public void courierLogInWithoutLogin() {
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService
                .courierLogIn(new CourierLoginData(null, courierData.getPassword()))
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка авторизации пользователя без указания пароля")
    public void courierLogInWithoutPassword() {
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService
                .courierLogIn(new CourierLoginData(courierData.getLogin(), null))
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка авторизации пользователя с некорректным логином")
    public void courierLogInWithWrongLogin() {
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService.courierLogIn(new CourierLoginData(genString(), courierData.getPassword()))
                .then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Проверка авторизации пользователя с некорректным паролем")
    public void courierLogInWithWrongPassword() {
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService
                .courierLogIn(new CourierLoginData(courierData.getLogin(), genString()))
                .then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}