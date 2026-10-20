# ตรวจฐานข้อมูลหลังติดตั้งใหม่

| | |
|---|---|
| ผู้รับผิดชอบ | พิชญาภา |
| วันที่ | อ. 20 ต.ค. 2569 |
| วิธีทำ | `docker compose down -v` → `docker compose up -d --build` แล้ว query ตาราง `dbo.parking_lot` |

| ข้อ | ตรวจอะไร | ผลที่ต้องได้ | ผลจริง | ผล |
|---|---|---|---|---|
| DB-1 | จำนวนลาน | 8 ลาน (P1–P6 รถยนต์, M1–M2 จักรยานยนต์) | 8 ลาน, P1–P6 CAR, M1–M2 MOTORCYCLE | ผ่าน |
| DB-2 | พิกัดตรง OpenStreetMap | ตรงพิกัดใน `database/03-seed.sql` (กลางเขตลานจาก OSM) | ตรงครบ 8 ลาน | ผ่าน |
| DB-3 | เขตลาน (`zone`) | ทุกลานมี JSON `[[lat,lng],...]` | `zone_null` = 0 | ผ่าน |
| DB-4 | คอลัมน์ตาม ER | มี `zone NVARCHAR(MAX) NULL` | `zone nvarchar -1 YES` (-1 = MAX) | ผ่าน |
| DB-5 | รันซ้ำได้ | `up` ซ้ำไม่ error ไม่เพิ่มข้อมูลซ้ำ | `sp-db-init` Exited (0) ทุกครั้ง, ยังเป็น 8 ลาน (ดู system-test.md SYS-7) | ผ่าน |

## หลักฐาน

```sql
SELECT code, vehicle_type, latitude, longitude, LEN(zone) AS zone_chars, LEFT(zone,34) AS zone_start
FROM dbo.parking_lot ORDER BY code;
SELECT COUNT(*) AS lots, SUM(CASE WHEN zone IS NULL THEN 1 ELSE 0 END) AS zone_null FROM dbo.parking_lot;
SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'parking_lot';
```

```text
code vehicle_type lat lng zone_chars zone_start
---- ------------ --- --- ---------- ----------
M1 MOTORCYCLE 14.074170 100.606962 116 [[14.074523,100.606821],[14.074523
M2 MOTORCYCLE 14.066319 100.608535 116 [[14.066195,100.608176],[14.066448
P1 CAR 14.071790 100.606734 478 [[14.071944,100.60666],[14.07195,1
P2 CAR 14.069832 100.604829 162 [[14.070117,100.604697],[14.070124
P3 CAR 14.071449 100.603702 139 [[14.071926,100.603493],[14.071926
P4 CAR 14.069751 100.606956 114 [[14.070051,100.606769],[14.07005,
P5 CAR 14.073977 100.610587 116 [[14.074764,100.610669],[14.074763
P6 CAR 14.066475 100.604555 277 [[14.066723,100.604822],[14.066234
lots zone_null
---- ---------
8 0
COLUMN_NAME DATA_TYPE max_len IS_NULLABLE
----------- --------- ------- -----------
id bigint NULL NO
code nvarchar 10 NO
name_th nvarchar 150 NO
building nvarchar 150 YES
vehicle_type nvarchar 12 NO
latitude decimal NULL NO
longitude decimal NULL NO
total_spots int NULL NO
available_spots int NULL NO
updated_at datetime2 NULL NO
zone nvarchar -1 YES
```
