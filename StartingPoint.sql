--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

-- Started on 2024-01-10 00:32:03

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
-- TOC entry 5 (class 2615 OID 41523)
-- Name: v1; Type: SCHEMA; Schema: -; Owner: Admin
--

CREATE SCHEMA v1;


ALTER SCHEMA v1 OWNER TO "Admin";

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 41524)
-- Name: authorities; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.authorities (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    CONSTRAINT "Authorities_name_check" CHECK (((name)::text <> ''::text))
);


ALTER TABLE v1.authorities OWNER TO "Admin";

--
-- TOC entry 216 (class 1259 OID 41528)
-- Name: Authorities_id_seq; Type: SEQUENCE; Schema: v1; Owner: Admin
--

CREATE SEQUENCE v1."Authorities_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE v1."Authorities_id_seq" OWNER TO "Admin";

--
-- TOC entry 4915 (class 0 OID 0)
-- Dependencies: 216
-- Name: Authorities_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Authorities_id_seq" OWNED BY v1.authorities.id;


--
-- TOC entry 217 (class 1259 OID 41529)
-- Name: business_units; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.business_units (
    id integer NOT NULL,
    name character varying(150) NOT NULL,
    type integer NOT NULL,
    companies_id integer,
    projects_id integer,
    whiteboards_id integer,
    CONSTRAINT "BusinessUnits_name_check" CHECK (((name)::text <> ''::text)),
    CONSTRAINT "BusinessUnits_type_check" CHECK (((type)::text <> ''::text))
);


ALTER TABLE v1.business_units OWNER TO "Admin";

--
-- TOC entry 218 (class 1259 OID 41534)
-- Name: BusinessUnits_id_seq; Type: SEQUENCE; Schema: v1; Owner: Admin
--

CREATE SEQUENCE v1."BusinessUnits_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE v1."BusinessUnits_id_seq" OWNER TO "Admin";

--
-- TOC entry 4918 (class 0 OID 0)
-- Dependencies: 218
-- Name: BusinessUnits_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."BusinessUnits_id_seq" OWNED BY v1.business_units.id;


--
-- TOC entry 219 (class 1259 OID 41535)
-- Name: columns; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.columns (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    "position" integer NOT NULL,
    whiteboards_id integer NOT NULL
);


ALTER TABLE v1.columns OWNER TO "Admin";

--
-- TOC entry 220 (class 1259 OID 41538)
-- Name: Columns_id_seq; Type: SEQUENCE; Schema: v1; Owner: Admin
--

CREATE SEQUENCE v1."Columns_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE v1."Columns_id_seq" OWNER TO "Admin";

--
-- TOC entry 4921 (class 0 OID 0)
-- Dependencies: 220
-- Name: Columns_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Columns_id_seq" OWNED BY v1.columns.id;


--
-- TOC entry 221 (class 1259 OID 41539)
-- Name: invites; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.invites (
    id integer NOT NULL,
    state integer NOT NULL,
    receiver_id integer NOT NULL,
    business_units_id integer NOT NULL,
    CONSTRAINT "Invites_state_check" CHECK (((state)::text <> ''::text))
);


ALTER TABLE v1.invites OWNER TO "Admin";

--
-- TOC entry 222 (class 1259 OID 41543)
-- Name: Invites_id_seq; Type: SEQUENCE; Schema: v1; Owner: Admin
--

CREATE SEQUENCE v1."Invites_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE v1."Invites_id_seq" OWNER TO "Admin";

--
-- TOC entry 4924 (class 0 OID 0)
-- Dependencies: 222
-- Name: Invites_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Invites_id_seq" OWNED BY v1.invites.id;


--
-- TOC entry 223 (class 1259 OID 41544)
-- Name: notes; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.notes (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(500) NOT NULL,
    "position" integer NOT NULL,
    columns_id integer NOT NULL,
    CONSTRAINT "Notes_name_check" CHECK (((name)::text <> ''::text))
);


ALTER TABLE v1.notes OWNER TO "Admin";

--
-- TOC entry 224 (class 1259 OID 41550)
-- Name: Notes_id_seq; Type: SEQUENCE; Schema: v1; Owner: Admin
--

