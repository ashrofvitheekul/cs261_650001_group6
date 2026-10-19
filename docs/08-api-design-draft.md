# API Design — Sprint 1

Base URL `http://localhost:8081` · JSON · ชั้นของโค้ด: Controller → Service → Repository → Entity → MS SQL

| Method | Path | Story | ตอบ |
|---|---|---|---|
| GET | `/api/health` | — | 200 `{"api":"UP","database":"UP"}` · DB ล่ม `"database":"DOWN"` (มีอยู่แล้ว) |
| GET | `/api/parking-lots` | US02 | 200 ลานทั้งหมด เรียงตามรหัส |
| GET | `/api/parking-lots?q={คำค้น}` | US04 | 200 เฉพาะลานที่รหัส / ชื่อลาน / ชื่ออาคารมีคำค้น (ไม่สนตัวพิมพ์) · ไม่พบ `[]` |
| GET | `/api/parking-lots/{id}` | — | 200 ลาน 1 ลาน · ไม่พบ 404 `{"message":"ไม่พบลานจอด"}` |
| GET | `/api/parking-lots/nearest?lat=&lng=&type=CAR\|MOTORCYCLE` | US05 | 200 `{lot, distanceMeters}` · ไม่มีลานประเภทนั้นเลย 404 `{"message":"ยังไม่มีข้อมูลลานจอด"}` · type ผิด 400 |

## ตัวอย่าง `GET /api/parking-lots` (1 ลาน)

```json
{
 "id": 1,
 "code": "P1",
 "nameTh": "ลานจอดรถคณะวิทยาศาสตร์และเทคโนโลยี",
 "building": "คณะวิทยาศาสตร์และเทคโนโลยี",
 "vehicleType": "CAR",
 "latitude": 14.071450,
 "longitude": 100.604200
}