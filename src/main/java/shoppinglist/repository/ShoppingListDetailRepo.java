package shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import shoppinglist.entity.ShoppingList;
import shoppinglist.entity.ShoppingListDetail;

import java.util.List;

public interface ShoppingListDetailRepo extends JpaRepository<ShoppingListDetail, Integer> {
    @Modifying
    @Query("update ShoppingListDetail s set s.deletedAt = CURRENT_TIMESTAMP where s.id not in ?1 and s.shoppingList = ?2")
    void deleteShoppingListDetailsByIdNotInAndShoppingList(List<Integer> ids, ShoppingList shoppingList);
}
