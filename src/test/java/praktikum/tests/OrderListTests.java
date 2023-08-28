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

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.fail;

@DisplayName("5. Получение списка заказов")
@Link(value = "Документация", url = "https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf")
public class OrderListTests {
    private String email, password, name, token;
    private boolean isOrderCreated = false;
    private final OrderApiClient orderApi = new OrderApiClient();
    private final UserApiClient userApi = new UserApiClient();
    private final ResponseChecks baseApi = new ResponseChecks();

    @Before
    @Step("Подготовка тестовых данных")
    public void prepareTestData() {
        email = "e-mail_" + UUID.randomUUID() + "@mail.com";
        password = "pass";
        name = "name";

        // Создание пользователя
        Response response = userApi.createUser(email, password, name);
        baseApi.checkStatusCode(response, 200);

        // Получение токена авторизации
        if (response.getStatusCode() == 200) {
            token = userApi.getToken(response);
        }

        //Получение списка ингредиентов
        response = orderApi.getIngredientList();
        baseApi.checkStatusCode(response, 200);

        List<Ingredient> ingredients = response.body().as(IngredientsResponsed.class).getData();

        // Создание заказа
        response = orderApi.createOrder(
                List.of(ingredients.get(0).get_id(), ingredients.get(ingredients.size() - 1).get_id()),
                token
        );
        baseApi.checkStatusCode(response, 200);

        if(response.getStatusCode() == 200) {
            isOrderCreated = true;
        }
    }
    @After
    @Step("Удаление тестовых пользователей")
    public void cleanTestData() {
        if(token == null)
            return;

        baseApi.checkStatusCode(userApi.deleteUser(token), 202);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя: авторизованный пользователь")
    public void getOrderListWithAuthIsSuccess() {
        if (token == null || !isOrderCreated)
            fail("Не создан тестовый пользователь или заказ");

        Response response = orderApi.getOrderList(token);

        baseApi.checkStatusCode(response, 200);
        baseApi.checkLabelSuccess(response, "true");
    }
    @Test
    @DisplayName("Получение заказов конкретного пользователя: неавторизованный пользователь")
    public void getOrderListWithoutAuthIsFailed() {
        if (token == null || !isOrderCreated)
            fail("Не создан тестовый пользователь или заказ");

        Response response = orderApi.getOrderList("");

        baseApi.checkStatusCode(response, 401);
        baseApi.checkLabelSuccess(response, "false");
        baseApi.checkLabelMessage(response, "You should be authorised");
    }
    @Test
    @DisplayName("Получение всех заказов")
    public void getOrderListAllIsSuccess() {
        Response response = orderApi.getOrderListAll();

        baseApi.checkStatusCode(response, 200);
        baseApi.checkLabelSuccess(response, "true");
    }
}
