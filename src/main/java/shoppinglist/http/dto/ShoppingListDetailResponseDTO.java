package shoppinglist.http.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shoppinglist.entity.ShoppingList;
import shoppinglist.entity.ShoppingListDetail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListDetailResponseDTO {
    private Integer id;

    private Integer order;

    private String product;

    private Float quantity;

    private String unit;

    private String memo;

    public ShoppingListDetailResponseDTO(ShoppingListDetail entity) {
        if(entity == null) return;

        setId(entity.getId());
        setOrder(entity.getOrder());
        setProduct(entity.getProduct());
        setQuantity(entity.getQuantity());
        setUnit(entity.getUnit());
        setMemo(entity.getMemo());
    }
}
