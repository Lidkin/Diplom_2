package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import practicum.customer.Credentials;
import practicum.customer.CustomerBody;
import practicum.customer.Customer;


public class RegisterCustomerTest {

    String accessToken;
    Response response;
    Credentials credentials = new Credentials();
    Customer customer = new Customer();
    CustomerBody body = new CustomerBody(
            credentials.getEmail(),
            credentials.getPassword(),
            credentials.getName());
    ResponseMessage responseMessage = new ResponseMessage(body.getEmail(), body.getName());

    @Before
    public void registerCustomer(){
        response = customer.doRegister(body);
        accessToken = response.body().path("accessToken").toString().substring(7);
    }

    @After
    public void cleanUp() {
        customer.doDelete(accessToken);
    }

    @Description("code: 200. success: true.")
    @DisplayName("Register new customer: positive flow.")
    @Test
    public void registerCustomerPositiveFlowTest() throws InterruptedException, JSONException {
        Thread.sleep(500);
        response.then().assertThat().statusCode(200);
        String expected = responseMessage.validResponseBody(accessToken,response.body().path("refreshToken").toString());
        JSONAssert.assertEquals(expected, response.getBody().asString(), true);
    }

    @Description("code: 403. success: false. \n" +
            "Message: \"User already exists\"")
    @DisplayName("Register existing customer.")
    @Test
    public void registerExistingCustomerTest() throws InterruptedException, JSONException {
        Thread.sleep(500);
        Response repeatedResponse = customer.doRegister(body);
        repeatedResponse.then().assertThat().statusCode(403);
        String expected = responseMessage.errorMessage("User already exists");
        JSONAssert.assertEquals(expected, repeatedResponse.getBody().asString(), true);
    }
}