CREATE SEQUENCE v1."Notes_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE v1."Notes_id_seq" OWNER TO "Admin";

--
-- TOC entry 4927 (class 0 OID 0)
-- Dependencies: 224
-- Name: Notes_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Notes_id_seq" OWNED BY v1.notes.id;


--
-- TOC entry 225 (class 1259 OID 41551)
-- Name: roles; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.roles (
    id integer NOT NULL,
    name character varying(150) NOT NULL,
    business_units_id integer NOT NULL,
    CONSTRAINT "Roles_name_check" CHECK (((name)::text <> ''::text))
);


ALTER TABLE v1.roles OWNER TO "Admin";

--
-- TOC entry 226 (class 1259 OID 41555)
-- Name: Roles_id_seq; Type: SEQUENCE; Schema: v1; Owner: Admin
--

CREATE SEQUENCE v1."Roles_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE v1."Roles_id_seq" OWNER TO "Admin";

--
-- TOC entry 4930 (class 0 OID 0)
-- Dependencies: 226
-- Name: Roles_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Roles_id_seq" OWNED BY v1.roles.id;


--
-- TOC entry 227 (class 1259 OID 41556)
-- Name: users_business_units; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.users_business_units (
    id integer NOT NULL,
    users_id integer NOT NULL,
    business_units_id integer NOT NULL
);


ALTER TABLE v1.users_business_units OWNER TO "Admin";

--
-- TOC entry 228 (class 1259 OID 41559)
-- Name: UserBusinessUnits_id_seq; Type: SEQUENCE; Schema: v1; Owner: Admin
--

CREATE SEQUENCE v1."UserBusinessUnits_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE v1."UserBusinessUnits_id_seq" OWNER TO "Admin";

--
-- TOC entry 4933 (class 0 OID 0)
-- Dependencies: 228
-- Name: UserBusinessUnits_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."UserBusinessUnits_id_seq" OWNED BY v1.users_business_units.id;


--
-- TOC entry 229 (class 1259 OID 41560)
-- Name: users; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.users (
    id integer NOT NULL,
    email character varying(256) NOT NULL,
    password character varying(69) NOT NULL,
    CONSTRAINT "Users_email_check" CHECK (((email)::text <> ''::text)),
    CONSTRAINT "Users_password_check" CHECK (((password)::text <> ''::text))
);


ALTER TABLE v1.users OWNER TO "Admin";

--
-- TOC entry 230 (class 1259 OID 41565)
-- Name: Users_id_seq; Type: SEQUENCE; Schema: v1; Owner: Admin
--

CREATE SEQUENCE v1."Users_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE v1."Users_id_seq" OWNER TO "Admin";

--
-- TOC entry 4936 (class 0 OID 0)
-- Dependencies: 230
-- Name: Users_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Users_id_seq" OWNED BY v1.users.id;


--
-- TOC entry 231 (class 1259 OID 41566)
-- Name: whiteboards; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.whiteboards (
    id integer NOT NULL,
    name character varying(200) NOT NULL
);


ALTER TABLE v1.whiteboards OWNER TO "Admin";

--
-- TOC entry 232 (class 1259 OID 41569)
-- Name: Whiteboards_id_seq; Type: SEQUENCE; Schema: v1; Owner: Admin
--

CREATE SEQUENCE v1."Whiteboards_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE v1."Whiteboards_id_seq" OWNER TO "Admin";

--
-- TOC entry 4939 (class 0 OID 0)
-- Dependencies: 232
-- Name: Whiteboards_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Whiteboards_id_seq" OWNED BY v1.whiteboards.id;


--
-- TOC entry 233 (class 1259 OID 41570)
-- Name: roles_authorities; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.roles_authorities (
    roles_id integer NOT NULL,
    authorities_id integer NOT NULL
);


ALTER TABLE v1.roles_authorities OWNER TO "Admin";

--
-- TOC entry 234 (class 1259 OID 41573)
-- Name: users_business_units_roles; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.users_business_units_roles (
    users_business_units_id integer NOT NULL,
    roles_id integer NOT NULL
);


