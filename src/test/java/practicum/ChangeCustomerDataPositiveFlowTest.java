package practicum;

import io.restassured.response.Response;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import practicum.customer.Customer;
import practicum.customer.CustomerClient;
import practicum.customer.TokenAndResponseBody;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ChangeCustomerDataPositiveFlowTest {

    static String email = "lidkin@mail.ru";
    static String password = "123";
    static String name = "Lidkin";

    static String newEmail = "lidLid@mail.ru";
    static String newPassword = "321";
    static String newName = "LidLid";

    static String accessToken;
    static CustomerClient customerClient = new CustomerClient();
    static Customer customer = new Customer(
            email,
            password,
            name);

    @Parameterized.Parameter
    public String emailParameter;

    @Parameterized.Parameter(1)
    public String passwordParameter;

    @Parameterized.Parameter(2)
    public String nameParameter;

    @Parameterized.Parameters(name = "email {0}, password {1}, name {2} || statusCode {3}")
    public static Object[][] registrationData(){
        return new Object[][]{
                {newEmail, newPassword, newName},
                {email, newPassword, newName},
                {email, password, newName},
                {newEmail, newPassword, name},
                {newEmail, password, name},
        };
    }

    @BeforeClass
    public static void setUp(){
        Response response = customerClient.doRegister(customer);
        accessToken = new TokenAndResponseBody().token(response).get(0);
    }

    @AfterClass
    public static void cleanUp(){
        customerClient.doDelete(accessToken);
    }

    @Test
    public void changeCustomerInformation(){
        Customer changedCustomer = new Customer(emailParameter, passwordParameter, nameParameter);
        TokenAndResponseBody body = new TokenAndResponseBody(emailParameter.toLowerCase(), nameParameter);
        Response response = customerClient.doPatch(changedCustomer, accessToken);
        response.then().assertThat().statusCode(200);
        String actualResponseBody = response.body().prettyPrint();
        String expectedResponseBody = body.customerDataBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }

}
