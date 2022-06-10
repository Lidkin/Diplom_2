package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.*;
import org.skyscreamer.jsonassert.JSONAssert;
import practicum.customer.Credentials;
import practicum.customer.CustomerBody;
import practicum.customer.Customer;


public class ChangeCustomerDataNegativeFlowTest {

    Credentials credentials = new Credentials();
    ResponseMessage responseMessage = new ResponseMessage();
    Customer customer = new Customer();
    CustomerBody body = new CustomerBody(credentials.getEmail(), credentials.getPassword(), credentials.getName());
    CustomerBody bodyExists = new CustomerBody(credentials.getEmail(), credentials.getPassword(), credentials.getName());
    String accessToken, accessTokenExists;
    Response response;

    @Before
    public void setUp() throws InterruptedException {
        Thread.sleep(1000);
        accessToken = customer.doRegister(body).body().path("accessToken").toString().substring(7);
        accessTokenExists = customer.doRegister(bodyExists).body().path("accessToken").toString().substring(7);
    }

    @After
    public void cleanUp(){
        customer.doDelete(accessToken);
        customer.doDelete(accessTokenExists);
    }

    @Description("code: 403. success: false. \n" +
            "Message: \"User with such email already exists\"")
    @DisplayName("Use email that is already exists.")
    @Test
    public void emailAlreadyExistsTest() throws JSONException {
        Response response = customer.doUpdate(body.setEmail(bodyExists.getEmail()), accessToken);
        response.then().assertThat().statusCode(403);
        String expected = responseMessage.errorMessage("User with such email already exists");
        JSONAssert.assertEquals(expected, response.body().asString(), true);
    }

    @Description("code: 401. success: false. \n" +
            "Message: \"You should be authorised\"")
    @DisplayName("Unauthorized customer.")
    @Test
    public void unauthorizedCustomerTest() throws JSONException {
        response = customer.doUpdateEmptyData();
        response.then().assertThat().statusCode(401);
        String expected = responseMessage.errorMessage("You should be authorised");
        JSONAssert.assertEquals(expected, response.body().asString(), true);
    }

}
