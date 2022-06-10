package practicum.order;

import io.restassured.response.Response;
import practicum.BaseHttp;

public class Order extends BaseHttp {

    private final String apiIngredients = "/api/ingredients";
    private final String apiOrders = "/api/orders";


    public Response doCreateOrder(Object body) {
        return doPostRequest(apiOrders, body);
    }

    public Response doCreateOrderWithToken(Object body, String token){
        return doPostRequestWithParam(apiOrders, body, token);
    }

    public Response doGetIngredients() {
        return doGetRequest(apiIngredients);
    }

    public  Response doGetOrders(){
        return doGetRequest(apiOrders);
    }

    public  Response doGetOrdersWithToken(String token){
        return doGetWithTokenRequest(apiOrders, token);
    }

}
