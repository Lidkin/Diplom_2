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
import practicum.customer.CustomerBody;
import practicum.order.Ingredients;
import practicum.order.IngredientsNameAndId;
import practicum.order.Order;


@RunWith(Parameterized.class)
public class CreateOrderTest {

    static String email = "lidkin@mail.ru";
    static String password = "333-kokoko-333";
    static String name = "Kokofonik";

    static Customer customer = new Customer();
    static CustomerBody customerBody = new CustomerBody(email,password,name);
    static TokenAndResponseBody tokenOrBody = new TokenAndResponseBody();
    static IngredientsNameAndId nameIngredient = new IngredientsNameAndId();
    static String accessToken;

    @BeforeClass
    public static void getToken() {
        Response before = customer.doRegister(customerBody);
        accessToken = tokenOrBody.token(before).get(0);
    }

    @AfterClass
    public static void cleanUp() {
        customer.doDelete(accessToken);
    }

    Ingredients ingredients = new Ingredients();
    Order order = new Order();

    @Parameterized.Parameter
    public int sauceCount;

    @Parameterized.Parameter(1)
    public int mainCount;

    @Parameterized.Parameter(2)
    public int bunIndex;

    @Parameterized.Parameter(3)
    public String bunName;

    @Parameterized.Parameters(name = "Chosen: {0} sauces, {1} mains and {3}")
    public static Object[][] ingredientsAmount(){
        return new Object[][]{
            {0, 1, 0, nameIngredient.getBunName().get(0)},
            {1, 0, 1, nameIngredient.getBunName().get(1)},
            {6, 2, 1, nameIngredient.getBunName().get(1)},
            {4, 10, 0, nameIngredient.getBunName().get(0)},
        };
    }

    @Description("code: 200 success: true.")
    @DisplayName("create order for authorized customer")
    @Test
    public void createOrderAuthorizedCustomerPositiveFlowTest() throws InterruptedException {
        IngredientsNameAndId id = new IngredientsNameAndId();
        String body = "{\"ingredients\": [" + id.getBun().get(bunIndex) + "," + ingredients.randomIngredientsAmount(sauceCount, mainCount).toString().substring(1) + "}";
        String names = bunName + "," + ingredients.getSaucesNames().toString() + "," + ingredients.getMainNames().toString();
        Response response = order.doCreateOrderAuthorizedCustomer(body,accessToken);
        response.then().assertThat().statusCode(200);
        System.out.println(response.body().path("order.ingredients.name").toString());
        System.out.println(names);
        Thread.sleep(2000);
    }

    @Description("code: 200 success: true.")
    @DisplayName("create order for an unauthorized customer")
    @Test
    public void createOrderPositiveFlowTest() throws InterruptedException {
        IngredientsNameAndId id = new IngredientsNameAndId();
        String body = "{\"ingredients\": [" + id.getBun().get(bunIndex) + "," + ingredients.randomIngredientsAmount(sauceCount, mainCount).toString().substring(1) + "}";
        String names = ingredients.getMainNames() + "," + ingredients.getSaucesNames()+ "," + bunName;
        Response response = order.doCreateOrder(body);
        response.then().assertThat().statusCode(200);
        response.body().prettyPrint();
        System.out.println(names);
        Thread.sleep(2000);
    }
    
}
