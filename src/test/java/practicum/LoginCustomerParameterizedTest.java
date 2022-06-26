package practicum;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.skyscreamer.jsonassert.JSONAssert;
import practicum.customer.Credentials;
import practicum.customer.CustomerBody;
import practicum.customer.Customer;

@RunWith(Parameterized.class)
public class LoginCustomerParameterizedTest {

    static String registerToken;
    Response errorLoginResponse;
    static Credentials credentials = new Credentials();
    static ResponseMessage responseMessage = new ResponseMessage();
    static Customer customer = new Customer();
    static CustomerBody body = new CustomerBody(
            credentials.getEmail(),
            credentials.getPassword(),
            credentials.getName());


    @Parameterized.Parameter
    public String emailParameter;

    @Parameterized.Parameter(1)
    public String passwordParameter;

    @Parameterized.Parameters(name = " _{0}_ || _{1}_ ")
    public static Object[][] credentialsData(){
        return new Object[][]{
            {null, body.getPassword()},
            {credentials.getEmail(), body.getPassword()},
            {body.getEmail(), null},
            {body.getEmail(), credentials.getPassword()},
            {credentials.getEmail(), credentials.getPassword()},
        };
    }

    @BeforeClass
    public static void getToken() {
        registerToken = customer.doRegister(body).body().path("accessToken").toString().substring(7);
    }

    @AfterClass
    public static void cleanUp() {
        customer.doDelete(registerToken);
    }

    @After
    public void checkAndDelete() throws InterruptedException {
        Thread.sleep(1000);
        if (errorLoginResponse.body().path("accessToken") != null)
            customer.doDelete(errorLoginResponse.body().path("accessToken").toString().substring(7));
    }

    @Description("code: 401. success: false. \n" +
            "Message: \"email or password are incorrect\"")
    @Test
    public void loginCustomerWithIncompleteDataTest() throws JSONException {
        errorLoginResponse = customer.doLogin(new CustomerBody(emailParameter, passwordParameter));
        errorLoginResponse.then().assertThat().statusCode(401);
        String expected = responseMessage.errorMessage("email or password are incorrect");
        JSONAssert.assertEquals(expected, errorLoginResponse.getBody().asString(), true);
    }

}
