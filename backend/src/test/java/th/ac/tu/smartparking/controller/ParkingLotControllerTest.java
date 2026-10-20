package th.ac.tu.smartparking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import th.ac.tu.smartparking.dto.NearestLotResponse;
import th.ac.tu.smartparking.dto.ParkingLotResponse;
import th.ac.tu.smartparking.service.NotFoundException;
import th.ac.tu.smartparking.service.ParkingLotService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** test controller: status code, ข้อความ error, CORS (mock service) */
class ParkingLotControllerTest {

 private final ParkingLotService service = mock(ParkingLotService.class);
 private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new ParkingLotController(service))
 .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
 .build();

 private static ParkingLotResponse p2() {
 return new ParkingLotResponse(2L, "P2", "ลาน P2", "อาคาร P2", "CAR",
 new BigDecimal("14.069832"), new BigDecimal("100.604829"), null);
 }

 @Test
 void nearestCarReturnsLotAndDistance() throws Exception {
 when(service.findNearest(14.0687, 100.6056, "CAR")).thenReturn(new NearestLotResponse(p2(), 151));

 mvc.perform(get("/api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=CAR"))
 .andExpect(status().isOk())
 .andExpect(jsonPath("$.lot.code").value("P2"))
 .andExpect(jsonPath("$.distanceMeters").value(151));
 }

 @Test
 void wrongVehicleTypeIs400() throws Exception {
 when(service.findNearest(14.0687, 100.6056, "BUS"))
 .thenThrow(new IllegalArgumentException("ประเภทรถต้องเป็น CAR หรือ MOTORCYCLE"));

 mvc.perform(get("/api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=BUS"))
 .andExpect(status().isBadRequest())
 .andExpect(jsonPath("$.message").value("ประเภทรถต้องเป็น CAR หรือ MOTORCYCLE"));
 }

 @Test
 void missingLatitudeIs400InThai() throws Exception {
 mvc.perform(get("/api/parking-lots/nearest?lng=100.6056"))
 .andExpect(status().isBadRequest())
 .andExpect(jsonPath("$.message").value("ต้องระบุ lat"));
 }

 @Test
 void latitudeNotNumberIs400InThai() throws Exception {
 mvc.perform(get("/api/parking-lots/nearest?lat=abc&lng=100.6056"))
 .andExpect(status().isBadRequest())
 .andExpect(jsonPath("$.message").value("ค่า lat ไม่ถูกต้อง"));
 }

 @Test
 void unknownLotIs404() throws Exception {
 when(service.findById(99L)).thenThrow(new NotFoundException("ไม่พบลานจอด"));

 mvc.perform(get("/api/parking-lots/99"))
 .andExpect(status().isNotFound())
 .andExpect(jsonPath("$.message").value("ไม่พบลานจอด"));
 }

 @Test
 void allowsPageOpenedFrom127001() throws Exception {
 mvc.perform(get("/api/parking-lots").header("Origin", "http://127.0.0.1:3000"))
 .andExpect(status().isOk())
 .andExpect(header().string("Access-Control-Allow-Origin", "http://127.0.0.1:3000"));
 }
}
