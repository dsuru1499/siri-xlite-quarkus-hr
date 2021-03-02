--
-- PostgreSQL database dump
--

-- Dumped from database version 12.6
-- Dumped by pg_dump version 12.6

--
-- Name: call; Type: TABLE; Schema: public; Owner: siri-xlite
--

CREATE TABLE public.call (
    id integer NOT NULL,
    actualarrivaltime time without time zone,
    actualdeparturetime time without time zone,
    aimedarrivaltime time without time zone,
    aimeddeparturetime time without time zone,
    aimedheadwayinterval bigint,
    arrivalplatformname character varying(255),
    arrivalproximitytext character varying(255),
    arrivalstatus integer,
    cancellation boolean,
    departureboardingactivity integer,
    departureplatformname character varying(255),
    departurestatus integer,
    destinationdisplay character varying(255),
    distancefromstop bigint,
    expectedarrivaltime time without time zone,
    expecteddeparturetime time without time zone,
    expectedheadwayinterval bigint,
    extracall boolean,
    index integer,
    numberofstopsaway bigint,
    sequence integer,
    origindisplay character varying(255),
    platformtraversal boolean,
    situationrefs text,
    vehicleatstop boolean,
    stoppoint_stoppointref character varying(255),
    vehiclejourney_datedvehiclejourneyref character varying(255)
);


ALTER TABLE public.call OWNER TO "siri-xlite";

--
-- Name: call_seq; Type: SEQUENCE; Schema: public; Owner: siri-xlite
--

CREATE SEQUENCE public.call_seq
    START WITH 1
    INCREMENT BY 100
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.call_seq OWNER TO "siri-xlite";

--
-- Name: destination; Type: TABLE; Schema: public; Owner: siri-xlite
--

CREATE TABLE public.destination (
    id integer NOT NULL,
    destinationref character varying(255),
    placename character varying(255),
    line_lineref character varying(255)
);


ALTER TABLE public.destination OWNER TO "siri-xlite";

--
-- Name: destination_seq; Type: SEQUENCE; Schema: public; Owner: siri-xlite
--

CREATE SEQUENCE public.destination_seq
    START WITH 1
    INCREMENT BY 100
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.destination_seq OWNER TO "siri-xlite";

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: siri-xlite
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO "siri-xlite";

--
-- Name: interchange_seq; Type: SEQUENCE; Schema: public; Owner: siri-xlite
--

CREATE SEQUENCE public.interchange_seq
    START WITH 1
    INCREMENT BY 100
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.interchange_seq OWNER TO "siri-xlite";

--
-- Name: journeypart; Type: TABLE; Schema: public; Owner: siri-xlite
--

CREATE TABLE public.journeypart (
    id integer NOT NULL,
    journeypartref character varying(255),
    trainnumberref character varying(255),
    vehiclejourney_datedvehiclejourneyref character varying(255)
);


ALTER TABLE public.journeypart OWNER TO "siri-xlite";

--
-- Name: journeypart_seq; Type: SEQUENCE; Schema: public; Owner: siri-xlite
--

CREATE SEQUENCE public.journeypart_seq
    START WITH 1
    INCREMENT BY 100
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.journeypart_seq OWNER TO "siri-xlite";

--
-- Name: line; Type: TABLE; Schema: public; Owner: siri-xlite
--

CREATE TABLE public.line (
    lineref character varying(255) NOT NULL,
    linename character varying(255),
    monitored boolean,
    recordedattime timestamp without time zone
);


ALTER TABLE public.line OWNER TO "siri-xlite";

--
-- Name: line_seq; Type: SEQUENCE; Schema: public; Owner: siri-xlite
--

CREATE SEQUENCE public.line_seq
    START WITH 1
    INCREMENT BY 100
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.line_seq OWNER TO "siri-xlite";

--
-- Name: stoppoint; Type: TABLE; Schema: public; Owner: siri-xlite
--

CREATE TABLE public.stoppoint (
    stoppointref character varying(255) NOT NULL,
    linerefs text,
    latitude double precision,
    longitude double precision,
    recordedattime timestamp without time zone,
    stopname character varying(255),
    parent character varying(255)
);


ALTER TABLE public.stoppoint OWNER TO "siri-xlite";

--
-- Name: stoppoint_seq; Type: SEQUENCE; Schema: public; Owner: siri-xlite
--

CREATE SEQUENCE public.stoppoint_seq
    START WITH 1
    INCREMENT BY 1000
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.stoppoint_seq OWNER TO "siri-xlite";

--
-- Name: targetedinterchange; Type: TABLE; Schema: public; Owner: siri-xlite
--

CREATE TABLE public.targetedinterchange (
    interchangecode character varying(255) NOT NULL,
    connectioncode character varying(255),
    frequenttravellerduration bigint,
    guaranteed boolean,
    impairedaccessduration bigint,
    interchangeduration bigint,
    maximumwaittime bigint,
    occasionaltravellerduration bigint,
    recordedattime timestamp without time zone,
    stayseated boolean,
    call_id integer,
    distributorvehiclejourney_datedvehiclejourneyref character varying(255),
    stoppoint_stoppointref character varying(255)
);


