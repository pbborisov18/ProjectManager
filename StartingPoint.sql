--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

-- Started on 2023-12-23 00:25:42

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'WIN1252';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 16391)
-- Name: v1; Type: SCHEMA; Schema: -; Owner: Admin
--

CREATE SCHEMA v1;


ALTER SCHEMA v1 OWNER TO "Admin";

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 16392)
-- Name: authorities; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.authorities (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    CONSTRAINT "Authorities_name_check" CHECK (((name)::text <> ''::text))
);


ALTER TABLE v1.authorities OWNER TO "Admin";

--
-- TOC entry 216 (class 1259 OID 16396)
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
-- TOC entry 4917 (class 0 OID 0)
-- Dependencies: 216
-- Name: Authorities_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Authorities_id_seq" OWNED BY v1.authorities.id;


--
-- TOC entry 217 (class 1259 OID 16397)
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
-- TOC entry 218 (class 1259 OID 16402)
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
-- TOC entry 4920 (class 0 OID 0)
-- Dependencies: 218
-- Name: BusinessUnits_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."BusinessUnits_id_seq" OWNED BY v1.business_units.id;


--
-- TOC entry 219 (class 1259 OID 16403)
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
-- TOC entry 220 (class 1259 OID 16406)
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
-- TOC entry 4923 (class 0 OID 0)
-- Dependencies: 220
-- Name: Columns_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Columns_id_seq" OWNED BY v1.columns.id;


--
-- TOC entry 221 (class 1259 OID 16407)
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
-- TOC entry 222 (class 1259 OID 16411)
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
-- TOC entry 4926 (class 0 OID 0)
-- Dependencies: 222
-- Name: Invites_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Invites_id_seq" OWNED BY v1.invites.id;


--
-- TOC entry 223 (class 1259 OID 16412)
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
-- TOC entry 224 (class 1259 OID 16416)
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
-- TOC entry 4929 (class 0 OID 0)
-- Dependencies: 224
-- Name: Notes_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Notes_id_seq" OWNED BY v1.notes.id;


--
-- TOC entry 225 (class 1259 OID 16417)
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
-- TOC entry 227 (class 1259 OID 16424)
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
-- TOC entry 4932 (class 0 OID 0)
-- Dependencies: 227
-- Name: Roles_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Roles_id_seq" OWNED BY v1.roles.id;


--
-- TOC entry 228 (class 1259 OID 16425)
-- Name: users_business_units; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.users_business_units (
    id integer NOT NULL,
    users_id integer NOT NULL,
    business_units_id integer NOT NULL
);


ALTER TABLE v1.users_business_units OWNER TO "Admin";

--
-- TOC entry 229 (class 1259 OID 16428)
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
-- TOC entry 4935 (class 0 OID 0)
-- Dependencies: 229
-- Name: UserBusinessUnits_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."UserBusinessUnits_id_seq" OWNED BY v1.users_business_units.id;


--
-- TOC entry 230 (class 1259 OID 16429)
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
-- TOC entry 232 (class 1259 OID 16437)
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
-- TOC entry 4938 (class 0 OID 0)
-- Dependencies: 232
-- Name: Users_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Users_id_seq" OWNED BY v1.users.id;


--
-- TOC entry 233 (class 1259 OID 16438)
-- Name: whiteboards; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.whiteboards (
    id integer NOT NULL,
    name character varying(200) NOT NULL
);


ALTER TABLE v1.whiteboards OWNER TO "Admin";

--
-- TOC entry 234 (class 1259 OID 16441)
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
-- TOC entry 4941 (class 0 OID 0)
-- Dependencies: 234
-- Name: Whiteboards_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Whiteboards_id_seq" OWNED BY v1.whiteboards.id;


--
-- TOC entry 226 (class 1259 OID 16421)
-- Name: roles_authorities; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.roles_authorities (
    roles_id integer NOT NULL,
    authorities_id integer NOT NULL
);


ALTER TABLE v1.roles_authorities OWNER TO "Admin";

--
-- TOC entry 231 (class 1259 OID 16434)
-- Name: users_business_units_roles; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1.users_business_units_roles (
    users_business_units_id integer NOT NULL,
    roles_id integer NOT NULL
);


ALTER TABLE v1.users_business_units_roles OWNER TO "Admin";

--
-- TOC entry 4682 (class 2604 OID 16442)
-- Name: authorities id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.authorities ALTER COLUMN id SET DEFAULT nextval('v1."Authorities_id_seq"'::regclass);


