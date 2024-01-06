PGDMP                       {            ProjectManager    16.1    16.1 s    0           0    0    ENCODING    ENCODING     !   SET client_encoding = 'WIN1252';
                      false            1           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            2           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            3           1262    16386    ProjectManager    DATABASE     �   CREATE DATABASE "ProjectManager" WITH TEMPLATE = template0 ENCODING = 'WIN1252' LOCALE_PROVIDER = libc LOCALE = 'English_United Kingdom.1252';
     DROP DATABASE "ProjectManager";
                Admin    false                        2615    16391    v1    SCHEMA        CREATE SCHEMA v1;
    DROP SCHEMA v1;
                Admin    false            4           0    0 	   SCHEMA v1    ACL     �   REVOKE ALL ON SCHEMA v1 FROM "Admin";
GRANT ALL ON SCHEMA v1 TO "Admin" WITH GRANT OPTION;
GRANT USAGE ON SCHEMA v1 TO "Backend";
                   Admin    false    5            �            1259    16392    authorities    TABLE     �   CREATE TABLE v1.authorities (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    CONSTRAINT "Authorities_name_check" CHECK (((name)::text <> ''::text))
);
    DROP TABLE v1.authorities;
       v1         heap    Admin    false    5            5           0    0    TABLE authorities    ACL     �   REVOKE ALL ON TABLE v1.authorities FROM "Admin";
GRANT ALL ON TABLE v1.authorities TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.authorities TO "Backend";
          v1          Admin    false    215            �            1259    16396    Authorities_id_seq    SEQUENCE     �   CREATE SEQUENCE v1."Authorities_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE v1."Authorities_id_seq";
       v1          Admin    false    215    5            6           0    0    Authorities_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE v1."Authorities_id_seq" OWNED BY v1.authorities.id;
          v1          Admin    false    216            7           0    0    SEQUENCE "Authorities_id_seq"    ACL     �   REVOKE ALL ON SEQUENCE v1."Authorities_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Authorities_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Authorities_id_seq" TO "Backend";
          v1          Admin    false    216            �            1259    16397    business_units    TABLE     l  CREATE TABLE v1.business_units (
    id integer NOT NULL,
    name character varying(150) NOT NULL,
    type integer NOT NULL,
    companies_id integer,
    projects_id integer,
    whiteboards_id integer,
    CONSTRAINT "BusinessUnits_name_check" CHECK (((name)::text <> ''::text)),
    CONSTRAINT "BusinessUnits_type_check" CHECK (((type)::text <> ''::text))
);
    DROP TABLE v1.business_units;
       v1         heap    Admin    false    5            8           0    0    TABLE business_units    ACL     �   REVOKE ALL ON TABLE v1.business_units FROM "Admin";
GRANT ALL ON TABLE v1.business_units TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.business_units TO "Backend";
          v1          Admin    false    217            �            1259    16402    BusinessUnits_id_seq    SEQUENCE     �   CREATE SEQUENCE v1."BusinessUnits_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE v1."BusinessUnits_id_seq";
       v1          Admin    false    217    5            9           0    0    BusinessUnits_id_seq    SEQUENCE OWNED BY     H   ALTER SEQUENCE v1."BusinessUnits_id_seq" OWNED BY v1.business_units.id;
          v1          Admin    false    218            :           0    0    SEQUENCE "BusinessUnits_id_seq"    ACL     �   REVOKE ALL ON SEQUENCE v1."BusinessUnits_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."BusinessUnits_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."BusinessUnits_id_seq" TO "Backend";
          v1          Admin    false    218            �            1259    16403    columns    TABLE     �   CREATE TABLE v1.columns (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    "position" integer NOT NULL,
    whiteboards_id integer NOT NULL
);
    DROP TABLE v1.columns;
       v1         heap    Admin    false    5            ;           0    0    TABLE columns    ACL     �   REVOKE ALL ON TABLE v1.columns FROM "Admin";
GRANT ALL ON TABLE v1.columns TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.columns TO "Backend";
          v1          Admin    false    219            �            1259    16406    Columns_id_seq    SEQUENCE     �   CREATE SEQUENCE v1."Columns_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE v1."Columns_id_seq";
       v1          Admin    false    5    219            <           0    0    Columns_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE v1."Columns_id_seq" OWNED BY v1.columns.id;
          v1          Admin    false    220            =           0    0    SEQUENCE "Columns_id_seq"    ACL     �   REVOKE ALL ON SEQUENCE v1."Columns_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Columns_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Columns_id_seq" TO "Backend";
          v1          Admin    false    220            �            1259    16407    invites    TABLE     �   CREATE TABLE v1.invites (
    id integer NOT NULL,
    state integer NOT NULL,
    receiver_id integer NOT NULL,
    business_units_id integer NOT NULL,
    CONSTRAINT "Invites_state_check" CHECK (((state)::text <> ''::text))
);
    DROP TABLE v1.invites;
       v1         heap    Admin    false    5            >           0    0    TABLE invites    ACL     �   REVOKE ALL ON TABLE v1.invites FROM "Admin";
GRANT ALL ON TABLE v1.invites TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.invites TO "Backend";
          v1          Admin    false    221            �            1259    16411    Invites_id_seq    SEQUENCE     �   CREATE SEQUENCE v1."Invites_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE v1."Invites_id_seq";
       v1          Admin    false    221    5            ?           0    0    Invites_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE v1."Invites_id_seq" OWNED BY v1.invites.id;
          v1          Admin    false    222            @           0    0    SEQUENCE "Invites_id_seq"    ACL     �   REVOKE ALL ON SEQUENCE v1."Invites_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Invites_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Invites_id_seq" TO "Backend";
          v1          Admin    false    222            �            1259    16412    notes    TABLE       CREATE TABLE v1.notes (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(500) NOT NULL,
    "position" integer NOT NULL,
    columns_id integer NOT NULL,
    CONSTRAINT "Notes_name_check" CHECK (((name)::text <> ''::text))
);
    DROP TABLE v1.notes;
       v1         heap    Admin    false    5            A           0    0    TABLE notes    ACL     �   REVOKE ALL ON TABLE v1.notes FROM "Admin";
GRANT ALL ON TABLE v1.notes TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.notes TO "Backend";
          v1          Admin    false    223            �            1259    16416    Notes_id_seq    SEQUENCE     �   CREATE SEQUENCE v1."Notes_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE v1."Notes_id_seq";
       v1          Admin    false    223    5            B           0    0    Notes_id_seq    SEQUENCE OWNED BY     7   ALTER SEQUENCE v1."Notes_id_seq" OWNED BY v1.notes.id;
          v1          Admin    false    224            C           0    0    SEQUENCE "Notes_id_seq"    ACL     �   REVOKE ALL ON SEQUENCE v1."Notes_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Notes_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Notes_id_seq" TO "Backend";
          v1          Admin    false    224            �            1259    16417    roles    TABLE     �   CREATE TABLE v1.roles (
    id integer NOT NULL,
    name character varying(150) NOT NULL,
    business_units_id integer NOT NULL,
    CONSTRAINT "Roles_name_check" CHECK (((name)::text <> ''::text))
);
    DROP TABLE v1.roles;
       v1         heap    Admin    false    5            D           0    0    TABLE roles    ACL     �   REVOKE ALL ON TABLE v1.roles FROM "Admin";
GRANT ALL ON TABLE v1.roles TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.roles TO "Backend";
          v1          Admin    false    225            �            1259    16424    Roles_id_seq    SEQUENCE     �   CREATE SEQUENCE v1."Roles_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE v1."Roles_id_seq";
       v1          Admin    false    5    225            E           0    0    Roles_id_seq    SEQUENCE OWNED BY     7   ALTER SEQUENCE v1."Roles_id_seq" OWNED BY v1.roles.id;
          v1          Admin    false    227            F           0    0    SEQUENCE "Roles_id_seq"    ACL     �   REVOKE ALL ON SEQUENCE v1."Roles_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Roles_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Roles_id_seq" TO "Backend";
          v1          Admin    false    227            �            1259    16425    users_business_units    TABLE     �   CREATE TABLE v1.users_business_units (
    id integer NOT NULL,
    users_id integer NOT NULL,
    business_units_id integer NOT NULL
);
 $   DROP TABLE v1.users_business_units;
       v1         heap    Admin    false    5            G           0    0    TABLE users_business_units    ACL     �   REVOKE ALL ON TABLE v1.users_business_units FROM "Admin";
GRANT ALL ON TABLE v1.users_business_units TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.users_business_units TO "Backend";
          v1          Admin    false    228            �            1259    16428    UserBusinessUnits_id_seq    SEQUENCE     �   CREATE SEQUENCE v1."UserBusinessUnits_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE v1."UserBusinessUnits_id_seq";
       v1          Admin    false    5    228            H           0    0    UserBusinessUnits_id_seq    SEQUENCE OWNED BY     R   ALTER SEQUENCE v1."UserBusinessUnits_id_seq" OWNED BY v1.users_business_units.id;
          v1          Admin    false    229            I           0    0 #   SEQUENCE "UserBusinessUnits_id_seq"    ACL     �   REVOKE ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."UserBusinessUnits_id_seq" TO "Backend";
          v1          Admin    false    229            �            1259    16429    users    TABLE     !  CREATE TABLE v1.users (
    id integer NOT NULL,
    email character varying(256) NOT NULL,
    password character varying(69) NOT NULL,
    CONSTRAINT "Users_email_check" CHECK (((email)::text <> ''::text)),
    CONSTRAINT "Users_password_check" CHECK (((password)::text <> ''::text))
);
    DROP TABLE v1.users;
       v1         heap    Admin    false    5            J           0    0    TABLE users    ACL     �   REVOKE ALL ON TABLE v1.users FROM "Admin";
GRANT ALL ON TABLE v1.users TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.users TO "Backend";
          v1          Admin    false    230            �            1259    16437    Users_id_seq    SEQUENCE     �   CREATE SEQUENCE v1."Users_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE v1."Users_id_seq";
       v1          Admin    false    230    5            K           0    0    Users_id_seq    SEQUENCE OWNED BY     7   ALTER SEQUENCE v1."Users_id_seq" OWNED BY v1.users.id;
          v1          Admin    false    232            L           0    0    SEQUENCE "Users_id_seq"    ACL     �   REVOKE ALL ON SEQUENCE v1."Users_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Users_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Users_id_seq" TO "Backend";
          v1          Admin    false    232            �            1259    16438    whiteboards    TABLE     c   CREATE TABLE v1.whiteboards (
    id integer NOT NULL,
    name character varying(200) NOT NULL
);
    DROP TABLE v1.whiteboards;
       v1         heap    Admin    false    5            M           0    0    TABLE whiteboards    ACL     �   REVOKE ALL ON TABLE v1.whiteboards FROM "Admin";
GRANT ALL ON TABLE v1.whiteboards TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.whiteboards TO "Backend";
          v1          Admin    false    233            �            1259    16441    Whiteboards_id_seq    SEQUENCE     �   CREATE SEQUENCE v1."Whiteboards_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE v1."Whiteboards_id_seq";
       v1          Admin    false    5    233            N           0    0    Whiteboards_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE v1."Whiteboards_id_seq" OWNED BY v1.whiteboards.id;
          v1          Admin    false    234            O           0    0    SEQUENCE "Whiteboards_id_seq"    ACL     �   REVOKE ALL ON SEQUENCE v1."Whiteboards_id_seq" FROM "Admin";
GRANT ALL ON SEQUENCE v1."Whiteboards_id_seq" TO "Admin" WITH GRANT OPTION;
GRANT ALL ON SEQUENCE v1."Whiteboards_id_seq" TO "Backend";
          v1          Admin    false    234            �            1259    16421    roles_authorities    TABLE     j   CREATE TABLE v1.roles_authorities (
    roles_id integer NOT NULL,
    authorities_id integer NOT NULL
);
 !   DROP TABLE v1.roles_authorities;
       v1         heap    Admin    false    5            P           0    0    TABLE roles_authorities    ACL     �   REVOKE ALL ON TABLE v1.roles_authorities FROM "Admin";
GRANT ALL ON TABLE v1.roles_authorities TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.roles_authorities TO "Backend";
          v1          Admin    false    226            �            1259    16434    users_business_units_roles    TABLE     |   CREATE TABLE v1.users_business_units_roles (
    users_business_units_id integer NOT NULL,
    roles_id integer NOT NULL
);
 *   DROP TABLE v1.users_business_units_roles;
       v1         heap    Admin    false    5            Q           0    0     TABLE users_business_units_roles    ACL     �   REVOKE ALL ON TABLE v1.users_business_units_roles FROM "Admin";
GRANT ALL ON TABLE v1.users_business_units_roles TO "Admin" WITH GRANT OPTION;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE v1.users_business_units_roles TO "Backend";
          v1          Admin    false    231            J           2604    16442    authorities id    DEFAULT     j   ALTER TABLE ONLY v1.authorities ALTER COLUMN id SET DEFAULT nextval('v1."Authorities_id_seq"'::regclass);
 9   ALTER TABLE v1.authorities ALTER COLUMN id DROP DEFAULT;
       v1          Admin    false    216    215            K           2604    16443    business_units id    DEFAULT     o   ALTER TABLE ONLY v1.business_units ALTER COLUMN id SET DEFAULT nextval('v1."BusinessUnits_id_seq"'::regclass);
 <   ALTER TABLE v1.business_units ALTER COLUMN id DROP DEFAULT;
       v1          Admin    false    218    217            L           2604    16444 
   columns id    DEFAULT     b   ALTER TABLE ONLY v1.columns ALTER COLUMN id SET DEFAULT nextval('v1."Columns_id_seq"'::regclass);
 5   ALTER TABLE v1.columns ALTER COLUMN id DROP DEFAULT;
       v1          Admin    false    220    219            M           2604    16445 
   invites id    DEFAULT     b   ALTER TABLE ONLY v1.invites ALTER COLUMN id SET DEFAULT nextval('v1."Invites_id_seq"'::regclass);
 5   ALTER TABLE v1.invites ALTER COLUMN id DROP DEFAULT;
       v1          Admin    false    222    221            N           2604    16446    notes id    DEFAULT     ^   ALTER TABLE ONLY v1.notes ALTER COLUMN id SET DEFAULT nextval('v1."Notes_id_seq"'::regclass);
 3   ALTER TABLE v1.notes ALTER COLUMN id DROP DEFAULT;
       v1          Admin    false    224    223            O           2604    16447    roles id    DEFAULT     ^   ALTER TABLE ONLY v1.roles ALTER COLUMN id SET DEFAULT nextval('v1."Roles_id_seq"'::regclass);
 3   ALTER TABLE v1.roles ALTER COLUMN id DROP DEFAULT;
       v1          Admin    false    227    225            Q           2604    16448    users id    DEFAULT     ^   ALTER TABLE ONLY v1.users ALTER COLUMN id SET DEFAULT nextval('v1."Users_id_seq"'::regclass);
 3   ALTER TABLE v1.users ALTER COLUMN id DROP DEFAULT;
       v1          Admin    false    232    230            P           2604    16449    users_business_units id    DEFAULT     y   ALTER TABLE ONLY v1.users_business_units ALTER COLUMN id SET DEFAULT nextval('v1."UserBusinessUnits_id_seq"'::regclass);
 B   ALTER TABLE v1.users_business_units ALTER COLUMN id DROP DEFAULT;
       v1          Admin    false    229    228            R           2604    16450    whiteboards id    DEFAULT     j   ALTER TABLE ONLY v1.whiteboards ALTER COLUMN id SET DEFAULT nextval('v1."Whiteboards_id_seq"'::regclass);
 9   ALTER TABLE v1.whiteboards ALTER COLUMN id DROP DEFAULT;
       v1          Admin    false    234    233                      0    16392    authorities 
   TABLE DATA           +   COPY v1.authorities (id, name) FROM stdin;
    v1          Admin    false    215   _�                 0    16397    business_units 
   TABLE DATA           _   COPY v1.business_units (id, name, type, companies_id, projects_id, whiteboards_id) FROM stdin;
    v1          Admin    false    217   �                 0    16403    columns 
   TABLE DATA           C   COPY v1.columns (id, name, "position", whiteboards_id) FROM stdin;
    v1          Admin    false    219   	�                  0    16407    invites 
   TABLE DATA           H   COPY v1.invites (id, state, receiver_id, business_units_id) FROM stdin;
    v1          Admin    false    221   &�       "          0    16412    notes 
   TABLE DATA           J   COPY v1.notes (id, name, description, "position", columns_id) FROM stdin;
    v1          Admin    false    223   C�       $          0    16417    roles 
   TABLE DATA           8   COPY v1.roles (id, name, business_units_id) FROM stdin;
    v1          Admin    false    225   `�       %          0    16421    roles_authorities 
   TABLE DATA           A   COPY v1.roles_authorities (roles_id, authorities_id) FROM stdin;
    v1          Admin    false    226   }�       )          0    16429    users 
   TABLE DATA           0   COPY v1.users (id, email, password) FROM stdin;
    v1          Admin    false    230   ��       '          0    16425    users_business_units 
   TABLE DATA           K   COPY v1.users_business_units (id, users_id, business_units_id) FROM stdin;
    v1          Admin    false    228   ��       *          0    16434    users_business_units_roles 
   TABLE DATA           S   COPY v1.users_business_units_roles (users_business_units_id, roles_id) FROM stdin;
    v1          Admin    false    231   ԇ       ,          0    16438    whiteboards 
   TABLE DATA           +   COPY v1.whiteboards (id, name) FROM stdin;
    v1          Admin    false    233   �       R           0    0    Authorities_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('v1."Authorities_id_seq"', 1, false);
          v1          Admin    false    216            S           0    0    BusinessUnits_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('v1."BusinessUnits_id_seq"', 1, false);
          v1          Admin    false    218            T           0    0    Columns_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('v1."Columns_id_seq"', 1, false);
          v1          Admin    false    220            U           0    0    Invites_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('v1."Invites_id_seq"', 1, false);
          v1          Admin    false    222            V           0    0    Notes_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('v1."Notes_id_seq"', 1, false);
          v1          Admin    false    224            W           0    0    Roles_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('v1."Roles_id_seq"', 1, false);
          v1          Admin    false    227            X           0    0    UserBusinessUnits_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('v1."UserBusinessUnits_id_seq"', 1, false);
          v1          Admin    false    229            Y           0    0    Users_id_seq    SEQUENCE SET     8   SELECT pg_catalog.setval('v1."Users_id_seq"', 2, true);
          v1          Admin    false    232            Z           0    0    Whiteboards_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('v1."Whiteboards_id_seq"', 1, false);
          v1          Admin    false    234            \           2606    16452    authorities Authorities_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY v1.authorities
    ADD CONSTRAINT "Authorities_pkey" PRIMARY KEY (id);
 D   ALTER TABLE ONLY v1.authorities DROP CONSTRAINT "Authorities_pkey";
       v1            Admin    false    215            `           2606    16454 !   business_units BusinessUnits_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_pkey" PRIMARY KEY (id);
 I   ALTER TABLE ONLY v1.business_units DROP CONSTRAINT "BusinessUnits_pkey";
       v1            Admin    false    217            b           2606    16456    columns Columns_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY v1.columns
    ADD CONSTRAINT "Columns_pkey" PRIMARY KEY (id);
 <   ALTER TABLE ONLY v1.columns DROP CONSTRAINT "Columns_pkey";
       v1            Admin    false    219            f           2606    16458    invites Invites_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT "Invites_pkey" PRIMARY KEY (id);
 <   ALTER TABLE ONLY v1.invites DROP CONSTRAINT "Invites_pkey";
       v1            Admin    false    221            j           2606    16460    notes Notes_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY v1.notes
    ADD CONSTRAINT "Notes_pkey" PRIMARY KEY (id);
 8   ALTER TABLE ONLY v1.notes DROP CONSTRAINT "Notes_pkey";
       v1            Admin    false    223            n           2606    16462    roles Roles_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY v1.roles
    ADD CONSTRAINT "Roles_pkey" PRIMARY KEY (id);
 8   ALTER TABLE ONLY v1.roles DROP CONSTRAINT "Roles_pkey";
       v1            Admin    false    225            t           2606    16464 +   users_business_units UserBusinessUnits_pkey 
   CONSTRAINT     g   ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT "UserBusinessUnits_pkey" PRIMARY KEY (id);
 S   ALTER TABLE ONLY v1.users_business_units DROP CONSTRAINT "UserBusinessUnits_pkey";
       v1            Admin    false    228            x           2606    16466    users Users_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY v1.users
    ADD CONSTRAINT "Users_pkey" PRIMARY KEY (id);
 8   ALTER TABLE ONLY v1.users DROP CONSTRAINT "Users_pkey";
       v1            Admin    false    230            |           2606    16468    whiteboards Whiteboards_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY v1.whiteboards
    ADD CONSTRAINT "Whiteboards_pkey" PRIMARY KEY (id);
 D   ALTER TABLE ONLY v1.whiteboards DROP CONSTRAINT "Whiteboards_pkey";
       v1            Admin    false    233            ^           2606    16470    authorities authorities_un 
   CONSTRAINT     Q   ALTER TABLE ONLY v1.authorities
    ADD CONSTRAINT authorities_un UNIQUE (name);
 @   ALTER TABLE ONLY v1.authorities DROP CONSTRAINT authorities_un;
       v1            Admin    false    215            d           2606    16472    columns columns_un 
   CONSTRAINT     _   ALTER TABLE ONLY v1.columns
    ADD CONSTRAINT columns_un UNIQUE (whiteboards_id, "position");
 8   ALTER TABLE ONLY v1.columns DROP CONSTRAINT columns_un;
       v1            Admin    false    219    219            h           2606    16474    invites invites_un 
   CONSTRAINT     c   ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT invites_un UNIQUE (receiver_id, business_units_id);
 8   ALTER TABLE ONLY v1.invites DROP CONSTRAINT invites_un;
       v1            Admin    false    221    221            l           2606    16476    notes notes_un 
   CONSTRAINT     W   ALTER TABLE ONLY v1.notes
    ADD CONSTRAINT notes_un UNIQUE (columns_id, "position");
 4   ALTER TABLE ONLY v1.notes DROP CONSTRAINT notes_un;
       v1            Admin    false    223    223            p           2606    16478    roles roles_un 
   CONSTRAINT     X   ALTER TABLE ONLY v1.roles
    ADD CONSTRAINT roles_un UNIQUE (name, business_units_id);
 4   ALTER TABLE ONLY v1.roles DROP CONSTRAINT roles_un;
       v1            Admin    false    225    225            r           2606    16480 %   roles_authorities rolesauthorities_pk 
   CONSTRAINT     u   ALTER TABLE ONLY v1.roles_authorities
    ADD CONSTRAINT rolesauthorities_pk PRIMARY KEY (roles_id, authorities_id);
 K   ALTER TABLE ONLY v1.roles_authorities DROP CONSTRAINT rolesauthorities_pk;
       v1            Admin    false    226    226            v           2606    16482 )   users_business_units userbusinessunits_un 
   CONSTRAINT     w   ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT userbusinessunits_un UNIQUE (users_id, business_units_id);
 O   ALTER TABLE ONLY v1.users_business_units DROP CONSTRAINT userbusinessunits_un;
       v1            Admin    false    228    228            z           2606    16484    users users_un 
   CONSTRAINT     F   ALTER TABLE ONLY v1.users
    ADD CONSTRAINT users_un UNIQUE (email);
 4   ALTER TABLE ONLY v1.users DROP CONSTRAINT users_un;
       v1            Admin    false    230            }           2606    16485 -   business_units BusinessUnits_companiesid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_companiesid_fkey" FOREIGN KEY (companies_id) REFERENCES v1.business_units(id);
 U   ALTER TABLE ONLY v1.business_units DROP CONSTRAINT "BusinessUnits_companiesid_fkey";
       v1          Admin    false    217    4704    217            ~           2606    16490 ,   business_units BusinessUnits_projectsid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_projectsid_fkey" FOREIGN KEY (projects_id) REFERENCES v1.business_units(id);
 T   ALTER TABLE ONLY v1.business_units DROP CONSTRAINT "BusinessUnits_projectsid_fkey";
       v1          Admin    false    4704    217    217                       2606    16495 /   business_units BusinessUnits_whiteboardsid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.business_units
    ADD CONSTRAINT "BusinessUnits_whiteboardsid_fkey" FOREIGN KEY (whiteboards_id) REFERENCES v1.whiteboards(id);
 W   ALTER TABLE ONLY v1.business_units DROP CONSTRAINT "BusinessUnits_whiteboardsid_fkey";
       v1          Admin    false    233    217    4732            �           2606    16500 "   columns Columns_whiteboardsid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.columns
    ADD CONSTRAINT "Columns_whiteboardsid_fkey" FOREIGN KEY (whiteboards_id) REFERENCES v1.whiteboards(id);
 J   ALTER TABLE ONLY v1.columns DROP CONSTRAINT "Columns_whiteboardsid_fkey";
       v1          Admin    false    233    4732    219            �           2606    16505 $   invites Invites_businessunitsid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT "Invites_businessunitsid_fkey" FOREIGN KEY (business_units_id) REFERENCES v1.business_units(id);
 L   ALTER TABLE ONLY v1.invites DROP CONSTRAINT "Invites_businessunitsid_fkey";
       v1          Admin    false    217    221    4704            �           2606    16510    invites Invites_receiverid_fkey    FK CONSTRAINT     |   ALTER TABLE ONLY v1.invites
    ADD CONSTRAINT "Invites_receiverid_fkey" FOREIGN KEY (receiver_id) REFERENCES v1.users(id);
 G   ALTER TABLE ONLY v1.invites DROP CONSTRAINT "Invites_receiverid_fkey";
       v1          Admin    false    4728    221    230            �           2606    16515    notes Notes_columnsid_fkey    FK CONSTRAINT     x   ALTER TABLE ONLY v1.notes
    ADD CONSTRAINT "Notes_columnsid_fkey" FOREIGN KEY (columns_id) REFERENCES v1.columns(id);
 B   ALTER TABLE ONLY v1.notes DROP CONSTRAINT "Notes_columnsid_fkey";
       v1          Admin    false    223    4706    219            �           2606    16520 5   roles_authorities RolesAuthorities_authoritiesid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.roles_authorities
    ADD CONSTRAINT "RolesAuthorities_authoritiesid_fkey" FOREIGN KEY (authorities_id) REFERENCES v1.authorities(id);
 ]   ALTER TABLE ONLY v1.roles_authorities DROP CONSTRAINT "RolesAuthorities_authoritiesid_fkey";
       v1          Admin    false    4700    215    226            �           2606    16525 /   roles_authorities RolesAuthorities_rolesid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.roles_authorities
    ADD CONSTRAINT "RolesAuthorities_rolesid_fkey" FOREIGN KEY (roles_id) REFERENCES v1.roles(id);
 W   ALTER TABLE ONLY v1.roles_authorities DROP CONSTRAINT "RolesAuthorities_rolesid_fkey";
       v1          Admin    false    226    4718    225            �           2606    16530     roles Roles_businessunitsid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.roles
    ADD CONSTRAINT "Roles_businessunitsid_fkey" FOREIGN KEY (business_units_id) REFERENCES v1.business_units(id);
 H   ALTER TABLE ONLY v1.roles DROP CONSTRAINT "Roles_businessunitsid_fkey";
       v1          Admin    false    225    217    4704            �           2606    16535 >   users_business_units_roles UserBusinessUnitsRoles_rolesid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.users_business_units_roles
    ADD CONSTRAINT "UserBusinessUnitsRoles_rolesid_fkey" FOREIGN KEY (roles_id) REFERENCES v1.roles(id);
 f   ALTER TABLE ONLY v1.users_business_units_roles DROP CONSTRAINT "UserBusinessUnitsRoles_rolesid_fkey";
       v1          Admin    false    231    225    4718            �           2606    16540 J   users_business_units_roles UserBusinessUnitsRoles_userbusinessunitsid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.users_business_units_roles
    ADD CONSTRAINT "UserBusinessUnitsRoles_userbusinessunitsid_fkey" FOREIGN KEY (users_business_units_id) REFERENCES v1.users_business_units(id);
 r   ALTER TABLE ONLY v1.users_business_units_roles DROP CONSTRAINT "UserBusinessUnitsRoles_userbusinessunitsid_fkey";
       v1          Admin    false    228    231    4724            �           2606    16545 ;   users_business_units UserBusinessUnits_businessunitsid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT "UserBusinessUnits_businessunitsid_fkey" FOREIGN KEY (business_units_id) REFERENCES v1.business_units(id);
 c   ALTER TABLE ONLY v1.users_business_units DROP CONSTRAINT "UserBusinessUnits_businessunitsid_fkey";
       v1          Admin    false    228    217    4704            �           2606    16550 3   users_business_units UserBusinessUnits_usersid_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY v1.users_business_units
    ADD CONSTRAINT "UserBusinessUnits_usersid_fkey" FOREIGN KEY (users_id) REFERENCES v1.users(id);
 [   ALTER TABLE ONLY v1.users_business_units DROP CONSTRAINT "UserBusinessUnits_usersid_fkey";
       v1          Admin    false    230    4728    228               }   x�Uλ
�@��z�a�xAm]��B��=d���>�")b��s8;
I��6�R�+
q���hP>�Х�xLb��t� ǹ�jM�:L��eO��@WQ��@�����f[d{j�w`C�+�o��b�k?7�            x������ � �            x������ � �             x������ � �      "      x������ � �      $      x������ � �      %      x������ � �      )      x������ � �      '      x������ � �      *      x������ � �      ,      x������ � �     