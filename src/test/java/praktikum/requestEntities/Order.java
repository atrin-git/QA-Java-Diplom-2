package praktikum.requestEntities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Order {
    @Getter @Setter
    List<String> ingredients;

    public Order() { }

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
