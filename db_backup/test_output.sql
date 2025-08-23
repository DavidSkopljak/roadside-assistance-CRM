--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: case_state; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.case_state (
    id bigint NOT NULL,
    state text NOT NULL
);


ALTER TABLE public.case_state OWNER TO crmadmin;

--
-- Name: case_state_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.case_state ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.case_state_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: case_table; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.case_table (
    id bigint NOT NULL,
    location_id bigint NOT NULL,
    first_operator_id bigint NOT NULL,
    last_edited_operator_id bigint NOT NULL,
    client_vehicle_id bigint NOT NULL,
    damage_description text NOT NULL,
    case_state_id bigint NOT NULL,
    damage_type_id bigint NOT NULL,
    created_date_time timestamp without time zone NOT NULL,
    active_service_id bigint,
    client_id bigint NOT NULL,
    case_notes jsonb DEFAULT '[]'::jsonb,
    vehicle_damage_cause_id bigint NOT NULL
);


ALTER TABLE public.case_table OWNER TO crmadmin;

--
-- Name: case_table_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.case_table ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.case_table_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: client; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.client (
    id bigint NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    contact_number text NOT NULL
);


ALTER TABLE public.client OWNER TO crmadmin;

--
-- Name: client_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.client ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.client_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: driver; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.driver (
    id bigint NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    contact_number text NOT NULL,
    driver_state_id bigint NOT NULL,
    current_location_id bigint NOT NULL,
    vehicle_id bigint NOT NULL
);


ALTER TABLE public.driver OWNER TO crmadmin;

--
-- Name: driver_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.driver ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.driver_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: driver_state; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.driver_state (
    id bigint NOT NULL,
    state text NOT NULL
);


ALTER TABLE public.driver_state OWNER TO crmadmin;

--
-- Name: driver_state_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.driver_state ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.driver_state_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: location; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.location (
    id bigint NOT NULL,
    address text NOT NULL,
    city text NOT NULL,
    country text NOT NULL,
    postal_code text NOT NULL,
    coordinates_x numeric(10,6) NOT NULL,
    coordinates_y numeric(10,6) NOT NULL
);


ALTER TABLE public.location OWNER TO crmadmin;

--
-- Name: location_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.location ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: operator; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.operator (
    id bigint NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL
);


ALTER TABLE public.operator OWNER TO crmadmin;

--
-- Name: operator_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.operator ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.operator_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: service; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.service (
    id bigint NOT NULL,
    assigned_driver_id bigint,
    service_type_id bigint NOT NULL,
    service_state_id bigint NOT NULL,
    service_notes jsonb DEFAULT '[]'::jsonb
);


ALTER TABLE public.service OWNER TO crmadmin;

--
-- Name: service_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.service ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.service_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: service_state; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.service_state (
    id bigint NOT NULL,
    state text NOT NULL
);


ALTER TABLE public.service_state OWNER TO crmadmin;

--
-- Name: service_state_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.service_state ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.service_state_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: service_type; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.service_type (
    id bigint NOT NULL,
    type text NOT NULL
);


ALTER TABLE public.service_type OWNER TO crmadmin;

--
-- Name: service_type_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.service_type ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.service_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: vehicle; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.vehicle (
    id bigint NOT NULL,
    license_plate text NOT NULL,
    vehicle_model_id bigint NOT NULL,
    first_registration_date date,
    vin text NOT NULL,
    CONSTRAINT vehicle_vin_check CHECK ((length(vin) = 17))
);


ALTER TABLE public.vehicle OWNER TO crmadmin;

--
-- Name: vehicle_damage_cause; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.vehicle_damage_cause (
    id bigint NOT NULL,
    damage_cause text NOT NULL
);


ALTER TABLE public.vehicle_damage_cause OWNER TO crmadmin;

--
-- Name: vehicle_damage_cause_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.vehicle_damage_cause ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.vehicle_damage_cause_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: vehicle_damage_type; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.vehicle_damage_type (
    id bigint NOT NULL,
    damage_type text NOT NULL
);


ALTER TABLE public.vehicle_damage_type OWNER TO crmadmin;

--
-- Name: vehicle_damage_type_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.vehicle_damage_type ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.vehicle_damage_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: vehicle_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.vehicle ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.vehicle_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: vehicle_model; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.vehicle_model (
    id bigint NOT NULL,
    model text NOT NULL
);


ALTER TABLE public.vehicle_model OWNER TO crmadmin;

--
-- Name: vehicle_model_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.vehicle_model ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.vehicle_model_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: workshop; Type: TABLE; Schema: public; Owner: crmadmin
--

CREATE TABLE public.workshop (
    id bigint NOT NULL,
    name text NOT NULL,
    location_id bigint NOT NULL
);


ALTER TABLE public.workshop OWNER TO crmadmin;

--
-- Name: workshop_id_seq; Type: SEQUENCE; Schema: public; Owner: crmadmin
--

ALTER TABLE public.workshop ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.workshop_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: case_state; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.case_state (id, state) FROM stdin;
1	ACTIVE
2	RESOLVED
3	CANCELLED
\.


