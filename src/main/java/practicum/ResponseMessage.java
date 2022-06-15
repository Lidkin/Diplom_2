package practicum;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class ResponseMessage {

    String email;
    String name;

    public ResponseMessage(){}

    public ResponseMessage(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String errorMessage(String message){
        Map<String, Object> messageBody = new HashMap<>();
        messageBody.put("success", false);
        messageBody.put("message", message);

        Gson gson = new Gson();
        return gson.toJson(messageBody);
    }

    public String validResponseBody(Object accessToken, Object refreshToken) {

        Map<String, String> customer = new HashMap<>();
        customer.put("email", email);
        customer.put("name", name);

        Map<String, Object> validResponse = new HashMap<>();
        validResponse.put("success", true);
        validResponse.put("accessToken", accessToken);
        validResponse.put("refreshToken", refreshToken);
        validResponse.put("user", customer);

        Gson gson = new Gson();
        return gson.toJson(validResponse);
    }

    public String responseBody() {

        Map<String, String> customer = new HashMap<>();
        customer.put("email", email);
        customer.put("name", name);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("user", customer);

        Gson gson = new Gson();
        return gson.toJson(responseBody);
    }

}
