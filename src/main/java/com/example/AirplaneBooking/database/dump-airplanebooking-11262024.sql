--
-- PostgreSQL database dump
--

-- Dumped from database version 14.3
-- Dumped by pg_dump version 14.3

-- Started on 2024-11-26 19:47:46

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3466 (class 1262 OID 18476)
-- Name: airplane_booking; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE airplane_booking WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Vietnamese_Vietnam.1258';


ALTER DATABASE airplane_booking OWNER TO postgres;

\connect airplane_booking

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 3467 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 209 (class 1259 OID 18662)
-- Name: airline; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.airline (
    airline_id uuid DEFAULT gen_random_uuid() NOT NULL,
    airline_name character varying(255) NOT NULL,
    airline_code character varying(255) NOT NULL,
    hotline character varying(255)
);


ALTER TABLE public.airline OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 18670)
-- Name: airplane; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.airplane (
    aircraft_id uuid DEFAULT gen_random_uuid() NOT NULL,
    aircraft_code character varying(255) NOT NULL,
    aircraft_name character varying(255) NOT NULL,
    aircraft_type character varying(255),
    seat_capacity integer,
    airline_id uuid
);


ALTER TABLE public.airplane OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 18683)
-- Name: airport; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.airport (
    airport_id uuid DEFAULT gen_random_uuid() NOT NULL,
    airport_name character varying(255) NOT NULL,
    airport_code character varying(255) NOT NULL,
    city character varying(255),
    country character varying(255)
);


ALTER TABLE public.airport OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 18748)
-- Name: booking; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.booking (
    booking_id uuid DEFAULT gen_random_uuid() NOT NULL,
    booking_reference character varying(255) NOT NULL,
    user_id uuid,
    flight_id uuid,
    booking_date timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    total_amount numeric(38,2),
    status character varying(255) DEFAULT 'PENDING'::character varying,
    payment_status character varying(255) DEFAULT 'UNPAID'::character varying,
    passenger_count integer
);


ALTER TABLE public.booking OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 21492)
-- Name: booking_seats; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.booking_seats (
    booking_id uuid NOT NULL,
    seat_code character varying(255)
);


ALTER TABLE public.booking_seats OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 18691)
-- Name: flight; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flight (
    flight_id uuid DEFAULT gen_random_uuid() NOT NULL,
    flight_code character varying(255) NOT NULL,
    aircraft_id uuid,
    departure_airport_id uuid,
    arrival_airport_id uuid,
    departure_datetime timestamp(6) without time zone,
    arrival_datetime timestamp(6) without time zone,
    base_fare numeric(38,2),
    status character varying(255) DEFAULT 'SCHEDULED'::character varying NOT NULL,
    available_seats integer
);


ALTER TABLE public.flight OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 20210)
-- Name: passenger; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.passenger (
    passenger_id uuid DEFAULT gen_random_uuid() NOT NULL,
    full_name character varying(255) NOT NULL,
    gender character varying(255),
    date_of_birth date NOT NULL,
    citizen_id character varying(255),
    booking_id uuid,
    CONSTRAINT passenger_citizen_id_check CHECK (((citizen_id)::text ~ '^[0-9]{12}$'::text)),
    CONSTRAINT passenger_gender_check CHECK (((gender)::text = ANY (ARRAY[('MALE'::character varying)::text, ('FEMALE'::character varying)::text, ('OTHER'::character varying)::text])))
);


ALTER TABLE public.passenger OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 18821)
-- Name: payment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment (
    payment_id uuid DEFAULT gen_random_uuid() NOT NULL,
    booking_id uuid,
    method_id uuid,
    amount numeric(38,2) NOT NULL,
    transaction_code character varying(255),
    payment_status character varying(255) DEFAULT 'PENDING'::character varying,
    payment_date timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP,
    note character varying(255)
);


ALTER TABLE public.payment OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 18810)
-- Name: payment_method; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_method (
    method_id uuid DEFAULT gen_random_uuid() NOT NULL,
    method_name character varying(255) NOT NULL,
    method_code character varying(255) NOT NULL,
    description character varying(255),
    status character varying(255) DEFAULT 'ACTIVE'::character varying
);


