--建表说明
--实体表应该以t_开头
--关系表应该以r_开头
--里面字段为全部小写以下划线分割
--流程实体
DROP TABLE IF EXISTS t_process_ins;
CREATE TABLE t_process_ins
(
  process_ins_id     VARCHAR NOT NULL
    CONSTRAINT process_ins_id_pkey
    PRIMARY KEY,
  process_id VARCHAR NOT NULL
);

--流程定义
DROP TABLE IF EXISTS t_process_definition;
CREATE TABLE t_process_definition
(
  process_id     VARCHAR NOT NULL
    CONSTRAINT process_id_pkey
    PRIMARY KEY,
  process_name VARCHAR NOT NULL,
  process_type VARCHAR NOT NULL,
  status VARCHAR NOT NULL
);

DROP TABLE IF EXISTS t_process_edge_definition;
CREATE TABLE t_process_edge_definition
(
  edge_id     VARCHAR NOT NULL
    CONSTRAINT edge_id_pkey
    PRIMARY KEY,
  process_id VARCHAR NOT NULL,
  current_tokentemplate_id VARCHAR NOT NULL,
  next_tokentemplate_id VARCHAR NOT NULL,
  from_ VARCHAR NOT NULL,
  to_ VARCHAR NOT NULL,
  weight VARCHAR,
  order_ INT NOT NULL
);

DROP TABLE IF EXISTS t_process_taskcenter_definition;
CREATE TABLE t_process_taskcenter_definition
(
  process_taskcenter_id VARCHAR NOT NULL
    CONSTRAINT process_taskcenter_id_pkey
    PRIMARY KEY,
  taskcenter_id VARCHAR NOT NULL,
  taskcenter_name VARCHAR NOT NULL,
  order_ INT NOT NULL
);

DROP TABLE IF EXISTS t_process_vertex_definition;
CREATE TABLE t_process_vertex_definition
(
  vertex_id VARCHAR NOT NULL
    CONSTRAINT vertex_id_pkey
    PRIMARY KEY,
  process_id VARCHAR NOT NULL,
  tokentemplate_id VARCHAR NOT NULL,
  tokentemplate_name VARCHAR NOT NULL,
  place_type VARCHAR NOT NULL,
  order_ INT NOT NULL
);

DROP TABLE IF EXISTS r_procvertex_proctc;
CREATE TABLE r_procvertex_proctc
(
  rel_id serial 
    CONSTRAINT rel_id_pkey
    PRIMARY KEY,
  vertex_id VARCHAR NOT NULL,
  proc_tc_id VARCHAR NOT NULL,
  process_id VARCHAR NOT NULL
);


-- meta部分建表语句
DROP TABLE IF EXISTS t_token_meta_formation;
create table t_token_meta_formation
(
  token_meta_id   varchar not null
    constraint t_token_meta_fkeys_pkey
    primary key,
  token_meta_name varchar,
  meta_type       varchar
);

DROP TABLE IF EXISTS t_token_meta_fkey_type;
create table t_token_meta_fkey_type
(
  key  varchar not null
    constraint t_token_meta_fkey_type_pkey
    primary key,
  type varchar
);

DROP TABLE IF EXISTS t_token_meta_fkey_caption;
create table t_token_meta_fkey_caption
(
  key  varchar not null
    constraint t_token_meta_fkey_type_pkey
    primary key,
  caption varchar
);




DROP TABLE IF EXISTS t_token_meta_fkey;
create table t_token_meta_fkey
(
  id serial PRIMARY KEY,
  meta_id   varchar,
  key       varchar,
  caption   varchar,
  fkey_type varchar
);

DROP TABLE IF EXISTS t_token_meta_permissions;
create table t_token_meta_permissions
(
  id serial PRIMARY KEY,
  token_meta_id varchar,
  key           varchar,
  visible       boolean,
  readonly      boolean,
  required      boolean
);

DROP TABLE IF EXISTS t_token_meta_ralvalue;
create table t_token_meta_ralvalue
(
  id serial PRIMARY KEY,
  token_meta_id varchar,
  key           varchar,
  meta_name     varchar,
  fkey          varchar,
  meta_key      varchar
);

DROP TABLE IF EXISTS t_talend_model_meta;
create table t_talend_model_meta
(
  id serial PRIMARY KEY,
  x_pk_x_talend_id   varchar,
	meta_id VARCHAR,
	meta_json json
);

-- 单据定义建表语句
DROP TABLE IF EXISTS r_place_tokentemplate;
create table r_place_tokentemplate
(
  place_id          varchar not null
    constraint r_place_tokentemplate_pkey
    primary key,
  process_ins_id    varchar,
  token_template_id varchar,
  status_of_place varchar,
  status_of_process varchar,
  vertex_id varchar
);

DROP TABLE IF EXISTS r_processins_token;
create table r_processins_token
(
  id serial PRIMARY KEY,
  process_ins_id varchar,
  meta_id        varchar,
  token_id       varchar,
  status         varchar,
  is_valid		 boolean
);

DROP TABLE IF EXISTS r_tokentemplate_meta;
create table r_tokentemplate_meta
(
  id serial PRIMARY KEY,
  token_template_id varchar,
  meta_id           varchar,
  self_id           integer not null,
  parent_id         integer not null,
  token_template_name varchar
);


-- WBS和PO

DROP TABLE IF EXISTS t_po_meta_definition;
CREATE TABLE t_po_meta_definition
(
  meta_id VARCHAR NOT NULL
    CONSTRAINT meta_id_pkey
    PRIMARY KEY,
  po_type VARCHAR NOT NULL,
  is_ins BOOL NOT NULL
);

