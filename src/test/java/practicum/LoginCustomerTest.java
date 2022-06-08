package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import practicum.customer.Credentials;
import practicum.customer.CustomerBody;
import practicum.customer.Customer;


public class LoginCustomerTest {

    String accessToken, token;
    Credentials credentials = new Credentials();
    Customer customer = new Customer();
    CustomerBody body = new CustomerBody(
            credentials.getEmail(),
            credentials.getPassword(),
            credentials.getName());
    ResponseMessage responseMessage = new ResponseMessage(body.getEmail(), body.getName());

    @Before
    public void getToken() throws InterruptedException {
        Thread.sleep(1000);
        token = customer.doRegister(body).body().path("accessToken").toString().substring(7);
    }

    @After
    public void cleanUp() {
        if(accessToken == null)
            customer.doDelete(token);
        customer.doDelete(accessToken);
    }

    @Description("code: 200. success: true.")
    @DisplayName("login customer: positive flow")
    @Test
    public void loginCustomerPositiveFlowTest() throws InterruptedException, JSONException {
        Thread.sleep(1000);
        io.restassured.response.Response response = customer.doLogin(body);
        response.then().assertThat().statusCode(200);
        accessToken = response.body().path("accessToken").toString().substring(7);
        String actualResponseBody = response.getBody().asString();
        String expectedResponseBody = responseMessage.validResponse(accessToken, response.body().path("refreshToken").toString());
        JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, true);
    }

}
