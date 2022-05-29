package practicum.order;

import java.util.*;

public class Ingredients {

    IngredientsNameAndId nameOrId = new IngredientsNameAndId();

    List sauces = new ArrayList<>(nameOrId.getSauce());
    List saucesNames = new ArrayList<>(nameOrId.getSauceName());
    List sauceName = new ArrayList<>();
    List sauceId = new ArrayList<>();

    List main = new ArrayList<>(nameOrId.getMain());
    List mainNames = new ArrayList<>(nameOrId.getMainName());
    List mainName = new ArrayList<>();
    List mainId = new ArrayList<>();


    public List randomIngredientsAmount(int amountOfSauces, int amountOfMain) {
        List customer = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < amountOfSauces; i++){
            int index = random.nextInt(sauces.size());
            customer.add(sauces.get(index));
            sauceName.add(saucesNames.get(index));
            sauceId.add(sauces.get(index));
        }
        for (int k = 0; k < amountOfMain; k++) {
            int index = random.nextInt(main.size());
            customer.add(main.get(index));
            mainName.add(mainNames.get(index));
            mainId.add(main.get(index));
        }
        return customer;
    }

    public List getSaucesNames(){
        return sauceName;
    }

    public List getMainNames(){
        return mainName;
    }

}
