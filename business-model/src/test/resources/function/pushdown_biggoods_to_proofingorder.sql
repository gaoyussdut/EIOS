CREATE OR REPLACE FUNCTION "public"."pushdown_biggoods_to_proofingorder"("fromtokenid" varchar, "totokenid" varchar, "status" varchar)
  RETURNS "pg_catalog"."bool" AS $BODY$
declare
  toentryid   varchar;
  fromentryid varchar;
  insid  varchar;
  tokentemplateid varchar;
	ordertokenid varchar;
BEGIN

  SELECT sub_token_id
  into fromentryid
  from r_parent_sub_token
  where parent_token_id = fromtokenid
  limit 1;
  toentryid := uuid();
  insert into fs_proofing_bill (
    id,
    fstyle,
    fcustomersample
    )
    select
      totokenid,
      fstyle,
      fcustomersample
    from fs_proofing_bill
    where id = fromtokenid;


SELECT ins_id into insid from t_bill_token_ins where bill_token_id = fromtokenid;

SELECT from_token_id into ordertokenid from t_edge_ins where to_token_id = fromtokenid limit 1 ;

INSERT INTO t_bill_token_ins(token_template_id,bill_token_id,ins_id,ins_name,type) VALUES
        ('TT_test_gsz2',totokenid,insid,'cccc',status);

INSERT INTO t_edge_ins(ins_id,from_token_template_id,to_token_template_id,from_token_id,to_token_id)
        VALUES(insid,'TT_test_lzs','TT_test_gsz2',ordertokenid,totokenid);

        INSERT INTO t_token(token_id,table_name,is_valid,status) VALUES(totokenid,'fs_proofing_bill',true,'DRAFT');


  RETURN true;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100