--
-- Data for Name: case_table; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.case_table (id, location_id, first_operator_id, last_edited_operator_id, client_vehicle_id, damage_description, case_state_id, damage_type_id, created_date_time, active_service_id, client_id, case_notes, vehicle_damage_cause_id) FROM stdin;
2	30	26	27	36	flat tire	1	2	2025-02-19 16:01:41.467325	7	17	[]	1
3	32	28	29	38	flat tire	1	2	2025-02-19 16:01:49.842796	8	18	[]	1
4	34	30	31	40	flat tire	1	2	2025-02-19 16:02:22.092793	9	19	[]	1
5	36	32	33	42	flat tire	1	2	2025-02-19 16:04:23.754281	10	20	[]	1
6	38	34	35	44	flat tire	1	2	2025-02-19 16:20:05.468296	11	21	[]	1
7	40	36	37	46	flat tire	1	2	2025-02-19 16:25:06.109198	12	22	[]	1
8	42	38	39	48	flat tire	1	2	2025-02-19 16:27:32.890586	13	23	[]	1
9	44	40	41	50	flat tire	1	2	2025-02-19 16:30:51.677817	14	24	[]	1
10	46	42	43	52	flat tire	1	2	2025-02-19 16:32:59.177625	15	25	[]	1
11	48	44	45	54	flat tire	1	2	2025-02-19 16:33:35.75844	16	26	[]	1
12	50	46	47	56	flat tire	1	2	2025-02-19 16:34:08.830931	17	27	[]	1
13	52	48	49	58	flat tire	1	2	2025-02-19 19:18:05	18	28	[]	1
35	96	92	93	102	flat tire	1	2	2025-02-19 21:13:26	40	50	[{"message": "new note!", "timestamp": "2025-02-19 20:13:26"}, {"message": "new note 2!", "timestamp": "2025-02-19 20:20:54"}, {"message": "new note 2!", "timestamp": "2025-02-19 20:22:11"}]	1
38	102	98	99	108	flat tire	1	2	2025-02-19 21:22:43	43	53	[]	1
14	54	50	51	60	flat tire	1	2	2025-02-19 19:37:05	19	29	[]	1
15	56	52	53	62	flat tire	1	2	2025-02-19 19:37:27	20	30	[]	1
16	58	54	55	64	flat tire	1	2	2025-02-19 19:37:37	21	31	[]	1
17	60	56	57	66	flat tire	1	2	2025-02-19 19:38:33	22	32	[]	1
18	62	58	59	68	flat tire	1	2	2025-02-19 19:39:00	23	33	[]	1
19	64	60	61	70	flat tire	1	2	2025-02-19 19:41:23	24	34	[]	1
20	66	62	63	72	flat tire	1	2	2025-02-19 19:41:48	25	35	[]	1
21	68	64	65	74	flat tire	1	2	2025-02-19 19:42:04	26	36	[]	1
22	70	66	67	76	flat tire	1	2	2025-02-19 19:49:32	27	37	[]	1
23	72	68	69	78	flat tire	1	2	2025-02-19 19:53:26	28	38	[]	1
24	74	70	71	80	flat tire	1	2	2025-02-19 19:55:28	29	39	[]	1
25	76	72	73	82	flat tire	1	2	2025-02-19 19:56:26	30	40	[]	1
26	78	74	75	84	flat tire	1	2	2025-02-19 19:56:59	31	41	[]	1
1	28	24	25	34	flat tire	1	2	2025-02-19 15:58:18.921878	6	16	[{"message": "This is a new note", "timestamp": "2025-02-19 15:58:18"}, {"message": "This is another new note", "timestamp": "2025-02-19 15:58:18"}, {"message": "This is another new note", "timestamp": "2025-02-19 15:58:18"}]	1
27	80	76	77	86	flat tire	1	2	2025-02-19 20:54:29	32	42	[]	1
28	82	78	79	88	flat tire	1	2	2025-02-19 20:55:48	33	43	[]	1
29	84	80	81	90	flat tire	1	2	2025-02-19 20:58:41	34	44	[]	1
30	86	82	83	92	flat tire	1	2	2025-02-19 20:59:25	35	45	[]	1
31	88	84	85	94	flat tire	1	2	2025-02-19 21:01:46	36	46	[{"id": -1, "message": "new note!", "timestamp": "2025-02-19 20:01:46"}]	1
32	90	86	87	96	flat tire	1	2	2025-02-19 21:06:09	37	47	[{"id": -1, "message": "new note!", "timestamp": "2025-02-19 20:06:09"}]	1
33	92	88	89	98	flat tire	1	2	2025-02-19 21:07:40	38	48	[{"message": "new note!", "timestamp": "2025-02-19 20:07:40"}]	1
34	94	90	91	100	flat tire	1	2	2025-02-19 21:13:23	39	49	[{"message": "new note!", "timestamp": "2025-02-19 20:13:23"}]	1
39	104	100	101	110	flat tire	1	2	2025-02-19 21:24:35	44	54	[]	1
36	98	94	95	104	flat tire	1	2	2025-02-19 21:20:54	41	51	[]	1
37	100	96	97	106	flat tire	1	2	2025-02-19 21:22:11	42	52	[]	1
40	106	102	103	112	flat tire	1	2	2025-02-19 21:25:33	45	55	[]	1
41	112	108	109	118	flat tire	1	2	2025-02-19 21:35:14	48	58	[]	1
42	114	110	111	120	flat tire	1	2	2025-02-19 21:36:26	49	59	[]	1
43	116	112	113	122	flat tire	1	2	2025-02-19 21:37:53	50	60	[]	1
44	118	114	115	124	flat tire	1	2	2025-02-19 23:52:06	51	61	[]	1
\.


