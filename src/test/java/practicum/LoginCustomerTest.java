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


public class LoginCustomerTest {

    Object loginToken, registerToken;
    Credentials credentials = new Credentials();
    Customer customer = new Customer();
    CustomerBody body = new CustomerBody(
            credentials.getEmail(),
            credentials.getPassword(),
            credentials.getName());
    ResponseMessage responseMessage = new ResponseMessage(body.getEmail(), body.getName());

    @Before
    public void getToken() {
        registerToken = customer.doRegister(body).body().path("accessToken").toString().substring(7);
    }

    @After
    public void cleanUp() throws InterruptedException {
        Thread.sleep(1000);
        if (loginToken == null) {
            customer.doDelete(registerToken.toString().substring(7));
        } else {
            customer.doDelete(loginToken.toString().substring(7));
        }
    }

    @Description("code: 200. success: true.")
    @DisplayName("login customer: positive flow")
    @Test
    public void loginCustomerPositiveFlowTest() throws JSONException {
        Response response = customer.doLogin(body);
        response.then().assertThat().statusCode(200);
        loginToken = response.body().path("accessToken");
        String expected = responseMessage.validResponseBody(loginToken, response.body().path("refreshToken"));
        JSONAssert.assertEquals(expected, response.getBody().asString(), true);
    }

}
