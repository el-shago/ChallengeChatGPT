package mx.edu.cetys.santiagopm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(OpenAiController.class)
public class OpenAiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChallengeApplication openAIService;

    @BeforeEach
    void setUp() {
        String prompt = "";
        String expectedResponse = "";
    }

    @Test
    void returnValidResponseFromOpenAI() throws Exception {
        String prompt = "Hola, ¿cómo estás?";
        String expectedResponse = "Estoy bien, gracias.";

        when(openAIService.getCompletion(prompt)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/openai")
                .param("prompt", prompt))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}
