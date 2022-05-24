package practicum.customer;

import io.restassured.response.Response;
import practicum.BaseHttp;

public class CustomerClient extends BaseHttp {

    private final String baseURI = "https://stellarburgers.nomoreparties.site";
    private final String apiUser = "/api/auth/user"; //удаление пользователя
    private final String apiLogin = "/api/auth/login"; //логин
    private final String apiRegister = "/api/auth/register"; //создание уникального пользователя


    public Response doPost(Object body){
        return doPostRequest(baseURI + apiRegister, body);
    }

    public Response doDelete(String token){
        return doDeleteRequest(baseURI + apiUser, token);
    }

    public Response doGet(String token){
         return doGetWithTokenRequest(baseURI + apiUser, token);
    }

}
