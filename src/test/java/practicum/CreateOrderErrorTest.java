package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import practicum.customer.Customer;
import practicum.customer.CustomerBody;
import practicum.order.IngredientsNameAndId;
import practicum.order.Order;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CreateOrderErrorTest {

    static String email = "lidkin@mail.ru";
    static String password = "333-kokoko-333";
    static String name = "Kokofonik";

    static Customer customer = new Customer();
    static CustomerBody customerBody = new CustomerBody(email, password, name);
    static TokenAndResponseBody tokenOrBody = new TokenAndResponseBody();
    static String accessToken;
    IngredientsNameAndId id = new IngredientsNameAndId();
    Order order = new Order();

    @Step("replacing all '0' with the letter 'o' in a random Id of an ingredient")
    public String getWrongId() {
        Random random = new Random();
        int i = random.nextInt(id.getIngredientsId().length);
        return id.getIngredientsId()[i].replaceAll("0", "o");
    }

    @BeforeClass
    public static void getToken() throws InterruptedException {
        Thread.sleep(1000);
        Response before = customer.doRegister(customerBody);
        accessToken = tokenOrBody.token(before).get(0);
    }

    @AfterClass
    public static void cleanUp() {
        customer.doDelete(accessToken);
    }

    @Description("code: 400. success: false. \n" +
            "Message: \"Ingredient ids must be provided\"")
    @DisplayName("Test with an empty list of ingredients")
    @Test
    public void emptyIngredientsTest() throws InterruptedException {
        Thread.sleep(2000);
        String body = "{\"ingredients\": []}";
        Response response = order.doCreateOrder(body);
        response.then().assertThat().statusCode(400);
        String actualResponseBody = response.body().prettyPrint();
        String expectedResponseBody = tokenOrBody.emptyIngredientsOrderMessageBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Description("code: 400. success: false. \n" +
            "Message: \"Ingredient ids must be provided\"")
    @DisplayName("Test with an empty list of ingredients for authorized customer")
    @Test
    public void emptyIngredientsAuthorizedCustomerTest() throws InterruptedException {
        Thread.sleep(2000);
        String body = "{\"ingredients\": []}";
        Response response = order.doCreateOrderWithToken(body,accessToken);
        response.then().assertThat().statusCode(400);
        String actualResponseBody = response.body().prettyPrint();
        String expectedResponseBody = tokenOrBody.emptyIngredientsOrderMessageBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Description("code: 500 \n" +
            "Message: \"Internal Server Error\"")
    @DisplayName("Create order with invalid ingredient id")
    @Test
    public void invalidIngredientIdTest() throws InterruptedException {
        Thread.sleep(2000);
        String wrongId = getWrongId();
        for (String idIngredient : id.getIngredientsId())
            assertNotEquals(idIngredient, wrongId);
        String body = "{\"ingredients\": [\"" + wrongId + "\"]}";
        Response response = order.doCreateOrder(body);
        response.then().assertThat().statusCode(500);
        response.body().prettyPrint();
    }

    @Description("code: 500 \n" +
            "Message: \"Internal Server Error\"")
    @DisplayName("Create order with invalid ingredient id for authorized customer.")
    @Test
    public void invalidIngredientIdAuthorizedCustomerTest() throws InterruptedException {
        Thread.sleep(2000);
        String wrongId = getWrongId();
        for (String idIngredient : id.getIngredientsId())
            assertNotEquals(idIngredient, wrongId);
        String body = "{\"ingredients\": [\"" + wrongId + "\"]}";
        Response response = order.doCreateOrderWithToken(body, accessToken);
        response.then().assertThat().statusCode(500);
        response.body().prettyPrint();
    }
}
