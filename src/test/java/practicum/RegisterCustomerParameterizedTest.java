package practicum;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.skyscreamer.jsonassert.JSONAssert;
import practicum.customer.Credentials;
import practicum.customer.CustomerBody;
import practicum.customer.Customer;

@RunWith(Parameterized.class)
public class RegisterCustomerParameterizedTest {

    ResponseMessage responseMessage = new ResponseMessage();
    Customer customer = new Customer();
    static Credentials credentials = new Credentials();
    Response response;

    @Parameterized.Parameter
    public String emailParameter;

    @Parameterized.Parameter(1)
    public String passwordParameter;

    @Parameterized.Parameter(2)
    public String nameParameter;

    @Parameterized.Parameter(3)
    public String notFilled;

    @Parameterized.Parameters(name = " _{0}_ || _{1}_ || _{2}_ || not filled: {3}")
    public static Object[][] registrationData(){
        return new Object[][]{
            {null, credentials.getPassword(), credentials.getName(), "email"},
            {credentials.getEmail(), null, credentials.getName(), "password"},
            {credentials.getEmail(), credentials.getPassword(), null, "name"},
        };
    }

    @After
    public void cleanUp() {
        if (response.body().path("accessToken") != null)
            customer.doDelete(response.body().path("accessToken").toString().substring(7));
    }

    @Description("code: 403. success: false. \n" +
            "Message: \"Email, password and name are required fields\"")
    @Test
    public void registerCustomerWithIncompleteDataTest() throws InterruptedException, JSONException {
        Thread.sleep(2000);
        response = customer.doRegister(new CustomerBody(emailParameter, passwordParameter, nameParameter));
        response.then().assertThat().statusCode(403);
        String actualResponseBody = response.getBody().asString();
        String expectedResponseBody = responseMessage.errorMessage("Email, password and name are required fields");
        JSONAssert.assertEquals(expectedResponseBody, actualResponseBody, true);
    }
}
