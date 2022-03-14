package shoppinglist.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import shoppinglist.entity.ShoppingList;
import shoppinglist.entity.ShoppingListDetail;
import shoppinglist.http.dto.ShoppingListCreateDTO;
import shoppinglist.http.dto.ShoppingListDetailCreateDTO;
import shoppinglist.http.dto.ShoppingListResponseDTO;
import shoppinglist.repository.ShoppingListDetailRepo;
import shoppinglist.repository.ShoppingListRepo;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ShoppingListService {

    private final ShoppingListRepo shoppingListRepo;
    private final ShoppingListDetailRepo shoppingListDetailRepo;

    public List<ShoppingList> getAllData(String title){
        if (title == null){
            return shoppingListRepo.findAllDeletedAtIsNull();
        }else{
            return shoppingListRepo.findByTitleLikeIgnoreCase(title);
        }
    }

    @Transactional
    public ShoppingListResponseDTO create(ShoppingListCreateDTO json){
        try{
            ShoppingList entity = new ShoppingList(json);

            entity = shoppingListRepo.save(entity);


            List<ShoppingListDetailCreateDTO> details = json.getDetails();
            List<ShoppingListDetail> detailEntities = new ArrayList();

            for (int i = 0; i < details.size() ; i++) {
                detailEntities.add(new ShoppingListDetail(i+1, details.get(i),entity));
            }

            detailEntities = shoppingListDetailRepo.saveAll(detailEntities);

            entity.setDetails(detailEntities);
            return new ShoppingListResponseDTO(entity);
        }catch (Exception e){
            return null;
        }
    }

    public ShoppingListResponseDTO update(ShoppingListCreateDTO json, ShoppingList toUpdate){
        toUpdate.applyUpdate(json);
        List<Integer> ids = new ArrayList<>();
        List<ShoppingListDetail> details = new ArrayList<>();
        ids.add(0);
        for (int i = 0; i < json.getDetails().size() ; i++) {
            ShoppingListDetailCreateDTO shoppingListDetailCreateDTO = json.getDetails().get(i);
            ShoppingListDetail shoppingListDetail = new ShoppingListDetail(i+1, shoppingListDetailCreateDTO, toUpdate);
            if (shoppingListDetailCreateDTO.getId() != null) {
                ShoppingListDetail existingDetail= shoppingListDetailRepo.findById(shoppingListDetailCreateDTO.getId()).orElse(null);

                if (existingDetail != null && existingDetail.getShoppingList().getId() == toUpdate.getId()) {
                    ids.add(shoppingListDetailCreateDTO.getId());
                    existingDetail.applyUpdate(i+1, shoppingListDetailCreateDTO);
                    shoppingListDetail = existingDetail;
                }
            }
            details.add(shoppingListDetail);
        }
        shoppingListDetailRepo.deleteShoppingListDetailsByIdNotInAndShoppingList(ids, toUpdate);
        shoppingListDetailRepo.saveAll(details);
        toUpdate.setDetails(details);
        toUpdate = shoppingListRepo.save(toUpdate);
        return new ShoppingListResponseDTO(toUpdate);
    }

    public ShoppingList delete(ShoppingList toDelete){

        List<ShoppingListDetail> details = toDelete.getDetails();
        for (ShoppingListDetail item : details) {
            item.applyDelete();
        }
        shoppingListDetailRepo.saveAll(details);

        toDelete.applyDelete();
        return shoppingListRepo.save(toDelete);
    }
}
