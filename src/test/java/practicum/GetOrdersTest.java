package practicum;

import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
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

public class GetOrdersTest {

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

    @BeforeClass
    public static void getToken() throws InterruptedException {
        Thread.sleep(1000);
        accessToken = customer.doRegister(customerBody).body().path("accessToken").toString().substring(7);
    }

    @AfterClass
    public static void cleanUp() {
        customer.doDelete(accessToken);
    }


    @Step
    public Response createNewOrder(String token){
        Map<String, List<String>> body = new HashMap<>();
        body.put("ingredients", burger.get("id"));
    return order.doCreateOrderWithToken(gson.toJson(body), token);
    }

    @Description("code: 200. success: true.")
    @DisplayName("Get orders from a unique customer.")
    @Test
    public void getOrdersUniqueCustomerTest(){
        Response registerCustomer = createNewOrder(accessToken);
        Response response = order.doGetOrdersWithToken(accessToken);
        registerCustomer.then().assertThat().statusCode(200);
        response.then().assertThat().statusCode(200);
        String expected = registerCustomer.getBody().jsonPath().getString("name");
        String actual = response.getBody().jsonPath().getString("orders.name").replaceAll("[]\\[]","");
        assertEquals(expected, actual);
    }

    @Description("code: 401. success: false. \n" +
            "Message: \"You should be authorised\"")
    @DisplayName("Get orders from unauthorized customer.")
    @Test
    public void getOrdersUnauthorizedCustomerTest(){
        Response response = order.doGetOrders();
        response.then().assertThat().statusCode(401);
        String expected = responseMessage.errorMessage("You should be authorised");
        assertEquals(expected, response.body().asString());
        }

}
