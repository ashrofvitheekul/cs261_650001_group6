# Release Notes

## v1.0 - Sprint 1 (20 ต.ค. 2569)

### ฟีเจอร์

| User Story | ทำอะไรได้ |
|---|---|
| US02 ดูแผนที่ลานจอด | เห็นลานจอด 8 ลานของศูนย์รังสิตบนแผนที่ OpenStreetMap พร้อมเขตลาน, กดหมุดหรือการ์ดเพื่อซูมไปที่ลาน |
| US04 ค้นหาลานจอด | ค้นจากรหัสลาน ชื่อลาน หรือชื่ออาคาร (ไม่สนตัวพิมพ์เล็ก/ใหญ่), ปุ่มล้างคำค้น |
| US05 หาลานใกล้ที่สุด | เลือกรถยนต์หรือจักรยานยนต์ แล้วหาลานที่ใกล้ตำแหน่งปัจจุบันที่สุด, แสดงระยะทาง, ปุ่มนำทางเปิด Google Maps, ถ้าไม่ให้ตำแหน่ง เลือกนำทางจากรายการได้ |

### วิธีรัน

```bash
git clone <ลิงก์ repo>
cd cs261_650001_group6
git checkout v1.0
docker compose up -d --build
```

เปิด http://localhost:3000 (รายละเอียดและวิธีแก้ปัญหาอยู่ใน README)

### ผลทดสอบ

| ระดับ | ผล | ไฟล์ |
|---|---|---|
| API unit test (Service + Controller) | 14/14 ผ่าน | `docs/test-reports/api-unit-test.md` |
| DAO unit test (MS SQL จริง) | 7/7 ผ่าน | `docs/test-reports/dao-unit-test.md` |
| Integration API ↔ DAO (Postman) | 9/9 ผ่าน | `docs/test-reports/integration-api-dao.md` |
| UI unit test US02 | 7/7 ผ่าน | `docs/test-reports/ui-unit-test-us02.md` |
| UI unit test US04 / US05 | 10/10 ผ่าน | `docs/test-reports/ui-unit-test-us04-us05.md` |
| Integration UI ↔ API | 5/5 ผ่าน | `docs/test-reports/integration-ui-api.md` |
| System test | 8/8 ผ่าน | `docs/test-reports/system-test.md` |
| UAT | 15/15 ผ่าน | `docs/test-reports/uat-sprint1.md` |

### เปลี่ยนจากแผนเดิม

- ตัดเรื่องจำนวนที่ว่างออกจาก Sprint 1 (ยังไม่มีแหล่งข้อมูลจริง), US05 จึงหาลานใกล้สุดตามระยะทางอย่างเดียว
- พิกัดลานใช้กลางเขตลานจาก OpenStreetMap ให้ตรงกับเขตลานใน Figma

### ข้อจำกัดที่รู้แล้ว

- ต้องต่ออินเทอร์เน็ต (แผนที่ OpenStreetMap, Leaflet, ฟอนต์ Noto Sans Thai, Google Maps)
- ระยะทางเป็นเส้นตรง ไม่ใช่ระยะตามถนน
- ยังไม่มีจำนวนที่ว่าง (ย้ายไป Sprint 2)
