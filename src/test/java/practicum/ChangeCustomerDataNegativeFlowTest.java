package practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import practicum.customer.CustomerBody;
import practicum.customer.Customer;
import static org.junit.Assert.assertEquals;

public class ChangeCustomerDataNegativeFlowTest {

    static String email = "lidkin@mail.ru";
    static String password = "12345";
    static String name = "Lid";
    static String accessToken;

    static Customer customer = new Customer();
    static CustomerBody body = new CustomerBody(email, password, name);
    TokenAndResponseBody tokenOrBody = new TokenAndResponseBody();

    @BeforeClass
    public static void setUp() throws InterruptedException {
        Thread.sleep(1000);
        Response response = customer.doRegister(body);
        accessToken = new TokenAndResponseBody().token(response).get(0);
    }

    @AfterClass
    public static void cleanUp(){
        customer.doDelete(accessToken);
    }

    @Description("code: 403. success: false. \n" +
            "Message: \"User with such email already exists\"")
    @DisplayName("Use email that is already exists.")
    @Test
    public void emailAlreadyExistsTest(){
        Response response = customer.doPatchWithData(body.setEmail("test-data@yandex.ru"), accessToken);
        response.then().assertThat().statusCode(403);
        String actualResponseBody = response.body().prettyPrint();
        String expectedResponseBody = tokenOrBody.usedCustomerEmailMessageBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Description("code: 401. success: false. \n" +
            "Message: \"You should be authorised\"")
    @DisplayName("Unauthorized customer.")
    @Test
    public void unauthorizedCustomerTest(){
        Response response = customer.doPatch();
        response.then().assertThat().statusCode(401);
        String actualResponseBody = response.body().prettyPrint();
        String expectedResponseBody = tokenOrBody.unauthorizedCustomerMessageBody();
        assertEquals(expectedResponseBody, actualResponseBody);
    }

}
