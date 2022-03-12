package shoppinglist.http;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shoppinglist.entity.ShoppingList;
import shoppinglist.http.dto.ShoppingListCreateDTO;
import shoppinglist.http.dto.ShoppingListResponseDTO;
import shoppinglist.service.ShoppingListService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("shopping-list")
@AllArgsConstructor
public class ShoppingListCtrl {

    private final ShoppingListService service;

    @GetMapping
    public Iterable<ShoppingListResponseDTO> getAll(
            @RequestParam(value = "title", required = false) String title
    ){
        List<ShoppingList> result = service.getAllData(title);
        return result.stream().map(ShoppingListResponseDTO::new).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ShoppingListResponseDTO> create(@RequestBody ShoppingListCreateDTO json){
        ShoppingListResponseDTO result = service.create(json);

        if(result != null){
            return ResponseEntity.ok(result);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("{id}")
    @Transactional
    public ShoppingListResponseDTO update(
            @PathVariable("id") ShoppingList shoppingList,
            @RequestBody ShoppingListCreateDTO json) throws Exception
    {
        return service.update(json, shoppingList);
    }

    @DeleteMapping("{id}")
    public ShoppingListResponseDTO delete(
            @PathVariable("id") ShoppingList shoppingList
    ) throws Exception {
        ShoppingList deletedEntity = service.delete(shoppingList);
        return new ShoppingListResponseDTO(deletedEntity);
    }
}