ALTER TABLE v1.users_business_units_roles OWNER TO "Admin";

--
-- TOC entry 4682 (class 2604 OID 41576)
-- Name: authorities id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.authorities ALTER COLUMN id SET DEFAULT nextval('v1."Authorities_id_seq"'::regclass);


--
-- TOC entry 4683 (class 2604 OID 41577)
-- Name: business_units id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.business_units ALTER COLUMN id SET DEFAULT nextval('v1."BusinessUnits_id_seq"'::regclass);


--
-- TOC entry 4684 (class 2604 OID 41578)
-- Name: columns id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.columns ALTER COLUMN id SET DEFAULT nextval('v1."Columns_id_seq"'::regclass);


--
-- TOC entry 4685 (class 2604 OID 41579)
-- Name: invites id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.invites ALTER COLUMN id SET DEFAULT nextval('v1."Invites_id_seq"'::regclass);


--
-- TOC entry 4686 (class 2604 OID 41580)
-- Name: notes id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.notes ALTER COLUMN id SET DEFAULT nextval('v1."Notes_id_seq"'::regclass);


--
-- TOC entry 4687 (class 2604 OID 41581)
-- Name: roles id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles ALTER COLUMN id SET DEFAULT nextval('v1."Roles_id_seq"'::regclass);


--
-- TOC entry 4689 (class 2604 OID 41582)
-- Name: users id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users ALTER COLUMN id SET DEFAULT nextval('v1."Users_id_seq"'::regclass);


--
-- TOC entry 4688 (class 2604 OID 41583)
-- Name: users_business_units id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units ALTER COLUMN id SET DEFAULT nextval('v1."UserBusinessUnits_id_seq"'::regclass);


--
-- TOC entry 4690 (class 2604 OID 41584)
-- Name: whiteboards id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.whiteboards ALTER COLUMN id SET DEFAULT nextval('v1."Whiteboards_id_seq"'::regclass);


--
-- TOC entry 4888 (class 0 OID 41524)
-- Dependencies: 215
-- Data for Name: authorities; Type: TABLE DATA; Schema: v1; Owner: Admin
--

INSERT INTO v1.authorities VALUES (4, 'ChangePermissions');
INSERT INTO v1.authorities VALUES (1, 'CreateChildren');
INSERT INTO v1.authorities VALUES (8, 'CreateWhiteboard');
INSERT INTO v1.authorities VALUES (3, 'DeleteBU');
INSERT INTO v1.authorities VALUES (10, 'InteractWithWhiteboard');
INSERT INTO v1.authorities VALUES (7, 'ManageSentInvites');
INSERT INTO v1.authorities VALUES (9, 'ManageWhiteboard');
INSERT INTO v1.authorities VALUES (5, 'SeePermissions');
INSERT INTO v1.authorities VALUES (2, 'UpdateBU');


--
-- TOC entry 4890 (class 0 OID 41529)
-- Dependencies: 217
-- Data for Name: business_units; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4892 (class 0 OID 41535)
-- Dependencies: 219
-- Data for Name: columns; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4894 (class 0 OID 41539)
-- Dependencies: 221
-- Data for Name: invites; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4896 (class 0 OID 41544)
-- Dependencies: 223
-- Data for Name: notes; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4898 (class 0 OID 41551)
-- Dependencies: 225
-- Data for Name: roles; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4906 (class 0 OID 41570)
-- Dependencies: 233
-- Data for Name: roles_authorities; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4902 (class 0 OID 41560)
-- Dependencies: 229
-- Data for Name: users; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4900 (class 0 OID 41556)
-- Dependencies: 227
-- Data for Name: users_business_units; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4907 (class 0 OID 41573)
-- Dependencies: 234
-- Data for Name: users_business_units_roles; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4904 (class 0 OID 41566)
-- Dependencies: 231
-- Data for Name: whiteboards; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4943 (class 0 OID 0)
-- Dependencies: 216
-- Name: Authorities_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Authorities_id_seq"', 1, false);


--
-- TOC entry 4944 (class 0 OID 0)
-- Dependencies: 218
-- Name: BusinessUnits_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."BusinessUnits_id_seq"', 1, false);


