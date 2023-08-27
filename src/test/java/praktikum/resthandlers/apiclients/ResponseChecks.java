package praktikum.resthandlers.apiclients;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import praktikum.responseEntities.UserResponsed;

import static org.hamcrest.Matchers.equalTo;

public class ResponseChecks {

    @Step("Проверка кода ответа")
    public void checkStatusCode(Response response, int code) {
        Allure.addAttachment("Ответ", response.getStatusLine());
        response.then().statusCode(code);
    }

    @Step("Проверка успешности обращения")
    public void checkLabelSuccess(Response response, String expectedValue) {
        MatcherAssert.assertThat(
                "Значение поля success не сопадает с ожидаемым",
                expectedValue,
                equalTo(response.body().as(UserResponsed.class).getSuccess())
        );
    }
    @Step("Проверка сообщения ответа")
    public void checkLabelMessage(Response response, String expectedMessage) {
        MatcherAssert.assertThat(
                "Значение поля message не сопадает с ожидаемым",
                expectedMessage,
                equalTo(response.body().as(UserResponsed.class).getMessage())
        );
    }
}
