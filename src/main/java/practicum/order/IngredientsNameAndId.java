package practicum.order;

import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

public class IngredientsNameAndId extends IngredientsData {

    List bun = new ArrayList<>();
    List bunName = new ArrayList<>();
    List main = new ArrayList<>();
    List mainName = new ArrayList<>();
    List sauce = new ArrayList<>();
    List sauceName = new ArrayList<>();

    Order order = new Order();
    Response response = order.doGetIngredients();

    String type = response.getBody().path("data.type").toString();
    String[] ingredientsType = ingredientData(type);

    String id = response.getBody().path("data._id").toString();
    String[] ingredientsId = ingredientData(id);

    String name = response.getBody().path("data.name").toString();
    int length = name.length();
    String[] ingredientsName = name.substring(1, length - 1).split(",");

    public IngredientsNameAndId() {
        for (int i = 0; i < ingredientsType.length; i++) {
            switch (ingredientsType[i]) {
                case "bun":
                    bun.add("\"" + ingredientsId[i] + "\"");
                    bunName.add(ingredientsName[i]);
                    break;
                case "main":
                    main.add("\"" + ingredientsId[i] + "\"");
                    mainName.add(ingredientsName[i]);
                    break;
                case "sauce":
                    sauce.add("\"" + ingredientsId[i] + "\"");
                    sauceName.add(ingredientsName[i]);
                    break;
            }
        }
    }

    public List getBun() {
        return bun;
    }
    public  List getBunName(){
        return bunName;
    }

    public List getMain() {
        return main;
    }

    public List getMainName() {
        return mainName;
    }

    public List getSauce() {
        return sauce;
    }

    public List getSauceName() {
        return sauceName;
    }
    public  String[] getIngredientsId(){
        return ingredientsId;
    }
}
