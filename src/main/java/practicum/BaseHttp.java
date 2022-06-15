package practicum;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;


public class BaseHttp {

    private final RestAssuredConfig config = RestAssuredConfig.newConfig()
            .sslConfig(new SSLConfig().relaxedHTTPSValidation())
            .redirect(new RedirectConfig().followRedirects(true));

    private final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://stellarburgers.nomoreparties.site")
            .setContentType(ContentType.JSON)
            .setConfig(config)
            .log(LogDetail.METHOD)
            .log(LogDetail.BODY)
            .addFilter(new ResponseLoggingFilter(LogDetail.STATUS))
            .build();

    protected Response doGetWithTokenRequest(String api, String token) {
        return given().spec(requestSpec)
                .auth().oauth2(token)
                .get(api);
    }

    protected Response doGetRequest(String api) {
        return given().spec(requestSpec)
                .get(api);
    }

    protected Response doPostRequest(String api, Object body) {
        return given().spec(requestSpec)
                .body(body)
                .post(api);
    }

    protected Response doPostRequestWithParam(String api, Object body, String token) {
        return given().spec(requestSpec)
                .auth().oauth2(token)
                .body(body)
                .post(api);
    }

    protected Response doPatchRequestWithParam(String api, Object body, String token) {
        return given()
                .spec(requestSpec)
                .auth().oauth2(token)
                .body(body)
                .patch(api);
    }

    protected Response doPatchRequest(String api, Object body) {
        return given().spec(requestSpec)
                .body(body)
                .patch(api);
    }

    protected Response doDeleteRequest(String api, String token) {
        return given().spec(requestSpec)
                .auth().oauth2(token)
                .delete(api);
    }

}
