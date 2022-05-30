package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import practicum.customer.CustomerBody;
import practicum.customer.Customer;
import static org.junit.Assert.assertEquals;


public class RegisterCustomerTest {

    String email = "lidkin@mail.ru";
    String password = "111-kokoko-333";
    String name = "fonik";
    String accessToken;
    String refreshToken;

    final Customer customer = new Customer();
    TokenAndResponseBody tokenAndBody = new TokenAndResponseBody(email, name);
    CustomerBody customerBody = new CustomerBody(
            email,
            password,
            name);

    @After
    public void cleanUp() {
        customer.doDelete(accessToken);
    }

    @Description("code: 200. success: true.")
    @DisplayName("Register new customer: positive flow.")
    @Test
    public void registerCustomerPositiveFlowTest() throws InterruptedException {
        Thread.sleep(2000);
        Response response = customer.doRegister(customerBody);
        response.then().assertThat().statusCode(200);
        accessToken = tokenAndBody.token(response).get(0);
        refreshToken = tokenAndBody.token(response).get(1);
        String actualResponseBody = response.body().prettyPrint();
        String expectedResponseBody = tokenAndBody.responseBody(accessToken,refreshToken);
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Description("code: 403. success: false. \n" +
            "Message: \"User already exists\"")
    @DisplayName("Register existing customer.")
    @Test
    public void registerExistingCustomerTest() throws InterruptedException {
        Thread.sleep(1000);
        Response response = customer.doRegister(customerBody);
        accessToken = tokenAndBody.token(response).get(0);
        Thread.sleep(1000);
        Response repeatedResponse = customer.doRegister(customerBody);
        repeatedResponse.then().assertThat().statusCode(403);
        String actualResponseBody = repeatedResponse.body().prettyPrint();
        String expectedResponseBody = tokenAndBody.repeatedRegisteredMessageBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }
}
