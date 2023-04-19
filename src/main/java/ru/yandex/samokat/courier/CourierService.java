package ru.yandex.samokat.courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierService {
    private static final String PATH = "api/v1/courier";
    private static final String LOGIN_PATH = "api/v1/courier/login";

    @Step("Get response from POST request to /api/v1/courier/login")
    public Integer getCourierId(CourierData courierData) {
        return given()
                .header("Content-type", "application/json")
                .body(courierData)
                .post(LOGIN_PATH)
                .then()
                .extract().path("id");
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response courierLogIn(CourierData courierData) {
        return given()
                .header("Content-type", "application/json")
                .body(courierData)
                .post(LOGIN_PATH);
    }

    public Response courierLogIn(CourierLoginData courierLoginData) {
        return given()
                .header("Content-type", "application/json")
                .body(courierLoginData)
                .post(LOGIN_PATH);
    }

    @Step("Send Delete request to /api/v1/courier")
    public Response deleteCourier(CourierData courierData) {
        return given()
                .when()
                .header("Content-type", "application/json")
                .delete(PATH + "/" + getCourierId(courierData));
    }

    @Step("Send POST request to /api/v1/courier")
    public Response createCourier(CourierData courierData) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierData)
                .when()
                .post(PATH);
    }
}