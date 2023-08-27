package praktikum.responseEntities;

import lombok.Getter;
import lombok.Setter;
import praktikum.requestEntities.Ingredient;

import java.util.List;

public class IngredientsResponsed {
    @Getter @Setter
    public String success;
    @Getter @Setter
    public List<Ingredient> data;

    public IngredientsResponsed() { }

    public IngredientsResponsed(String success, List<Ingredient> data) {
        this.success = success;
        this.data = data;
    }
}
