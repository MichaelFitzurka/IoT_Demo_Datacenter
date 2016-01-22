
CREATE TABLE "temperature" (
    pid                  SERIAL PRIMARY KEY,
    deviceID            DECIMAL,
    timestamp            VARCHAR,
    value                INTEGER,
    average		 REAL
);

ALTER TABLE public."temperature" OWNER TO psteiner;

CREATE SEQUENCE "temperature_pid_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public."temperature_pid_seq1" OWNER TO psteiner;
ALTER SEQUENCE "temperatureRate_pid_seq1" OWNED BY "temperature".pid;
ALTER TABLE ONLY "temperature" ALTER COLUMN pid SET DEFAULT nextval('"temperature_pid_seq1"'::regclass);
ALTER TABLE ONLY "temperature"
    ADD CONSTRAINT "temperature_pkey" PRIMARY KEY (pid);

CREATE TABLE "humidity" (
    pid                  SERIAL PRIMARY KEY,
    deviceID            DECIMAL,
    timestamp            VARCHAR,
    value                INTEGER,
    average		 REAL
);

ALTER TABLE public."humidity" OWNER TO psteiner;

CREATE SEQUENCE "humidity_pid_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public."humidity_pid_seq1" OWNER TO psteiner;
ALTER SEQUENCE "humidity_pid_seq1" OWNED BY "humidity".pid;
ALTER TABLE ONLY "humidity" ALTER COLUMN pid SET DEFAULT nextval('"humidity_pid_seq1"'::regclass);
ALTER TABLE ONLY "temperature"
    ADD CONSTRAINT "humidity_pkey" PRIMARY KEY (pid);

CREATE TABLE "voltage" (
    pid                  SERIAL PRIMARY KEY,
    deviceID            DECIMAL,
    timestamp            VARCHAR,
    value                INTEGER,
    average		 REAL
);

ALTER TABLE public."voltage" OWNER TO psteiner;

CREATE SEQUENCE "voltage_pid_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public."voltage_pid_seq1" OWNER TO psteiner;
ALTER SEQUENCE "voltage_pid_seq1" OWNED BY "voltage".pid;
ALTER TABLE ONLY "voltage" ALTER COLUMN pid SET DEFAULT nextval('"voltage_pid_seq1"'::regclass);
ALTER TABLE ONLY "voltage"
    ADD CONSTRAINT "voltage_pkey" PRIMARY KEY (pid);
