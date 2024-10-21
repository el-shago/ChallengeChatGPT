package mx.edu.cetys.santiagopm.service;

import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.json.JSONObject;

@Service
public class CarDataService {

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    @Value("${rapidapi.car.data.url}")
    private String carDataApiUrl;

    private final RestTemplate restTemplate;

    public CarDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCarDetails(String make, String model, String year) {
        // Cambiar la construcción de la URL para utilizar parámetros de consulta
        String url = carDataApiUrl + "/cars?make=" + make + "&model=" + model + "&year=" + year;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", rapidApiKey);
        headers.set("X-RapidAPI-Host", "car-data.p.rapidapi.com");

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody().trim().startsWith("[")) {
                JSONArray jsonResponseArray = new JSONArray(response.getBody());

                JSONObject firstCarObject = jsonResponseArray.getJSONObject(0);

                return firstCarObject.toString();
            } else {
                System.err.println("Error: La API devolvió un código de estado " + response.getStatusCodeValue());
                return "Error: No se pudo obtener datos del vehículo.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al conectarse a la API de Car Data: " + e.getMessage();
        }
    }

}
