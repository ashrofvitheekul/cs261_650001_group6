package th.ac.tu.smartparking.dao;

import com.example.demo.DemoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import th.ac.tu.smartparking.entity.ParkingLot;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DAO tests against the real MS SQL in Docker (seed data from database/03-seed.sql).
 * Name ends with "IT" so `mvnw package` skips it. Run it on purpose:
 *   docker compose up -d db db-init
 *   cd backend && ./mvnw test -Dtest=ParkingLotRepositoryIT
 */
@SpringBootTest(classes = DemoApplication.class)
class ParkingLotRepositoryIT {

    @Autowired
    private ParkingLotRepository repository;

    @Test
    void findsAllEightSeedLotsSortedByCode() {
        List<ParkingLot> lots = repository.findAllByOrderByCodeAsc();

        assertEquals(8, lots.size());
        assertEquals("M1", lots.get(0).getCode());
    }

    @Test
    void searchMatchesBuildingName() {
        List<ParkingLot> lots = repository.search("วิทยาศาสตร์");

        assertEquals(List.of("M1", "P1"), lots.stream().map(ParkingLot::getCode).toList());
    }

    @Test
    void searchIgnoresUpperAndLowerCase() {
        List<ParkingLot> lots = repository.search("sc");

        assertEquals("P2", lots.get(0).getCode());
    }

    @Test
    void searchByCode() {
        assertEquals("P4", repository.search("P4").get(0).getCode());
    }

    @Test
    void searchReturnsNothingForUnknownPlace() {
        assertTrue(repository.search("ตึกโดม").isEmpty());
    }

    @Test
    void onlyLotsWithFreeSpotsOfThatVehicleType() {
        List<ParkingLot> cars = repository.findByVehicleTypeAndAvailableSpotsGreaterThan(ParkingLot.CAR, 0);

        assertTrue(cars.stream().allMatch(l -> l.getVehicleType().equals("CAR") && l.getAvailableSpots() > 0));
        assertTrue(cars.stream().noneMatch(l -> l.getCode().equals("P4"))); // P4 is full in the seed
    }
}