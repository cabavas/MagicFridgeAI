package tech.vascon.MagicFridgeAI.Recipe;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import tech.vascon.MagicFridgeAI.Ai.AiService;
import tech.vascon.MagicFridgeAI.FoodItem.FoodItem;
import tech.vascon.MagicFridgeAI.FoodItem.FoodItemService;

import java.util.List;

@RestController
@RequestMapping("/generate")
public class RecipeController {

    private FoodItemService foodItemService;
    private final AiService aiService;

    public RecipeController(AiService aiService, FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
        this.aiService = aiService;
    }

    @GetMapping
    public Mono<ResponseEntity<String>> generateRecipe() {
        List<FoodItem> foodItems = foodItemService.findAll();
        return aiService.generateRecipe(foodItems)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
