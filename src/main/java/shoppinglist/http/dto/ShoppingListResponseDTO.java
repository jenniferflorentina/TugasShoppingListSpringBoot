package shoppinglist.http.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shoppinglist.entity.ShoppingList;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListResponseDTO {
    private Integer id;
    private String title;
    private String date;
    private List<ShoppingListDetailResponseDTO> details;

    public ShoppingListResponseDTO(ShoppingList entity) {
        if(entity == null) return;

        setId(entity.getId());
        setDate(entity.getDate().toString());
        setTitle(entity.getTitle());
        setDetails(entity.getDetails().stream().map(ShoppingListDetailResponseDTO::new).collect(Collectors.toList()));
    }
}
