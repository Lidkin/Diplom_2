package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import practicum.customer.CustomerBody;
import practicum.customer.Customer;

import static org.junit.Assert.assertEquals;


public class LoginCustomerTest {

    String email = "lidkin@mail.ru";
    String password = "333-kokoko-333";
    String name = "Kokofonik";
    String accessToken;

    Customer customer = new Customer();
    TokenAndResponseBody tokenOrBody = new TokenAndResponseBody(email, name);
    CustomerBody customerBody = new CustomerBody(
            email,
            password,
            name);

    @Before
    public void getToken() {
        Response before = customer.doRegister(customerBody);
        accessToken = tokenOrBody.token(before).get(0);
    }

    @After
    public void cleanUp() {
        customer.doDelete(accessToken);
    }

    @Description("code: 200. success: true.")
    @DisplayName("login customer: positive flow")
    @Test
    public void registerCustomerPositiveFlowTest() {
        Response response = customer.doLogin(customerBody);
        response.then().assertThat().statusCode(200);
        String loginAccessToken = tokenOrBody.token(response).get(0);
        String loginRefreshToken = tokenOrBody.token(response).get(1);
        String actualResponseBody = response.body().prettyPrint();
        String expectedResponseBody = tokenOrBody.validLoginResponseBody(loginAccessToken, loginRefreshToken);
        assertEquals(expectedResponseBody, actualResponseBody);
    }

}