--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.client (id, first_name, last_name, contact_number) FROM stdin;
1	testclient	testclient_lastname	112
2	test	test	123
3	test	test	123
4	test	test	123
5	test	test	123
6	test	test	123
7	test	test	123
8	test	test	123
9	test	test	123
10	test	test	123
11	test	test	123
12	test	test	123
13	testclient	testclient_lastname	112
14	testclient	testclient_lastname	112
15	testclient	testclient_lastname	112
16	testclient	testclient_lastname	112
17	testclient	testclient_lastname	112
18	testclient	testclient_lastname	112
19	testclient	testclient_lastname	112
20	testclient	testclient_lastname	112
21	testclient	testclient_lastname	112
22	testclient	testclient_lastname	112
23	testclient	testclient_lastname	112
24	testclient	testclient_lastname	112
25	testclient	testclient_lastname	112
26	testclient	testclient_lastname	112
27	testclient	testclient_lastname	112
28	testclient	testclient_lastname	112
29	testclient	testclient_lastname	112
30	testclient	testclient_lastname	112
31	testclient	testclient_lastname	112
32	testclient	testclient_lastname	112
33	testclient	testclient_lastname	112
34	testclient	testclient_lastname	112
35	testclient	testclient_lastname	112
36	testclient	testclient_lastname	112
37	testclient	testclient_lastname	112
38	testclient	testclient_lastname	112
39	testclient	testclient_lastname	112
40	testclient	testclient_lastname	112
41	testclient	testclient_lastname	112
42	testclient	testclient_lastname	112
43	testclient	testclient_lastname	112
44	testclient	testclient_lastname	112
45	testclient	testclient_lastname	112
46	testclient	testclient_lastname	112
47	testclient	testclient_lastname	112
48	testclient	testclient_lastname	112
49	testclient	testclient_lastname	112
50	testclient	testclient_lastname	112
51	testclient	testclient_lastname	112
52	testclient	testclient_lastname	112
53	testclient	testclient_lastname	112
54	testclient	testclient_lastname	112
55	testclient	testclient_lastname	112
56	testclient	testclient_lastname	112
57	testclient	testclient_lastname	112
58	testclient	testclient_lastname	112
59	testclient	testclient_lastname	112
60	testclient	testclient_lastname	112
61	testclient	testclient_lastname	112
\.


--
-- Data for Name: driver; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.driver (id, first_name, last_name, contact_number, driver_state_id, current_location_id, vehicle_id) FROM stdin;
3	vozko	vojkovic	192	1	1	16
4	vozko	vojkovic	192	1	2	17
5	vozko	vojkovic	192	1	3	18
6	vozko	vojkovic	192	1	4	19
7	vozko	vojkovic	192	1	5	20
8	vozko	vojkovic	192	1	23	29
9	vozko	vojkovic	192	1	25	31
10	vozko	vojkovic	192	1	27	33
11	vozko	vojkovic	192	1	29	35
12	vozko	vojkovic	192	1	31	37
13	vozko	vojkovic	192	1	33	39
14	vozko	vojkovic	192	1	35	41
15	vozko	vojkovic	192	1	37	43
16	vozko	vojkovic	192	1	39	45
17	vozko	vojkovic	192	1	41	47
18	vozko	vojkovic	192	1	43	49
19	vozko	vojkovic	192	1	45	51
20	vozko	vojkovic	192	1	47	53
21	vozko	vojkovic	192	1	49	55
22	vozko	vojkovic	192	1	51	57
23	vozko	vojkovic	192	1	53	59
24	vozko	vojkovic	192	1	55	61
25	vozko	vojkovic	192	1	57	63
26	vozko	vojkovic	192	1	59	65
27	vozko	vojkovic	192	1	61	67
28	vozko	vojkovic	192	1	63	69
29	vozko	vojkovic	192	1	65	71
30	vozko	vojkovic	192	1	67	73
31	vozko	vojkovic	192	1	69	75
32	vozko	vojkovic	192	1	71	77
33	vozko	vojkovic	192	1	73	79
34	vozko	vojkovic	192	1	75	81
35	vozko	vojkovic	192	1	77	83
36	vozko	vojkovic	192	1	79	85
37	vozko	vojkovic	192	1	81	87
38	vozko	vojkovic	192	1	83	89
39	vozko	vojkovic	192	1	85	91
40	vozko	vojkovic	192	1	87	93
41	vozko	vojkovic	192	1	89	95
42	vozko	vojkovic	192	1	91	97
43	vozko	vojkovic	192	1	93	99
44	vozko	vojkovic	192	1	95	101
45	vozko	vojkovic	192	1	97	103
46	vozko	vojkovic	192	1	99	105
47	vozko	vojkovic	192	1	101	107
48	vozko	vojkovic	192	1	103	109
49	vozko	vojkovic	192	1	105	111
50	vozko	vojkovic	192	1	107	113
51	vozko	vojkovic	192	1	109	115
52	vozko	vojkovic	192	1	111	117
53	vozko	vojkovic	192	1	113	119
54	vozko	vojkovic	192	1	115	121
55	vozko	vojkovic	192	1	117	123
56	vozko	vojkovic	192	1	119	125
\.


--
-- Data for Name: driver_state; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.driver_state (id, state) FROM stdin;
1	AVAILABLE
2	BUSY
3	UNAVAILABLE
\.


