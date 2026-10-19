# DAO Unit Test Spec — Sprint 1

**ผู้เขียน:** พิชญาภา · **ทดสอบ:** `ParkingLotRepository` · **ไฟล์ test:** `backend/src/test/java/th/ac/tu/smartparking/dao/ParkingLotRepositoryIT.java` (แทนไท)
**ข้อมูล:** seed 8 ลานจาก `database/03-seed.sql` (P1–P6 = CAR · M1–M2 = MOTORCYCLE)
**User Story:** US02 (DAO-1) · US04 (DAO-2 ถึง 5) · US05 (DAO-6)

| รหัส | US | ทดสอบ | method | ข้อมูล | ผลที่ต้องได้ |
|---|---|---|---|---|---|
| DAO-1 | US02 | อ่านครบ เรียงตามรหัส | `findAllByOrderByCodeAsc()` | seed 8 ลาน | ได้ 8 ลาน · ลานแรกคือ M1 |
| DAO-2 | US04 | ค้นจากชื่ออาคาร | `search("วิทยาศาสตร์")` | seed | M1, P1 |
| DAO-3 | US04 | ไม่สนตัวพิมพ์เล็ก/ใหญ่ | `search("sc")` | seed | P2 |
| DAO-4 | US04 | ค้นจากรหัสลาน | `search("P4")` | seed | P4 |
| DAO-5 | US04 | ค้นไม่พบ | `search("ตึกโดม")` | seed | รายการว่าง |
| DAO-6 | US05 | เฉพาะประเภทรถที่เลือก | `findByVehicleTypeOrderByCodeAsc("CAR")` | seed | 6 ลาน P1–P6 · ไม่มี M1, M2 |