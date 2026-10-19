-- Schema for Sprint 1 (ER diagram: docs/design/er-diagram).
-- Runs on every `docker compose up`, so every change must be safe to run twice.
USE SmartParking;
GO

IF OBJECT_ID(N'dbo.parking_lot', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.parking_lot (
        id              BIGINT IDENTITY(1,1) PRIMARY KEY,
        code            NVARCHAR(10)   NOT NULL UNIQUE,   -- e.g. P1, M1 (signage code)
        name_th         NVARCHAR(150)  NOT NULL,
        building        NVARCHAR(150)  NULL,              -- nearest building, used for search (US04)
        vehicle_type    NVARCHAR(12)   NOT NULL CONSTRAINT df_parking_lot_vehicle_type DEFAULT N'CAR', -- CAR / MOTORCYCLE (US05)
        latitude        DECIMAL(9,6)   NOT NULL,
        longitude       DECIMAL(9,6)   NOT NULL,
        total_spots     INT            NOT NULL CHECK (total_spots > 0),
        available_spots INT            NOT NULL,
        updated_at      DATETIME2      NOT NULL DEFAULT SYSDATETIME(),
        zone            NVARCHAR(MAX)  NULL,              -- lot outline as JSON [[lat,lng],...] (map highlight)
        CONSTRAINT ck_parking_lot_available CHECK (available_spots BETWEEN 0 AND total_spots),
        CONSTRAINT ck_parking_lot_vehicle_type CHECK (vehicle_type IN (N'CAR', N'MOTORCYCLE'))
    );
END
GO

-- Databases created before the zone column existed
IF COL_LENGTH(N'dbo.parking_lot', N'zone') IS NULL
    ALTER TABLE dbo.parking_lot ADD zone NVARCHAR(MAX) NULL;
GO
