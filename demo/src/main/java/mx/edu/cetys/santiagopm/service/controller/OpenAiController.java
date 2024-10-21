package mx.edu.cetys.santiagopm.service.controller;

import mx.edu.cetys.santiagopm.service.OpenAiService;
import mx.edu.cetys.santiagopm.service.model.ChatInteraction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/chat")
public class OpenAiController {
    private final OpenAiService openAiService;

    public OpenAiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/prompt-vehicle")
    public ResponseEntity<?> sendPromptWithVehicleInfo(
            @RequestParam String prompt,
            @RequestParam String make,
            @RequestParam String model,
            @RequestParam String year) {
        try {
            if (prompt == null || prompt.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El prompt no puede estar vacío.");
            }

            ChatInteraction interaction = openAiService.sendPromptWithVehicleInfo(prompt, make, model, year);
            return ResponseEntity.status(HttpStatus.OK).body(interaction);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }
}
