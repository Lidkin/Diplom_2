package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.skyscreamer.jsonassert.JSONAssert;
import practicum.customer.Credentials;
import practicum.customer.CustomerBody;
import practicum.customer.Customer;

@RunWith(Parameterized.class)
public class ChangeCustomerDataTest {

    static Credentials credentials = new Credentials();
    static String accessToken;
    static Customer customer = new Customer();
    static Response response;
    static CustomerBody body = new CustomerBody(
            credentials.getEmail(),
            credentials.getPassword(),
            credentials.getName());

    @Parameterized.Parameter
    public String emailParameter;

    @Parameterized.Parameter(1)
    public String passwordParameter;

    @Parameterized.Parameter(2)
    public String nameParameter;

    @Parameterized.Parameters(name = "email {0}, password {1}, name {2}")
    public static Object[][] registrationData(){
        return new Object[][]{
                {credentials.getEmail(), credentials.getPassword(), credentials.getName()},
                {body.getEmail(), credentials.getPassword(), credentials.getName()},
                {body.getEmail(), body.getPassword(), credentials.getName()},
                {credentials.getEmail(), credentials.getPassword(), body.getName()},
                {credentials.getEmail(), body.getPassword(), body.getName()},
        };
    }

    @BeforeClass
    public static void setUp() {
        accessToken = customer.doRegister(body).body().path("accessToken").toString().substring(7);
     }

    @AfterClass
    public static void cleanUp(){
        customer.doDelete(accessToken);
    }

    @Description("code: 200. success: true. \n" +
            "Change customer information. Parametrized Test.")
    @Test
    public void changeCustomerInformation() throws JSONException {
        CustomerBody changedCustomerBody = new CustomerBody(emailParameter, passwordParameter, nameParameter);
        response = customer.doUpdate(changedCustomerBody, accessToken);
        response.then().assertThat().statusCode(200);
        ResponseMessage responseMessage = new ResponseMessage(emailParameter, nameParameter);
        JSONAssert.assertEquals(responseMessage.responseBody(), response.getBody().asString(), true);
    }

    @Description("code: 401. success: false. \n" +
            "Message: \"You should be authorised\"")
    @DisplayName("Unauthorized customer.")
    @Test
    public void unauthorizedCustomerTest() throws JSONException {
        CustomerBody changedCustomerBody = new CustomerBody(emailParameter, passwordParameter, nameParameter);
        response = customer.doUpdateEmptyAccessToken(changedCustomerBody);
        response.then().assertThat().statusCode(401);
        String expected = new ResponseMessage().errorMessage("You should be authorised");
        JSONAssert.assertEquals(expected, response.body().asString(), true);
    }

}
