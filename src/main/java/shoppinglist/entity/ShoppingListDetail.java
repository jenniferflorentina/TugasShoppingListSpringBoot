package shoppinglist.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import shoppinglist.http.dto.ShoppingListDetailCreateDTO;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "shopping_list_detail")
@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "shopping_list_detail_gen", sequenceName = "shopping_list_detail_id_seq", allocationSize = 1)
public class ShoppingListDetail
{
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shopping_list_detail_gen"
    )
    private Integer id;

    @Column(name = "order_number")
    private Integer order;
    
    @Column(name = "product_name")
    private String product;
    
    @Column
    private Float quantity;
    
    @Column
    private String unit;
    
    @Column
    private String memo;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList shoppingList;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    protected Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    protected Timestamp updatedAt;

    @Column(name = "deleted_at")
    protected Timestamp deletedAt;

    public ShoppingListDetail(
            Integer order,
            ShoppingListDetailCreateDTO json,
            ShoppingList shoppingList
    ) {
        this.order = order;
        product = json.getProduct();
        quantity = json.getQuantity();
        unit = json.getUnit();
        memo = json.getMemo();
        this.shoppingList = shoppingList;
    }

    public void applyUpdate(Integer order, ShoppingListDetailCreateDTO json) {
        this.order = order;
        product = json.getProduct();
        quantity = json.getQuantity();
        unit = json.getUnit();
        memo = json.getMemo();
    }

    public void applyDelete() {
        deletedAt = new Timestamp(System.currentTimeMillis());
    }
}
