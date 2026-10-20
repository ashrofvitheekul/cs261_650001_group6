# ผลทดสอบ System Test - Sprint 1

| | |
|---|---|
| ผู้รับผิดชอบ | วชิรวิทย์ |
| test spec | [docs/test-specs/system-tests.md](../test-specs/system-tests.md) |
| วันที่ทดสอบ | อ. 20 ต.ค. 2569 |
| สภาพแวดล้อม | Windows 11, Docker Desktop 29.3, Chrome (Chromium 147), ระบบรันด้วย `docker compose up -d --build` จาก `develop` (`342eae4`) + PR แก้ไขช่วงเช้า (เวลาไทย/README, CORS + ข้อความ 400, ค้นหา % _, aria-label, favicon) |
| วิธีทดสอบ | clone repo ลงโฟลเดอร์ใหม่ แล้วเดินตาม sequence diagram ของ US02 / US04 / US05 |

| รหัส | ทดสอบ | ผลที่ต้องได้ | ผลจริง | ผล | หลักฐาน |
|---|---|---|---|---|---|
| SYS-1 | ติดตั้งจากศูนย์ | 3 container running ภายใน 10 นาที, `/api/health` = UP | clone ใหม่ + build แบบไม่ใช้ cache, health UP ใน 212 วินาที (3 นาที 32 วินาที) | ผ่าน | [SYS-1.jpg](img/SYS-1.jpg) |
| SYS-2 | Sequence US02 | หมุด 8 จุด ข้อมูลตรงกับตาราง `parking_lot` | หมุด 8, การ์ด 8, เขตลาน 8, "ลานจอดทั้งหมด 8 ลาน", M1, M2, P1, P2, P3, P4, P5, P6, ตรงกับ SELECT ในฐานข้อมูล (ดู database-check.md) | ผ่าน | [SYS-2.jpg](img/SYS-2.jpg) |
| SYS-3 | Sequence US04 | ค้นหาได้ P3 → ล้างแล้วกลับเป็น 8 ลาน | พิมพ์ "หอสมุด" → เหลือ P3, หมุด 1, "ผลการค้นหา 1 ลาน" → ช่องค้นหาว่าง, การ์ด 8, "ลานจอดทั้งหมด 8 ลาน" | ผ่าน | [SYS-3a.jpg](img/SYS-3a.jpg) [SYS-3b.jpg](img/SYS-3b.jpg) |
| SYS-4 | Sequence US05 | P2 ห่างประมาณ 150 ม. → เปิด Google Maps | การ์ดผล: P2 / ลานจอดรถอาคารเรียนรวมสังคมศาสตร์ (SC) / ห่างประมาณ 150 เมตร / นำทาง (เปิด Google Maps), ซูม 18, หมุด P2 แดง, เขต P2 ทึบ 0.55 → ปุ่ม "นำทาง (เปิด Google Maps)" target=_blank, href=https://www.google.com/maps/dir/?api=1&destination=14.069832,100.604829, แท็บใหม่เปิด https://www.google.com/maps/dir/?api=1&destination=14.069832,100.604829 | ผ่าน | [SYS-4a.jpg](img/SYS-4a.jpg) [SYS-4b.jpg](img/SYS-4b.jpg) |
| SYS-5 | US05 ไม่ให้ตำแหน่ง | ข้อความให้เลือกลานจากรายการ | "ไม่สามารถใช้ตำแหน่งของคุณได้ เลือกลานจากรายการเพื่อนำทาง", ปุ่ม "นำทาง" ที่เห็นในรายการ 8/8 | ผ่าน | [SYS-5.jpg](img/SYS-5.jpg) |
| SYS-6 | US02 ไม่มีข้อมูล | "ยังไม่มีข้อมูลลานจอด" → กลับมา 8 ลาน | "ยังไม่มีข้อมูลลานจอด", การ์ด 0, หมุด 0 → `down -v` + `up` → การ์ด 8, หมุด 8, "ลานจอดทั้งหมด 8 ลาน" | ผ่าน | [SYS-6a.jpg](img/SYS-6a.jpg) [SYS-6b.jpg](img/SYS-6b.jpg) |
| SYS-7 | ข้อมูลอยู่ต่อหลังปิด | ข้อมูลยังอยู่ครบ | `docker compose down` → `up -d` → การ์ด 8, หมุด 8, "ลานจอดทั้งหมด 8 ลาน" | ผ่าน | [SYS-7.jpg](img/SYS-7.jpg) |
| SYS-8 | มือถือ 360 px | ใช้งาน SYS-2 ถึง SYS-4 ได้ครบ | จอ 360 px, การ์ด 8, ความกว้างหน้า 360px (ไม่ล้นจอ), ค้นหา "หอสมุด" → ['P3'], หาลานใกล้สุด → P2 / ลานจอดรถอาคารเรียนรวมสังคมศาสตร์ (SC) / ห่างประมาณ 150 เมตร / นำทาง (เปิด Google Maps), กว้าง 360px | ผ่าน | [SYS-8a.jpg](img/SYS-8a.jpg) [SYS-8b.jpg](img/SYS-8b.jpg) [SYS-8c.jpg](img/SYS-8c.jpg) |

