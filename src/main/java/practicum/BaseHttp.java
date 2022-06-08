package practicum;

import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseHttp {

    private final String baseURI = "https://stellarburgers.nomoreparties.site";
    private final RestAssuredConfig config = RestAssuredConfig.newConfig()
            .sslConfig(new SSLConfig().relaxedHTTPSValidation())
            .redirect(new RedirectConfig().followRedirects(true));
    private final RequestSpecification specification = given().config(config).header("Content-Type", "application/json");

    protected Response doGetWithTokenRequest(String api, String token) {
        return given().config(config)
                .auth().oauth2(token)
                .get(baseURI + api);
    }

    protected Response doGetRequest(String api) {
        return specification.get(baseURI + api);
    }

    protected Response doPostRequest(String api, Object body){
        return specification
                .body(body)
                .post(baseURI + api);
    }

    protected Response doPostRequestWithParam(String api, Object body, String token){
        return specification
                .auth().oauth2(token)
                .body(body)
                .post(baseURI + api);
    }
    protected Response doPatchRequestWithParam(String api, Object body, String token){
        return specification
                .auth().oauth2(token)
                .body(body)
                .patch(baseURI + api);
    }

    protected Response doPatchRequest(String api){
        return specification.patch(baseURI + api);
    }

    protected Response doDeleteRequest(String api, String token){
        return given().config(config)
                .auth().oauth2(token)
                .delete(baseURI + api);
    }

}
