package shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.entity.DaftarBelanja;

import java.util.List;

public interface DaftarBelanjaRepo extends JpaRepository<DaftarBelanja, Long>
{
    List<DaftarBelanja> findByJudulContainingIgnoreCase(String judul);

}
