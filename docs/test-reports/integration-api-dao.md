# ผลทดสอบ Integration Test - API ↔ DAO

| | |
|---|---|
| ผู้รับผิดชอบ | แทนไท |
| test spec | [docs/test-specs/integration-api-dao.md](../test-specs/integration-api-dao.md) |
| วันที่ทดสอบ | อ. 20 ต.ค. 2569 |
| สภาพแวดล้อม | Windows 11, Docker Desktop 29.3, Chrome (Chromium 147), ระบบรันด้วย `docker compose up -d --build` จาก `develop` (`342eae4`) + PR แก้ไขช่วงเช้า (เวลาไทย/README, CORS + ข้อความ 400, ค้นหา % _, aria-label, favicon) |
| วิธีทดสอบ | Postman collection `docs/postman/sprint1.postman_collection.json` (10 request) ยิงไปที่ API จริงที่ต่อ MS SQL จริง |

| รหัส | ทดสอบ | request | ผลที่ต้องได้ | ผลจริง | ผล |
|---|---|---|---|---|---|
| IT-DAO-1 | ลานทั้งหมด | `GET /api/parking-lots` | 200, 8 ลาน, ไม่มีจำนวนที่ว่าง | HTTP 200, ✓ 200, ✓ 8 ลาน เรียงตามรหัส, ✓ ไม่ส่งจำนวนที่ว่าง | ผ่าน |
| IT-DAO-2 | ค้นหา | `GET /api/parking-lots?q=หอสมุด` | 200, P3 | HTTP 200, ✓ 200, ✓ มีแค่ P3 | ผ่าน |
| IT-DAO-3 | ค้นไม่พบ | `GET /api/parking-lots?q=ตึกโดม` | 200, `[]` | HTTP 200, ✓ ไม่พบ = [] | ผ่าน |
| IT-DAO-4 | ใกล้สุด รถยนต์ | `GET /api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=CAR` | 200, P2, 145–155 ม. | HTTP 200, ✓ 200, ✓ P2 ห่างประมาณ 151 ม. | ผ่าน |
| IT-DAO-5 | ใกล้สุด จักรยานยนต์ | `GET /api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=MOTORCYCLE` | 200, M2, 405–420 ม. | HTTP 200, ✓ M2 ห่างประมาณ 413 ม. | ผ่าน |
| IT-DAO-6 | ประเภทรถผิด | `GET /api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=BUS` | 400, "ประเภทรถต้องเป็น CAR หรือ MOTORCYCLE" | HTTP 400, ✓ 400, ✓ ข้อความ | ผ่าน |
| IT-DAO-7 | ไม่มีข้อมูล | รัน `tc02-empty.sql` แล้ว `GET /api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=CAR` | 404, "ยังไม่มีข้อมูลลานจอด" | HTTP 404, {"message":"ยังไม่มีข้อมูลลานจอด"} | ผ่าน |
| IT-DAO-8 | ลานเดียว / ไม่มีจริง | `GET /api/parking-lots/3` | 200, P3 | HTTP 200, ✓ 200 และเป็น P3 | ผ่าน |
| IT-DAO-9 | id ไม่มีจริง | `GET /api/parking-lots/99` | 404, "ไม่พบลานจอด" | HTTP 404, ✓ 404, ✓ ข้อความ | ผ่าน |

**สรุป: ผ่าน 9/9**

## หลักฐาน - Postman collection (10/10 request ผ่าน)

```text
PASS  200   286 ms  1 ลานทั้งหมด (US02)
PASS  200    42 ms  2 ค้นหา หอสมุด (US04)
PASS  200    11 ms  3 ค้นหา วิทยาศาสตร์ (US04)
PASS  200    11 ms  4 ค้นหา sc ตัวพิมพ์เล็ก (US04)
PASS  200     9 ms  5 ค้นหา ตึกโดม (US04)
PASS  200    33 ms  6 ลาน id 3
PASS  404    14 ms  7 ลาน id 99 ไม่มีจริง
PASS  200    30 ms  8 ใกล้สุด รถยนต์ (US05)
PASS  200    10 ms  9 ใกล้สุด จักรยานยนต์ (US05)
PASS  400     9 ms  10 ใกล้สุด ประเภทรถผิด (US05)
```

## หลักฐาน - ไม่มีข้อมูล (IT-DAO-7)

```text
[07:01:59] $ docker exec -i sp-db sqlcmd ... < database/test-data/tc02-empty.sql

(8 rows affected)
[07:01:59] GET /api/parking-lots
[]  -> HTTP 200
[07:01:59] GET /api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=CAR
{"message":"ยังไม่มีข้อมูลลานจอด"}  -> HTTP 404
```

## ทดสอบเพิ่ม - ค่าพารามิเตอร์ผิด และค้นหาด้วย % _

```text
GET /api/health
{"api":"UP","database":"UP"}
-> HTTP 200
GET /api/parking-lots?q=%25
[]
-> HTTP 200
GET /api/parking-lots?q=_
[]
-> HTTP 200
GET /api/parking-lots/nearest?lng=100.6056
{"message":"ต้องระบุ lat"}
-> HTTP 400
GET /api/parking-lots/nearest?lat=abc&lng=100.6056
{"message":"ค่า lat ไม่ถูกต้อง"}
-> HTTP 400
GET /api/parking-lots  (Origin: http://127.0.0.1:3000)
HTTP/1.1 200 
Access-Control-Allow-Origin: http://127.0.0.1:3000
```
