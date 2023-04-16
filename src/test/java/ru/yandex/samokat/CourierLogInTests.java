package ru.yandex.samokat;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLogInTests {

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
    public  void courierLogIn(){
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService
                .courierLogIn(courierData)
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("id", notNullValue());
        courierService.deleteCourier(courierData);
    }

    @Test
    public  void courierLogInWithoutData(){
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService
                .courierLogIn(new CourierLoginData(null,null))
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public  void courierLogInWithoutLogin(){
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
    public  void courierLogInWithoutPassword(){
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService
                .courierLogIn(new CourierLoginData(courierData.getLogin(),null))
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public  void courierLogInWithWrongLogin(){
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService.courierLogIn(new CourierLoginData(genString(),courierData.getPassword()))
                .then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public  void courierLogInWithWrongPassword(){
        CourierData courierData = new CourierData();
        courierService.createCourier(courierData);
        courierService
                .courierLogIn(new CourierLoginData(courierData.getLogin(),genString()))
                .then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}