-- Core tables for basic flight booking system

CREATE TABLE airline (
    airline_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    airline_name VARCHAR(100) NOT NULL,
    airline_code VARCHAR(20) NOT NULL UNIQUE,
    hotline VARCHAR(20)
);

CREATE TABLE airplane (
    aircraft_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aircraft_code VARCHAR(20) NOT NULL UNIQUE,
    aircraft_name VARCHAR(100) NOT NULL,
    aircraft_type VARCHAR(50),
    seat_capacity INTEGER,
    airline_id UUID REFERENCES airline(airline_id)
);

CREATE TABLE airport (
    airport_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    airport_name VARCHAR(100) NOT NULL,
    airport_code VARCHAR(10) NOT NULL UNIQUE,
    city VARCHAR(100),
    country VARCHAR(100)
);

CREATE TABLE flight (
    flight_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    flight_code VARCHAR(20) NOT NULL UNIQUE,
    aircraft_id UUID REFERENCES airplane(aircraft_id),
    departure_airport_id UUID REFERENCES airport(airport_id),
    arrival_airport_id UUID REFERENCES airport(airport_id),
    departure_datetime TIMESTAMP,
    arrival_datetime TIMESTAMP,
    base_fare DECIMAL(10,2)
);

CREATE TABLE seat (
    seat_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    seat_code VARCHAR(10) NOT NULL, -- Format: 12A, 14F
    seat_class VARCHAR(20) NOT NULL, -- Economy, Business, First
    seat_price DECIMAL(10,2) NOT NULL, -- Giá ghế theo hạng
    seat_status VARCHAR(20) DEFAULT 'AVAILABLE', -- AVAILABLE, BOOKED, BLOCKED
    flight_id UUID REFERENCES flight(flight_id),
    UNIQUE(flight_id, seat_code),
    CONSTRAINT check_seat_code_format CHECK (seat_code ~ '^[0-9]{1,2}[A-F]$')
);

CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    role VARCHAR(20) DEFAULT 'USER', -- ADMIN, USER
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE, BLOCKED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE passenger (
    passenger_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    date_of_birth DATE NOT NULL,
    citizen_id VARCHAR(12) UNIQUE CHECK (citizen_id ~ '^[0-9]{12}$')
);

-- 4. Tạo lại khóa ngoại cho bảng ticket
ALTER TABLE ticket
ADD CONSTRAINT ticket_passenger_id_fkey 
FOREIGN KEY (passenger_id) 
REFERENCES passenger(passenger_id);
CREATE TABLE booking (
    booking_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_reference VARCHAR(20) UNIQUE NOT NULL,
    user_id UUID REFERENCES users(user_id),
    flight_id UUID REFERENCES flight(flight_id),
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, CONFIRMED, CANCELLED
    payment_status VARCHAR(20) DEFAULT 'UNPAID' -- UNPAID, PAID, PARTIALLY_PAID
);

CREATE TABLE ticket (
    ticket_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id UUID REFERENCES booking(booking_id),
    passenger_id UUID REFERENCES passenger(passenger_id),
    flight_id UUID REFERENCES flight(flight_id),
    seat_id UUID REFERENCES seat(seat_id),
    price DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'ACTIVE' -- ACTIVE, CANCELLED, CHECKED_IN
);

-- Indexes for better query performance
CREATE INDEX idx_flight_departure ON flight(departure_datetime);
CREATE INDEX idx_flight_arrival ON flight(arrival_datetime);
CREATE INDEX idx_seat_status ON seat(seat_status);
CREATE INDEX idx_booking_reference ON booking(booking_reference);
CREATE INDEX idx_booking_user ON booking(user_id);
CREATE INDEX idx_booking_flight ON booking(flight_id);
CREATE INDEX idx_booking_status ON booking(status);
CREATE INDEX idx_ticket_booking ON ticket(booking_id);
CREATE INDEX idx_ticket_passenger ON ticket(passenger_id);
CREATE INDEX idx_ticket_flight ON ticket(flight_id);
CREATE INDEX idx_ticket_status ON ticket(status);
CREATE INDEX idx_flight_aircraft ON flight(aircraft_id);
CREATE INDEX idx_flight_airports ON flight(departure_airport_id, arrival_airport_id);
CREATE INDEX idx_airplane_airline ON airplane(airline_id);

-- Bảng phương thức thanh toán
CREATE TABLE payment_method (
    method_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    method_name VARCHAR(50) NOT NULL, -- VISA, MASTERCARD, MOMO, BANKING
    method_code VARCHAR(20) NOT NULL UNIQUE,
    description TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

-- Bảng lịch sử thanh toán
CREATE TABLE payment (
    payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id UUID REFERENCES booking(booking_id),
    method_id UUID REFERENCES payment_method(method_id),
    amount DECIMAL(10,2) NOT NULL,
    transaction_code VARCHAR(100) UNIQUE,
    payment_status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, SUCCESS, FAILED, REFUNDED
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    note TEXT
);

-- Tạo indexes
CREATE INDEX idx_payment_booking ON payment(booking_id);
CREATE INDEX idx_payment_status ON payment(payment_status);
CREATE INDEX idx_payment_date ON payment(payment_date);
CREATE INDEX idx_transaction_code ON payment(transaction_code);

-- Insert Airlines
INSERT INTO airline (airline_id, airline_name, airline_code, hotline) VALUES
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Vietnam Airlines', 'VNA', '19001100'),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Bamboo Airways', 'BAV', '19001166'),
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Vietjet Air', 'VJA', '19001886');

