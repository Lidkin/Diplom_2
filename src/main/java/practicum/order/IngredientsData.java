package practicum.order;

public class IngredientsData {

    protected String[] ingredientData(String ingredient) {
        int length = ingredient.length();
        return ingredient.substring(1, length - 1).replaceAll(" ", "").split(",");
    }

}
