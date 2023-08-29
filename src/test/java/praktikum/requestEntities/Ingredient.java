package praktikum.requestEntities;

import lombok.Getter;
import lombok.Setter;

public class Ingredient {
    @Getter
    @Setter
    String _id;
    @Getter @Setter
    String name;
    @Getter @Setter
    String type;
    @Getter @Setter
    String proteins;
    @Getter @Setter
    String fat;
    @Getter @Setter
    String carbohydrates;
    @Getter @Setter
    String calories;
    @Getter @Setter
    String price;
    @Getter @Setter
    String image;
    @Getter @Setter
    String image_mobile;
    @Getter @Setter
    String image_large;
    @Getter @Setter
    String __v;

    public Ingredient() { }

    public Ingredient(String _id, String name, String type, String proteins, String fat,
                        String carbohydrates, String calories, String price, String image,
                        String image_mobile, String image_large, String __v) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.proteins = proteins;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.calories = calories;
        this.price = price;
        this.image = image;
        this.image_mobile = image_mobile;
        this.image_large = image_large;
        this.__v = __v;
    }
}
