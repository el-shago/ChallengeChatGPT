package mx.edu.cetys.santiagopm.service;

import mx.edu.cetys.santiagopm.service.model.ChatInteraction;
import mx.edu.cetys.santiagopm.service.repository.OpenAiRepository;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class OpenAiService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final OpenAiRepository repository;
    private final CarDataService carDataService;

    public OpenAiService(OpenAiRepository repository, CarDataService carDataService) {
        this.repository = repository;
        this.carDataService = carDataService;
    }

    private static final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public ChatInteraction sendPromptWithVehicleInfo(String prompt, String make, String model, String year) throws Exception {
        ChatInteraction interaction = new ChatInteraction();
        interaction.setPrompt(prompt);

        String carData = carDataService.getCarDetails(make, model, year);

        String combinedPrompt = prompt + "\nInformacion del auto:\n" + carData;
        String response = getChatGPTResponse(combinedPrompt);

        interaction.setResponse(response);
        repository.save(interaction);

        return interaction;
    }

    String getChatGPTResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "gpt-3.5-turbo");
        jsonBody.put("messages", new JSONArray().put(new JSONObject().put("role", "user").put("content", prompt)));
        jsonBody.put("max_tokens", 1000);

        System.out.println("Request Body: " + jsonBody.toString());

        HttpEntity<String> request = new HttpEntity<>(jsonBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            } else {
                System.err.println("OpenAI API responded with status code: " + response.getStatusCodeValue());
                return "Error: La API de OpenAI devolvió un código de error " + response.getStatusCodeValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al obtener respuesta de ChatGPT: " + e.getMessage();
        }
    }
}