--
-- TOC entry 4683 (class 2604 OID 16443)
-- Name: business_units id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.business_units ALTER COLUMN id SET DEFAULT nextval('v1."BusinessUnits_id_seq"'::regclass);


--
-- TOC entry 4684 (class 2604 OID 16444)
-- Name: columns id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.columns ALTER COLUMN id SET DEFAULT nextval('v1."Columns_id_seq"'::regclass);


--
-- TOC entry 4685 (class 2604 OID 16445)
-- Name: invites id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.invites ALTER COLUMN id SET DEFAULT nextval('v1."Invites_id_seq"'::regclass);


--
-- TOC entry 4686 (class 2604 OID 16446)
-- Name: notes id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.notes ALTER COLUMN id SET DEFAULT nextval('v1."Notes_id_seq"'::regclass);


--
-- TOC entry 4687 (class 2604 OID 16447)
-- Name: roles id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles ALTER COLUMN id SET DEFAULT nextval('v1."Roles_id_seq"'::regclass);


--
-- TOC entry 4689 (class 2604 OID 16448)
-- Name: users id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users ALTER COLUMN id SET DEFAULT nextval('v1."Users_id_seq"'::regclass);


--
-- TOC entry 4688 (class 2604 OID 16449)
-- Name: users_business_units id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units ALTER COLUMN id SET DEFAULT nextval('v1."UserBusinessUnits_id_seq"'::regclass);


--
-- TOC entry 4690 (class 2604 OID 16450)
-- Name: whiteboards id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.whiteboards ALTER COLUMN id SET DEFAULT nextval('v1."Whiteboards_id_seq"'::regclass);


--
-- TOC entry 4890 (class 0 OID 16392)
-- Dependencies: 215
-- Data for Name: authorities; Type: TABLE DATA; Schema: v1; Owner: Admin
--

COPY v1.authorities (id, name) FROM stdin;
4	ChangePermissions
1	CreateChildren
8	CreateWhiteboard
3	DeleteBU
10	InteractWithWhiteboard
7	ManageSentInvites
9	ManageWhiteboard
5	SeePermissions
2	UpdateBU
\.


--
-- TOC entry 4892 (class 0 OID 16397)
-- Dependencies: 217
-- Data for Name: business_units; Type: TABLE DATA; Schema: v1; Owner: Admin
--

COPY v1.business_units (id, name, type, companies_id, projects_id, whiteboards_id) FROM stdin;
\.


--
-- TOC entry 4894 (class 0 OID 16403)
-- Dependencies: 219
-- Data for Name: columns; Type: TABLE DATA; Schema: v1; Owner: Admin
--

COPY v1.columns (id, name, "position", whiteboards_id) FROM stdin;
\.


--
-- TOC entry 4896 (class 0 OID 16407)
-- Dependencies: 221
-- Data for Name: invites; Type: TABLE DATA; Schema: v1; Owner: Admin
--

COPY v1.invites (id, state, receiver_id, business_units_id) FROM stdin;
\.


--
-- TOC entry 4898 (class 0 OID 16412)
-- Dependencies: 223
-- Data for Name: notes; Type: TABLE DATA; Schema: v1; Owner: Admin
--

COPY v1.notes (id, name, description, "position", columns_id) FROM stdin;
\.


--
-- TOC entry 4900 (class 0 OID 16417)
-- Dependencies: 225
-- Data for Name: roles; Type: TABLE DATA; Schema: v1; Owner: Admin
--

COPY v1.roles (id, name, business_units_id) FROM stdin;
\.


--
-- TOC entry 4901 (class 0 OID 16421)
-- Dependencies: 226
-- Data for Name: roles_authorities; Type: TABLE DATA; Schema: v1; Owner: Admin
--

COPY v1.roles_authorities (roles_id, authorities_id) FROM stdin;
\.


--
-- TOC entry 4905 (class 0 OID 16429)
-- Dependencies: 230
-- Data for Name: users; Type: TABLE DATA; Schema: v1; Owner: Admin
--

COPY v1.users (id, email, password) FROM stdin;
\.


--
-- TOC entry 4903 (class 0 OID 16425)
-- Dependencies: 228
-- Data for Name: users_business_units; Type: TABLE DATA; Schema: v1; Owner: Admin
--

COPY v1.users_business_units (id, users_id, business_units_id) FROM stdin;
\.


