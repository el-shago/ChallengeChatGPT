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

    public OpenAiService(OpenAiRepository repository) {
        this.repository = repository;
    }

    private static final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public ChatInteraction sendPrompt(String prompt) throws Exception {
        ChatInteraction interaction = new ChatInteraction();
        interaction.setPrompt(prompt);

        executorService.submit(() -> {
            try {
                String response = getChatGPTResponse(prompt);
                interaction.setResponse(response);
                repository.save(interaction);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }).get();

        return interaction;
    }

    private String getChatGPTResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String body = """
    {
        "model": "gpt-3.5-turbo",
        "messages": [{"role": "user", "content": "%s"}],
        "max_tokens": 1000
    }
    """.formatted(prompt);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONArray choices = jsonResponse.getJSONArray("choices");
            String chatGPTResponse = choices.getJSONObject(0).getJSONObject("message").getString("content");

            System.out.println("ChatGPT API processed response: " + chatGPTResponse);

            return chatGPTResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al obtener respuesta de ChatGPT";
        }
    }
}
