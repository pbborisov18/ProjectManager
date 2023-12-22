--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

-- Started on 2023-12-22 18:15:03

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
-- TOC entry 5 (class 2615 OID 16408)
-- Name: v1; Type: SCHEMA; Schema: -; Owner: Admin
--

CREATE SCHEMA v1;


ALTER SCHEMA v1 OWNER TO "Admin";

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 228 (class 1259 OID 16572)
-- Name: Authorities; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1."Authorities" (
    "Id" integer NOT NULL,
    "Name" character varying(100) NOT NULL,
    CONSTRAINT "Authorities_name_check" CHECK ((("Name")::text <> ''::text))
);


ALTER TABLE v1."Authorities" OWNER TO "Admin";

--
-- TOC entry 227 (class 1259 OID 16571)
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
-- Dependencies: 227
-- Name: Authorities_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Authorities_id_seq" OWNED BY v1."Authorities"."Id";


--
-- TOC entry 224 (class 1259 OID 16531)
-- Name: BusinessUnits; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1."BusinessUnits" (
    "Id" integer NOT NULL,
    "Name" character varying(150) NOT NULL,
    "Type" character varying(50) NOT NULL,
    "CompaniesId" integer,
    "ProjectsId" integer,
    "WhiteboardsId" integer,
    CONSTRAINT "BusinessUnits_name_check" CHECK ((("Name")::text <> ''::text)),
    CONSTRAINT "BusinessUnits_type_check" CHECK ((("Type")::text <> ''::text))
);


ALTER TABLE v1."BusinessUnits" OWNER TO "Admin";

--
-- TOC entry 223 (class 1259 OID 16530)
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
-- Dependencies: 223
-- Name: BusinessUnits_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."BusinessUnits_id_seq" OWNED BY v1."BusinessUnits"."Id";


--
-- TOC entry 220 (class 1259 OID 16495)
-- Name: Columns; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1."Columns" (
    "Id" integer NOT NULL,
    "Name" character varying(50) NOT NULL,
    "Position" integer NOT NULL,
    "WhiteboardsId" integer NOT NULL
);


ALTER TABLE v1."Columns" OWNER TO "Admin";

--
-- TOC entry 219 (class 1259 OID 16494)
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
-- Dependencies: 219
-- Name: Columns_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Columns_id_seq" OWNED BY v1."Columns"."Id";


--
-- TOC entry 234 (class 1259 OID 16619)
-- Name: Invites; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1."Invites" (
    "Id" integer NOT NULL,
    "State" character varying(50) NOT NULL,
    "ReceiverId" integer NOT NULL,
    "BusinessUnitsId" integer NOT NULL,
    CONSTRAINT "Invites_state_check" CHECK ((("State")::text <> ''::text))
);


ALTER TABLE v1."Invites" OWNER TO "Admin";

--
-- TOC entry 233 (class 1259 OID 16618)
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
-- Dependencies: 233
-- Name: Invites_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Invites_id_seq" OWNED BY v1."Invites"."Id";


--
-- TOC entry 222 (class 1259 OID 16507)
-- Name: Notes; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1."Notes" (
    "Id" integer NOT NULL,
    "Name" character varying(100) NOT NULL,
    "Description" character varying(500) NOT NULL,
    "Position" integer NOT NULL,
    "ColumnsId" integer NOT NULL,
    CONSTRAINT "Notes_name_check" CHECK ((("Name")::text <> ''::text))
);


ALTER TABLE v1."Notes" OWNER TO "Admin";

--
-- TOC entry 221 (class 1259 OID 16506)
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
-- Dependencies: 221
-- Name: Notes_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Notes_id_seq" OWNED BY v1."Notes"."Id";


--
-- TOC entry 230 (class 1259 OID 16580)
-- Name: Roles; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1."Roles" (
    "Id" integer NOT NULL,
    "Name" character varying(150) NOT NULL,
    "BusinessUnitsId" integer NOT NULL,
    CONSTRAINT "Roles_name_check" CHECK ((("Name")::text <> ''::text))
);


ALTER TABLE v1."Roles" OWNER TO "Admin";

--
-- TOC entry 231 (class 1259 OID 16592)
-- Name: RolesAuthorities; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1."RolesAuthorities" (
    "RolesId" integer NOT NULL,
    "AuthoritiesId" integer NOT NULL
);


