# Integration Tests: API ↔ DAO

ทดสอบว่า API (Controller → Service) ดึงข้อมูลจาก DAO และ MS SQL จริงได้ถูกต้อง, ใช้ Postman collection `docs/postman/sprint1.postman_collection.json` ยิงไปที่ `http://localhost:8081` หลัง `docker compose up -d --build` (ข้อมูล seed 8 ลาน)

| รหัส | ทดสอบ | request | ต้องได้ |
|---|---|---|---|
| IT-DAO-1 | ลานทั้งหมด (US02) | `GET /api/parking-lots` | 200, 8 ลาน เรียงตามรหัส, ไม่มีจำนวนที่ว่าง |
| IT-DAO-2 | ค้นหา (US04) | `GET /api/parking-lots?q=หอสมุด` | 200, ได้ P3 |
| IT-DAO-3 | ค้นไม่พบ (US04) | `GET /api/parking-lots?q=ตึกโดม` | 200, `[]` |
| IT-DAO-4 | ใกล้สุด รถยนต์ (US05) | `GET /api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=CAR` | 200, P2, 145–155 ม. |
| IT-DAO-5 | ใกล้สุด จักรยานยนต์ (US05) | `GET /api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=MOTORCYCLE` | 200, M2, 405–420 ม. |
| IT-DAO-6 | ประเภทรถผิด (US05) | `GET /api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=BUS` | 400, "ประเภทรถต้องเป็น CAR หรือ MOTORCYCLE" |
| IT-DAO-7 | ไม่มีข้อมูล (US05) | รัน `database/test-data/tc02-empty.sql` แล้วเรียก request IT-DAO-4 | 404, "ยังไม่มีข้อมูลลานจอด" |
| IT-DAO-8 | ลานเดียว | `GET /api/parking-lots/3` | 200, P3 |
| IT-DAO-9 | id ไม่มีจริง | `GET /api/parking-lots/99` | 404, "ไม่พบลานจอด" |

คืนข้อมูลหลัง IT-DAO-7: `docker compose down -v` แล้ว `docker compose up -d --build`
