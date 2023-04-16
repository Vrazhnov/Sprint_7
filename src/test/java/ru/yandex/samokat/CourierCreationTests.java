package ru.yandex.samokat;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CourierCreationTests {
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
    public  void createNewCourier(){
        CourierData courierData = new CourierData(genString(), genString(), genString());
        courierService
                .createCourier(courierData)
                .then()
                .statusCode(201)
                .and()
                .assertThat()
                .body("ok", is(true));
        courierService.deleteCourier(courierData);
    }

    @Test
    public  void createNewCourierWithExistingLogin(){  // Падает, т.к. текст ошибки отличается от докуентации: "Этот логин уже используется. Попробуйте другой."
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
    public  void createCourierWithoutPassword(){
        CourierData courierData = new CourierData(genString(), null, genString());
        courierService
                .createCourier(courierData)
                .then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public  void createCourierWithoutLogin(){
        CourierData courierData = new CourierData(null,genString(), genString());
        courierService
                .createCourier(courierData)
                .then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public  void checkCourierSuccessCreationStatus(){
        CourierData courierData = new CourierData();
        courierService
                .createCourier(courierData)
                .then()
                .assertThat()
                .statusCode(201);
        courierService.deleteCourier(courierData);
    }

    @Test
    public  void checkCourierSuccessCreationValue(){
        CourierData courierData = new CourierData();
        courierService
                .createCourier(courierData)
                .then().assertThat()
                .body("ok", equalTo(true));
        courierService.deleteCourier(courierData);
    }

    @Test
    public  void createCourierWithExistingLogin(){ // Падает, т.к. текст ошибки отличается от докуентации: "Этот логин уже используется. Попробуйте другой."
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