-- Insert Airplanes
INSERT INTO airplane (aircraft_id, aircraft_code, aircraft_name, aircraft_type, seat_capacity, airline_id) VALUES
    -- Máy bay hiện có
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'VN-A321', 'Airbus A321', 'A321', 200, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'BB-B787', 'Boeing 787', 'B787', 300, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12'),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'VJ-A320', 'Airbus A320', 'A320', 180, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13'),
    
    -- Thêm máy bay cho Vietnam Airlines (a11)
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'VN-B787', 'Boeing 787-9', 'B787', 274, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'VN-A350', 'Airbus A350-900', 'A350', 305, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'VN-A321N', 'Airbus A321neo', 'A321', 203, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
    
    -- Thêm máy bay cho Bamboo Airways (a12)
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'BB-A321N', 'Airbus A321neo', 'A321', 195, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12'),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'BB-B787-9', 'Boeing 787-9', 'B787', 294, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12'),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'BB-A320N', 'Airbus A320neo', 'A320', 180, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12'),
    
    -- Thêm máy bay cho Vietjet Air (a13)
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'VJ-A321N1', 'Airbus A321neo', 'A321', 230, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13'),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'VJ-A321N2', 'Airbus A321neo', 'A321', 230, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13'),
    ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'VJ-A320N', 'Airbus A320neo', 'A320', 180, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13');
-- Insert Airports
INSERT INTO airport (airport_id, airport_name, airport_code, city, country) VALUES
    -- Các sân bay hiện có
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Noi Bai International Airport', 'HAN', 'Hanoi', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Tan Son Nhat International Airport', 'SGN', 'Ho Chi Minh', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Da Nang International Airport', 'DAD', 'Da Nang', 'Vietnam'),
    -- Thêm các sân bay mới
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'Cam Ranh International Airport', 'CXR', 'Nha Trang', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Phu Bai International Airport', 'HUI', 'Hue', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'Phu Quoc International Airport', 'PQC', 'Phu Quoc', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'Cat Bi International Airport', 'HPH', 'Hai Phong', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'Van Don International Airport', 'VDO', 'Quang Ninh', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'Vinh International Airport', 'VII', 'Vinh', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'Lien Khuong Airport', 'DLI', 'Da Lat', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'Buon Ma Thuot Airport', 'BMV', 'Buon Ma Thuot', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Pleiku Airport', 'PXU', 'Pleiku', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23', 'Tho Xuan Airport', 'THD', 'Thanh Hoa', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', 'Phu Cat Airport', 'UIH', 'Quy Nhon', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', 'Con Dao Airport', 'VCS', 'Con Dao', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26', 'Dong Hoi Airport', 'VDH', 'Dong Hoi', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27', 'Rach Gia Airport', 'VKG', 'Rach Gia', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', 'Ca Mau Airport', 'CAH', 'Ca Mau', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', 'Dien Bien Phu Airport', 'DIN', 'Dien Bien Phu', 'Vietnam'),
    ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a30', 'Tuy Hoa Airport', 'TBB', 'Tuy Hoa', 'Vietnam');