--
-- Data for Name: location; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.location (id, address, city, country, postal_code, coordinates_x, coordinates_y) FROM stdin;
1	address	city	country	postalCode	45.808977	15.716705
2	address	city	country	postalCode	45.808977	15.716705
3	address	city	country	postalCode	45.808977	15.716705
4	address	city	country	postalCode	45.808977	15.716705
5	address	city	country	postalCode	45.808977	15.716705
6	address	city	country	postalCode	45.808977	15.716705
7	address	city	country	postalCode	45.808977	15.716705
8	address	city	country	postalCode	45.808977	15.716705
9	address	city	country	postalCode	45.808977	15.716705
10	address	city	country	postalCode	45.808977	15.716705
11	address	city	country	postalCode	45.808977	15.716705
12	address	city	country	postalCode	45.808977	15.716705
13	address	city	country	postalCode	45.808977	15.716705
14	address	city	country	postalCode	45.808977	15.716705
15	address	city	country	postalCode	45.808977	15.716705
16	address	city	country	postalCode	45.808977	15.716705
17	address	city	country	postalCode	45.808977	15.716705
18	address	city	country	postalCode	45.808977	15.716705
19	address	city	country	postalCode	45.808977	15.716705
20	address	city	country	postalCode	45.808977	15.716705
21	address	city	country	postalCode	45.808977	15.716705
22	address	city	country	postalCode	45.808977	15.716705
23	address	city	country	postalCode	45.808977	15.716705
24	address	city	country	postalCode	45.808977	15.716705
25	address	city	country	postalCode	45.808977	15.716705
26	address	city	country	postalCode	45.808977	15.716705
27	address	city	country	postalCode	45.808977	15.716705
28	address	city	country	postalCode	45.808977	15.716705
29	address	city	country	postalCode	45.808977	15.716705
30	address	city	country	postalCode	45.808977	15.716705
31	address	city	country	postalCode	45.808977	15.716705
32	address	city	country	postalCode	45.808977	15.716705
33	address	city	country	postalCode	45.808977	15.716705
34	address	city	country	postalCode	45.808977	15.716705
35	address	city	country	postalCode	45.808977	15.716705
36	address	city	country	postalCode	45.808977	15.716705
37	address	city	country	postalCode	45.808977	15.716705
38	address	city	country	postalCode	45.808977	15.716705
39	address	city	country	postalCode	45.808977	15.716705
40	address	city	country	postalCode	45.808977	15.716705
41	address	city	country	postalCode	45.808977	15.716705
42	address	city	country	postalCode	45.808977	15.716705
43	address	city	country	postalCode	45.808977	15.716705
44	address	city	country	postalCode	45.808977	15.716705
45	address	city	country	postalCode	45.808977	15.716705
46	address	city	country	postalCode	45.808977	15.716705
47	address	city	country	postalCode	45.808977	15.716705
48	address	city	country	postalCode	45.808977	15.716705
49	address	city	country	postalCode	45.808977	15.716705
50	address	city	country	postalCode	45.808977	15.716705
51	address	city	country	postalCode	45.808977	15.716705
52	address	city	country	postalCode	45.808977	15.716705
53	address	city	country	postalCode	45.808977	15.716705
54	address	city	country	postalCode	45.808977	15.716705
55	address	city	country	postalCode	45.808977	15.716705
56	address	city	country	postalCode	45.808977	15.716705
57	address	city	country	postalCode	45.808977	15.716705
58	address	city	country	postalCode	45.808977	15.716705
59	address	city	country	postalCode	45.808977	15.716705
60	address	city	country	postalCode	45.808977	15.716705
61	address	city	country	postalCode	45.808977	15.716705
62	address	city	country	postalCode	45.808977	15.716705
63	address	city	country	postalCode	45.808977	15.716705
64	address	city	country	postalCode	45.808977	15.716705
65	address	city	country	postalCode	45.808977	15.716705
66	address	city	country	postalCode	45.808977	15.716705
67	address	city	country	postalCode	45.808977	15.716705
68	address	city	country	postalCode	45.808977	15.716705
69	address	city	country	postalCode	45.808977	15.716705
70	address	city	country	postalCode	45.808977	15.716705
71	address	city	country	postalCode	45.808977	15.716705
72	address	city	country	postalCode	45.808977	15.716705
73	address	city	country	postalCode	45.808977	15.716705
74	address	city	country	postalCode	45.808977	15.716705
75	address	city	country	postalCode	45.808977	15.716705
76	address	city	country	postalCode	45.808977	15.716705
77	address	city	country	postalCode	45.808977	15.716705
78	address	city	country	postalCode	45.808977	15.716705
79	address	city	country	postalCode	45.808977	15.716705
80	address	city	country	postalCode	45.808977	15.716705
81	address	city	country	postalCode	45.808977	15.716705
82	address	city	country	postalCode	45.808977	15.716705
83	address	city	country	postalCode	45.808977	15.716705
84	address	city	country	postalCode	45.808977	15.716705
85	address	city	country	postalCode	45.808977	15.716705
86	address	city	country	postalCode	45.808977	15.716705
87	address	city	country	postalCode	45.808977	15.716705
88	address	city	country	postalCode	45.808977	15.716705
89	address	city	country	postalCode	45.808977	15.716705
90	address	city	country	postalCode	45.808977	15.716705
91	address	city	country	postalCode	45.808977	15.716705
92	address	city	country	postalCode	45.808977	15.716705
93	address	city	country	postalCode	45.808977	15.716705
94	address	city	country	postalCode	45.808977	15.716705
95	address	city	country	postalCode	45.808977	15.716705
96	address	city	country	postalCode	45.808977	15.716705
97	address	city	country	postalCode	45.808977	15.716705
98	address	city	country	postalCode	45.808977	15.716705
99	address	city	country	postalCode	45.808977	15.716705
100	address	city	country	postalCode	45.808977	15.716705
101	address	city	country	postalCode	45.808977	15.716705
102	address	city	country	postalCode	45.808977	15.716705
103	address	city	country	postalCode	45.808977	15.716705
104	address	city	country	postalCode	45.808977	15.716705
105	address	city	country	postalCode	45.808977	15.716705
106	address	city	country	postalCode	45.808977	15.716705
107	address	city	country	postalCode	45.808977	15.716705
108	address	city	country	postalCode	45.808977	15.716705
109	address	city	country	postalCode	45.808977	15.716705
110	address	city	country	postalCode	45.808977	15.716705
111	address	city	country	postalCode	45.808977	15.716705
112	address	city	country	postalCode	45.808977	15.716705
113	address	city	country	postalCode	45.808977	15.716705
114	address	city	country	postalCode	45.808977	15.716705
115	address	city	country	postalCode	45.808977	15.716705
116	address	city	country	postalCode	45.808977	15.716705
117	address	city	country	postalCode	45.808977	15.716705
118	address	city	country	postalCode	45.808977	15.716705
119	address	city	country	postalCode	45.808977	15.716705
\.