ALTER TABLE v1."RolesAuthorities" OWNER TO "Admin";

--
-- TOC entry 229 (class 1259 OID 16579)
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
-- TOC entry 4933 (class 0 OID 0)
-- Dependencies: 229
-- Name: Roles_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Roles_id_seq" OWNED BY v1."Roles"."Id";


--
-- TOC entry 226 (class 1259 OID 16555)
-- Name: UsersBusinessUnits; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1."UsersBusinessUnits" (
    "Id" integer NOT NULL,
    "UsersId" integer NOT NULL,
    "BusinessUnitsId" integer NOT NULL
);


ALTER TABLE v1."UsersBusinessUnits" OWNER TO "Admin";

--
-- TOC entry 225 (class 1259 OID 16554)
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
-- TOC entry 4936 (class 0 OID 0)
-- Dependencies: 225
-- Name: UserBusinessUnits_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."UserBusinessUnits_id_seq" OWNED BY v1."UsersBusinessUnits"."Id";


--
-- TOC entry 216 (class 1259 OID 16442)
-- Name: Users; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1."Users" (
    "Id" integer NOT NULL,
    "Email" character varying(256) NOT NULL,
    "Password" character varying(69) NOT NULL,
    CONSTRAINT "Users_email_check" CHECK ((("Email")::text <> ''::text)),
    CONSTRAINT "Users_password_check" CHECK ((("Password")::text <> ''::text))
);


ALTER TABLE v1."Users" OWNER TO "Admin";

--
-- TOC entry 232 (class 1259 OID 16605)
-- Name: UsersBusinessUnitsRoles; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1."UsersBusinessUnitsRoles" (
    "UsersBusinessUnitsId" integer NOT NULL,
    "RolesId" integer NOT NULL
);


ALTER TABLE v1."UsersBusinessUnitsRoles" OWNER TO "Admin";

--
-- TOC entry 215 (class 1259 OID 16441)
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
-- TOC entry 4940 (class 0 OID 0)
-- Dependencies: 215
-- Name: Users_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Users_id_seq" OWNED BY v1."Users"."Id";


--
-- TOC entry 218 (class 1259 OID 16487)
-- Name: Whiteboards; Type: TABLE; Schema: v1; Owner: Admin
--

CREATE TABLE v1."Whiteboards" (
    "Id" integer NOT NULL,
    "Name" character varying(200) NOT NULL
);


ALTER TABLE v1."Whiteboards" OWNER TO "Admin";

--
-- TOC entry 217 (class 1259 OID 16486)
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
-- TOC entry 4943 (class 0 OID 0)
-- Dependencies: 217
-- Name: Whiteboards_id_seq; Type: SEQUENCE OWNED BY; Schema: v1; Owner: Admin
--

ALTER SEQUENCE v1."Whiteboards_id_seq" OWNED BY v1."Whiteboards"."Id";


--
-- TOC entry 4688 (class 2604 OID 16575)
-- Name: Authorities Id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Authorities" ALTER COLUMN "Id" SET DEFAULT nextval('v1."Authorities_id_seq"'::regclass);


--
-- TOC entry 4686 (class 2604 OID 16534)
-- Name: BusinessUnits Id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."BusinessUnits" ALTER COLUMN "Id" SET DEFAULT nextval('v1."BusinessUnits_id_seq"'::regclass);


--
-- TOC entry 4684 (class 2604 OID 16498)
-- Name: Columns Id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Columns" ALTER COLUMN "Id" SET DEFAULT nextval('v1."Columns_id_seq"'::regclass);


--
-- TOC entry 4690 (class 2604 OID 16622)
-- Name: Invites Id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Invites" ALTER COLUMN "Id" SET DEFAULT nextval('v1."Invites_id_seq"'::regclass);


--
-- TOC entry 4685 (class 2604 OID 16510)
-- Name: Notes Id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Notes" ALTER COLUMN "Id" SET DEFAULT nextval('v1."Notes_id_seq"'::regclass);


--
-- TOC entry 4689 (class 2604 OID 16583)
-- Name: Roles Id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Roles" ALTER COLUMN "Id" SET DEFAULT nextval('v1."Roles_id_seq"'::regclass);


--
-- TOC entry 4682 (class 2604 OID 16445)
-- Name: Users Id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Users" ALTER COLUMN "Id" SET DEFAULT nextval('v1."Users_id_seq"'::regclass);


