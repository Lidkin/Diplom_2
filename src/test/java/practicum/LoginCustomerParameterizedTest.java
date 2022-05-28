package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import practicum.customer.Customer;
import practicum.customer.CustomerClient;
import practicum.customer.TokenAndResponseBody;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class LoginCustomerParameterizedTest {

    static String email = "lidkin@mail.ru";
    static String password = "333-kokoko-333";
    static String name = "Kokofonik";

    static String wrongEmail = "lidkin@mail.com";
    static String wrongPassword = "333-kokoko-111";

    static String accessToken;
    static CustomerClient customerClient = new CustomerClient();
    static TokenAndResponseBody tokenOrBody = new TokenAndResponseBody();
    static Customer customerBody = new Customer(
            email,
            password,
            name);

    @BeforeClass
    public static void getToken() {
        Response before = customerClient.doRegister(customerBody);
        accessToken = tokenOrBody.token(before).get(0);
    }

    @AfterClass
    public static void cleanUp() {
        customerClient.doDelete(accessToken);
    }

    @Parameterized.Parameter
    public String emailParameter;

    @Parameterized.Parameter(1)
    public String passwordParameter;

    @Parameterized.Parameters(name = " _{0}_ || _{1}_ ")
    public static Object[][] credentialsData(){
        return new Object[][]{
            {null, password},
            {wrongEmail, password},
            {email, null},
            {email, wrongPassword},
        };
    }

    @Description("code: 401. success: false.")
    @DisplayName("email or password are incorrect")
    @Test
    public void registerCustomerWithIncompleteDataTest() {
        Response errorResponse = customerClient.doLogin(new Customer(emailParameter, passwordParameter));
        errorResponse.then().assertThat().statusCode(401);
        String actualResponseBody = errorResponse.body().prettyPrint();
        String expectedResponseBody = tokenOrBody.wrongLoginMessageBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }
}