--
-- TOC entry 4906 (class 0 OID 16434)
-- Dependencies: 231
-- Data for Name: users_business_units_roles; Type: TABLE DATA; Schema: v1; Owner: Admin
--

COPY v1.users_business_units_roles (users_business_units_id, roles_id) FROM stdin;
\.


--
-- TOC entry 4908 (class 0 OID 16438)
-- Dependencies: 233
-- Data for Name: whiteboards; Type: TABLE DATA; Schema: v1; Owner: Admin
--

COPY v1.whiteboards (id, name) FROM stdin;
\.


--
-- TOC entry 4945 (class 0 OID 0)
-- Dependencies: 216
-- Name: Authorities_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Authorities_id_seq"', 1, false);


--
-- TOC entry 4946 (class 0 OID 0)
-- Dependencies: 218
-- Name: BusinessUnits_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."BusinessUnits_id_seq"', 1, false);


--
-- TOC entry 4947 (class 0 OID 0)
-- Dependencies: 220
-- Name: Columns_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Columns_id_seq"', 1, false);


--
-- TOC entry 4948 (class 0 OID 0)
-- Dependencies: 222
-- Name: Invites_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Invites_id_seq"', 1, false);


--
-- TOC entry 4949 (class 0 OID 0)
-- Dependencies: 224
-- Name: Notes_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Notes_id_seq"', 1, false);


--
-- TOC entry 4950 (class 0 OID 0)
-- Dependencies: 227
-- Name: Roles_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Roles_id_seq"', 1, false);


--
-- TOC entry 4951 (class 0 OID 0)
-- Dependencies: 229
-- Name: UserBusinessUnits_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."UserBusinessUnits_id_seq"', 1, false);


--
-- TOC entry 4952 (class 0 OID 0)
-- Dependencies: 232
-- Name: Users_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Users_id_seq"', 2, true);


--
-- TOC entry 4953 (class 0 OID 0)
-- Dependencies: 234
-- Name: Whiteboards_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Whiteboards_id_seq"', 1, false);


--
-- TOC entry 4700 (class 2606 OID 16452)
-- Name: authorities Authorities_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.authorities
    ADD CONSTRAINT "Authorities_pkey" PRIMARY KEY (id);


--
-- TOC entry 4704 (class 2606 OID 16454)
-- Name: business_units BusinessUnits_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_pkey" PRIMARY KEY (id);


--
-- TOC entry 4706 (class 2606 OID 16456)
-- Name: columns Columns_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.columns
    ADD CONSTRAINT "Columns_pkey" PRIMARY KEY (id);


--
-- TOC entry 4710 (class 2606 OID 16458)
-- Name: invites Invites_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT "Invites_pkey" PRIMARY KEY (id);


--
-- TOC entry 4714 (class 2606 OID 16460)
-- Name: notes Notes_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.notes
    ADD CONSTRAINT "Notes_pkey" PRIMARY KEY (id);


--
-- TOC entry 4718 (class 2606 OID 16462)
-- Name: roles Roles_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles
    ADD CONSTRAINT "Roles_pkey" PRIMARY KEY (id);


--
-- TOC entry 4724 (class 2606 OID 16464)
-- Name: users_business_units UserBusinessUnits_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT "UserBusinessUnits_pkey" PRIMARY KEY (id);


--
-- TOC entry 4728 (class 2606 OID 16466)
-- Name: users Users_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users
    ADD CONSTRAINT "Users_pkey" PRIMARY KEY (id);


--
-- TOC entry 4732 (class 2606 OID 16468)
-- Name: whiteboards Whiteboards_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.whiteboards
    ADD CONSTRAINT "Whiteboards_pkey" PRIMARY KEY (id);


--
-- TOC entry 4702 (class 2606 OID 16470)
-- Name: authorities authorities_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.authorities
    ADD CONSTRAINT authorities_un UNIQUE (name);


--
-- TOC entry 4708 (class 2606 OID 16472)
-- Name: columns columns_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.columns
    ADD CONSTRAINT columns_un UNIQUE (whiteboards_id, "position");


--
-- TOC entry 4712 (class 2606 OID 16474)
-- Name: invites invites_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT invites_un UNIQUE (receiver_id, business_units_id);


--
-- TOC entry 4716 (class 2606 OID 16476)
-- Name: notes notes_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.notes
    ADD CONSTRAINT notes_un UNIQUE (columns_id, "position");


