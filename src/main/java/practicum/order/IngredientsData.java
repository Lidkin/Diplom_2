package practicum.order;

public class IngredientsData {

    String _id;
    String name;
    String type;
    Number proteins;
    Number fat;
    Number carbohydrates;
    Number calories;
    Number price;
    String image;
    String image_mobile;
    String image_large;
    Number __v;

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Number getProteins() {
        return proteins;
    }

    public void setProteins(Number proteins) {
        this.proteins = proteins;
    }

    public Number getFat() {
        return fat;
    }

    public void setFat(Number fat) {
        this.fat = fat;
    }

    public Number getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Number carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Number getCalories() {
        return calories;
    }

    public void setCalories(Number calories) {
        this.calories = calories;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_mobile() {
        return image_mobile;
    }

    public void setImage_mobile(String image_mobile) {
        this.image_mobile = image_mobile;
    }

    public String getImage_large() {
        return image_large;
    }

    public void setImage_large(String image_large) {
        this.image_large = image_large;
    }

    public Number get__v() {
        return __v;
    }

    public void set__v(Number __v) {
        this.__v = __v;
    }

}
