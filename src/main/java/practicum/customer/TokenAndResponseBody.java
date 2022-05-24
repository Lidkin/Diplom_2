package practicum.customer;

import io.restassured.response.Response;
import java.util.ArrayList;


public class TokenAndResponseBody {

    public TokenAndResponseBody(){}

    public TokenAndResponseBody(String email, String name) {
        this.email = email;
        this.name = name;
    }

    String email;
    String name;

    public ArrayList<String> token(Response response){
        ArrayList<String> tokens = new ArrayList<>();

            String[] partOfResponse = response.body().path("accessToken").toString().split(" ");
            tokens.add(partOfResponse[1]);
            tokens.add(response.body().path("refreshToken").toString());

        return tokens;
    }

    public  String expectedResponseBody(String accessToken, String refreshToken){
        String responseBody = "{\n" +
                "    \"success\": true,\n" +
                "    \"user\": {\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"name\": \"" + name + "\"\n" +
                "    },\n" +
                "    \"accessToken\": \"Bearer " + accessToken + "\",\n" +
                "    \"refreshToken\": \"" + refreshToken + "\"\n" +
                "}";
        return responseBody;
    }

    public String expectedRepeatedRegisteredBody(){
        String responseBody = "{\n" +
                "    \"success\": false,\n" +
                "    \"message\": \"User already exists\"\n" +
                "}";
        return responseBody;
    }

    public String expectedWrongRegisteredBody(){
        String responseBody = "{\n" +
                "    \"success\": false,\n" +
                "    \"message\": \"Email, password and name are required fields\"\n" +
                "}";
        return responseBody;
    }

}
