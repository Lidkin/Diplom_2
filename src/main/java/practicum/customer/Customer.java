package practicum.customer;

import io.restassured.response.Response;
import practicum.BaseHttp;

public class Customer extends BaseHttp {

    private final String apiUser = "/api/auth/user";
    private final String apiLogin = "/api/auth/login";
    private final String apiRegister = "/api/auth/register";


    public Response doRegister(CustomerBody body){
        return doPostRequest(apiRegister, body);
    }

    public Response doLogin(CustomerBody body){
        return doPostRequest(apiLogin, body);
    }

    public Response doDelete(String token){
        return doDeleteRequest(apiUser, token);
    }

    public Response doUpdate(CustomerBody body, String token){
        return doPatchRequestWithParam(apiUser, body, token);
    }
    public Response doUpdateEmptyData(){
        return doPatchRequest(apiUser);
    }

}
