package shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shoppinglist.entity.ShoppingList;

import java.util.List;

public interface ShoppingListRepo extends JpaRepository<ShoppingList, Integer>
{
    @Query("select s from ShoppingList s where upper(s.title) like upper(?1) and s.deletedAt is null")
    List<ShoppingList> findByTitleLikeIgnoreCase(String title);

    @Query("select s from ShoppingList s where s.deletedAt is null")
    List<ShoppingList> findAllDeletedAtIsNull();

}