--
-- TOC entry 4720 (class 2606 OID 16478)
-- Name: roles roles_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles
    ADD CONSTRAINT roles_un UNIQUE (name, business_units_id);


--
-- TOC entry 4722 (class 2606 OID 16480)
-- Name: roles_authorities rolesauthorities_pk; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles_authorities
    ADD CONSTRAINT rolesauthorities_pk PRIMARY KEY (roles_id, authorities_id);


--
-- TOC entry 4726 (class 2606 OID 16482)
-- Name: users_business_units userbusinessunits_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT userbusinessunits_un UNIQUE (users_id, business_units_id);


--
-- TOC entry 4730 (class 2606 OID 16484)
-- Name: users users_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users
    ADD CONSTRAINT users_un UNIQUE (email);


--
-- TOC entry 4733 (class 2606 OID 16485)
-- Name: business_units BusinessUnits_companiesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_companiesid_fkey" FOREIGN KEY (companies_id) REFERENCES v1.business_units(id);


--
-- TOC entry 4734 (class 2606 OID 16490)
-- Name: business_units BusinessUnits_projectsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_projectsid_fkey" FOREIGN KEY (projects_id) REFERENCES v1.business_units(id);


--
-- TOC entry 4735 (class 2606 OID 16495)
-- Name: business_units BusinessUnits_whiteboardsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_whiteboardsid_fkey" FOREIGN KEY (whiteboards_id) REFERENCES v1.whiteboards(id);


--
-- TOC entry 4736 (class 2606 OID 16500)
-- Name: columns Columns_whiteboardsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.columns
    ADD CONSTRAINT "Columns_whiteboardsid_fkey" FOREIGN KEY (whiteboards_id) REFERENCES v1.whiteboards(id);


--
-- TOC entry 4737 (class 2606 OID 16505)
-- Name: invites Invites_businessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT "Invites_businessunitsid_fkey" FOREIGN KEY (business_units_id) REFERENCES v1.business_units(id);


--
-- TOC entry 4738 (class 2606 OID 16510)
-- Name: invites Invites_receiverid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT "Invites_receiverid_fkey" FOREIGN KEY (receiver_id) REFERENCES v1.users(id);


--
-- TOC entry 4739 (class 2606 OID 16515)
-- Name: notes Notes_columnsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.notes
    ADD CONSTRAINT "Notes_columnsid_fkey" FOREIGN KEY (columns_id) REFERENCES v1.columns(id);


--
-- TOC entry 4741 (class 2606 OID 16520)
-- Name: roles_authorities RolesAuthorities_authoritiesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles_authorities
    ADD CONSTRAINT "RolesAuthorities_authoritiesid_fkey" FOREIGN KEY (authorities_id) REFERENCES v1.authorities(id);


--
-- TOC entry 4742 (class 2606 OID 16525)
-- Name: roles_authorities RolesAuthorities_rolesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles_authorities
    ADD CONSTRAINT "RolesAuthorities_rolesid_fkey" FOREIGN KEY (roles_id) REFERENCES v1.roles(id);


--
-- TOC entry 4740 (class 2606 OID 16530)
-- Name: roles Roles_businessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.roles
    ADD CONSTRAINT "Roles_businessunitsid_fkey" FOREIGN KEY (business_units_id) REFERENCES v1.business_units(id);


--
-- TOC entry 4745 (class 2606 OID 16535)
-- Name: users_business_units_roles UserBusinessUnitsRoles_rolesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units_roles
    ADD CONSTRAINT "UserBusinessUnitsRoles_rolesid_fkey" FOREIGN KEY (roles_id) REFERENCES v1.roles(id);


--
-- TOC entry 4746 (class 2606 OID 16540)
-- Name: users_business_units_roles UserBusinessUnitsRoles_userbusinessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units_roles
    ADD CONSTRAINT "UserBusinessUnitsRoles_userbusinessunitsid_fkey" FOREIGN KEY (users_business_units_id) REFERENCES v1.users_business_units(id);


--
-- TOC entry 4743 (class 2606 OID 16545)
-- Name: users_business_units UserBusinessUnits_businessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT "UserBusinessUnits_businessunitsid_fkey" FOREIGN KEY (business_units_id) REFERENCES v1.business_units(id);


--
-- TOC entry 4744 (class 2606 OID 16550)
-- Name: users_business_units UserBusinessUnits_usersid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT "UserBusinessUnits_usersid_fkey" FOREIGN KEY (users_id) REFERENCES v1.users(id);


