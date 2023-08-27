package praktikum.tests;

import io.qameta.allure.Link;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.resthandlers.apiclients.ResponseChecks;
import praktikum.resthandlers.apiclients.UserApiClient;

import java.util.UUID;

import static org.junit.Assert.fail;

@DisplayName("2. Логин пользователя")
@Link(value = "Документация", url = "https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf")
public class LoginUserTests {
    private String email, password, name, token;
    private final UserApiClient userApi = new UserApiClient();
    private final ResponseChecks checks = new ResponseChecks();

    @Before
    @Step("Подготовка тестовых данных")
    public void prepareTestData() {
        email = "e-mail_" + UUID.randomUUID() + "@mail.com";
        password = "pass";
        name = "name";

        // Создание пользователя
        Response response = userApi.createUser(email, password, name);
        checks.checkStatusCode(response, 200);

        // Получение токена авторизации
        if (response.getStatusCode() == 200) {
            token = userApi.getToken(response);
        }
        if (token == null)
            fail("Не создался тестовый пользователь");

    }
    @After
    @Step("Удаление тестовых пользователей")
    public void cleanTestData() {
        if(token.isEmpty())
            return;

        checks.checkStatusCode(userApi.deleteUser(token), 202);
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    public void loginUserIsSuccess() {
        Response response = userApi.loginUser(email, password);

        checks.checkStatusCode(response, 200);
        checks.checkLabelSuccess(response, "true");
    }
    @Test
    @DisplayName("Авторизация пользователя с некорректным email")
    public void loginUserIncorrectEmailIsFailed() {
        Response response = userApi.loginUser("newE-mail_" + UUID.randomUUID() + "@mail.com", password);

        checks.checkStatusCode(response, 401);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "email or password are incorrect");
    }
    @Test
    @DisplayName("Авторизация пользователя с некорректным паролем")
    public void loginUserIncorrectPasswordIsFailed() {
        Response response = userApi.loginUser(email, password  + UUID.randomUUID());

        checks.checkStatusCode(response, 401);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "email or password are incorrect");
    }
    @Test
    @DisplayName("Авторизация пользователя без email-а")
    public void loginUserMissedEmailIsFailed() {
        Response response = userApi.loginUser("", password);

        checks.checkStatusCode(response, 401);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "email or password are incorrect");
    }
    @Test
    @DisplayName("Авторизация пользователя без пароля")
    public void loginUserMissedPasswordIsFailed() {
        Response response = userApi.loginUser(email, "");

        checks.checkStatusCode(response, 401);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "email or password are incorrect");
    }

}