-- Insert Flights
INSERT INTO flight (flight_id, flight_code, aircraft_id, departure_airport_id, arrival_airport_id, departure_datetime, arrival_datetime, base_fare) VALUES
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'VN100', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-03-20 08:00:00', '2024-03-20 10:00:00', 1000000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'BB200', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '2024-03-20 14:00:00', '2024-03-20 15:30:00', 800000);
-- Thêm các chuyến bay từ 20-27/12/2024
INSERT INTO flight (flight_id, flight_code, aircraft_id, departure_airport_id, arrival_airport_id, departure_datetime, arrival_datetime, base_fare) VALUES
    -- 20/12/2024
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41', 'VN201', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-20 06:00:00', '2024-12-20 08:00:00', 1200000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a42', 'VJ201', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-20 09:00:00', '2024-12-20 11:00:00', 900000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a43', 'BB201', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-20 07:30:00', '2024-12-20 09:00:00', 850000),

    -- 21/12/2024
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44', 'VN202', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', '2024-12-21 08:00:00', '2024-12-21 09:30:00', 950000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a45', 'VJ202', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '2024-12-21 10:00:00', '2024-12-21 11:30:00', 800000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a46', 'BB202', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-21 14:00:00', '2024-12-21 15:30:00', 1100000),

    -- 22/12/2024
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a47', 'VN203', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', '2024-12-22 07:00:00', '2024-12-22 08:30:00', 1300000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a48', 'VJ203', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '2024-12-22 11:00:00', '2024-12-22 12:30:00', 950000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a49', 'BB203', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', '2024-12-22 15:00:00', '2024-12-22 16:30:00', 880000),

    -- 23/12/2024 (Thêm nhiều chuyến vì gần Giáng sinh)
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a50', 'VN204', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-23 06:00:00', '2024-12-23 08:00:00', 1500000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a51', 'VN205', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-23 09:00:00', '2024-12-23 11:00:00', 1500000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a52', 'VJ204', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-23 07:00:00', '2024-12-23 08:30:00', 1200000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a53', 'BB204', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '2024-12-23 10:00:00', '2024-12-23 11:30:00', 1200000),

    -- 24/12/2024 (Đêm Giáng sinh - giá cao hơn)
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a54', 'VN206', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-24 07:00:00', '2024-12-24 09:00:00', 1800000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55', 'VJ205', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-24 10:00:00', '2024-12-24 12:00:00', 1600000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a56', 'BB205', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-24 14:00:00', '2024-12-24 15:30:00', 1500000),

    -- 25/12/2024 (Ngày Giáng sinh)
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a57', 'VN207', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '2024-12-25 08:00:00', '2024-12-25 09:30:00', 1700000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a58', 'VJ206', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-25 11:00:00', '2024-12-25 13:00:00', 1500000),

    -- 26/12/2024
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a59', 'VN208', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-26 07:00:00', '2024-12-26 09:00:00', 1400000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a60', 'BB206', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-26 15:00:00', '2024-12-26 16:30:00', 1300000),

    -- 27/12/2024
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a61', 'VN209', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-27 08:00:00', '2024-12-27 10:00:00', 1200000),
    ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', 'VJ207', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '2024-12-27 11:00:00', '2024-12-27 12:30:00', 1100000);
-- Insert Seats (chỉ thêm một vài ghế mẫu)
INSERT INTO seat (seat_id, seat_code, seat_class, seat_price, seat_status, flight_id) VALUES
    ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '1A', 'Business', 2000000, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
    ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '1B', 'Business', 2000000, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
    ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '10A', 'Economy', 1000000, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');

-- Insert Users
INSERT INTO users (user_id, username, password, email, phone, role, status) VALUES
    ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'admin', '$2a$10$xP3/YnXjZKqyXwPEHNhqG.1Z', 'admin@example.com', '0901234567', 'ADMIN', 'ACTIVE'),
    ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'user1', '$2a$10$xP3/YnXjZKqyXwPEHNhqG.1Z', 'user1@example.com', '0901234568', 'USER', 'ACTIVE');

--- Insert Passengers
INSERT INTO passenger (passenger_id, passenger_name, email, phone_number, date_of_birth) VALUES
    ('70eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Nguyen Van A', 'nguyenvana@email.com', '0901234569', '1990-01-01'),
    ('71eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Tran Thi B', 'tranthib@email.com', '0901234570', '1992-02-02');

-- Insert Bookings
INSERT INTO booking (booking_id, booking_reference, user_id, flight_id, total_amount, status, payment_status) VALUES
    ('80eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'BK001', 'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 2000000, 'CONFIRMED', 'PAID');

-- Insert Tickets
INSERT INTO ticket (ticket_id, booking_id, passenger_id, flight_id, seat_id, price, status) VALUES
    ('90eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '80eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '70eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 2000000, 'ACTIVE');

-- Insert Payment Methods
INSERT INTO payment_method (method_id, method_name, method_code, description, status) VALUES
    ('91eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'VISA', 'VISA', 'Thanh toán qua thẻ VISA', 'ACTIVE'),
    ('92eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'MOMO', 'MOMO', 'Thanh toán qua ví MoMo', 'ACTIVE');

-- Insert Payments
INSERT INTO payment (payment_id, booking_id, method_id, amount, transaction_code, payment_status) VALUES
    ('93eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '80eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '91eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 2000000, 'TRX001', 'SUCCESS');

-- Insert Payments
INSERT INTO payment (payment_id, booking_id, method_id, amount, transaction_code, payment_status) VALUES
    ('k0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'h0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'j0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 2000000, 'TRX001', 'SUCCESS');

-- Thêm chuyến bay về (SGN-HAN) cho chuyến VN100
INSERT INTO flight (
    flight_id, 
    flight_code, 
    aircraft_id, 
    departure_airport_id, 
    arrival_airport_id, 
    departure_datetime, 
    arrival_datetime, 
    base_fare
) VALUES (
    'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', -- new UUID
    'VN101', -- chuyến về
    'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', -- cùng máy bay
    'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', -- từ SGN
    'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', -- về HAN
    '2024-03-22 15:00:00', -- 2 ngày sau
    '2024-03-22 17:00:00',
    1200000
);

-- Thêm ghế cho chuyến về
INSERT INTO seat (
    seat_id, 
    seat_code, 
    seat_class, 
    seat_price, 
    seat_status, 
    flight_id
) VALUES
    ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', '1A', 'Business', 2200000, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13'),
    ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', '1B', 'Business', 2200000, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13'),
    ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', '10A', 'Economy', 1200000, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13');