ALTER TABLE public.targetedinterchange OWNER TO "siri-xlite";

--
-- Name: targetinterchange_seq; Type: SEQUENCE; Schema: public; Owner: siri-xlite
--

CREATE SEQUENCE public.targetinterchange_seq
    START WITH 1
    INCREMENT BY 100
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.targetinterchange_seq OWNER TO "siri-xlite";

--
-- Name: vehiclejourney; Type: TABLE; Schema: public; Owner: siri-xlite
--

CREATE TABLE public.vehiclejourney (
    datedvehiclejourneyref character varying(255) NOT NULL,
    bearing double precision,
    cancellation boolean,
    delay bigint,
    destinationaimedarrivaltime time without time zone,
    destinationdisplay character varying(255),
    destinationexpectedarrivaltime time without time zone,
    destinationname character varying(255),
    destinationref character varying(255),
    directionname character varying(255),
    directionref character varying(255),
    extrajourney boolean,
    firstorlastjourney integer,
    headwayservice boolean,
    incongestion boolean,
    inpanic boolean,
    journeynotes text,
    journeypatternname character varying(255),
    journeypatternref character varying(255),
    monitored boolean,
    monitoringerror character varying(255),
    occupancy integer,
    operatorref character varying(255),
    originaimeddeparturetime time without time zone,
    origindisplay character varying(255),
    originexpecteddeparturetime time without time zone,
    originname character varying(255),
    originref character varying(255),
    productcategoryref character varying(255),
    publishedlinename character varying(255),
    recordedattime timestamp without time zone,
    routeref character varying(255),
    servicefeaturerefs text,
    situationrefs text,
    trainnumbers text,
    vehiclefeaturerefs text,
    vehiclejourneyname character varying(255),
    latitude double precision,
    longitude double precision,
    vehiclemodes integer,
    line_lineref character varying(255)
);


ALTER TABLE public.vehiclejourney OWNER TO "siri-xlite";

--
-- Name: vehiclejourney_seq; Type: SEQUENCE; Schema: public; Owner: siri-xlite
--

CREATE SEQUENCE public.vehiclejourney_seq
    START WITH 1
    INCREMENT BY 100
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.vehiclejourney_seq OWNER TO "siri-xlite";

--
-- Name: via; Type: TABLE; Schema: public; Owner: siri-xlite
--

CREATE TABLE public.via (
    id integer NOT NULL,
    placename character varying(255),
    placeref character varying(255),
    vehiclejourney_datedvehiclejourneyref character varying(255)
);


ALTER TABLE public.via OWNER TO "siri-xlite";

--
-- Name: via_seq; Type: SEQUENCE; Schema: public; Owner: siri-xlite
--

CREATE SEQUENCE public.via_seq
    START WITH 1
    INCREMENT BY 100
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.via_seq OWNER TO "siri-xlite";

--
-- Name: call call_pkey; Type: CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.call
    ADD CONSTRAINT call_pkey PRIMARY KEY (id);


--
-- Name: destination destination_pkey; Type: CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.destination
    ADD CONSTRAINT destination_pkey PRIMARY KEY (id);


--
-- Name: journeypart journeypart_pkey; Type: CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.journeypart
    ADD CONSTRAINT journeypart_pkey PRIMARY KEY (id);


--
-- Name: line line_pkey; Type: CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.line
    ADD CONSTRAINT line_pkey PRIMARY KEY (lineref);


--
-- Name: stoppoint stoppoint_pkey; Type: CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.stoppoint
    ADD CONSTRAINT stoppoint_pkey PRIMARY KEY (stoppointref);


--
-- Name: targetedinterchange targetedinterchange_pkey; Type: CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.targetedinterchange
    ADD CONSTRAINT targetedinterchange_pkey PRIMARY KEY (interchangecode);


--
-- Name: vehiclejourney vehiclejourney_pkey; Type: CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.vehiclejourney
    ADD CONSTRAINT vehiclejourney_pkey PRIMARY KEY (datedvehiclejourneyref);


--
-- Name: via via_pkey; Type: CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.via
    ADD CONSTRAINT via_pkey PRIMARY KEY (id);


--
-- Name: call_stoppoint_stoppointref_idx; Type: INDEX; Schema: public; Owner: siri-xlite
--

CREATE INDEX call_stoppoint_stoppointref_idx ON public.call USING btree (stoppoint_stoppointref);


--
-- Name: call_vehiclejourney_datedvehiclejourneyref_idx; Type: INDEX; Schema: public; Owner: siri-xlite
--

CREATE INDEX call_vehiclejourney_datedvehiclejourneyref_idx ON public.call USING btree (vehiclejourney_datedvehiclejourneyref);