ALTER TABLE public.payment_method OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 18714)
-- Name: seat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.seat (
    seat_id uuid DEFAULT gen_random_uuid() NOT NULL,
    seat_code character varying(255) NOT NULL,
    seat_class character varying(255) NOT NULL,
    seat_price numeric(38,2) NOT NULL,
    seat_status character varying(255) DEFAULT 'AVAILABLE'::character varying,
    flight_id uuid,
    CONSTRAINT check_seat_code_format CHECK (((seat_code)::text ~ '^[0-9]{1,2}[A-F]$'::text))
);


ALTER TABLE public.seat OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 18769)
-- Name: ticket; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ticket (
    ticket_id uuid DEFAULT gen_random_uuid() NOT NULL,
    booking_id uuid,
    passenger_id uuid,
    flight_id uuid,
    seat_id uuid,
    price numeric(38,2),
    status character varying(255) DEFAULT 'ACTIVE'::character varying
);


ALTER TABLE public.ticket OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 18729)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id uuid DEFAULT gen_random_uuid() NOT NULL,
    username character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    phone character varying(255),
    role character varying(255) DEFAULT 'USER'::character varying,
    status character varying(255) DEFAULT 'ACTIVE'::character varying,
    created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 3449 (class 0 OID 18662)
-- Dependencies: 209
-- Data for Name: airline; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.airline VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Vietnam Airlines', 'VNA', '19001100');
INSERT INTO public.airline VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Bamboo Airways', 'BAV', '19001166');
INSERT INTO public.airline VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Vietjet Air', 'VJA', '19001886');


--
-- TOC entry 3450 (class 0 OID 18670)
-- Dependencies: 210
-- Data for Name: airplane; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'VN-A321', 'Airbus A321', 'A321', 200, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'BB-B787', 'Boeing 787', 'B787', 300, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12');
INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'VJ-A320', 'Airbus A320', 'A320', 180, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13');
INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'VN-B787', 'Boeing 787-9', 'B787', 274, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'VN-A350', 'Airbus A350-900', 'A350', 305, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'VN-A321N', 'Airbus A321neo', 'A321', 203, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');
INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'BB-A321N', 'Airbus A321neo', 'A321', 195, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12');
INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'BB-B787-9', 'Boeing 787-9', 'B787', 294, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12');
INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'BB-A320N', 'Airbus A320neo', 'A320', 180, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12');
INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'VJ-A321N1', 'Airbus A321neo', 'A321', 230, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13');
INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'VJ-A321N2', 'Airbus A321neo', 'A321', 230, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13');
INSERT INTO public.airplane VALUES ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'VJ-A320N', 'Airbus A320neo', 'A320', 180, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13');


--
-- TOC entry 3451 (class 0 OID 18683)
-- Dependencies: 211
-- Data for Name: airport; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Noi Bai International Airport', 'HAN', 'Hanoi', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Tan Son Nhat International Airport', 'SGN', 'Ho Chi Minh', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Da Nang International Airport', 'DAD', 'Da Nang', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'Cam Ranh International Airport', 'CXR', 'Nha Trang', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Phu Bai International Airport', 'HUI', 'Hue', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'Phu Quoc International Airport', 'PQC', 'Phu Quoc', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'Cat Bi International Airport', 'HPH', 'Hai Phong', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'Van Don International Airport', 'VDO', 'Quang Ninh', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'Vinh International Airport', 'VII', 'Vinh', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'Lien Khuong Airport', 'DLI', 'Da Lat', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'Buon Ma Thuot Airport', 'BMV', 'Buon Ma Thuot', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Pleiku Airport', 'PXU', 'Pleiku', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a23', 'Tho Xuan Airport', 'THD', 'Thanh Hoa', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a24', 'Phu Cat Airport', 'UIH', 'Quy Nhon', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a25', 'Con Dao Airport', 'VCS', 'Con Dao', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a26', 'Dong Hoi Airport', 'VDH', 'Dong Hoi', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a27', 'Rach Gia Airport', 'VKG', 'Rach Gia', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a28', 'Ca Mau Airport', 'CAH', 'Ca Mau', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a29', 'Dien Bien Phu Airport', 'DIN', 'Dien Bien Phu', 'Vietnam');
INSERT INTO public.airport VALUES ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a30', 'Tuy Hoa Airport', 'TBB', 'Tuy Hoa', 'Vietnam');