--
-- Data for Name: operator; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.operator (id, first_name, last_name) FROM stdin;
1	operatorko	operatorovic
2	marko	marulic
3	marko	marulic
4	First Operator	First Operator
5	First Operator	First Operator
6	First Operator	First Operator
7	First Operator	First Operator
8	First Operator	First Operator
9	First Operator	First Operator
10	First Operator	First Operator
11	First Operator	First Operator
12	First Operator	First Operator
13	First Operator	First Operator
14	First Operator	First Operator
15	First Operator	First Operator
16	First Operator	First Operator
17	First Operator	First Operator
18	First Operator	First Operator
19	First Operator	First Operator
20	First Operator	First Operator
21	First Operator	First Operator
22	First Operator	First Operator
23	First Operator	First Operator
24	First Operator	First Operator
25	First Operator	First Operator
26	First Operator	First Operator
27	First Operator	First Operator
28	First Operator	First Operator
29	First Operator	First Operator
30	First Operator	First Operator
31	First Operator	First Operator
32	First Operator	First Operator
33	First Operator	First Operator
34	First Operator	First Operator
35	First Operator	First Operator
36	First Operator	First Operator
37	First Operator	First Operator
38	First Operator	First Operator
39	First Operator	First Operator
40	First Operator	First Operator
41	First Operator	First Operator
42	First Operator	First Operator
43	First Operator	First Operator
44	First Operator	First Operator
45	First Operator	First Operator
46	First Operator	First Operator
47	First Operator	First Operator
48	First Operator	First Operator
49	First Operator	First Operator
50	First Operator	First Operator
51	First Operator	First Operator
52	First Operator	First Operator
53	First Operator	First Operator
54	First Operator	First Operator
55	First Operator	First Operator
56	First Operator	First Operator
57	First Operator	First Operator
58	First Operator	First Operator
59	First Operator	First Operator
60	First Operator	First Operator
61	First Operator	First Operator
62	First Operator	First Operator
63	First Operator	First Operator
64	First Operator	First Operator
65	First Operator	First Operator
66	First Operator	First Operator
67	First Operator	First Operator
68	First Operator	First Operator
69	First Operator	First Operator
70	First Operator	First Operator
71	First Operator	First Operator
72	First Operator	First Operator
73	First Operator	First Operator
74	First Operator	First Operator
75	First Operator	First Operator
76	First Operator	First Operator
77	First Operator	First Operator
78	First Operator	First Operator
79	First Operator	First Operator
80	First Operator	First Operator
81	First Operator	First Operator
82	First Operator	First Operator
83	First Operator	First Operator
84	First Operator	First Operator
85	First Operator	First Operator
86	First Operator	First Operator
87	First Operator	First Operator
88	First Operator	First Operator
89	First Operator	First Operator
90	First Operator	First Operator
91	First Operator	First Operator
92	First Operator	First Operator
93	First Operator	First Operator
94	First Operator	First Operator
95	First Operator	First Operator
96	First Operator	First Operator
97	First Operator	First Operator
98	First Operator	First Operator
99	First Operator	First Operator
100	First Operator	First Operator
101	First Operator	First Operator
102	First Operator	First Operator
103	First Operator	First Operator
104	First Operator	First Operator
105	First Operator	First Operator
106	First Operator	First Operator
107	First Operator	First Operator
108	First Operator	First Operator
109	First Operator	First Operator
110	First Operator	First Operator
111	First Operator	First Operator
112	First Operator	First Operator
113	First Operator	First Operator
114	First Operator	First Operator
115	First Operator	First Operator
\.