DROP TABLE IF EXISTS t_po_meta_ins;
CREATE TABLE t_po_meta_ins
(
  po_token_id VARCHAR NOT NULL
    CONSTRAINT po_token_id_pkey
    PRIMARY KEY,
  meta_id VARCHAR NOT NULL,
  po_type VARCHAR NOT NULL,
  is_ins BOOL NOT NULL,
  status integer NOT NULL
);

DROP TABLE IF EXISTS t_wbs_definition;
CREATE TABLE t_wbs_definition
(
  id VARCHAR NOT NULL
    CONSTRAINT t_wbs_definition_pkey
    PRIMARY KEY,
  wbs_id VARCHAR NOT NULL,
  wbs_name VARCHAR NOT NULL,
  from_token_meta VARCHAR NOT NULL,
  to_token_meta VARCHAR NOT NULL
);

DROP TABLE IF EXISTS t_wbs_ins;
CREATE TABLE t_wbs_ins
(
  id VARCHAR NOT NULL
    CONSTRAINT t_wbs_ins_pkey
    PRIMARY KEY,
  wbs_ins_id VARCHAR NOT NULL,
  wbs_ins_name VARCHAR NOT NULL,
  from_token_meta VARCHAR NOT NULL,
  to_token_meta VARCHAR NOT NULL,
  from_token_id VARCHAR NOT NULL,
  to_token_id VARCHAR NOT NULL
);

DROP TABLE IF EXISTS r_wbs_wbsins;
CREATE TABLE r_wbs_wbsins
(
  rel_id VARCHAR NOT NULL
    CONSTRAINT r_wbs_wbsins_pkey
    PRIMARY KEY,
  wbs_id VARCHAR NOT NULL,
  wbsins_id VARCHAR NOT NULL
);

DROP TABLE IF EXISTS r_po_process;
CREATE TABLE r_po_process
(
  rel_id VARCHAR NOT NULL
    CONSTRAINT r_po_proces_pkey
    PRIMARY KEY,
  meta_id VARCHAR NOT NULL,
  process_id VARCHAR NOT NULL,
  status INT NOT NULL
)

DROP TABLE IF EXISTS r_poins_processins;
CREATE TABLE r_poins_processins
(
  rel_id VARCHAR NOT NULL
    CONSTRAINT r_poins_processins_pkey
    PRIMARY KEY,
  po_token_id VARCHAR NOT NULL,
  process_ins_id VARCHAR NOT NULL,
  status INT NOT NULL
)

DROP TABLE IF EXISTS t_process_table;
CREATE TABLE t_process_table
(
  id VARCHAR NOT NULL
    CONSTRAINT t_process_table_pkey
    PRIMARY KEY,
  process_id VARCHAR NOT NULL,
  table_name VARCHAR NOT NULL,
  vertex_id VARCHAR NOT NULL,
  meta_id VARCHAR NOT NULL
)

DROP TABLE IF EXISTS r_projectins_wbsins;
CREATE TABLE r_projectins_wbsins
(
  id integer NOT NULL,
  project_ins_id character varying,
  wbs_ins_id character varying,
  CONSTRAINT r_projectins_wbsins_pk PRIMARY KEY (id)
)

DROP TABLE IF EXISTS r_projectins_wbsins;
CREATE TABLE r_project_wbs
(
  id integer NOT NULL,
  project_id character varying,
  wbs_id character varying,
  CONSTRAINT r_project_wbs_pk PRIMARY KEY (id)
)

DROP TABLE IF EXISTS r_project_projectins;
CREATE TABLE r_project_projectins
(
  id integer NOT NULL,
  project_id character varying,
  project_ins_id character varying,
  CONSTRAINT r_project_projectins_pk PRIMARY KEY (id)
)

DROP TABLE IF EXISTS r_aoins_token;
CREATE TABLE r_aoins_token
(
  id integer NOT NULL,
  ao_token_id character varying ,
  ao_meta_id character varying,
  po_token_id character varying,
  process_ins_id character varying,
  process_id character varying,
  token_id character varying,
  meta_id character varying,
  CONSTRAINT r_aoins_token_pk PRIMARY KEY (id)
)

DROP TABLE IF EXISTS public.t_ao_definition;

CREATE TABLE public.t_ao_definition
(
  id integer NOT NULL,
  ao_meta_id character varying NOT NULL,
  po_meta_id character varying,
  process_id character varying,
  token_meta_id character varying,
  CONSTRAINT t_ao_definition_pk PRIMARY KEY (id)
)

DROP TABLE IF EXISTS public.r_aoins_token;
CREATE TABLE public.r_aoins_token
(
  ao_token_id character varying NOT NULL,
  ao_meta_id character varying,
  po_token_id character varying,
  process_ins_id character varying,
  process_id character varying,
  token_id character varying,
  meta_id character varying,
  CONSTRAINT r_aoins_token_pk PRIMARY KEY ()
)

-- 公式定义
DROP TABLE IF EXISTS t_formula_formation;
create table t_formula_formation
(
  id serial PRIMARY KEY,
  business_aspect_id varchar,
  formula   varchar,
	key_list varchar
);

-- 预定义好的公式
DROP TABLE IF EXISTS t_predefined_formation;
create table t_predefined_formation
(
  id serial PRIMARY KEY,
  formula   varchar,
	describe varchar,
	formula_type varchar
);
