package practicum.order;

import io.restassured.response.Response;
import practicum.BaseHttp;

public class Order extends BaseHttp {

    private final String baseURI = "https://stellarburgers.nomoreparties.site";
    private final String apiIngredients = "/api/ingredients";
    private final String apiOrders = "/api/orders";


    public Response doCreateOrder(Object body) {
        return doPostRequest(baseURI + apiOrders, body);
    }

    public Response doCreateOrderAuthorizedCustomer(Object body, String token){
        return doPostRequestWithParam(baseURI + apiOrders, body, token);
    }

    public Response doGetIngredients() {
        return doGetRequest(baseURI + apiIngredients);
    }

    public  Response doGetOrders(){
        return doGetRequest(baseURI + apiOrders);
    }

    public  Response doGetOrdersWithParam(String token){
        return doGetWithTokenRequest(baseURI + apiOrders, token);
    }

}
