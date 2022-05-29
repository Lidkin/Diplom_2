package practicum.customer;

import io.restassured.response.Response;
import practicum.BaseHttp;

public class Customer extends BaseHttp {

    private final String baseURI = "https://stellarburgers.nomoreparties.site";
    private final String apiUser = "/api/auth/user";
    private final String apiLogin = "/api/auth/login";
    private final String apiRegister = "/api/auth/register";


    public Response doRegister(Object body){
        return doPostRequest(baseURI + apiRegister, body);
    }

    public Response doLogin(Object body){
        return doPostRequest(baseURI + apiLogin, body);
    }

    public Response doDelete(String token){
        return doDeleteRequest(baseURI + apiUser, token);
    }

    public Response doGet(String token){
         return doGetWithTokenRequest(baseURI + apiUser, token);
    }

    public Response doPatchWithData(Object body, String token){
        return doPatchRequestWithParam(baseURI + apiUser, body, token);
    }
    public Response doPatch(){
        return doPatchRequest(baseURI + apiUser);
    }

}