**สรุป: ผ่าน 8/8**

## หลักฐาน SYS-1

```text
[07:03:34] git clone (ใหม่) → docker compose build --no-cache → docker compose up -d
build exit=0 (196 s)
 Container sp-frontend Started 
[07:07:06] /api/health = {"api":"UP","database":"UP"}, รวม 212 วินาที
sp-backend Up 8 seconds (healthy)
sp-db Up 15 seconds (healthy)
sp-db-init Exited (0) 9 seconds ago
sp-frontend Up 1 second
```

## เวลาในทุก container เป็นเวลาไทย

```text
$ docker compose exec frontend node -e "console.log(new Date().toString())"
Tue Oct 20 2026 07:07:15 GMT+0700 (Indochina Time)
$ docker compose exec backend date
Tue Oct 20 07:07:15 AM +07 2026
$ docker compose exec db date
Tue Oct 20 07:07:15 +07 2026
$ docker compose exec backend sh -c "echo \$TZ"
Asia/Bangkok
```

## หลักฐาน SYS-6 / SYS-7 (คำสั่ง Docker ตามลำดับ)

```text
[07:01:59] $ docker exec -i sp-db sqlcmd ... < database/test-data/tc02-empty.sql

(8 rows affected)
[07:01:59] GET /api/parking-lots
[]  -> HTTP 200
[07:01:59] GET /api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=CAR
{"message":"ยังไม่มีข้อมูลลานจอด"}  -> HTTP 404
PASS empty :: "ยังไม่มีข้อมูลลานจอด", การ์ด 0, หมุด 0
[07:02:05] $ docker compose down -v ; docker compose up -d --build
 Network sptest_app-network Removed 
 Container sp-frontend Started 
[07:02:27] up finished
PASS restored :: การ์ด 8, หมุด 8, "ลานจอดทั้งหมด 8 ลาน"
[07:02:32] $ docker compose stop backend
 Container sp-backend Stopped 
PASS api-down :: "โหลดข้อมูลลานจอดไม่สำเร็จ กรุณาลองใหม่", หน้าเว็บไม่ค้าง
[07:02:39] $ docker compose start backend
 Container sp-backend Started 
[07:02:48] backend healthy again
[07:02:48] $ docker compose down  (no -v)
 Network sptest_app-network Removed 
[07:02:51] $ docker compose up -d
 Container sp-frontend Started 
[07:03:06] up finished
PASS persisted :: การ์ด 8, หมุด 8, "ลานจอดทั้งหมด 8 ลาน"
sp-backend Up 14 seconds (healthy)
sp-db Up 20 seconds (healthy)
sp-db-init Exited (0) 14 seconds ago
sp-frontend Up 6 seconds
```
