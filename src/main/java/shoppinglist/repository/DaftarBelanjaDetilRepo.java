package shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.entity.DaftarBelanjaDetilId;

public interface DaftarBelanjaDetilRepo extends JpaRepository<DaftarBelanjaDetil, DaftarBelanjaDetilId> {

}