--
-- TOC entry 4687 (class 2604 OID 16558)
-- Name: UsersBusinessUnits Id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."UsersBusinessUnits" ALTER COLUMN "Id" SET DEFAULT nextval('v1."UserBusinessUnits_id_seq"'::regclass);


--
-- TOC entry 4683 (class 2604 OID 16490)
-- Name: Whiteboards Id; Type: DEFAULT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Whiteboards" ALTER COLUMN "Id" SET DEFAULT nextval('v1."Whiteboards_id_seq"'::regclass);


--
-- TOC entry 4903 (class 0 OID 16572)
-- Dependencies: 228
-- Data for Name: Authorities; Type: TABLE DATA; Schema: v1; Owner: Admin
--

INSERT INTO v1."Authorities" VALUES (4, 'ChangePermissions');
INSERT INTO v1."Authorities" VALUES (1, 'CreateChildren');
INSERT INTO v1."Authorities" VALUES (8, 'CreateWhiteboard');
INSERT INTO v1."Authorities" VALUES (3, 'DeleteBU');
INSERT INTO v1."Authorities" VALUES (10, 'InteractWithWhiteboard');
INSERT INTO v1."Authorities" VALUES (7, 'ManageSentInvites');
INSERT INTO v1."Authorities" VALUES (9, 'ManageWhiteboard');
INSERT INTO v1."Authorities" VALUES (5, 'SeePermissions');
INSERT INTO v1."Authorities" VALUES (2, 'UpdateBU');


--
-- TOC entry 4899 (class 0 OID 16531)
-- Dependencies: 224
-- Data for Name: BusinessUnits; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4895 (class 0 OID 16495)
-- Dependencies: 220
-- Data for Name: Columns; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4909 (class 0 OID 16619)
-- Dependencies: 234
-- Data for Name: Invites; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4897 (class 0 OID 16507)
-- Dependencies: 222
-- Data for Name: Notes; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4905 (class 0 OID 16580)
-- Dependencies: 230
-- Data for Name: Roles; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4906 (class 0 OID 16592)
-- Dependencies: 231
-- Data for Name: RolesAuthorities; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4891 (class 0 OID 16442)
-- Dependencies: 216
-- Data for Name: Users; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4901 (class 0 OID 16555)
-- Dependencies: 226
-- Data for Name: UsersBusinessUnits; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4907 (class 0 OID 16605)
-- Dependencies: 232
-- Data for Name: UsersBusinessUnitsRoles; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4893 (class 0 OID 16487)
-- Dependencies: 218
-- Data for Name: Whiteboards; Type: TABLE DATA; Schema: v1; Owner: Admin
--



--
-- TOC entry 4945 (class 0 OID 0)
-- Dependencies: 227
-- Name: Authorities_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Authorities_id_seq"', 1, false);


--
-- TOC entry 4946 (class 0 OID 0)
-- Dependencies: 223
-- Name: BusinessUnits_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."BusinessUnits_id_seq"', 1, false);


--
-- TOC entry 4947 (class 0 OID 0)
-- Dependencies: 219
-- Name: Columns_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Columns_id_seq"', 1, false);


--
-- TOC entry 4948 (class 0 OID 0)
-- Dependencies: 233
-- Name: Invites_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Invites_id_seq"', 1, false);


--
-- TOC entry 4949 (class 0 OID 0)
-- Dependencies: 221
-- Name: Notes_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Notes_id_seq"', 1, false);


--
-- TOC entry 4950 (class 0 OID 0)
-- Dependencies: 229
-- Name: Roles_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Roles_id_seq"', 1, false);


--
-- TOC entry 4951 (class 0 OID 0)
-- Dependencies: 225
-- Name: UserBusinessUnits_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."UserBusinessUnits_id_seq"', 1, false);


--
-- TOC entry 4952 (class 0 OID 0)
-- Dependencies: 215
-- Name: Users_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Users_id_seq"', 2, true);


--
-- TOC entry 4953 (class 0 OID 0)
-- Dependencies: 217
-- Name: Whiteboards_id_seq; Type: SEQUENCE SET; Schema: v1; Owner: Admin
--

SELECT pg_catalog.setval('v1."Whiteboards_id_seq"', 1, false);


--
-- TOC entry 4720 (class 2606 OID 16578)
-- Name: Authorities Authorities_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Authorities"
    ADD CONSTRAINT "Authorities_pkey" PRIMARY KEY ("Id");


