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

    @PostMapping("/prompt")
    public ResponseEntity<ChatInteraction> sendPrompt(@RequestParam("prompt") String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prompt cannot be empty or null");
        }

        try {
            ChatInteraction interaction = openAiService.sendPrompt(prompt);
            return ResponseEntity.ok(interaction);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request", e);
        }
    }
}