--
-- TOC entry 4945 (class 0 OID 0)
-- Dependencies: 220
-- Name: Columns_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Columns_id_seq"', 1, false);


--
-- TOC entry 4946 (class 0 OID 0)
-- Dependencies: 222
-- Name: Invites_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Invites_id_seq"', 1, false);


--
-- TOC entry 4947 (class 0 OID 0)
-- Dependencies: 224
-- Name: Notes_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Notes_id_seq"', 1, false);


--
-- TOC entry 4948 (class 0 OID 0)
-- Dependencies: 226
-- Name: Roles_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Roles_id_seq"', 1, false);


--
-- TOC entry 4949 (class 0 OID 0)
-- Dependencies: 228
-- Name: UserBusinessUnits_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."UserBusinessUnits_id_seq"', 1, false);


--
-- TOC entry 4950 (class 0 OID 0)
-- Dependencies: 230
-- Name: Users_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Users_id_seq"', 2, true);


--
-- TOC entry 4951 (class 0 OID 0)
-- Dependencies: 232
-- Name: Whiteboards_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Whiteboards_id_seq"', 1, false);


--
-- TOC entry 4700 (class 2606 OID 41586)
-- Name: authorities Authorities_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.authorities
    ADD CONSTRAINT "Authorities_pkey" PRIMARY KEY (id);


--
-- TOC entry 4704 (class 2606 OID 41588)
-- Name: business_units BusinessUnits_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_pkey" PRIMARY KEY (id);


--
-- TOC entry 4706 (class 2606 OID 41590)
-- Name: columns Columns_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.columns
    ADD CONSTRAINT "Columns_pkey" PRIMARY KEY (id);


--
-- TOC entry 4710 (class 2606 OID 41592)
-- Name: invites Invites_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT "Invites_pkey" PRIMARY KEY (id);


--
-- TOC entry 4714 (class 2606 OID 41594)
-- Name: notes Notes_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.notes
    ADD CONSTRAINT "Notes_pkey" PRIMARY KEY (id);


--
-- TOC entry 4716 (class 2606 OID 41596)
-- Name: roles Roles_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles
    ADD CONSTRAINT "Roles_pkey" PRIMARY KEY (id);


--
-- TOC entry 4720 (class 2606 OID 41598)
-- Name: users_business_units UserBusinessUnits_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT "UserBusinessUnits_pkey" PRIMARY KEY (id);


--
-- TOC entry 4724 (class 2606 OID 41600)
-- Name: users Users_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users
    ADD CONSTRAINT "Users_pkey" PRIMARY KEY (id);


--
-- TOC entry 4728 (class 2606 OID 41602)
-- Name: whiteboards Whiteboards_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.whiteboards
    ADD CONSTRAINT "Whiteboards_pkey" PRIMARY KEY (id);


--
-- TOC entry 4702 (class 2606 OID 41604)
-- Name: authorities authorities_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.authorities
    ADD CONSTRAINT authorities_un UNIQUE (name);


--
-- TOC entry 4708 (class 2606 OID 41606)
-- Name: columns columns_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.columns
    ADD CONSTRAINT columns_un UNIQUE (whiteboards_id, "position");


--
-- TOC entry 4712 (class 2606 OID 41608)
-- Name: invites invites_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT invites_un UNIQUE (receiver_id, business_units_id);


--
-- TOC entry 4718 (class 2606 OID 41612)
-- Name: roles roles_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles
    ADD CONSTRAINT roles_un UNIQUE (name, business_units_id);


--
-- TOC entry 4730 (class 2606 OID 41614)
-- Name: roles_authorities rolesauthorities_pk; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles_authorities
    ADD CONSTRAINT rolesauthorities_pk PRIMARY KEY (roles_id, authorities_id);


--
-- TOC entry 4722 (class 2606 OID 41616)
-- Name: users_business_units userbusinessunits_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT userbusinessunits_un UNIQUE (users_id, business_units_id);


--
-- TOC entry 4726 (class 2606 OID 41618)
-- Name: users users_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users
    ADD CONSTRAINT users_un UNIQUE (email);


