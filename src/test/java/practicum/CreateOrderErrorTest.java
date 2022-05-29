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

    @Step("replacing all '0' with the letter 'o' in a random hash of an ingredient")
    public String getWrongId() {
        Random random = new Random();
        int i = random.nextInt(id.getIngredientsId().length);
        return id.getIngredientsId()[i].replaceAll("0", "o");
    }

    @BeforeClass
    public static void getToken() {
        Response before = customer.doRegister(customerBody);
        accessToken = tokenOrBody.token(before).get(0);
    }

    @AfterClass
    public static void cleanUp() {
        customer.doDelete(accessToken);
    }

    @Description("code: 400. success: false.")
    @DisplayName("Ingredient ids must be provided")
    @Test
    public void emptyIngredientsTest(){
        String body = "{\"ingredients\": []}";
        Response response = order.doCreateOrder(body);
        response.then().assertThat().statusCode(400);
        String actualResponseBody = response.body().prettyPrint();
        String expectedResponseBody = tokenOrBody.emptyIngredientsOrderMessageBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Description("code: 400. success: false.")
    @DisplayName("Ingredient ids must be provided")
    @Test
    public void emptyIngredientsAuthorizedCustomerTest(){
        String body = "{\"ingredients\": []}";
        Response response = order.doCreateOrderAuthorizedCustomer(body,accessToken);
        response.then().assertThat().statusCode(400);
        String actualResponseBody = response.body().prettyPrint();
        String expectedResponseBody = tokenOrBody.emptyIngredientsOrderMessageBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Description("code: 500")
    @DisplayName("Internal Server Error")
    @Test
    public void invalidIngredientIdTest(){
        String wrongId = getWrongId();
        for (String idIngredient : id.getIngredientsId())
            assertNotEquals(idIngredient, wrongId);
        String body = "{\"ingredients\": [\"" + wrongId + "\"]}";
        Response response = order.doCreateOrder(body);
        response.then().assertThat().statusCode(500);
        response.body().prettyPrint();
    }

    @Description("code: 500")
    @DisplayName("Internal Server Error")
    @Test
    public void invalidIngredientIdAuthorizedCustomerTest(){
        String wrongId = getWrongId();
        for (String idIngredient : id.getIngredientsId())
            assertNotEquals(idIngredient, wrongId);
        String body = "{\"ingredients\": [\"" + wrongId + "\"]}";
        Response response = order.doCreateOrderAuthorizedCustomer(body, accessToken);
        response.then().assertThat().statusCode(500);
        response.body().prettyPrint();
    }
}
