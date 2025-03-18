package tech.vascon.MagicFridgeAI.FoodItem;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    private FoodItemRepository repository;

    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.repository = foodItemRepository;
    }

    public FoodItem save(FoodItem foodItem) {
        return repository.save(foodItem);
    }

    public List<FoodItem> findAll() {
        return repository.findAll();
    }

    public Optional<FoodItem> findById(Long id) {
        return repository.findById(id);
    }

    public FoodItem update(FoodItem foodItem) {
        if(repository.findById(id).isPresent()) {
            foodItem.setId(id);
            return repository.save(foodItem);
        }
        return null;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