--
-- Data for Name: service; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.service (id, assigned_driver_id, service_type_id, service_state_id, service_notes) FROM stdin;
1	6	1	1	[]
2	7	1	1	[]
3	8	1	1	[]
4	9	1	1	[]
5	10	1	1	[]
6	11	1	1	[]
7	12	1	1	[]
8	13	1	1	[]
9	14	1	1	[]
10	15	1	1	[]
11	16	1	1	[]
12	17	1	1	[]
13	18	1	1	[]
14	19	1	1	[]
15	20	1	1	[]
16	21	1	1	[]
17	22	1	1	[]
18	23	1	1	[]
19	24	1	1	[]
20	25	1	1	[]
21	26	1	1	[]
22	27	1	1	[]
23	28	1	1	[]
24	29	1	1	[]
25	30	1	1	[]
26	31	1	1	[]
27	32	1	1	[]
28	33	1	1	[]
29	34	1	1	[]
30	35	1	1	[]
31	36	1	1	[]
32	37	1	1	[]
33	38	1	1	[]
34	39	1	1	[]
35	40	1	1	[]
36	41	1	1	[]
37	42	1	1	[]
38	43	1	1	[]
39	44	1	1	[]
40	45	1	1	[]
41	46	1	1	[]
42	47	1	1	[]
43	48	1	1	[]
44	49	1	1	[]
45	50	1	1	[]
46	51	1	1	[]
47	52	1	1	[]
48	53	1	1	[]
49	54	1	1	[]
50	55	1	1	[]
51	56	1	1	[]
\.


--
-- Data for Name: service_state; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.service_state (id, state) FROM stdin;
1	ASSIGNED
2	IN_PROGRESS
3	FINISHED
4	CANCELLED
\.


--
-- Data for Name: service_type; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.service_type (id, type) FROM stdin;
1	TOWING
2	TAXI
3	REPAIR
\.


--
-- Data for Name: vehicle; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.vehicle (id, license_plate, vehicle_model_id, first_registration_date, vin) FROM stdin;
1	TESTPLATE	13	\N	11111111111111111
2	TESTPLATE2	11	\N	11111111111111112
3	TESTPLATE2	11	\N	11111111111111112
4	TESTPLATE2	11	\N	11111111111111112
5	TESTPLATE2	11	\N	11111111111111112
6	TESTPLATE2	11	\N	11111111111111112
7	TESTPLATE2	11	\N	11111111111111112
8	TESTPLATE2	11	\N	11111111111111112
9	TESTPLATE2	11	\N	11111111111111112
10	TESTPLATE2	11	\N	11111111111111112
11	TESTPLATE2	11	\N	11111111111111112
12	TESTPLATE2	11	\N	11111111111111112
13	licensePlate	27	\N	11111111111111111
14	licensePlate	27	\N	11111111111111111
15	licensePlate	27	\N	11111111111111111
16	licensePlate	27	\N	11111111111111111
17	licensePlate	27	\N	11111111111111111
18	licensePlate	27	\N	11111111111111111
19	licensePlate	27	\N	11111111111111111
20	licensePlate	27	\N	11111111111111111
21	TESTPLATE	13	\N	11111111111111111
22	TESTPLATE	13	\N	11111111111111111
23	TESTPLATE	13	\N	11111111111111111
24	TESTPLATE	13	\N	11111111111111111
25	TESTPLATE	13	\N	11111111111111111
26	TESTPLATE	13	\N	11111111111111111
27	TESTPLATE	13	\N	11111111111111111
28	TESTPLATE	13	\N	11111111111111111
29	licensePlate	27	\N	11111111111111111
30	TESTPLATE	13	\N	11111111111111111
31	licensePlate	27	\N	11111111111111111
32	TESTPLATE	13	\N	11111111111111111
33	licensePlate	27	\N	11111111111111111
34	TESTPLATE	13	\N	11111111111111111
35	licensePlate	27	\N	11111111111111111
36	TESTPLATE	13	\N	11111111111111111
37	licensePlate	27	\N	11111111111111111
38	TESTPLATE	13	\N	11111111111111111
39	licensePlate	27	\N	11111111111111111
40	TESTPLATE	13	\N	11111111111111111
41	licensePlate	27	\N	11111111111111111
42	TESTPLATE	13	\N	11111111111111111
43	licensePlate	27	\N	11111111111111111
44	TESTPLATE	13	\N	11111111111111111
45	licensePlate	27	\N	11111111111111111
46	TESTPLATE	13	\N	11111111111111111
47	licensePlate	27	\N	11111111111111111
48	TESTPLATE	13	\N	11111111111111111
49	licensePlate	27	\N	11111111111111111
50	TESTPLATE	13	\N	11111111111111111
51	licensePlate	27	\N	11111111111111111
52	TESTPLATE	13	\N	11111111111111111
53	licensePlate	27	\N	11111111111111111
54	TESTPLATE	13	\N	11111111111111111
55	licensePlate	27	\N	11111111111111111
56	TESTPLATE	13	\N	11111111111111111
57	licensePlate	27	\N	11111111111111111
58	TESTPLATE	13	\N	11111111111111111
59	licensePlate	27	\N	11111111111111111
60	TESTPLATE	13	\N	11111111111111111
61	licensePlate	27	\N	11111111111111111
62	TESTPLATE	13	\N	11111111111111111
63	licensePlate	27	\N	11111111111111111
64	TESTPLATE	13	\N	11111111111111111
65	licensePlate	27	\N	11111111111111111
66	TESTPLATE	13	\N	11111111111111111
67	licensePlate	27	\N	11111111111111111
68	TESTPLATE	13	\N	11111111111111111
69	licensePlate	27	\N	11111111111111111
70	TESTPLATE	13	\N	11111111111111111
71	licensePlate	27	\N	11111111111111111
72	TESTPLATE	13	\N	11111111111111111
73	licensePlate	27	\N	11111111111111111
74	TESTPLATE	13	\N	11111111111111111
75	licensePlate	27	\N	11111111111111111
76	TESTPLATE	13	\N	11111111111111111
77	licensePlate	27	\N	11111111111111111
78	TESTPLATE	13	\N	11111111111111111
79	licensePlate	27	\N	11111111111111111
80	TESTPLATE	13	\N	11111111111111111
81	licensePlate	27	\N	11111111111111111
82	TESTPLATE	13	\N	11111111111111111
83	licensePlate	27	\N	11111111111111111
84	TESTPLATE	13	\N	11111111111111111
85	licensePlate	27	\N	11111111111111111
86	TESTPLATE	13	\N	11111111111111111
87	licensePlate	27	\N	11111111111111111
88	TESTPLATE	13	\N	11111111111111111
89	licensePlate	27	\N	11111111111111111
90	TESTPLATE	13	\N	11111111111111111
91	licensePlate	27	\N	11111111111111111
92	TESTPLATE	13	\N	11111111111111111
93	licensePlate	27	\N	11111111111111111
94	TESTPLATE	13	\N	11111111111111111
95	licensePlate	27	\N	11111111111111111
96	TESTPLATE	13	\N	11111111111111111
97	licensePlate	27	\N	11111111111111111
98	TESTPLATE	13	\N	11111111111111111
99	licensePlate	27	\N	11111111111111111
100	TESTPLATE	13	\N	11111111111111111
101	licensePlate	27	\N	11111111111111111
102	TESTPLATE	13	\N	11111111111111111
103	licensePlate	27	\N	11111111111111111
104	TESTPLATE	13	\N	11111111111111111
105	licensePlate	27	\N	11111111111111111
106	TESTPLATE	13	\N	11111111111111111
107	licensePlate	27	\N	11111111111111111
108	TESTPLATE	13	\N	11111111111111111
109	licensePlate	27	\N	11111111111111111
110	TESTPLATE	13	\N	11111111111111111
111	licensePlate	27	\N	11111111111111111
112	TESTPLATE	13	\N	11111111111111111
113	licensePlate	27	\N	11111111111111111
114	TESTPLATE	13	\N	11111111111111111
115	licensePlate	27	\N	11111111111111111
116	TESTPLATE	13	\N	11111111111111111
117	licensePlate	27	\N	11111111111111111
118	TESTPLATE	13	\N	11111111111111111
119	licensePlate	27	\N	11111111111111111
120	TESTPLATE	13	\N	11111111111111111
121	licensePlate	27	\N	11111111111111111
122	TESTPLATE	13	\N	11111111111111111
123	licensePlate	27	\N	11111111111111111
124	TESTPLATE	13	\N	11111111111111111
125	licensePlate	27	\N	11111111111111111
\.


