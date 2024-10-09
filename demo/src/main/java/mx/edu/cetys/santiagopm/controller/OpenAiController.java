package mx.edu.cetys.santiagopm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

class OpenAi {
    String prompt;
    String expectedResponse;

    public OpenAi(String prompt, String expectedResponse) {
        this.prompt = prompt;
        this.expectedResponse = expectedResponse;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getExpectedResponse() {
        return expectedResponse;
    }

    public void setExpectedResponse(String expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    @RestController
    @ResponseBody
    @RequestMapping("/api/openai")
    public void OpenAiController{}

}