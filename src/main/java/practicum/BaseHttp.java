package practicum;

import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BaseHttp {

    private final String JSON = "application/json";
    private final RestAssuredConfig config = RestAssuredConfig.newConfig()
            .sslConfig(new SSLConfig().relaxedHTTPSValidation())
            .redirect(new RedirectConfig().followRedirects(true));


    protected Response doGetWithTokenRequest(String uri, String token) {
        return given().config(config)
                .auth().oauth2(token)
                .get(uri);
    }

    protected Response doGetRequest(String uri) {
        return given().config(config)
                .header("Content-Type", JSON)
                .get(uri);
    }

    protected Response doPostRequest(String uri, Object body){
        return given().config(config)
                .header("Content-Type", JSON)
                .body(body)
                .post(uri);
    }

    protected Response doPostRequestWithParam(String uri, Object body, String token){
        return given().config(config)
                .header("Content-Type", JSON)
                .auth().oauth2(token)
                .body(body)
                .post(uri);
    }
    protected Response doPatchRequestWithParam(String uri, Object body, String token){
        return given().config(config)
                .header("Content-Type", JSON)
                .auth().oauth2(token)
                .body(body)
                .patch(uri);
    }

    protected Response doPatchRequest(String uri){
        return given().config(config)
                .header("Content-Type", JSON)
                .patch(uri);
    }

    protected Response doDeleteRequest(String uri, String token){
        return given().config(config)
                .auth().oauth2(token)
                .delete(uri);
    }

}
