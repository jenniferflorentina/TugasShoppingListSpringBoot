package shoppinglist.http.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListDetailCreateDTO {
    private Integer id;

    private String product;

    private Float quantity;

    private String unit;

    private String memo;
}
