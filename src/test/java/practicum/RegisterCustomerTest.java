package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.After;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import practicum.customer.Credentials;
import practicum.customer.CustomerBody;
import practicum.customer.Customer;


public class RegisterCustomerTest {

    Object registerToken, repeatedRegisterToken;
    Response response;
    Credentials credentials = new Credentials();
    Customer customer = new Customer();
    CustomerBody body = new CustomerBody(
            credentials.getEmail(),
            credentials.getPassword(),
            credentials.getName());
    ResponseMessage responseMessage = new ResponseMessage(body.getEmail(), body.getName());

    @After
    public void cleanUp() throws InterruptedException {
        Thread.sleep(500);
        if (repeatedRegisterToken != null)
            customer.doDelete(repeatedRegisterToken.toString().substring(7));
        customer.doDelete(registerToken.toString().substring(7));
    }

    @Description("code: 200. success: true.")
    @DisplayName("Register new customer: positive flow.")
    @Test
    public void registerCustomerPositiveFlowTest() throws JSONException {
        response = customer.doRegister(body);
        registerToken = response.body().path("accessToken");
        response.then().assertThat().statusCode(200);
        String expected = responseMessage.validResponseBody(registerToken, response.body().path("refreshToken"));
        JSONAssert.assertEquals(expected, response.getBody().asString(), true);
    }

    @Description("code: 403. success: false. \n" +
            "Message: \"User already exists\"")
    @DisplayName("Register existing customer.")
    @Test
    public void registerExistingCustomerTest() throws JSONException {
        response = customer.doRegister(body);
        registerToken = response.body().path("accessToken");
        Response repeatedResponse = customer.doRegister(body);
        repeatedRegisterToken = repeatedResponse.body().path("accessToken");
        repeatedResponse.then().assertThat().statusCode(403);
        String expected = responseMessage.errorMessage("User already exists");
        JSONAssert.assertEquals(expected, repeatedResponse.getBody().asString(), true);
    }
}
