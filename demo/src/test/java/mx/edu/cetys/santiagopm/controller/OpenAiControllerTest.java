package mx.edu.cetys.santiagopm.controller;

import mx.edu.cetys.santiagopm.service.OpenAiService;
import mx.edu.cetys.santiagopm.service.model.ChatInteraction;
import mx.edu.cetys.santiagopm.service.controller.OpenAiController;
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
    void testSendPromptWithVehicleInfo_ReturnsChatInteraction() throws Exception {
        ChatInteraction mockInteraction = new ChatInteraction();
        mockInteraction.setPrompt("Dime más sobre este auto");
        mockInteraction.setResponse("Este es un Buick Enclave 2008, tipo SUV.");

        when(openAiService.sendPromptWithVehicleInfo(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockInteraction);

        mockMvc.perform(post("/api/chat/prompt-vehicle")
                        .param("prompt", "Dime más sobre este auto")
                        .param("make", "Buick")
                        .param("model", "Enclave")
                        .param("year", "2008")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prompt").value("Dime más sobre este auto"))
                .andExpect(jsonPath("$.response").value("Este es un Buick Enclave 2008, tipo SUV."));
    }

    @Test
    void testSendPromptWithVehicleInfo_ThrowsException() throws Exception {
        when(openAiService.sendPromptWithVehicleInfo(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Error en el servicio"));

        mockMvc.perform(post("/api/chat/prompt-vehicle")
                        .param("prompt", "Dime más sobre este auto")
                        .param("make", "Buick")
                        .param("model", "Enclave")
                        .param("year", "2008")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testSendPromptWithVehicleInfo_EmptyPrompt() throws Exception {
        mockMvc.perform(post("/api/chat/prompt-vehicle")
                        .param("prompt", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSendPromptWithVehicleInfo_NullPrompt() throws Exception {
        mockMvc.perform(post("/api/chat/prompt-vehicle")
                        .param("prompt", (String) null)
                        .param("make", "Buick")
                        .param("model", "Enclave")
                        .param("year", "2008")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
