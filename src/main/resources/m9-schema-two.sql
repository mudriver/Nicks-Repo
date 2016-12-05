CREATE TABLE "currency" (
	"uid" bigint NOT NULL,
	"iso_code" char(3) NOT NULL,
	"name" varchar(20) NOT NULL,
	CONSTRAINT currency_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "country" (
	"uid" bigint NOT NULL,
	"iso_code" char(2) NOT NULL,
	"name" varchar(30) NOT NULL,
	"currency_id" bigint NOT NULL,
	"region_id" bigint NOT NULL,
	"tax_name" varchar(10) NOT NULL,
	"tax_pcent" numeric(16,8) NOT NULL,
	CONSTRAINT country_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "region" (
	"uid" bigint NOT NULL,
	"shortname" varchar(15) NOT NULL,
	"name" varchar(40) NOT NULL,
	CONSTRAINT region_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "biz_entity" (
	"uid" bigint NOT NULL,
	"shortname" varchar(15) NOT NULL,
	"name" varchar(40) NOT NULL,
	"type" bigint NOT NULL,
	"country_id" bigint NOT NULL,
	CONSTRAINT biz_entity_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);


CREATE TABLE "prod_by_country" (
	"uid" bigint NOT NULL,
	"product_id" bigint NOT NULL,
	"country_id" bigint NOT NULL,
	"sales_category_id" bigint NOT NULL,
	"currency_id" bigint NOT NULL,
	"list_price" numeric(8,8) NOT NULL,
	"charge_frequency" integer NOT NULL,
	CONSTRAINT prod_by_country_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "sales_category" (
	"uid" bigint NOT NULL,
	"shortname" varchar(15) NOT NULL,
	"name" varchar(50) NOT NULL,
	"sales_group_id" bigint NOT NULL,
	CONSTRAINT sales_category_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "sales_group" (
	"uid" bigint NOT NULL,
	"shortname" varchar(15) NOT NULL,
	"name" varchar(50) NOT NULL,
	CONSTRAINT sales_group_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "pctr_rev_alloc" (
	"uid" bigint NOT NULL,
	"prod_by_ctry_id" bigint NOT NULL,
	"profit_ctr_id" bigint NOT NULL,
	"rev_recipient" bigint NOT NULL,
	"alloc_pcent" numeric(16,8) NOT NULL,
	"note" varchar NOT NULL,
	CONSTRAINT pctr_rev_alloc_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "profit_centre" (
	"uid" bigint NOT NULL,
	"shortname" varchar(15) NOT NULL,
	"name" varchar(50) NOT NULL,
	"type" char(1) NOT NULL,
	"profit_ctr_group_id" bigint NOT NULL,
	"note" varchar NOT NULL,
	CONSTRAINT profit_centre_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "profit_ctr_group" (
	"uid" bigint NOT NULL,
	"shortname" varchar(15) NOT NULL,
	"name" varchar(50) NOT NULL,
	CONSTRAINT profit_ctr_group_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "biz_entity_type" (
	"uid" bigint NOT NULL,
	"shortname" varchar(25) NOT NULL,
	CONSTRAINT biz_entity_type_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "pctr_applic_entity_types" (
	"uid" bigint NOT NULL,
	"profit_ctr_id" bigint NOT NULL,
	"biz_entity_type_id" bigint NOT NULL,
	CONSTRAINT pctr_applic_entity_types_pk PRIMARY KEY ("uid")
) WITH (
  OIDS=FALSE
);




ALTER TABLE "country" ADD CONSTRAINT "country_fk0" FOREIGN KEY ("currency_id") REFERENCES "currency"("uid");
ALTER TABLE "country" ADD CONSTRAINT "country_fk1" FOREIGN KEY ("region_id") REFERENCES "region"("uid");


ALTER TABLE "biz_entity" ADD CONSTRAINT "biz_entity_fk0" FOREIGN KEY ("type") REFERENCES "biz_entity_type"("uid");
ALTER TABLE "biz_entity" ADD CONSTRAINT "biz_entity_fk1" FOREIGN KEY ("country_id") REFERENCES "country"("uid");


ALTER TABLE "prod_by_country" ADD CONSTRAINT "prod_by_country_fk0" FOREIGN KEY ("product_id") REFERENCES "product"("uid");
ALTER TABLE "prod_by_country" ADD CONSTRAINT "prod_by_country_fk1" FOREIGN KEY ("country_id") REFERENCES "country"("uid");
ALTER TABLE "prod_by_country" ADD CONSTRAINT "prod_by_country_fk2" FOREIGN KEY ("sales_category_id") REFERENCES "sales_category"("uid");
ALTER TABLE "prod_by_country" ADD CONSTRAINT "prod_by_country_fk3" FOREIGN KEY ("currency_id") REFERENCES "currency"("uid");

ALTER TABLE "sales_category" ADD CONSTRAINT "sales_category_fk0" FOREIGN KEY ("sales_group_id") REFERENCES "sales_group"("uid");


ALTER TABLE "pctr_rev_alloc" ADD CONSTRAINT "pctr_rev_alloc_fk0" FOREIGN KEY ("prod_by_ctry_id") REFERENCES "prod_by_country"("uid");
ALTER TABLE "pctr_rev_alloc" ADD CONSTRAINT "pctr_rev_alloc_fk1" FOREIGN KEY ("profit_ctr_id") REFERENCES "profit_centre"("uid");
ALTER TABLE "pctr_rev_alloc" ADD CONSTRAINT "pctr_rev_alloc_fk2" FOREIGN KEY ("rev_recipient") REFERENCES "biz_entity"("uid");

ALTER TABLE "profit_centre" ADD CONSTRAINT "profit_centre_fk0" FOREIGN KEY ("profit_ctr_group_id") REFERENCES "profit_ctr_group"("uid");



ALTER TABLE "pctr_applic_entity_types" ADD CONSTRAINT "pctr_applic_entity_types_fk0" FOREIGN KEY ("profit_ctr_id") REFERENCES "profit_centre"("uid");
ALTER TABLE "pctr_applic_entity_types" ADD CONSTRAINT "pctr_applic_entity_types_fk1" FOREIGN KEY ("biz_entity_type_id") REFERENCES "biz_entity_type"("uid");