--
-- TOC entry 3455 (class 0 OID 18748)
-- Dependencies: 215
-- Data for Name: booking; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.booking VALUES ('7c6e683e-afc9-4f6c-aed8-0ccf940bf88e', 'EP310450', 'd328a94b-9f76-4688-b50b-8c6486197054', NULL, '2024-11-25 22:27:48.647737', 1200000.00, 'PENDING', 'UNPAID', 1);
INSERT INTO public.booking VALUES ('4f6c14fb-f292-412b-9af2-53f80e59dbc4', 'JM939647', 'd328a94b-9f76-4688-b50b-8c6486197054', NULL, '2024-11-26 13:49:34.203072', 1200000.00, 'PENDING', 'UNPAID', 1);
INSERT INTO public.booking VALUES ('2e30e11d-390f-4a0b-a02b-9df4ba5456e9', 'SQ780451', 'd328a94b-9f76-4688-b50b-8c6486197054', NULL, '2024-11-26 13:51:05.691146', 3600000.00, 'PENDING', 'UNPAID', 1);
INSERT INTO public.booking VALUES ('a498dc08-d62f-4215-8bbb-fee751310552', 'WC424742', 'd328a94b-9f76-4688-b50b-8c6486197054', NULL, '2024-11-26 13:57:28.350272', 3600000.00, 'PENDING', 'UNPAID', 1);
INSERT INTO public.booking VALUES ('c5b05f47-8381-43b9-970e-90f7293a04b9', 'LC452964', 'd328a94b-9f76-4688-b50b-8c6486197054', NULL, '2024-11-26 13:57:59.408895', 3600000.00, 'PENDING', 'UNPAID', 1);
INSERT INTO public.booking VALUES ('a73a9d6c-5a71-41b8-addb-4ca0c0b78b62', 'EP310451', 'd328a94b-9f76-4688-b50b-8c6486197054', NULL, '2024-11-26 14:00:53.284376', 999999.00, 'PENDING', 'UNPAID', 1);
INSERT INTO public.booking VALUES ('106d4971-6dff-46e3-81c9-4294d5380523', 'EP310411', 'd328a94b-9f76-4688-b50b-8c6486197054', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41', '2024-11-26 14:02:05.192588', 999999.00, 'PENDING', 'UNPAID', 1);
INSERT INTO public.booking VALUES ('93af76cd-b4d6-4b8b-b612-0dfeb3eb7d40', 'AK216546', 'd328a94b-9f76-4688-b50b-8c6486197054', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41', '2024-11-26 14:02:26.506824', 3600000.00, 'PENDING', 'PAID', 1);
INSERT INTO public.booking VALUES ('718ba74d-9b5e-4a48-811a-63da5303d8e5', 'HE558211', 'd328a94b-9f76-4688-b50b-8c6486197054', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41', '2024-11-26 14:49:21.720667', 3600000.00, 'PENDING', 'UNPAID', 1);


--
-- TOC entry 3460 (class 0 OID 21492)
-- Dependencies: 220
-- Data for Name: booking_seats; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.booking_seats VALUES ('4f6c14fb-f292-412b-9af2-53f80e59dbc4', '1B');
INSERT INTO public.booking_seats VALUES ('2e30e11d-390f-4a0b-a02b-9df4ba5456e9', '1C');
INSERT INTO public.booking_seats VALUES ('a498dc08-d62f-4215-8bbb-fee751310552', '1D');
INSERT INTO public.booking_seats VALUES ('c5b05f47-8381-43b9-970e-90f7293a04b9', '1A');
INSERT INTO public.booking_seats VALUES ('a73a9d6c-5a71-41b8-addb-4ca0c0b78b62', '1C');
INSERT INTO public.booking_seats VALUES ('106d4971-6dff-46e3-81c9-4294d5380523', '1C');
INSERT INTO public.booking_seats VALUES ('93af76cd-b4d6-4b8b-b612-0dfeb3eb7d40', '1B');
INSERT INTO public.booking_seats VALUES ('718ba74d-9b5e-4a48-811a-63da5303d8e5', '1D');


--
-- TOC entry 3452 (class 0 OID 18691)
-- Dependencies: 212
-- Data for Name: flight; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'VN101', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-03-22 15:00:00', '2024-03-22 17:00:00', 1200000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41', 'VN201', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-20 06:00:00', '2024-12-20 08:00:00', 1200000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a42', 'VJ201', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-20 09:00:00', '2024-12-20 11:00:00', 900000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a43', 'BB201', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-20 07:30:00', '2024-12-20 09:00:00', 850000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44', 'VN202', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', '2024-12-21 08:00:00', '2024-12-21 09:30:00', 950000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a45', 'VJ202', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '2024-12-21 10:00:00', '2024-12-21 11:30:00', 800000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a46', 'BB202', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-21 14:00:00', '2024-12-21 15:30:00', 1100000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a47', 'VN203', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', '2024-12-22 07:00:00', '2024-12-22 08:30:00', 1300000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a48', 'VJ203', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '2024-12-22 11:00:00', '2024-12-22 12:30:00', 950000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a49', 'BB203', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', '2024-12-22 15:00:00', '2024-12-22 16:30:00', 880000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a50', 'VN204', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-23 06:00:00', '2024-12-23 08:00:00', 1500000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a51', 'VN205', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-23 09:00:00', '2024-12-23 11:00:00', 1500000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a52', 'VJ204', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-23 07:00:00', '2024-12-23 08:30:00', 1200000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a53', 'BB204', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '2024-12-23 10:00:00', '2024-12-23 11:30:00', 1200000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a54', 'VN206', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-24 07:00:00', '2024-12-24 09:00:00', 1800000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55', 'VJ205', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a21', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-24 10:00:00', '2024-12-24 12:00:00', 1600000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a56', 'BB205', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-24 14:00:00', '2024-12-24 15:30:00', 1500000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a57', 'VN207', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '2024-12-25 08:00:00', '2024-12-25 09:30:00', 1700000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a58', 'VJ206', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-25 11:00:00', '2024-12-25 13:00:00', 1500000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a59', 'VN208', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', '2024-12-26 07:00:00', '2024-12-26 09:00:00', 1400000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a60', 'BB206', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-26 15:00:00', '2024-12-26 16:30:00', 1300000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a61', 'VN209', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', '2024-12-27 08:00:00', '2024-12-27 10:00:00', 1200000.00, 'SCHEDULED', 100);
INSERT INTO public.flight VALUES ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a62', 'VJ207', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', '2024-12-27 11:00:00', '2024-12-27 12:30:00', 1100000.00, 'SCHEDULED', 100);


--
-- TOC entry 3459 (class 0 OID 20210)
-- Dependencies: 219
-- Data for Name: passenger; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.passenger VALUES ('e63a350d-6de9-4fb5-b3fe-fabfc4266868', 'Nguyễn A', 'MALE', '1999-09-09', '123456789123', NULL);
INSERT INTO public.passenger VALUES ('a1b4435d-0f04-4e05-8e77-a58f06e55da2', 'Nguyễn Văn Trí', 'MALE', '2009-09-09', '123456789012', NULL);
INSERT INTO public.passenger VALUES ('bbef7cd6-f928-4004-9e4d-81f710cac71b', 'Nguyễn A', 'FEMALE', '2023-09-09', '123456789013', NULL);


--
-- TOC entry 3458 (class 0 OID 18821)
-- Dependencies: 218
-- Data for Name: payment; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3457 (class 0 OID 18810)
-- Dependencies: 217
-- Data for Name: payment_method; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.payment_method VALUES ('91eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'VISA', 'VISA', 'Thanh toán qua thẻ VISA', 'ACTIVE');
INSERT INTO public.payment_method VALUES ('92eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'MOMO', 'MOMO', 'Thanh toán qua ví MoMo', 'ACTIVE');


--
-- TOC entry 3453 (class 0 OID 18714)
-- Dependencies: 213
-- Data for Name: seat; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.seat VALUES ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', '1A', 'Business', 2200000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13');
INSERT INTO public.seat VALUES ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', '1B', 'Business', 2200000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13');
INSERT INTO public.seat VALUES ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', '10A', 'Economy', 1200000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13');
INSERT INTO public.seat VALUES ('25722d59-0fe8-4baf-9f4e-867fffae6a76', '1A', 'Business', 2400000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41');
INSERT INTO public.seat VALUES ('29a7d703-5227-4335-bb3a-0aca750702e5', '1B', 'Business', 2400000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41');
INSERT INTO public.seat VALUES ('d4a48054-3356-4a06-924b-1d0bcefcbbbb', '1C', 'Business', 2400000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41');
INSERT INTO public.seat VALUES ('127c0db7-bf34-4881-91cb-9c583ca09693', '1D', 'Business', 2400000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41');
INSERT INTO public.seat VALUES ('cb4a0601-ad44-42e8-a09b-195254e628f4', '10A', 'Economy', 1200000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41');
INSERT INTO public.seat VALUES ('0a29aac0-8926-4af2-be9b-bc7413f3f949', '10B', 'Economy', 1200000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41');
INSERT INTO public.seat VALUES ('4c5c9166-829e-45e4-9504-1bd7c7b1fa4b', '10C', 'Economy', 1200000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41');
INSERT INTO public.seat VALUES ('ada71efb-f15b-4a28-9b15-518002a7d317', '11A', 'Economy', 1200000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41');
INSERT INTO public.seat VALUES ('58df1384-d284-45f3-8f25-4a453917c3af', '11B', 'Economy', 1200000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41');
INSERT INTO public.seat VALUES ('c57321a8-52f5-45f1-8be4-793a7dcc6214', '11C', 'Economy', 1200000.00, 'AVAILABLE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a41');


--
-- TOC entry 3456 (class 0 OID 18769)
-- Dependencies: 216
-- Data for Name: ticket; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3454 (class 0 OID 18729)
-- Dependencies: 214
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users VALUES ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'user1', '$2a$10$ZWvae5d7jlrgmyh52zNh7uGHMO5iEZDnupUg6R6IcwUdwyPCH7Pay', 'user1@example.com', '0901234568', 'ADMIN', 'ACTIVE', '2024-11-17 00:07:29.785291');
INSERT INTO public.users VALUES ('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'admin', '$2a$10$qdblsozLYDIUNi37jYt1qu45jkKdYSeTfde9.hHVTRt8nz6220jpq', 'admin@example.com', '0901234567', 'USER', 'ACTIVE', '2024-11-17 00:07:29.785291');
INSERT INTO public.users VALUES ('a6eaad78-0d6c-4025-a69a-7ba972f6579d', 'myadmin', '$2a$10$KGLqXIXw6Adw58pgv4trbOKPURfrU1MoMJHmWCX6VaocC9wLuW0Ha', 'myadmin@gmail.com', '0123456789', 'USER', 'ACTIVE', '2024-11-24 13:13:58.576539');
INSERT INTO public.users VALUES ('4a060e8b-9dc2-4d29-adc3-ac746f468e62', 'string', '$2a$10$waihL36L8fdg4qxBcQdj8eTsckDu9WaOd2NYvKMoBwZPXRzoFFQM.', 'string@gmail.com', '1234567890', 'ADMIN', 'ACTIVE', '2024-11-24 18:26:09.043859');
INSERT INTO public.users VALUES ('d328a94b-9f76-4688-b50b-8c6486197054', 'Trịnh Đình Thành', '$2a$10$fLrpQSBrxR9P89ZlJfB.bOq1iiWkqdHmX6bJYsQ4e8drFpiTN9o7W', 'abc123@gmail.com', '0123456789', 'USER', 'ACTIVE', '2024-11-17 09:54:52.27183');


--
-- TOC entry 3234 (class 2606 OID 18848)
-- Name: airline airline_airline_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.airline
    ADD CONSTRAINT airline_airline_code_key UNIQUE (airline_code);


--
-- TOC entry 3236 (class 2606 OID 18667)
-- Name: airline airline_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.airline
    ADD CONSTRAINT airline_pkey PRIMARY KEY (airline_id);


--
-- TOC entry 3238 (class 2606 OID 18852)
-- Name: airplane airplane_aircraft_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.airplane
    ADD CONSTRAINT airplane_aircraft_code_key UNIQUE (aircraft_code);


--
-- TOC entry 3240 (class 2606 OID 18675)
-- Name: airplane airplane_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.airplane
    ADD CONSTRAINT airplane_pkey PRIMARY KEY (aircraft_id);


--
-- TOC entry 3243 (class 2606 OID 18856)
-- Name: airport airport_airport_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.airport
    ADD CONSTRAINT airport_airport_code_key UNIQUE (airport_code);


--
-- TOC entry 3245 (class 2606 OID 18688)
-- Name: airport airport_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.airport
    ADD CONSTRAINT airport_pkey PRIMARY KEY (airport_id);


--
-- TOC entry 3266 (class 2606 OID 18861)
-- Name: booking booking_booking_reference_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT booking_booking_reference_key UNIQUE (booking_reference);


--
-- TOC entry 3268 (class 2606 OID 18756)
-- Name: booking booking_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT booking_pkey PRIMARY KEY (booking_id);


--
-- TOC entry 3247 (class 2606 OID 18871)
-- Name: flight flight_flight_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_flight_code_key UNIQUE (flight_code);


--
-- TOC entry 3249 (class 2606 OID 18696)
-- Name: flight flight_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_pkey PRIMARY KEY (flight_id);


--
-- TOC entry 3292 (class 2606 OID 20298)
-- Name: passenger passenger_citizen_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.passenger
    ADD CONSTRAINT passenger_citizen_id_key UNIQUE (citizen_id);


--
-- TOC entry 3294 (class 2606 OID 20217)
-- Name: passenger passenger_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.passenger
    ADD CONSTRAINT passenger_pkey PRIMARY KEY (passenger_id);


--
-- TOC entry 3280 (class 2606 OID 18898)
-- Name: payment_method payment_method_method_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_method
    ADD CONSTRAINT payment_method_method_code_key UNIQUE (method_code);


--
-- TOC entry 3282 (class 2606 OID 18818)
-- Name: payment_method payment_method_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_method
    ADD CONSTRAINT payment_method_pkey PRIMARY KEY (method_id);


--
-- TOC entry 3288 (class 2606 OID 18830)
-- Name: payment payment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (payment_id);


--
-- TOC entry 3290 (class 2606 OID 18890)
-- Name: payment payment_transaction_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_transaction_code_key UNIQUE (transaction_code);


--
-- TOC entry 3256 (class 2606 OID 18903)
-- Name: seat seat_flight_id_seat_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seat
    ADD CONSTRAINT seat_flight_id_seat_code_key UNIQUE (flight_id, seat_code);


--
-- TOC entry 3258 (class 2606 OID 18721)
-- Name: seat seat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seat
    ADD CONSTRAINT seat_pkey PRIMARY KEY (seat_id);


--
-- TOC entry 3278 (class 2606 OID 18775)
-- Name: ticket ticket_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_pkey PRIMARY KEY (ticket_id);


--
-- TOC entry 3260 (class 2606 OID 18913)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 3262 (class 2606 OID 18737)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3264 (class 2606 OID 18919)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 3241 (class 1259 OID 18809)
-- Name: idx_airplane_airline; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_airplane_airline ON public.airplane USING btree (airline_id);


--
-- TOC entry 3269 (class 1259 OID 18801)
-- Name: idx_booking_flight; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_booking_flight ON public.booking USING btree (flight_id);


--
-- TOC entry 3270 (class 1259 OID 18862)
-- Name: idx_booking_reference; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_booking_reference ON public.booking USING btree (booking_reference);


--
-- TOC entry 3271 (class 1259 OID 18867)
-- Name: idx_booking_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_booking_status ON public.booking USING btree (status);


--
-- TOC entry 3272 (class 1259 OID 18800)
-- Name: idx_booking_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_booking_user ON public.booking USING btree (user_id);


--
-- TOC entry 3250 (class 1259 OID 18807)
-- Name: idx_flight_aircraft; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_flight_aircraft ON public.flight USING btree (aircraft_id);


--
-- TOC entry 3251 (class 1259 OID 18808)
-- Name: idx_flight_airports; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_flight_airports ON public.flight USING btree (departure_airport_id, arrival_airport_id);


--
-- TOC entry 3252 (class 1259 OID 21627)
-- Name: idx_flight_arrival; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_flight_arrival ON public.flight USING btree (arrival_datetime);


--
-- TOC entry 3253 (class 1259 OID 21628)
-- Name: idx_flight_departure; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_flight_departure ON public.flight USING btree (departure_datetime);


--
-- TOC entry 3283 (class 1259 OID 18843)
-- Name: idx_payment_booking; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_payment_booking ON public.payment USING btree (booking_id);


--
-- TOC entry 3284 (class 1259 OID 21630)
-- Name: idx_payment_date; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_payment_date ON public.payment USING btree (payment_date);


--
-- TOC entry 3285 (class 1259 OID 18886)
-- Name: idx_payment_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_payment_status ON public.payment USING btree (payment_status);


--
-- TOC entry 3254 (class 1259 OID 18908)
-- Name: idx_seat_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_seat_status ON public.seat USING btree (seat_status);


--
-- TOC entry 3273 (class 1259 OID 18803)
-- Name: idx_ticket_booking; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_ticket_booking ON public.ticket USING btree (booking_id);


--
-- TOC entry 3274 (class 1259 OID 18805)
-- Name: idx_ticket_flight; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_ticket_flight ON public.ticket USING btree (flight_id);


--
-- TOC entry 3275 (class 1259 OID 18804)
-- Name: idx_ticket_passenger; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_ticket_passenger ON public.ticket USING btree (passenger_id);


--
-- TOC entry 3276 (class 1259 OID 18910)
-- Name: idx_ticket_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_ticket_status ON public.ticket USING btree (status);


--
-- TOC entry 3286 (class 1259 OID 18891)
-- Name: idx_transaction_code; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_transaction_code ON public.payment USING btree (transaction_code);


--
-- TOC entry 3295 (class 2606 OID 18678)
-- Name: airplane airplane_airline_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.airplane
    ADD CONSTRAINT airplane_airline_id_fkey FOREIGN KEY (airline_id) REFERENCES public.airline(airline_id);


--
-- TOC entry 3301 (class 2606 OID 18764)
-- Name: booking booking_flight_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT booking_flight_id_fkey FOREIGN KEY (flight_id) REFERENCES public.flight(flight_id);


--
-- TOC entry 3300 (class 2606 OID 18759)
-- Name: booking booking_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT booking_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 3309 (class 2606 OID 21500)
-- Name: booking_seats fkiafqjbiteckjb95679jbqwona; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_seats
    ADD CONSTRAINT fkiafqjbiteckjb95679jbqwona FOREIGN KEY (booking_id) REFERENCES public.booking(booking_id);


--
-- TOC entry 3308 (class 2606 OID 20858)
-- Name: passenger fktco0omesfld1qi5sw76eomvt4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.passenger
    ADD CONSTRAINT fktco0omesfld1qi5sw76eomvt4 FOREIGN KEY (booking_id) REFERENCES public.booking(booking_id);


--
-- TOC entry 3296 (class 2606 OID 18699)
-- Name: flight flight_aircraft_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_aircraft_id_fkey FOREIGN KEY (aircraft_id) REFERENCES public.airplane(aircraft_id);


--
-- TOC entry 3298 (class 2606 OID 18709)
-- Name: flight flight_arrival_airport_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_arrival_airport_id_fkey FOREIGN KEY (arrival_airport_id) REFERENCES public.airport(airport_id);


--
-- TOC entry 3297 (class 2606 OID 18704)
-- Name: flight flight_departure_airport_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flight
    ADD CONSTRAINT flight_departure_airport_id_fkey FOREIGN KEY (departure_airport_id) REFERENCES public.airport(airport_id);


--
-- TOC entry 3306 (class 2606 OID 18833)
-- Name: payment payment_booking_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_booking_id_fkey FOREIGN KEY (booking_id) REFERENCES public.booking(booking_id);


--
-- TOC entry 3307 (class 2606 OID 18838)
-- Name: payment payment_method_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_method_id_fkey FOREIGN KEY (method_id) REFERENCES public.payment_method(method_id);


--
-- TOC entry 3299 (class 2606 OID 18724)
-- Name: seat seat_flight_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seat
    ADD CONSTRAINT seat_flight_id_fkey FOREIGN KEY (flight_id) REFERENCES public.flight(flight_id);


--
-- TOC entry 3302 (class 2606 OID 18776)
-- Name: ticket ticket_booking_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_booking_id_fkey FOREIGN KEY (booking_id) REFERENCES public.booking(booking_id);


--
-- TOC entry 3303 (class 2606 OID 18786)
-- Name: ticket ticket_flight_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_flight_id_fkey FOREIGN KEY (flight_id) REFERENCES public.flight(flight_id);


--
-- TOC entry 3305 (class 2606 OID 20220)
-- Name: ticket ticket_passenger_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_passenger_id_fkey FOREIGN KEY (passenger_id) REFERENCES public.passenger(passenger_id);


--
-- TOC entry 3304 (class 2606 OID 18791)
-- Name: ticket ticket_seat_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_seat_id_fkey FOREIGN KEY (seat_id) REFERENCES public.seat(seat_id);


--
-- TOC entry 3468 (class 0 OID 0)
-- Dependencies: 209
-- Name: TABLE airline; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.airline TO pg_database_owner WITH GRANT OPTION;


--
-- TOC entry 3469 (class 0 OID 0)
-- Dependencies: 210
-- Name: TABLE airplane; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.airplane TO pg_database_owner WITH GRANT OPTION;


--
-- TOC entry 3470 (class 0 OID 0)
-- Dependencies: 211
-- Name: TABLE airport; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.airport TO pg_database_owner WITH GRANT OPTION;


--
-- TOC entry 3471 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE booking; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.booking TO pg_database_owner WITH GRANT OPTION;


--
-- TOC entry 3472 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE booking_seats; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.booking_seats TO pg_database_owner WITH GRANT OPTION;


--
-- TOC entry 3473 (class 0 OID 0)
-- Dependencies: 212
-- Name: TABLE flight; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.flight TO pg_database_owner WITH GRANT OPTION;


--
-- TOC entry 3474 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE passenger; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.passenger TO pg_database_owner WITH GRANT OPTION;


--
-- TOC entry 3475 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE payment; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.payment TO pg_database_owner WITH GRANT OPTION;


--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE payment_method; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.payment_method TO pg_database_owner WITH GRANT OPTION;


--
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 213
-- Name: TABLE seat; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.seat TO pg_database_owner WITH GRANT OPTION;


--
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE ticket; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.ticket TO pg_database_owner WITH GRANT OPTION;


--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 214
-- Name: TABLE users; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.users TO pg_database_owner WITH GRANT OPTION;


-- Completed on 2024-11-26 19:47:48

--
-- PostgreSQL database dump complete
--

