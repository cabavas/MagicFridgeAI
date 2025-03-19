package tech.vascon.MagicFridgeAI.FoodItem;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    private final FoodItemRepository repository;

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
        if(repository.findById(foodItem.getId()).isPresent()) {
            return repository.save(foodItem);
        }
        return null;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