--
-- Data for Name: vehicle_damage_cause; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.vehicle_damage_cause (id, damage_cause) FROM stdin;
1	ACCIDENT
2	CAUSED_BY_CLIENT
3	BREAKDOWN
4	VANDALISM
\.


--
-- Data for Name: vehicle_damage_type; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.vehicle_damage_type (id, damage_type) FROM stdin;
1	ENGINE
2	TIRE
3	BATTERY
4	CHASSIS
5	BRAKE
6	FUEL
\.


--
-- Data for Name: vehicle_model; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.vehicle_model (id, model) FROM stdin;
1	TOYOTA
2	VOLKSWAGEN
3	HONDA
4	FORD
5	HYUNDAI
6	NISSAN
7	SUZUKI
8	KIA
9	CHEVROLET
10	BYD
11	BMW
12	MERCEDES_BENZ
13	AUDI
14	TESLA
15	RENAULT
16	FIAT
17	MAZDA
18	PEUGEOT
19	CHANGAN
20	JEEP
21	GEELY
22	TATA
23	SUBARU
24	WULING
25	GAC
26	SKODA
27	CHERY
28	DAIHATSU
29	VOLVO
30	BUICK
31	GMC
32	MITSUBISHI
33	HAVAL
34	CITROEN
35	RAM
36	PERODUA
37	OPEL
38	DACIA
39	MG
40	LEXUS
41	SEAT
\.


--
-- Data for Name: workshop; Type: TABLE DATA; Schema: public; Owner: crmadmin
--

COPY public.workshop (id, name, location_id) FROM stdin;
1	workshop_test	12
2	workshop_test	13
3	workshop_test	14
\.


--
-- Name: case_state_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.case_state_id_seq', 3, true);


--
-- Name: case_table_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.case_table_id_seq', 44, true);


--
-- Name: client_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.client_id_seq', 61, true);


--
-- Name: driver_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.driver_id_seq', 56, true);


--
-- Name: driver_state_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.driver_state_id_seq', 3, true);


--
-- Name: location_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.location_id_seq', 119, true);


--
-- Name: operator_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.operator_id_seq', 115, true);


--
-- Name: service_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.service_id_seq', 51, true);


--
-- Name: service_state_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.service_state_id_seq', 4, true);


--
-- Name: service_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.service_type_id_seq', 3, true);


--
-- Name: vehicle_damage_cause_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.vehicle_damage_cause_id_seq', 4, true);


--
-- Name: vehicle_damage_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.vehicle_damage_type_id_seq', 6, true);


--
-- Name: vehicle_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.vehicle_id_seq', 125, true);


--
-- Name: vehicle_model_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.vehicle_model_id_seq', 41, true);


--
-- Name: workshop_id_seq; Type: SEQUENCE SET; Schema: public; Owner: crmadmin
--

SELECT pg_catalog.setval('public.workshop_id_seq', 3, true);


--
-- Name: case_state case_state_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.case_state
    ADD CONSTRAINT case_state_pkey PRIMARY KEY (id);


