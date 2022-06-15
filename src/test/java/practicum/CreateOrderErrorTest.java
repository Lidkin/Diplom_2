package practicum;

import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import practicum.customer.Credentials;
import practicum.customer.Customer;
import practicum.customer.CustomerBody;
import practicum.order.Ingredients;
import practicum.order.Order;
import java.util.*;

import static org.junit.Assert.assertEquals;


public class CreateOrderErrorTest {

    static Credentials credentials = new Credentials();
    static Ingredients ingredients;

    static {
        try {
            ingredients = new Ingredients();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    static Gson gson = new Gson();
    static ResponseMessage responseMessage = new ResponseMessage();
    static Customer customer = new Customer();
    static CustomerBody customerBody = new CustomerBody(
            credentials.getEmail(),
            credentials.getPassword(),
            credentials.getName());
    static String accessToken;
    Order order = new Order();
    Map<String, List<String>> burger = new HashMap<>(ingredients.buildRandomBurger(1, 1));

    @Step("replacing all '0' with the letter 'o' in a random ingredients id and adding in list of valid ingredients id")
    public Map<String, List<String>> getWrongId() {
        Map<String, List<String>> wrongBody = new HashMap<>();
        List<String> wrongId = new ArrayList<>();
        for (String el : burger.get("id"))
            wrongId.add(el.replaceAll("0", "o"));
        wrongId.addAll(burger.get("id"));
        wrongBody.put("ingredients", wrongId);
        return wrongBody;
    }

    @Step("empty body of ingredients")
    public Map<String, String> getEmptyBody() {
        Map<String, String> bodyIngredients = new HashMap<>();
        bodyIngredients.put("ingredients", "");
        return bodyIngredients;
    }

    @BeforeClass
    public static void getToken() {
        accessToken = customer.doRegister(customerBody).body().path("accessToken").toString().substring(7);
    }

    @After
    public void waitBeforeNextTest() throws InterruptedException {
        Thread.sleep(500);
    }

    @AfterClass
    public static void cleanUp() {
        customer.doDelete(accessToken);
    }

    @Description("code: 400. success: false. \n" +
            "Message: \"Ingredient ids must be provided\"")
    @DisplayName("Test with an empty list of ingredients")
    @Test
    public void emptyIngredientsTest() {
        Response response = order.doCreateOrder(gson.toJson(getEmptyBody()));
        response.then().assertThat().statusCode(400);
        String expected = responseMessage.errorMessage("Ingredient ids must be provided");
        assertEquals(expected, response.body().asString());
    }

    @Description("code: 400. success: false. \n" +
            "Message: \"Ingredient ids must be provided\"")
    @DisplayName("Test with an empty list of ingredients for authorized customer")
    @Test
    public void emptyIngredientsAuthorizedCustomerTest() {
        Response response = order.doCreateOrderWithToken(gson.toJson(getEmptyBody()), accessToken);
        response.then().assertThat().statusCode(400);
        String expected = responseMessage.errorMessage("Ingredient ids must be provided");
        assertEquals(expected, response.body().asString());
    }

    @Description("code: 500 \n" +
            "Message: \"Internal Server Error\"")
    @DisplayName("Create order with invalid ingredients id")
    @Test
    public void invalidIngredientIdTest() {
        Response response = order.doCreateOrder(gson.toJson(getWrongId()));
        response.then().assertThat().statusCode(500);
        String actual = response.getBody().htmlPath().getString("body").substring(9);
        assertEquals("Internal Server Error", actual);
    }

    @Description("code: 500 \n" +
            "Message: \"Internal Server Error\"")
    @DisplayName("Create order with invalid ingredients id for authorized customer.")
    @Test
    public void invalidIngredientIdAuthorizedCustomerTest() {
        Response response = order.doCreateOrderWithToken(gson.toJson(getWrongId()), accessToken);
        response.then().assertThat().statusCode(500);
        String actual = response.getBody().htmlPath().getString("body").substring(9);
        assertEquals("Internal Server Error", actual);
    }
}