--
-- TOC entry 4731 (class 2606 OID 41619)
-- Name: business_units BusinessUnits_companiesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_companiesid_fkey" FOREIGN KEY (companies_id) REFERENCES v1.business_units(id);


--
-- TOC entry 4732 (class 2606 OID 41624)
-- Name: business_units BusinessUnits_projectsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_projectsid_fkey" FOREIGN KEY (projects_id) REFERENCES v1.business_units(id);


--
-- TOC entry 4733 (class 2606 OID 41629)
-- Name: business_units BusinessUnits_whiteboardsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_whiteboardsid_fkey" FOREIGN KEY (whiteboards_id) REFERENCES v1.whiteboards(id);


--
-- TOC entry 4734 (class 2606 OID 41634)
-- Name: columns Columns_whiteboardsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.columns
    ADD CONSTRAINT "Columns_whiteboardsid_fkey" FOREIGN KEY (whiteboards_id) REFERENCES v1.whiteboards(id);


--
-- TOC entry 4735 (class 2606 OID 41639)
-- Name: invites Invites_businessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT "Invites_businessunitsid_fkey" FOREIGN KEY (business_units_id) REFERENCES v1.business_units(id);


--
-- TOC entry 4736 (class 2606 OID 41644)
-- Name: invites Invites_receiverid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT "Invites_receiverid_fkey" FOREIGN KEY (receiver_id) REFERENCES v1.users(id);


--
-- TOC entry 4737 (class 2606 OID 41649)
-- Name: notes Notes_columnsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.notes
    ADD CONSTRAINT "Notes_columnsid_fkey" FOREIGN KEY (columns_id) REFERENCES v1.columns(id);


--
-- TOC entry 4741 (class 2606 OID 41654)
-- Name: roles_authorities RolesAuthorities_authoritiesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles_authorities
    ADD CONSTRAINT "RolesAuthorities_authoritiesid_fkey" FOREIGN KEY (authorities_id) REFERENCES v1.authorities(id);


--
-- TOC entry 4742 (class 2606 OID 41659)
-- Name: roles_authorities RolesAuthorities_rolesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles_authorities
    ADD CONSTRAINT "RolesAuthorities_rolesid_fkey" FOREIGN KEY (roles_id) REFERENCES v1.roles(id);


--
-- TOC entry 4738 (class 2606 OID 41664)
-- Name: roles Roles_businessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles
    ADD CONSTRAINT "Roles_businessunitsid_fkey" FOREIGN KEY (business_units_id) REFERENCES v1.business_units(id);


--
-- TOC entry 4743 (class 2606 OID 41669)
-- Name: users_business_units_roles UserBusinessUnitsRoles_rolesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units_roles
    ADD CONSTRAINT "UserBusinessUnitsRoles_rolesid_fkey" FOREIGN KEY (roles_id) REFERENCES v1.roles(id);


--
-- TOC entry 4744 (class 2606 OID 41674)
-- Name: users_business_units_roles UserBusinessUnitsRoles_userbusinessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units_roles
    ADD CONSTRAINT "UserBusinessUnitsRoles_userbusinessunitsid_fkey" FOREIGN KEY (users_business_units_id) REFERENCES v1.users_business_units(id);


--
-- TOC entry 4739 (class 2606 OID 41679)
-- Name: users_business_units UserBusinessUnits_businessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT "UserBusinessUnits_businessunitsid_fkey" FOREIGN KEY (business_units_id) REFERENCES v1.business_units(id);


--
-- TOC entry 4740 (class 2606 OID 41684)
-- Name: users_business_units UserBusinessUnits_usersid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT "UserBusinessUnits_usersid_fkey" FOREIGN KEY (users_id) REFERENCES v1.users(id);


--
-- TOC entry 4913 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA v1; Type: ACL; Schema: -; Owner: Admin
--

REVOKE ALL ON SCHEMA v1 FROM "Admin";
GRANT ALL ON SCHEMA v1 TO "Admin" WITH GRANT OPTION;
GRANT USAGE ON SCHEMA v1 TO "Backend";


--
-- TOC entry 4914 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE authorities; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.authorities FROM "Admin";
GRANT ALL ON TABLE v1.authorities TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.authorities TO "Backend";


