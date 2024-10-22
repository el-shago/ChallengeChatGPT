package mx.edu.cetys.santiagopm.service;

import mx.edu.cetys.santiagopm.service.repository.OpenAiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application.properties")
class OpenAiServiceTest {

    @Mock
    private OpenAiRepository repository;

    @Mock
    private CarDataService carDataService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenAiService openAiService;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetChatGPTResponseErrorHandling() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("API Error"));

        String response = openAiService.getChatGPTResponse("Test prompt");

        assertTrue(response.contains("Error al obtener respuesta de ChatGPT:"));
    }
}