--
-- Name: destination_line_lineref_idx; Type: INDEX; Schema: public; Owner: siri-xlite
--

CREATE INDEX destination_line_lineref_idx ON public.destination USING btree (line_lineref);


--
-- Name: journeypart_vehiclejourney_datedvehiclejourneyref_idx; Type: INDEX; Schema: public; Owner: siri-xlite
--

CREATE INDEX journeypart_vehiclejourney_datedvehiclejourneyref_idx ON public.journeypart USING btree (vehiclejourney_datedvehiclejourneyref);


--
-- Name: stoppoint_latitude_idx; Type: INDEX; Schema: public; Owner: siri-xlite
--

CREATE INDEX stoppoint_latitude_idx ON public.stoppoint USING btree (latitude);


--
-- Name: stoppoint_longitude_idx; Type: INDEX; Schema: public; Owner: siri-xlite
--

CREATE INDEX stoppoint_longitude_idx ON public.stoppoint USING btree (longitude);


--
-- Name: stoppoint_parent_idx; Type: INDEX; Schema: public; Owner: siri-xlite
--

CREATE INDEX stoppoint_parent_idx ON public.stoppoint USING btree (parent);


--
-- Name: vehiclejourney_line_lineref_idx; Type: INDEX; Schema: public; Owner: siri-xlite
--

CREATE INDEX vehiclejourney_line_lineref_idx ON public.vehiclejourney USING btree (line_lineref);


--
-- Name: via_vehiclejourney_datedvehiclejourneyref_idx; Type: INDEX; Schema: public; Owner: siri-xlite
--

CREATE INDEX via_vehiclejourney_datedvehiclejourneyref_idx ON public.via USING btree (vehiclejourney_datedvehiclejourneyref);


--
-- Name: destination destination_line_lineref_fkey; Type: FK CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.destination
    ADD CONSTRAINT destination_line_lineref_fkey FOREIGN KEY (line_lineref) REFERENCES public.line(lineref);


--
-- Name: targetedinterchange fk7c936plcttkubgcvht0vi60is; Type: FK CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.targetedinterchange
    ADD CONSTRAINT fk7c936plcttkubgcvht0vi60is FOREIGN KEY (distributorvehiclejourney_datedvehiclejourneyref) REFERENCES public.vehiclejourney(datedvehiclejourneyref);


--
-- Name: targetedinterchange fk9rtiv48aub7a8ya0w24beou27; Type: FK CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.targetedinterchange
    ADD CONSTRAINT fk9rtiv48aub7a8ya0w24beou27 FOREIGN KEY (call_id) REFERENCES public.call(id);


--
-- Name: call fkemb5dta4x350378h6lfj6vs4; Type: FK CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.call
    ADD CONSTRAINT fkemb5dta4x350378h6lfj6vs4 FOREIGN KEY (vehiclejourney_datedvehiclejourneyref) REFERENCES public.vehiclejourney(datedvehiclejourneyref);


--
-- Name: journeypart fkfg61q1ivn81v7iqmcugmd5fmo; Type: FK CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.journeypart
    ADD CONSTRAINT fkfg61q1ivn81v7iqmcugmd5fmo FOREIGN KEY (vehiclejourney_datedvehiclejourneyref) REFERENCES public.vehiclejourney(datedvehiclejourneyref);


--
-- Name: targetedinterchange fkmfprim4s8ylsyym4cxa5ysutc; Type: FK CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.targetedinterchange
    ADD CONSTRAINT fkmfprim4s8ylsyym4cxa5ysutc FOREIGN KEY (stoppoint_stoppointref) REFERENCES public.stoppoint(stoppointref);


--
-- Name: via fknw4iqb08e2pg5ahu3wvxc163r; Type: FK CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.via
    ADD CONSTRAINT fknw4iqb08e2pg5ahu3wvxc163r FOREIGN KEY (vehiclejourney_datedvehiclejourneyref) REFERENCES public.vehiclejourney(datedvehiclejourneyref);


--
-- Name: vehiclejourney fko8c4fc1mae7fdq7brnhb3fb3t; Type: FK CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.vehiclejourney
    ADD CONSTRAINT fko8c4fc1mae7fdq7brnhb3fb3t FOREIGN KEY (line_lineref) REFERENCES public.line(lineref);


--
-- Name: call fkqq39y0p9tk6orgk5gy64cb97t; Type: FK CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.call
    ADD CONSTRAINT fkqq39y0p9tk6orgk5gy64cb97t FOREIGN KEY (stoppoint_stoppointref) REFERENCES public.stoppoint(stoppointref);


--
-- Name: stoppoint stoppoint_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: siri-xlite
--

ALTER TABLE ONLY public.stoppoint
    ADD CONSTRAINT stoppoint_parent_fkey FOREIGN KEY (parent) REFERENCES public.stoppoint(stoppointref);


--
-- PostgreSQL database dump complete
--

