package practicum.order;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import java.util.*;


public class Ingredients extends IngredientsData {

    Order order = new Order();
    String json = order.doGetIngredients().asString();
    Map<Integer, List<String>> bun = new HashMap<>();
    Map<Integer,  List<String>> sauce = new HashMap<>();
    Map<Integer,  List<String>> filling = new HashMap<>();

    public Ingredients() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json).get("data");
        IngredientsData[] ingredients = mapper.readValue(node.toString(), IngredientsData[].class);
        int b = 0, f = 0, s = 0;
        for (IngredientsData el : ingredients) {
            switch (el.getType()) {
                case "bun" -> {
                    bun.put(b, Arrays.asList(el.get_id(), el.getName()));
                    b++;
                }
                case "main" -> {
                    filling.put(f, Arrays.asList(el.get_id(), el.getName()));
                    f++;
                }
                case "sauce" -> {
                    sauce.put(s, Arrays.asList(el.get_id(), el.getName()));
                    s++;
                }
            }
        }
    }

    public Map <String, List<String>> buildRandomBurger(int amountOfSauces, int amountOfMain){
        Map<String, List<String>> burger = new HashMap<>();
        List <String> customIngredients = new ArrayList<>();
        List <String> ingredientsNames = new ArrayList<>();
        Random random = new Random();
        int n = random.nextInt(bun.size());
        customIngredients.add(bun.get(n).get(0));
        ingredientsNames.add(bun.get(n).get(1));
        for (int i = 0; i < amountOfSauces; i++){
            int index = random.nextInt(sauce.size());
            customIngredients.add(sauce.get(index).get(0));
            ingredientsNames.add(sauce.get(index).get(1));
        }
        for (int k = 0; k < amountOfMain; k++) {
            int index = random.nextInt(filling.size());
            customIngredients.add(filling.get(index).get(0));
            ingredientsNames.add(filling.get(index).get(1));
        }
        burger.put("id", customIngredients);
        burger.put("name", ingredientsNames);
        return burger;
    }

}
