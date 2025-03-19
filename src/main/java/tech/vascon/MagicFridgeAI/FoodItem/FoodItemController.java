package tech.vascon.MagicFridgeAI.FoodItem;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/food")
public class FoodItemController {

    private final FoodItemService service;

    public FoodItemController(FoodItemService foodItemService) {
        this.service = foodItemService;
    }

    @PostMapping("/create")
    public ResponseEntity<FoodItem> create(@RequestBody FoodItem foodItem) {
        FoodItem saved = service.save(foodItem);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FoodItem>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/list/{id}")
    public Optional<FoodItem> update(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FoodItem> updateById(@PathVariable Long id, @RequestBody FoodItem foodItem) {
        return service.findById(id)
                .map(existingItem -> {
                    foodItem.setId(id);
                    FoodItem updated = service.update(foodItem);
                    return ResponseEntity.ok().body(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
