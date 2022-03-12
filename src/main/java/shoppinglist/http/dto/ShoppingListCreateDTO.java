package shoppinglist.http.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ShoppingListCreateDTO {
    private String title;
    private Date date;
    private List<ShoppingListDetailCreateDTO> details;
}