--
-- TOC entry 4714 (class 2606 OID 16538)
-- Name: BusinessUnits BusinessUnits_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."BusinessUnits"
    ADD CONSTRAINT "BusinessUnits_pkey" PRIMARY KEY ("Id");


--
-- TOC entry 4706 (class 2606 OID 16500)
-- Name: Columns Columns_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Columns"
    ADD CONSTRAINT "Columns_pkey" PRIMARY KEY ("Id");


--
-- TOC entry 4730 (class 2606 OID 16625)
-- Name: Invites Invites_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Invites"
    ADD CONSTRAINT "Invites_pkey" PRIMARY KEY ("Id");


--
-- TOC entry 4710 (class 2606 OID 16515)
-- Name: Notes Notes_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Notes"
    ADD CONSTRAINT "Notes_pkey" PRIMARY KEY ("Id");


--
-- TOC entry 4724 (class 2606 OID 16586)
-- Name: Roles Roles_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Roles"
    ADD CONSTRAINT "Roles_pkey" PRIMARY KEY ("Id");


--
-- TOC entry 4716 (class 2606 OID 16560)
-- Name: UsersBusinessUnits UserBusinessUnits_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."UsersBusinessUnits"
    ADD CONSTRAINT "UserBusinessUnits_pkey" PRIMARY KEY ("Id");


--
-- TOC entry 4700 (class 2606 OID 16449)
-- Name: Users Users_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Users"
    ADD CONSTRAINT "Users_pkey" PRIMARY KEY ("Id");


--
-- TOC entry 4704 (class 2606 OID 16492)
-- Name: Whiteboards Whiteboards_pkey; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Whiteboards"
    ADD CONSTRAINT "Whiteboards_pkey" PRIMARY KEY ("Id");


--
-- TOC entry 4722 (class 2606 OID 16637)
-- Name: Authorities authorities_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Authorities"
    ADD CONSTRAINT authorities_un UNIQUE ("Name");


--
-- TOC entry 4708 (class 2606 OID 16639)
-- Name: Columns columns_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Columns"
    ADD CONSTRAINT columns_un UNIQUE ("WhiteboardsId", "Position");


--
-- TOC entry 4732 (class 2606 OID 16641)
-- Name: Invites invites_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Invites"
    ADD CONSTRAINT invites_un UNIQUE ("ReceiverId", "BusinessUnitsId");


--
-- TOC entry 4712 (class 2606 OID 16643)
-- Name: Notes notes_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Notes"
    ADD CONSTRAINT notes_un UNIQUE ("ColumnsId", "Position");


--
-- TOC entry 4726 (class 2606 OID 16645)
-- Name: Roles roles_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Roles"
    ADD CONSTRAINT roles_un UNIQUE ("Name", "BusinessUnitsId");


--
-- TOC entry 4728 (class 2606 OID 16647)
-- Name: RolesAuthorities rolesauthorities_pk; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."RolesAuthorities"
    ADD CONSTRAINT rolesauthorities_pk PRIMARY KEY ("RolesId", "AuthoritiesId");


--
-- TOC entry 4718 (class 2606 OID 16649)
-- Name: UsersBusinessUnits userbusinessunits_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."UsersBusinessUnits"
    ADD CONSTRAINT userbusinessunits_un UNIQUE ("UsersId", "BusinessUnitsId");


--
-- TOC entry 4702 (class 2606 OID 16651)
-- Name: Users users_un; Type: CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Users"
    ADD CONSTRAINT users_un UNIQUE ("Email");


--
-- TOC entry 4735 (class 2606 OID 16544)
-- Name: BusinessUnits BusinessUnits_companiesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."BusinessUnits"
    ADD CONSTRAINT "BusinessUnits_companiesid_fkey" FOREIGN KEY ("CompaniesId") REFERENCES v1."BusinessUnits"("Id");


--
-- TOC entry 4736 (class 2606 OID 16549)
-- Name: BusinessUnits BusinessUnits_projectsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."BusinessUnits"
    ADD CONSTRAINT "BusinessUnits_projectsid_fkey" FOREIGN KEY ("ProjectsId") REFERENCES v1."BusinessUnits"("Id");


