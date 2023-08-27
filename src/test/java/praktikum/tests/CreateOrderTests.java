package praktikum.tests;

import io.qameta.allure.Link;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.requestEntities.Ingredient;
import praktikum.responseEntities.IngredientsResponsed;
import praktikum.resthandlers.apiclients.ResponseChecks;
import praktikum.resthandlers.apiclients.OrderApiClient;
import praktikum.resthandlers.apiclients.UserApiClient;

import java.util.*;

import static org.junit.Assert.fail;

@DisplayName("4. Создание заказа")
@Link(value = "Документация", url = "https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf")
public class CreateOrderTests {

    private String email, password, name, token;
    private List<Ingredient> ingredients = new ArrayList<>();
    private final OrderApiClient orderApi = new OrderApiClient();
    private final UserApiClient userApi = new UserApiClient();
    private final ResponseChecks checks = new ResponseChecks();

    @Before
    @Step("Подготовка тестовых данных")
    public void prepareTestData() {
        email = "e-mail_" + UUID.randomUUID() + "@mail.com";
        password = "pass_" + UUID.randomUUID();
        name = "name";

        // Создание пользователя
        Response response = userApi.createUser(email, password, name);
        checks.checkStatusCode(response, 200);

        // Получение токена
        if (response.getStatusCode() == 200) {
            token = userApi.getToken(response);
        }

        // Получение списка ингредиентов
        response = orderApi.getIngredientList();
        checks.checkStatusCode(response, 200);

        ingredients = response.body().as(IngredientsResponsed.class).getData();

        if(token == null || ingredients.isEmpty())
            fail("Отсутствует токен или не получен список ингредиентов");
    }
    @After
    @Step("Удаление тестовых пользователей")
    public void cleanTestData() {
        if(token == null)
            return;

        checks.checkStatusCode(userApi.deleteUser(token), 202);
    }
    @Test
    @DisplayName("Создание заказа: с авторизацией и с ингредиентами")
    public void createOrderWithAuthAndIngredientsIsSuccess() {
        Response response = orderApi.createOrder(
        List.of(ingredients.get(0).get_id(), ingredients.get(ingredients.size() - 1).get_id()),
                token
        );

        checks.checkStatusCode(response, 200);
        checks.checkLabelSuccess(response, "true");
    }
    @Test
    @DisplayName("Создание заказа: без авторизации и с ингредиентами")
    public void createOrderWithoutAuthAndWithIngredientsIsFailed() {
        Response response = orderApi.createOrder(
                List.of(ingredients.get(0).get_id(), ingredients.get(ingredients.size() - 1).get_id()),
                ""
        );

        checks.checkStatusCode(response, 400);
    }
    @Test
    @DisplayName("Создание заказа: с авторизацией и без ингредиентов")
    public void createOrderWithAuthAndWithoutIngredientsIsSuccess() {
        Response response = orderApi.createOrder(
                List.of(),
                token
        );

        checks.checkStatusCode(response, 400);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "Ingredient ids must be provided");
    }
    @Test
    @DisplayName("Создание заказа: с неверным хешем ингредиентов")
    public void createOrderWithAuthAndIncorrectIngredientsIsFailed() {
        Response response = orderApi.createOrder(
                List.of(ingredients.get(0).get_id(), UUID.randomUUID().toString()),
                token
        );

        checks.checkStatusCode(response, 500);
    }
}
