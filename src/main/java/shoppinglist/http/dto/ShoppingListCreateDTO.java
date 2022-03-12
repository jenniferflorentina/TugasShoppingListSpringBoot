package shoppinglist.http.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListCreateDTO {
    private String title;
    private Date date;
    private List<ShoppingListDetailCreateDTO> details;
}