--
-- TOC entry 4737 (class 2606 OID 16539)
-- Name: BusinessUnits BusinessUnits_whiteboardsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."BusinessUnits"
    ADD CONSTRAINT "BusinessUnits_whiteboardsid_fkey" FOREIGN KEY ("WhiteboardsId") REFERENCES v1."Whiteboards"("Id");


--
-- TOC entry 4733 (class 2606 OID 16501)
-- Name: Columns Columns_whiteboardsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Columns"
    ADD CONSTRAINT "Columns_whiteboardsid_fkey" FOREIGN KEY ("WhiteboardsId") REFERENCES v1."Whiteboards"("Id");


--
-- TOC entry 4745 (class 2606 OID 16631)
-- Name: Invites Invites_businessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Invites"
    ADD CONSTRAINT "Invites_businessunitsid_fkey" FOREIGN KEY ("BusinessUnitsId") REFERENCES v1."BusinessUnits"("Id");


--
-- TOC entry 4746 (class 2606 OID 16626)
-- Name: Invites Invites_receiverid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Invites"
    ADD CONSTRAINT "Invites_receiverid_fkey" FOREIGN KEY ("ReceiverId") REFERENCES v1."Users"("Id");


--
-- TOC entry 4734 (class 2606 OID 16516)
-- Name: Notes Notes_columnsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Notes"
    ADD CONSTRAINT "Notes_columnsid_fkey" FOREIGN KEY ("ColumnsId") REFERENCES v1."Columns"("Id");


--
-- TOC entry 4741 (class 2606 OID 16600)
-- Name: RolesAuthorities RolesAuthorities_authoritiesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."RolesAuthorities"
    ADD CONSTRAINT "RolesAuthorities_authoritiesid_fkey" FOREIGN KEY ("AuthoritiesId") REFERENCES v1."Authorities"("Id");


--
-- TOC entry 4742 (class 2606 OID 16595)
-- Name: RolesAuthorities RolesAuthorities_rolesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."RolesAuthorities"
    ADD CONSTRAINT "RolesAuthorities_rolesid_fkey" FOREIGN KEY ("RolesId") REFERENCES v1."Roles"("Id");


--
-- TOC entry 4740 (class 2606 OID 16587)
-- Name: Roles Roles_businessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."Roles"
    ADD CONSTRAINT "Roles_businessunitsid_fkey" FOREIGN KEY ("BusinessUnitsId") REFERENCES v1."BusinessUnits"("Id");


--
-- TOC entry 4743 (class 2606 OID 16608)
-- Name: UsersBusinessUnitsRoles UserBusinessUnitsRoles_rolesid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."UsersBusinessUnitsRoles"
    ADD CONSTRAINT "UserBusinessUnitsRoles_rolesid_fkey" FOREIGN KEY ("RolesId") REFERENCES v1."Roles"("Id");


--
-- TOC entry 4744 (class 2606 OID 16613)
-- Name: UsersBusinessUnitsRoles UserBusinessUnitsRoles_userbusinessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."UsersBusinessUnitsRoles"
    ADD CONSTRAINT "UserBusinessUnitsRoles_userbusinessunitsid_fkey" FOREIGN KEY ("UsersBusinessUnitsId") REFERENCES v1."UsersBusinessUnits"("Id");


--
-- TOC entry 4738 (class 2606 OID 16566)
-- Name: UsersBusinessUnits UserBusinessUnits_businessunitsid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."UsersBusinessUnits"
    ADD CONSTRAINT "UserBusinessUnits_businessunitsid_fkey" FOREIGN KEY ("BusinessUnitsId") REFERENCES v1."BusinessUnits"("Id");


--
-- TOC entry 4739 (class 2606 OID 16561)
-- Name: UsersBusinessUnits UserBusinessUnits_usersid_fkey; Type: FK CONSTRAINT; Schema: v1; Owner: Admin
--

ALTER TABLE ONLY v1."UsersBusinessUnits"
    ADD CONSTRAINT "UserBusinessUnits_usersid_fkey" FOREIGN KEY ("UsersId") REFERENCES v1."Users"("Id");


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
-- Dependencies: 228
-- Name: TABLE "Authorities"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1."Authorities" FROM "Admin";
GRANT ALL ON TABLE v1."Authorities" TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1."Authorities" TO "Backend";


--
-- TOC entry 4918 (class 0 OID 0)
-- Dependencies: 227
-- Name: SEQUENCE "Authorities_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Authorities_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Authorities_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Authorities_id_seq" TO "Backend";


