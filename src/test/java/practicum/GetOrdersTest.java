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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class GetOrdersTest {

    static String email = "lidkin@mail.ru";
    static String password = "333-kokoko-333";
    static String name = "Kokofonik";

    static Customer customer = new Customer();
    static CustomerBody customerBody = new CustomerBody(email, password, name);
    static TokenAndResponseBody tokenOrBody = new TokenAndResponseBody();
    static String accessToken;
    Order order = new Order();
    List body = new ArrayList<>();

    @BeforeClass
    public static void getToken() {
        Response before = customer.doRegister(customerBody);
        accessToken = tokenOrBody.token(before).get(0);
    }

    @AfterClass
    public static void cleanUp() {
        customer.doDelete(accessToken);
    }

    @Step
    public Response createNewOrder(String token){
        Random random = new Random();
        IngredientsNameAndId id = new IngredientsNameAndId();
        for(int k = 0; k < 3; k++) {
            int i = random.nextInt(id.getIngredientsId().length);
            body.add("\"" + id.getIngredientsId()[i] + "\"");
        }
        String bodyOrder = body.toString();
        return order.doCreateOrderAuthorizedCustomer("{\"ingredients\": " + bodyOrder + "}", token);
    }

    @Description("code: 200. success: true.")
    @DisplayName(" ")
    @Test
    public void getOrdersUniqueCustomerTest(){
        Response registerCustomer = createNewOrder(accessToken);
        String expectedResponseBody = registerCustomer.getBody().path("name");
        Response response = order.doGetOrdersWithParam(accessToken);
        response.then().assertThat().statusCode(200);
        String actualResponseBody = response.getBody().path("orders.name").toString().substring(1).replaceAll("]", "");
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Description("code: 401. success: false.")
    @DisplayName("You should be authorised")
    @Test
    public void getOrdersUnauthorizedCustomerTest(){
        Response response = order.doGetOrders();
        response.then().assertThat().statusCode(401);
        String actualResponseBody = response.body().prettyPrint();
        String expectedResponseBody = tokenOrBody.unauthorizedCustomerMessageBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }

}
