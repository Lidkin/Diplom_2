package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import practicum.customer.Customer;
import practicum.customer.CustomerClient;
import practicum.customer.TokenAndResponseBody;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RegisterCustomerParameterizedTest {

    static String email = "lidkin@mail.ru";
    static String password = "333-kokoko-333";
    static String name = "Kokofonik";

    TokenAndResponseBody body = new TokenAndResponseBody();
    final CustomerClient customerClient = new CustomerClient();

    @Parameterized.Parameter
    public String emailParameter;

    @Parameterized.Parameter(1)
    public String passwordParameter;

    @Parameterized.Parameter(2)
    public String nameParameter;

    @Parameterized.Parameter(3)
    public String notFilled;

    @Parameterized.Parameters(name = " _{0}_ || _{1}_ || _{2}_ || not filled: {3}")
    public static Object[][] getData(){
        return new Object[][]{
            {null, password, name, "email"},
            {email, null, name, "password"},
            {email, password, null, "name"},
        };
    }

    @Description("code: 403. success: false.")
    @DisplayName("Email, password and name are required fields")
    @Test
    public void registerCustomerWithIncompleteData() {
        Response errorResponse = customerClient.doPost(new Customer(emailParameter, passwordParameter, nameParameter));
        errorResponse.then().assertThat().statusCode(403);
        String actualResponseBody = errorResponse.body().prettyPrint();
        String expectedResponseBody = body.expectedWrongRegisteredBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }
}
