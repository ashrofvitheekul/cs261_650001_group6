# ผลทดสอบ Integration Test - UI ↔ API

| | |
|---|---|
| ผู้รับผิดชอบ | สุจิรา |
| test spec | [docs/test-specs/integration-ui-api.md](../test-specs/integration-ui-api.md) |
| วันที่ทดสอบ | อ. 20 ต.ค. 2569 |
| สภาพแวดล้อม | Windows 11, Docker Desktop 29.3, Chrome (Chromium 147), ระบบรันด้วย `docker compose up -d --build` จาก `develop` (`342eae4`) + PR แก้ไขช่วงเช้า (เวลาไทย/README, CORS + ข้อความ 400, ค้นหา % _, aria-label, favicon) |
| วิธีทดสอบ | เปิด F12 → Network ดู request ที่หน้าเว็บส่งไป API (พอร์ต 8081) และ status ที่ได้กลับมา |

| รหัส | ทดสอบ | วิธีทำ | ผลที่ต้องได้ | ผลจริง | ผล | หลักฐาน |
|---|---|---|---|---|---|---|
| IT-UI-1 | โหลดลาน | เปิดหน้าแรก | `GET /api/parking-lots` 200, ได้ 8 ลาน | `GET http://localhost:8081/api/parking-lots -> 200`, หมุด 8, การ์ด 8, เขตลาน 8, "ลานจอดทั้งหมด 8 ลาน", M1, M2, P1, P2, P3, P4, P5, P6 | ผ่าน | [IT-UI-1.jpg](img/IT-UI-1.jpg) |
| IT-UI-2 | ค้นหา | พิมพ์ "หอสมุด" | `GET /api/parking-lots?q=...` 200, ได้ P3 | `GET http://localhost:8081/api/parking-lots?q=%E0%B8%AB%E0%B8%AD%E0%B8%AA%E0%B8%A1%E0%B8%B8%E0%B8%94 -> 200`, พิมพ์ "หอสมุด" → เหลือ P3, หมุด 1, "ผลการค้นหา 1 ลาน" | ผ่าน | [IT-UI-2.jpg](img/IT-UI-2.jpg) |
| IT-UI-3 | ใกล้สุด | ตำแหน่ง 14.0687, 100.6056 → รถยนต์ → หาลาน | `GET /api/parking-lots/nearest?...&type=CAR` 200, ได้ P2 | `GET http://localhost:8081/api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=CAR -> 200`, การ์ดผล: P2 / ลานจอดรถอาคารเรียนรวมสังคมศาสตร์ (SC) / ห่างประมาณ 150 เมตร / นำทาง (เปิด Google Maps), ซูม 18, หมุด P2 แดง, เขต P2 ทึบ 0.55 | ผ่าน | [IT-UI-3.jpg](img/IT-UI-3.jpg) |
| IT-UI-4 | API ล่ม | `docker compose stop backend` → F5 | ขึ้นข้อความแจ้งข้อผิดพลาด ไม่ค้าง | "โหลดข้อมูลลานจอดไม่สำเร็จ กรุณาลองใหม่", หน้าเว็บไม่ค้าง | ผ่าน | [IT-UI-4.jpg](img/IT-UI-4.jpg) |
| IT-UI-5 | CORS | เปิดจาก http://localhost:3000 ดู Console | ไม่มี CORS error | Console error: 0 รายการ [], เปิดจาก http://127.0.0.1:3000 → การ์ด 8, Console error 0 | ผ่าน | [IT-UI-5.jpg](img/IT-UI-5.jpg) |

**สรุป: ผ่าน 5/5**

> IT-UI-5 ทดสอบเพิ่มการเปิดจาก http://127.0.0.1:3000 ด้วย (ก่อนแก้ CORS เปิดแบบนี้แล้วโหลดลานไม่ได้)
