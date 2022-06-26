package practicum.order;

public class IngredientsData {

    private String _id;
    private String name;
    private String type;
    private Number proteins;
    private Number fat;
    private Number carbohydrates;
    private Number calories;
    private Number price;
    private String image;
    private String image_mobile;
    private String image_large;
    private Number __v;

    protected String get_id() {
        return _id;
    }

    protected String getName() {
        return name;
    }

    protected String getType() {
        return type;
    }

    protected void set_id(String _id) {
        this._id = _id;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setType(String type) {
        this.type = type;
    }

    protected Number getProteins() {
        return proteins;
    }

    protected void setProteins(Number proteins) {
        this.proteins = proteins;
    }

    protected Number getFat() {
        return fat;
    }

    protected void setFat(Number fat) {
        this.fat = fat;
    }

    protected Number getCarbohydrates() {
        return carbohydrates;
    }

    protected void setCarbohydrates(Number carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    protected Number getCalories() {
        return calories;
    }

    protected void setCalories(Number calories) {
        this.calories = calories;
    }

    protected Number getPrice() {
        return price;
    }

    protected void setPrice(Number price) {
        this.price = price;
    }

    protected String getImage() {
        return image;
    }

    protected void setImage(String image) {
        this.image = image;
    }

    protected String getImage_mobile() {
        return image_mobile;
    }

    protected void setImage_mobile(String image_mobile) {
        this.image_mobile = image_mobile;
    }

    protected String getImage_large() {
        return image_large;
    }

    protected void setImage_large(String image_large) {
        this.image_large = image_large;
    }

    protected Number get__v() {
        return __v;
    }

    protected void set__v(Number __v) {
        this.__v = __v;
    }

}
