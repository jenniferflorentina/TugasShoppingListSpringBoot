package shoppinglist.http.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingListDetailCreateDTO {
    private Integer id;

    private String product;

    private Float quantity;

    private String unit;

    private String memo;
}