--
-- TOC entry 4919 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE "BusinessUnits"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1."BusinessUnits" FROM "Admin";
GRANT ALL ON TABLE v1."BusinessUnits" TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1."BusinessUnits" TO "Backend";


--
-- TOC entry 4921 (class 0 OID 0)
-- Dependencies: 223
-- Name: SEQUENCE "BusinessUnits_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."BusinessUnits_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."BusinessUnits_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."BusinessUnits_id_seq" TO "Backend";


--
-- TOC entry 4922 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE "Columns"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1."Columns" FROM "Admin";
GRANT ALL ON TABLE v1."Columns" TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1."Columns" TO "Backend";


--
-- TOC entry 4924 (class 0 OID 0)
-- Dependencies: 219
-- Name: SEQUENCE "Columns_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Columns_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Columns_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Columns_id_seq" TO "Backend";


--
-- TOC entry 4925 (class 0 OID 0)
-- Dependencies: 234
-- Name: TABLE "Invites"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1."Invites" FROM "Admin";
GRANT ALL ON TABLE v1."Invites" TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1."Invites" TO "Backend";


--
-- TOC entry 4927 (class 0 OID 0)
-- Dependencies: 233
-- Name: SEQUENCE "Invites_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Invites_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Invites_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Invites_id_seq" TO "Backend";


--
-- TOC entry 4928 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE "Notes"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1."Notes" FROM "Admin";
GRANT ALL ON TABLE v1."Notes" TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1."Notes" TO "Backend";


--
-- TOC entry 4930 (class 0 OID 0)
-- Dependencies: 221
-- Name: SEQUENCE "Notes_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Notes_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Notes_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Notes_id_seq" TO "Backend";


--
-- TOC entry 4931 (class 0 OID 0)
-- Dependencies: 230
-- Name: TABLE "Roles"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1."Roles" FROM "Admin";
GRANT ALL ON TABLE v1."Roles" TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1."Roles" TO "Backend";


--
-- TOC entry 4932 (class 0 OID 0)
-- Dependencies: 231
-- Name: TABLE "RolesAuthorities"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1."RolesAuthorities" FROM "Admin";
GRANT ALL ON TABLE v1."RolesAuthorities" TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1."RolesAuthorities" TO "Backend";


--
-- TOC entry 4934 (class 0 OID 0)
-- Dependencies: 229
-- Name: SEQUENCE "Roles_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Roles_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Roles_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Roles_id_seq" TO "Backend";


--
-- TOC entry 4935 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE "UsersBusinessUnits"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1."UsersBusinessUnits" FROM "Admin";
GRANT ALL ON TABLE v1."UsersBusinessUnits" TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1."UsersBusinessUnits" TO "Backend";


--
-- TOC entry 4937 (class 0 OID 0)
-- Dependencies: 225
-- Name: SEQUENCE "UserBusinessUnits_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" TO "Backend";


--
-- TOC entry 4938 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE "Users"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1."Users" FROM "Admin";
GRANT ALL ON TABLE v1."Users" TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1."Users" TO "Backend";


--
-- TOC entry 4939 (class 0 OID 0)
-- Dependencies: 232
-- Name: TABLE "UsersBusinessUnitsRoles"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1."UsersBusinessUnitsRoles" FROM "Admin";
GRANT ALL ON TABLE v1."UsersBusinessUnitsRoles" TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1."UsersBusinessUnitsRoles" TO "Backend";


--
-- TOC entry 4941 (class 0 OID 0)
-- Dependencies: 215
-- Name: SEQUENCE "Users_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Users_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Users_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Users_id_seq" TO "Backend";


--
-- TOC entry 4942 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE "Whiteboards"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON TABLE v1."Whiteboards" FROM "Admin";
GRANT ALL ON TABLE v1."Whiteboards" TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1."Whiteboards" TO "Backend";


--
-- TOC entry 4944 (class 0 OID 0)
-- Dependencies: 217
-- Name: SEQUENCE "Whiteboards_id_seq"; Type: ACL; Schema: v1; Owner: Admin
--

REVOKE ALL ON SEQUENCE v1."Whiteboards_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Whiteboards_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Whiteboards_id_seq" TO "Backend";


-- Completed on 2023-12-22 18:15:04

--
-- PostgreSQL database dump complete
--

