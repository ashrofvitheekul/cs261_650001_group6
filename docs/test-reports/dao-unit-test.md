# ผลทดสอบ DAO Unit Test - ParkingLotRepository

| | |
|---|---|
| ผู้รับผิดชอบ | แทนไท |
| test spec | [docs/test-specs/dao-unit-tests.md](../test-specs/dao-unit-tests.md) |
| วันที่ทดสอบ | อ. 20 ต.ค. 2569 |
| สภาพแวดล้อม | Windows 11, Docker Desktop 29.3, Chrome (Chromium 147), ระบบรันด้วย `docker compose up -d --build` จาก `develop` (`342eae4`) + PR แก้ไขช่วงเช้า (เวลาไทย/README, CORS + ข้อความ 400, ค้นหา % _, aria-label, favicon) |
| วิธีทดสอบ | JUnit กับ MS SQL จริงใน Docker (ข้อมูล seed 8 ลาน), `docker compose up -d` แล้ว `cd backend` → `./mvnw test -Dtest=ParkingLotRepositoryIT` |

| รหัส | ทดสอบ | test method | ผลที่ต้องได้ | ผลจริง | ผล |
|---|---|---|---|---|---|
| DAO-1 | อ่านครบ เรียงตามรหัส | `findsAllEightSeedLotsSortedByCode()` | ได้ 8 ลาน, ลานแรก M1 | ผ่าน (0.088 s) | ผ่าน |
| DAO-2 | ค้นจากชื่ออาคาร "วิทยาศาสตร์" | `searchMatchesBuildingName()` | M1, P1 | ผ่าน (0.007 s) | ผ่าน |
| DAO-3 | ไม่สนตัวพิมพ์ "sc" | `searchIgnoresUpperAndLowerCase()` | P2 | ผ่าน (0.007 s) | ผ่าน |
| DAO-4 | ค้นจากรหัส "P4" | `searchByCode()` | P4 | ผ่าน (0.519 s) | ผ่าน |
| DAO-5 | ค้นไม่พบ "ตึกโดม" | `searchReturnsNothingForUnknownPlace()` | รายการว่าง | ผ่าน (0.008 s) | ผ่าน |
| DAO-6 | เฉพาะ CAR | `onlyLotsOfThatVehicleType()` | P1–P6 | ผ่าน (0.048 s) | ผ่าน |
| DAO-7 | พิมพ์ % _ [ ไม่ใช่ wildcard | `percentAndUnderscoreAreNotWildcards()` | รายการว่าง | ผ่าน (0.012 s) | ผ่าน |

**สรุป: ผ่าน 7/7**

## หลักฐาน (Maven Surefire)

```text
-------------------------------------------------------------------------------
Test set: th.ac.tu.smartparking.dao.ParkingLotRepositoryIT
-------------------------------------------------------------------------------
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 17.12 s -- in th.ac.tu.smartparking.dao.ParkingLotRepositoryIT
```