--
-- TOC entry 4916 (class 0 OID 0)
-- Dependencies: 216
-- Name: SEQUENCE "Authorities_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Authorities_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Authorities_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Authorities_id_seq" TO "Backend";


--
-- TOC entry 4917 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE business_units; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.business_units FROM "Admin";
GRANT ALL ON TABLE v1.business_units TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.business_units TO "Backend";


--
-- TOC entry 4919 (class 0 OID 0)
-- Dependencies: 218
-- Name: SEQUENCE "BusinessUnits_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."BusinessUnits_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."BusinessUnits_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."BusinessUnits_id_seq" TO "Backend";


--
-- TOC entry 4920 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE columns; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.columns FROM "Admin";
GRANT ALL ON TABLE v1.columns TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.columns TO "Backend";


--
-- TOC entry 4922 (class 0 OID 0)
-- Dependencies: 220
-- Name: SEQUENCE "Columns_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Columns_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Columns_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Columns_id_seq" TO "Backend";


--
-- TOC entry 4923 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE invites; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.invites FROM "Admin";
GRANT ALL ON TABLE v1.invites TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.invites TO "Backend";


--
-- TOC entry 4925 (class 0 OID 0)
-- Dependencies: 222
-- Name: SEQUENCE "Invites_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Invites_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Invites_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Invites_id_seq" TO "Backend";


--
-- TOC entry 4926 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE notes; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.notes FROM "Admin";
GRANT ALL ON TABLE v1.notes TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.notes TO "Backend";


--
-- TOC entry 4928 (class 0 OID 0)
-- Dependencies: 224
-- Name: SEQUENCE "Notes_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Notes_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Notes_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Notes_id_seq" TO "Backend";


--
-- TOC entry 4929 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE roles; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.roles FROM "Admin";
GRANT ALL ON TABLE v1.roles TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.roles TO "Backend";


--
-- TOC entry 4931 (class 0 OID 0)
-- Dependencies: 226
-- Name: SEQUENCE "Roles_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Roles_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Roles_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Roles_id_seq" TO "Backend";


--
-- TOC entry 4932 (class 0 OID 0)
-- Dependencies: 227
-- Name: TABLE users_business_units; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.users_business_units FROM "Admin";
GRANT ALL ON TABLE v1.users_business_units TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.users_business_units TO "Backend";


--
-- TOC entry 4934 (class 0 OID 0)
-- Dependencies: 228
-- Name: SEQUENCE "UserBusinessUnits_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" TO "Backend";


--
-- TOC entry 4935 (class 0 OID 0)
-- Dependencies: 229
-- Name: TABLE users; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.users FROM "Admin";
GRANT ALL ON TABLE v1.users TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.users TO "Backend";


--
-- TOC entry 4937 (class 0 OID 0)
-- Dependencies: 230
-- Name: SEQUENCE "Users_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Users_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Users_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Users_id_seq" TO "Backend";


--
-- TOC entry 4938 (class 0 OID 0)
-- Dependencies: 231
-- Name: TABLE whiteboards; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.whiteboards FROM "Admin";
GRANT ALL ON TABLE v1.whiteboards TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.whiteboards TO "Backend";


--
-- TOC entry 4940 (class 0 OID 0)
-- Dependencies: 232
-- Name: SEQUENCE "Whiteboards_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Whiteboards_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Whiteboards_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Whiteboards_id_seq" TO "Backend";


--
-- TOC entry 4941 (class 0 OID 0)
-- Dependencies: 233
-- Name: TABLE roles_authorities; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.roles_authorities FROM "Admin";
GRANT ALL ON TABLE v1.roles_authorities TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.roles_authorities TO "Backend";


--
-- TOC entry 4942 (class 0 OID 0)
-- Dependencies: 234
-- Name: TABLE users_business_units_roles; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.users_business_units_roles FROM "Admin";
GRANT ALL ON TABLE v1.users_business_units_roles TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.users_business_units_roles TO "Backend";


-- Completed on 2024-01-10 00:32:03

--
-- PostgreSQL database dump complete
--

