package mx.edu.cetys.santiagopm.controller;

import mx.edu.cetys.santiagopm.service.controller.OpenAiController;
import mx.edu.cetys.santiagopm.service.model.ChatInteraction;
import mx.edu.cetys.santiagopm.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class OpenAiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OpenAiService openAiService;

    @InjectMocks
    private OpenAiController openAiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(openAiController).build();
    }

    @Test
    void testSendPrompt_ReturnsChatInteraction() throws Exception {
        ChatInteraction mockInteraction = new ChatInteraction();
        mockInteraction.setPrompt("Hola, ¿cómo estás?");
        mockInteraction.setResponse("Estoy bien, ¿y tú?");

        when(openAiService.sendPrompt(anyString())).thenReturn(mockInteraction);

        mockMvc.perform(post("/api/chat/prompt")
                        .param("prompt", "Hola, ¿cómo estás?")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prompt").value("Hola, ¿cómo estás?"))
                .andExpect(jsonPath("$.response").value("Estoy bien, ¿y tú?"));
    }

    @Test
    void testSendPrompt_ThrowsException() throws Exception {
        when(openAiService.sendPrompt(anyString())).thenThrow(new RuntimeException("Error en el servicio"));

        mockMvc.perform(post("/api/chat/prompt")
                        .param("prompt", "Hola, ¿cómo estás?")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testSendPrompt_EmptyPrompt() throws Exception {
        mockMvc.perform(post("/api/chat/prompt")
                        .param("prompt", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSendPrompt_NullPrompt() throws Exception {
        mockMvc.perform(post("/api/chat/prompt")
                        .param("prompt", (String) null)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