--
-- TOC entry 4915 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA v1; Type: ACL; Schema: -; Owner: Admin
--

REVOKE ALL ON SCHEMA v1 FROM "Admin";
GRANT ALL ON SCHEMA v1 TO "Admin" WITH GRANT OPTION;
GRANT USAGE ON SCHEMA v1 TO "Backend";


--
-- TOC entry 4916 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE authorities; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.authorities FROM "Admin";
GRANT ALL ON TABLE v1.authorities TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.authorities TO "Backend";


--
-- TOC entry 4918 (class 0 OID 0)
-- Dependencies: 216
-- Name: SEQUENCE "Authorities_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Authorities_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Authorities_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Authorities_id_seq" TO "Backend";


--
-- TOC entry 4919 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE business_units; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.business_units FROM "Admin";
GRANT ALL ON TABLE v1.business_units TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.business_units TO "Backend";


--
-- TOC entry 4921 (class 0 OID 0)
-- Dependencies: 218
-- Name: SEQUENCE "BusinessUnits_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."BusinessUnits_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."BusinessUnits_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."BusinessUnits_id_seq" TO "Backend";


--
-- TOC entry 4922 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE columns; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.columns FROM "Admin";
GRANT ALL ON TABLE v1.columns TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.columns TO "Backend";


--
-- TOC entry 4924 (class 0 OID 0)
-- Dependencies: 220
-- Name: SEQUENCE "Columns_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Columns_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Columns_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Columns_id_seq" TO "Backend";


--
-- TOC entry 4925 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE invites; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.invites FROM "Admin";
GRANT ALL ON TABLE v1.invites TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.invites TO "Backend";


--
-- TOC entry 4927 (class 0 OID 0)
-- Dependencies: 222
-- Name: SEQUENCE "Invites_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Invites_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Invites_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Invites_id_seq" TO "Backend";


--
-- TOC entry 4928 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE notes; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.notes FROM "Admin";
GRANT ALL ON TABLE v1.notes TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.notes TO "Backend";


--
-- TOC entry 4930 (class 0 OID 0)
-- Dependencies: 224
-- Name: SEQUENCE "Notes_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Notes_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Notes_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Notes_id_seq" TO "Backend";


--
-- TOC entry 4931 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE roles; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.roles FROM "Admin";
GRANT ALL ON TABLE v1.roles TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.roles TO "Backend";


--
-- TOC entry 4933 (class 0 OID 0)
-- Dependencies: 227
-- Name: SEQUENCE "Roles_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Roles_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Roles_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Roles_id_seq" TO "Backend";


--
-- TOC entry 4934 (class 0 OID 0)
-- Dependencies: 228
-- Name: TABLE users_business_units; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.users_business_units FROM "Admin";
GRANT ALL ON TABLE v1.users_business_units TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.users_business_units TO "Backend";


--
-- TOC entry 4936 (class 0 OID 0)
-- Dependencies: 229
-- Name: SEQUENCE "UserBusinessUnits_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" TO "Backend";


--
-- TOC entry 4937 (class 0 OID 0)
-- Dependencies: 230
-- Name: TABLE users; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.users FROM "Admin";
GRANT ALL ON TABLE v1.users TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.users TO "Backend";


--
-- TOC entry 4939 (class 0 OID 0)
-- Dependencies: 232
-- Name: SEQUENCE "Users_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Users_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Users_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Users_id_seq" TO "Backend";


--
-- TOC entry 4940 (class 0 OID 0)
-- Dependencies: 233
-- Name: TABLE whiteboards; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.whiteboards FROM "Admin";
GRANT ALL ON TABLE v1.whiteboards TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.whiteboards TO "Backend";


--
-- TOC entry 4942 (class 0 OID 0)
-- Dependencies: 234
-- Name: SEQUENCE "Whiteboards_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Whiteboards_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Whiteboards_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Whiteboards_id_seq" TO "Backend";


--
-- TOC entry 4943 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE roles_authorities; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.roles_authorities FROM "Admin";
GRANT ALL ON TABLE v1.roles_authorities TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.roles_authorities TO "Backend";


--
-- TOC entry 4944 (class 0 OID 0)
-- Dependencies: 231
-- Name: TABLE users_business_units_roles; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1.users_business_units_roles FROM "Admin";
GRANT ALL ON TABLE v1.users_business_units_roles TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.users_business_units_roles TO "Backend";


-- Completed on 2023-12-23 00:25:42

--
-- PostgreSQL database dump complete
--

