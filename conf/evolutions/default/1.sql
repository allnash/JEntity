# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table json_schema (
  id                            varchar(255) not null,
  name                          varchar(255),
  string                        varchar(255),
  version                       bigint not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_json_schema primary key (id)
);

create table owner (
  id                            varchar(255) not null,
  external_id                   varchar(255),
  enabled                       integer,
  version                       bigint not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_owner primary key (id)
);

create index ix_json_schema_name on json_schema (name);
create index ix_owner_external_id on owner (external_id);

# --- !Downs

drop table if exists json_schema;

drop table if exists owner;

drop index if exists ix_json_schema_name;
drop index if exists ix_owner_external_id;
