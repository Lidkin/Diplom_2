package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import practicum.customer.Customer;
import practicum.customer.CustomerClient;
import practicum.customer.TokenAndResponseBody;
import static org.junit.Assert.assertEquals;


public class RegisterCustomerTest {

    String email = "lidkin@mail.ru";
    String password = "333-kokoko-333";
    String name = "Kokofonik";

    Customer customerBody = new Customer(
            email,
            password,
            name);

    String accessToken;
    String refreshToken;

    TokenAndResponseBody tokenAndBody = new TokenAndResponseBody(email, name);
    TokenAndResponseBody body = new TokenAndResponseBody();
    final CustomerClient customerClient = new CustomerClient();

    @After
    public void cleanUp() {
        customerClient.doDelete(accessToken);
    }

    @Description("code: 200. success: true.")
    @DisplayName("register new customer: positive flow")
    @Test
    public void registerCustomerPositiveFlow() {
        Response response = customerClient.doPost(customerBody);
        response.then().assertThat().statusCode(200);
        accessToken = tokenAndBody.token(response).get(0);
        refreshToken = tokenAndBody.token(response).get(1);
        String actualResponseBody = response.body().prettyPrint();
        String expectedResponseBody = tokenAndBody.expectedResponseBody(accessToken,refreshToken);
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Description("code: 403. success: false.")
    @DisplayName("register existing customer")
    @Test
    public void registerExistingCustomer() {
        Response response = customerClient.doPost(customerBody);
        accessToken = tokenAndBody.token(response).get(0);
        Response repeatedResponse = customerClient.doPost(customerBody);
        repeatedResponse.then().assertThat().statusCode(403);
        String actualResponseBody = repeatedResponse.body().prettyPrint();
        String expectedResponseBody = body.expectedRepeatedRegisteredBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }
}
