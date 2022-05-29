package practicum;

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

    public  String responseBody(String accessToken, String refreshToken){
        return  "{\n" +
                "    \"success\": true,\n" +
                "    \"user\": {\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"name\": \"" + name + "\"\n" +
                "    },\n" +
                "    \"accessToken\": \"Bearer " + accessToken + "\",\n" +
                "    \"refreshToken\": \"" + refreshToken + "\"\n" +
                "}";
    }

    public String repeatedRegisteredMessageBody(){
        return  "{\n" +
                "    \"success\": false,\n" +
                "    \"message\": \"User already exists\"\n" +
                "}";
    }

    public String wrongRegisteredMessageBody(){
        return  "{\n" +
                "    \"success\": false,\n" +
                "    \"message\": \"Email, password and name are required fields\"\n" +
                "}";
    }

    public String validLoginResponseBody(String accessToken, String refreshToken){
        return  "{\n" +
                "    \"success\": true,\n" +
                "    \"accessToken\": \"Bearer " + accessToken + "\",\n" +
                "    \"refreshToken\": \"" + refreshToken + "\",\n" +
                "    \"user\": {\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"name\": \"" + name + "\"\n" +
                "    }\n" +
                "}";
    }

    public String wrongLoginMessageBody(){
        return  "{\n" +
                "    \"success\": false,\n" +
                "    \"message\": \"email or password are incorrect\"\n" +
                "}";
    }

    public String customerDataBody(){
        return  "{\n" +
                "    \"success\": true,\n" +
                "    \"user\": {\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"name\": \"" + name + "\"\n" +
                "    }\n" +
                "}";
    }

    public String usedCustomerEmailMessageBody(){
        return  "{\n" +
                "    \"success\": false,\n" +
                "    \"message\": \"User with such email already exists\"\n" +
                "}";
    }

    public String unauthorizedCustomerMessageBody(){
        return  "{\n" +
                "    \"success\": false,\n" +
                "    \"message\": \"You should be authorised\"\n" +
                "}";
    }

    public String emptyIngredientsOrderMessageBody(){
        return  "{\n" +
                "    \"success\": false,\n" +
                "    \"message\": \"Ingredient ids must be provided\"\n" +
                "}";
    }
}
