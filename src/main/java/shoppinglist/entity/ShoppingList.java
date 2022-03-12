package shoppinglist.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import shoppinglist.http.dto.ShoppingListCreateDTO;
import shoppinglist.http.dto.ShoppingListDetailCreateDTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import javax.persistence.*;

@Table(name = "shopping_list")
@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "shopping_list_gen", sequenceName = "shopping_list_id_seq", allocationSize = 1)
public class ShoppingList
{
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "shopping_list_gen"
    )
    private Integer id;
    
    @Column(name="date",columnDefinition = "TIMESTAMP")
    private LocalDateTime date;
    
    @Column
    private String title;

    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL)
    private List<ShoppingListDetail> details;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    protected Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    protected Timestamp updatedAt;

    @Column(name = "deleted_at")
    protected Timestamp deletedAt;

    public ShoppingList(
            ShoppingListCreateDTO json
    ) {
        date = json.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        title = json.getTitle();
    }

    public void applyUpdate(ShoppingListCreateDTO json) {
        date = json.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        title = json.getTitle();
    }

    public void applyDelete() {
        deletedAt = new Timestamp(System.currentTimeMillis());
    }

}
