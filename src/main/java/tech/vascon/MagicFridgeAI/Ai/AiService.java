package tech.vascon.MagicFridgeAI.Ai;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.vascon.MagicFridgeAI.FoodItem.FoodItem;

@Service
public class AiService {

    private final WebClient webClient;
    private String apiKey = System.getenv("API_KEY");
    private static final Logger logger = LoggerFactory.getLogger(AiService.class);

    public AiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> generateRecipe(List<FoodItem> foodItems) {
        String ingredients = foodItems.stream()
                .map(item -> String.format("%s (%s) - Quantidade: %d", item.getName(), item.getCategory(), item.getQuantity()))
                .collect(Collectors.joining("\n"));

        String prompt = "Você é um chef de cozinha e vai me sugerir uma receita (não é necessário utilizar todos os ingredientes, mas deve ser usados somente os ingredientes que estão na lista, com base nos seguintes itens: " + ingredients;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        return webClient.post()
                .uri("?key=" + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    logger.info("Resposta da API do Gemini: {}", response);

                    Object candidatesObj = response.get("candidates");
                    if (candidatesObj instanceof List) {
                        List<Map<String, Object>> candidates = (List<Map<String, Object>>) candidatesObj;
                        if (!candidates.isEmpty()) { // Verificação para evitar IndexOutOfBoundsException
                            return extractRecipeFromMap(candidates.get(0)); // Pega o primeiro elemento da lista
                        }
                    }
                    return "Não foi possível gerar uma receita.";
                })
                .onErrorResume(e -> Mono.just("Ocorreu um erro: " + e.getMessage()));
    }

    private String extractRecipeFromMap(Map<String, Object> candidatesMap) {
        if (candidatesMap.containsKey("content") && candidatesMap.get("content") instanceof Map) {
            Map<String, Object> contentMap = (Map<String, Object>) candidatesMap.get("content");
            if (contentMap.containsKey("parts") && contentMap.get("parts") instanceof List) {
                List<Map<String, Object>> partsList = (List<Map<String, Object>>) contentMap.get("parts");
                if (!partsList.isEmpty() && partsList.get(0).containsKey("text")) {
                    return (String) partsList.get(0).get("text");
                } else {
                    logger.warn("partsList está vazia ou não contém 'text'");
                }
            } else {
                logger.warn("contentMap não contém 'parts' ou 'parts' não é uma lista");
            }
        } else {
            logger.warn("candidatesMap não contém 'content' ou 'content' não é um mapa");
        }
        return "Não foi possível gerar uma receita.";
    }
}