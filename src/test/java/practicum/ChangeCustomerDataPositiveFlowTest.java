package practicum;

import io.qameta.allure.Description;
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
public class ChangeCustomerDataPositiveFlowTest {

    static Credentials credentials = new Credentials();
    static String accessToken;
    static Customer customer = new Customer();
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
    public static void setUp() throws InterruptedException {
        Thread.sleep(1000);
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
        ResponseMessage responseMessage = new ResponseMessage(emailParameter, nameParameter);
        Response response = customer.doUpdate(changedCustomerBody, accessToken);
        response.then().assertThat().statusCode(200);
        JSONAssert.assertEquals(responseMessage.responseBody(), response.getBody().asString(), true);
    }

}
