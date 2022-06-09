package practicum;

import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import practicum.customer.Credentials;
import practicum.customer.Customer;
import practicum.customer.CustomerBody;
import practicum.order.Ingredients;
import practicum.order.Order;
import java.util.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    static Credentials credentials = new Credentials();
    static Order order = new Order();
    static Customer customer = new Customer();
    static CustomerBody body = new CustomerBody(
            credentials.getEmail(),
            credentials.getPassword(),
            credentials.getName());
    static Ingredients ingredients;
    static String accessToken;
    static Gson gson = new Gson();
    static {
        try {
            ingredients = new Ingredients();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void getToken() throws InterruptedException {
        Thread.sleep(1000);
        accessToken = customer.doRegister(body).body().path("accessToken").toString().substring(7);
    }

    @AfterClass
    public static void cleanUp() {
        customer.doDelete(accessToken);
    }

    @Parameterized.Parameter
    public int sauceCount;

    @Parameterized.Parameter(1)
    public int fillingCount;

    @Parameterized.Parameters(name = "Chosen: {0} sauces and {1} fillings")
    public static Object[][] ingredientsAmount(){
        return new Object[][]{
            {0, 1},
            {1, 0},
            {6, 2},
            {4, 10},
        };
    }

    @Description("code: 200 success: true. \n" +
            "Create order for an authorized customer. Parametrized Test.")
    @Test
    public void createOrderAuthorizedCustomerPositiveFlowTest() throws InterruptedException {
        Map<String, List<String>> burger = new HashMap<>(ingredients.buildRandomBurger(sauceCount, fillingCount));
        Map<String, List<String>> body = new HashMap<>();
        body.put("ingredients", burger.get("id"));
        Response response = order.doCreateOrderWithToken(gson.toJson(body),accessToken);
        response.then().assertThat().statusCode(200);
        List<String> actual = response.getBody().path("order.ingredients.name");
        List<String> expected = burger.get("name");
        assertThat("List equality without order",
                actual, containsInAnyOrder(expected.toArray()));
        Thread.sleep(500);
    }

    @Description("code: 200 success: true. \n" +
            "Create order for an unauthorized customer. Parametrized Test.")
    @Test
    public void createOrderPositiveFlowTest() throws InterruptedException {
        Map<String, List<String>> burger = new HashMap<>(ingredients.buildRandomBurger(sauceCount, fillingCount));
        Map<String, List<String>> body = new HashMap<>();
        body.put("ingredients", burger.get("id"));
        Response response = order.doCreateOrder(gson.toJson(body));
        response.then().assertThat().statusCode(200);
        Boolean actual = response.getBody().path("success");
        assertTrue(actual);
        Thread.sleep(500);
    }

}
