package mx.edu.cetys.santiagopm.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarDataServiceTest {

    @Autowired
    private CarDataService carDataService;

    @Test
    void testGetCarDetailsSuccess() {
        String carDetails = carDataService.getCarDetails("Toyota", "Corolla", "2020");

        assertNotNull(carDetails);
        assertTrue(carDetails.contains("Toyota"));
    }
}