--
-- Name: case_table case_table_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.case_table
    ADD CONSTRAINT case_table_pkey PRIMARY KEY (id);


--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);


--
-- Name: driver driver_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.driver
    ADD CONSTRAINT driver_pkey PRIMARY KEY (id);


--
-- Name: driver_state driver_state_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.driver_state
    ADD CONSTRAINT driver_state_pkey PRIMARY KEY (id);


--
-- Name: location location_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_pkey PRIMARY KEY (id);


--
-- Name: operator operator_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.operator
    ADD CONSTRAINT operator_pkey PRIMARY KEY (id);


--
-- Name: service service_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT service_pkey PRIMARY KEY (id);


--
-- Name: service_state service_state_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.service_state
    ADD CONSTRAINT service_state_pkey PRIMARY KEY (id);


--
-- Name: service_type service_type_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.service_type
    ADD CONSTRAINT service_type_pkey PRIMARY KEY (id);


--
-- Name: vehicle_damage_cause vehicle_damage_cause_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.vehicle_damage_cause
    ADD CONSTRAINT vehicle_damage_cause_pkey PRIMARY KEY (id);


--
-- Name: vehicle_damage_type vehicle_damage_type_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.vehicle_damage_type
    ADD CONSTRAINT vehicle_damage_type_pkey PRIMARY KEY (id);


--
-- Name: vehicle_model vehicle_model_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.vehicle_model
    ADD CONSTRAINT vehicle_model_pkey PRIMARY KEY (id);


--
-- Name: vehicle vehicle_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.vehicle
    ADD CONSTRAINT vehicle_pkey PRIMARY KEY (id);


--
-- Name: workshop workshop_pkey; Type: CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.workshop
    ADD CONSTRAINT workshop_pkey PRIMARY KEY (id);


--
-- Name: service fk_assigned_driver; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT fk_assigned_driver FOREIGN KEY (assigned_driver_id) REFERENCES public.driver(id) ON DELETE SET NULL;


--
-- Name: case_table fk_case_active_service; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.case_table
    ADD CONSTRAINT fk_case_active_service FOREIGN KEY (active_service_id) REFERENCES public.service(id) ON DELETE SET NULL;


--
-- Name: case_table fk_case_client_vehicle; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.case_table
    ADD CONSTRAINT fk_case_client_vehicle FOREIGN KEY (client_vehicle_id) REFERENCES public.vehicle(id) ON DELETE SET NULL;


--
-- Name: case_table fk_case_damage_type; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.case_table
    ADD CONSTRAINT fk_case_damage_type FOREIGN KEY (damage_type_id) REFERENCES public.vehicle_damage_type(id) ON DELETE SET NULL;


--
-- Name: case_table fk_case_first_operator; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.case_table
    ADD CONSTRAINT fk_case_first_operator FOREIGN KEY (first_operator_id) REFERENCES public.operator(id) ON DELETE SET NULL;


--
-- Name: case_table fk_case_last_edited_operator; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.case_table
    ADD CONSTRAINT fk_case_last_edited_operator FOREIGN KEY (last_edited_operator_id) REFERENCES public.operator(id) ON DELETE SET NULL;


--
-- Name: case_table fk_case_location; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.case_table
    ADD CONSTRAINT fk_case_location FOREIGN KEY (location_id) REFERENCES public.location(id) ON DELETE SET NULL;


--
-- Name: case_table fk_case_state; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.case_table
    ADD CONSTRAINT fk_case_state FOREIGN KEY (case_state_id) REFERENCES public.case_state(id) ON DELETE SET NULL;


--
-- Name: case_table fk_client; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.case_table
    ADD CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- Name: driver fk_driver_location; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.driver
    ADD CONSTRAINT fk_driver_location FOREIGN KEY (current_location_id) REFERENCES public.location(id) ON DELETE SET NULL;


--
-- Name: driver fk_driver_state; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.driver
    ADD CONSTRAINT fk_driver_state FOREIGN KEY (driver_state_id) REFERENCES public.driver_state(id) ON DELETE SET NULL;


--
-- Name: driver fk_driver_vehicle; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.driver
    ADD CONSTRAINT fk_driver_vehicle FOREIGN KEY (vehicle_id) REFERENCES public.vehicle(id) ON DELETE SET NULL;


--
-- Name: workshop fk_location; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.workshop
    ADD CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES public.location(id);


--
-- Name: service fk_service_state; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT fk_service_state FOREIGN KEY (service_state_id) REFERENCES public.service_state(id) ON DELETE SET NULL;


--
-- Name: service fk_service_type; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.service
    ADD CONSTRAINT fk_service_type FOREIGN KEY (service_type_id) REFERENCES public.service_type(id) ON DELETE SET NULL;


--
-- Name: case_table fk_vehicle_damage_cause; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.case_table
    ADD CONSTRAINT fk_vehicle_damage_cause FOREIGN KEY (vehicle_damage_cause_id) REFERENCES public.vehicle_damage_cause(id) ON DELETE SET NULL;


--
-- Name: vehicle fk_vehicle_model; Type: FK CONSTRAINT; Schema: public; Owner: crmadmin
--

ALTER TABLE ONLY public.vehicle
    ADD CONSTRAINT fk_vehicle_model FOREIGN KEY (vehicle_model_id) REFERENCES public.vehicle_model(id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: pg_database_owner
--

GRANT ALL ON SCHEMA public TO crmadmin;


--
-- PostgreSQL database dump complete
--

