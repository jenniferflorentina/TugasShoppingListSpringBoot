package shoppinglist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shoppinglist.entity.ShoppingList;
import shoppinglist.entity.ShoppingListDetail;
import shoppinglist.http.dto.ShoppingListCreateDTO;
import shoppinglist.http.dto.ShoppingListDetailCreateDTO;
import shoppinglist.http.dto.ShoppingListResponseDTO;
import shoppinglist.repository.ShoppingListDetailRepo;
import shoppinglist.repository.ShoppingListRepo;
import shoppinglist.service.ShoppingListService;

import javax.xml.bind.ValidationException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTests {

	@InjectMocks
	private ShoppingListService shoppingListService;

	@Mock
	private ShoppingListRepo shoppingListRepo;
	@Mock
	private ShoppingListDetailRepo shoppingListDetailRepo;

	ShoppingListCreateDTO shoppingListCreateDTO ;
    ShoppingListCreateDTO shoppingListUpdateDTO ;

	List<ShoppingListDetailCreateDTO> detailCreateDTOList = new ArrayList<>();
	List<ShoppingListDetailCreateDTO> detailUpdateDTOList = new ArrayList<>();

	ShoppingList entity1;
	List<ShoppingListDetail> detailList = new ArrayList<>();
	List<ShoppingListDetail> updateList = new ArrayList<>();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeEach
	public void initUseCase() throws Exception {
		Instant instant = LocalDateTime.now().toInstant(ZoneOffset.UTC);
		detailCreateDTOList.add(new ShoppingListDetailCreateDTO(null, "Sugar", 10F,"kg","pack"));
		detailCreateDTOList.add(new ShoppingListDetailCreateDTO(null, "Flour", 10F,"kg","pack"));
		detailUpdateDTOList.add(new ShoppingListDetailCreateDTO(1, "Sugar", 10F,"kg","pack"));
		detailUpdateDTOList.add(new ShoppingListDetailCreateDTO(2, "Flour", 10F,"kg","pack"));
		detailUpdateDTOList.add(new ShoppingListDetailCreateDTO(null, "Oil", 10F,"kg","pack"));

		shoppingListCreateDTO = new ShoppingListCreateDTO("June 2021", Date.from(instant), detailCreateDTOList);
        shoppingListUpdateDTO = new ShoppingListCreateDTO("June 2021", Date.from(instant), detailUpdateDTOList);

		entity1 =  new ShoppingList(shoppingListCreateDTO);
		for (int i = 0; i < detailCreateDTOList.size(); i++) {
			detailList.add(new ShoppingListDetail(i+1,detailCreateDTOList.get(i), entity1));
		}

		for (int i = 0; i < detailUpdateDTOList.size(); i++) {
			updateList.add(new ShoppingListDetail(i+1,detailUpdateDTOList.get(i), entity1));
		}

	}

	@Test
	void whenGetAllShoppingList_itShouldReturnListShoppingList() {
		when(shoppingListRepo.findAllDeletedAtIsNull()).thenReturn(List.of(entity1));

		ShoppingList expected = entity1;
		expected.setDetails(detailList);

		List<ShoppingList> result =  shoppingListService.getAllData(null);

		assertThat(expected.getDate()).isEqualTo(result.get(0).getDate());
		assertThat(expected.getTitle()).isEqualTo(result.get(0).getTitle());
		assertNotNull(expected.getDetails());
	}

	@Test
	void whenFindShoppingListByTitle_itShouldGetAllShoppingListWithTitleParams() {
		when(shoppingListRepo.findByTitleLikeIgnoreCase(any(String.class))).thenReturn(List.of(entity1));

		ShoppingList expected = entity1;
		expected.setDetails(detailList);

		List<ShoppingList> result =  shoppingListService.getAllData("Belanja");

		assertThat(expected.getDate()).isEqualTo(result.get(0).getDate());
		assertThat(expected.getTitle()).isEqualTo(result.get(0).getTitle());
		assertNotNull(expected.getDetails());
	}

	@Test
	void whenSaveWithRightDTO_itShouldSaveShoppingList() throws Exception{
		when(shoppingListRepo.save(any(ShoppingList.class))).thenReturn(entity1);
		when(shoppingListDetailRepo.saveAll(any())).thenReturn(detailList);

		entity1.setDetails(detailList);
		ShoppingListResponseDTO expected = new ShoppingListResponseDTO(entity1);

		ShoppingListResponseDTO result =  shoppingListService.create(shoppingListCreateDTO);

		assertThat(expected.getDate()).isEqualTo(result.getDate());
		assertThat(expected.getTitle()).isEqualTo(result.getTitle());
		assertNotNull(expected.getDetails());
		assertThat(expected.getDetails().size()).isEqualTo(result.getDetails().size());
	}

	@Test
	void whenSaveWithWrongDTO_itShouldThrowsError() throws Exception{
		exception.expect(ValidationException.class);
		exception.expectMessage("Quantity must be positive!");

		when(shoppingListRepo.save(any(ShoppingList.class))).thenReturn(entity1);
		when(shoppingListDetailRepo.saveAll(any())).thenReturn(detailList);

		ShoppingListDetailCreateDTO addDto = new ShoppingListDetailCreateDTO(1, "Sugar", -1F,"kg","pack");
		detailList.add(new ShoppingListDetail(4,addDto, entity1));
		entity1.setDetails(detailList);

		ShoppingListResponseDTO result =  shoppingListService.create(shoppingListCreateDTO);
	}

	@Test
	void whenDelete_itShouldDeleteShoppingList(){
		ShoppingList expected = entity1;
		expected.setDetails(detailList);
		for (ShoppingListDetail item : expected.getDetails()) {
			item.applyDelete();
		}
		entity1.applyDelete();

		when(shoppingListRepo.save(any(ShoppingList.class))).thenReturn(entity1);
		when(shoppingListDetailRepo.saveAll(any())).thenReturn(detailList);

		ShoppingList result =  shoppingListService.delete(entity1);

		assertThat(expected.getDate()).isEqualTo(result.getDate());
		assertThat(expected.getTitle()).isEqualTo(result.getTitle());
		assertNotNull(expected.getDetails());
		assertThat(expected.getDetails().size()).isEqualTo(result.getDetails().size());
		assertThat(expected.getDeletedAt()).isEqualTo(result.getDeletedAt());
	}

	@Test
	void whenUpdateWithRightDTO_itShouldUpdateShoppingList() {
        when(shoppingListRepo.save(any(ShoppingList.class))).thenReturn(entity1);
		when(shoppingListDetailRepo.saveAll(any())).thenReturn(updateList);

		entity1.setDetails(updateList);
		ShoppingListResponseDTO expected = new ShoppingListResponseDTO(entity1);

		ShoppingListResponseDTO result =  shoppingListService.update(shoppingListUpdateDTO, entity1);

		assertThat(expected.getDate()).isEqualTo(result.getDate());
		assertThat(expected.getTitle()).isEqualTo(result.getTitle());
		assertNotNull(expected.getDetails());
		assertThat(expected.getDetails().size()).isEqualTo(result.getDetails().size());
	}

}
