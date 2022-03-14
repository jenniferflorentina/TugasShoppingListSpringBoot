package shoppinglist.repository;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shoppinglist.entity.ShoppingList;
import shoppinglist.http.dto.ShoppingListCreateDTO;
import shoppinglist.http.dto.ShoppingListDetailCreateDTO;
import shoppinglist.repository.ShoppingListRepo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestEntityManager
public class ShoppingListRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShoppingListRepo shoppingListRepo;

    ShoppingListCreateDTO shoppingListCreateDTO ;

    List<ShoppingListDetailCreateDTO> detailCreateDTOList = new ArrayList<>();

    ShoppingList entity1;

    @Test
    @Transactional
    public void whenFindByTitle_thenReturnShoppingList() {
        Instant instant = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        detailCreateDTOList.add(new ShoppingListDetailCreateDTO(null, "Sugar", 10F,"kg","pack"));
        detailCreateDTOList.add(new ShoppingListDetailCreateDTO(null, "Flour", 10F,"kg","pack"));
        shoppingListCreateDTO = new ShoppingListCreateDTO("June 2021", Date.from(instant), detailCreateDTOList);
        entity1 =  new ShoppingList(shoppingListCreateDTO);

        List<ShoppingList> expected = new ArrayList<ShoppingList>();
        expected.add(entity1);

        // given
        entityManager.persist(entity1);
        entityManager.flush();

        // when
        List<ShoppingList> result = shoppingListRepo.findByTitleLikeIgnoreCase(entity1.getTitle());

        // then
        assertThat(expected.size()).isEqualTo(result.size());
        assertThat(expected.get(0).getTitle()).isEqualTo(result.get(0).getTitle());
    }
}
