package praktikum.resthandlers.httpclients;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseHTTPClient {
    public Response doPostRequest(String url, Object requestBody, String contentType) {
        return given(this.baseRequest(contentType))
                .body(requestBody)
                .when()
                .post(url);
    }
    public Response doPostRequest(String url, Object requestBody, String contentType, String token) {
        return given(this.baseRequest(contentType))
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .post(url);
    }

    public Response doGetRequest(String url) {
        return given(this.baseRequest())
                .get(url);
    }

    public Response doGetRequest(String url, String token) {
        return given(this.baseRequest())
                .auth().oauth2(token)
                .when()
                .get(url);
    }

    public Response doDeleteRequest(String url, String token) {
        return given(this.baseRequest())
                .auth().oauth2(token)
                .delete(url);
    }

    public Response doPatchRequest(String url, Object requestBody, String contentType, String token) {
        return given(this.baseRequest(contentType))
                .auth().oauth2(token)
                .body(requestBody)
                .when()
                .patch(url);
    }

    private RequestSpecification baseRequest() {
        return new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .setRelaxedHTTPSValidation()
                .build();
    }

    private RequestSpecification baseRequest(String contentType) {
        return new RequestSpecBuilder()
                .addHeader("Content-type", contentType)
                .addFilter(new AllureRestAssured())
                .setRelaxedHTTPSValidation()
                .build();
    }
}
