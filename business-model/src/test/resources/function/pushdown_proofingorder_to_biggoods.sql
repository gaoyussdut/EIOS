CREATE OR REPLACE FUNCTION "public"."pushdown_proofingorder_to_biggoods"("fromtokenid" varchar, "totokenid" varchar, "status" varchar)
  RETURNS "pg_catalog"."bool" AS $BODY$
declare
  toentryid   varchar;
  fromentryid varchar;
  insid  varchar;
  tokentemplateid varchar;
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



INSERT INTO t_bill_token_ins(bill_meta_id,bill_token_id,ins_id,ins_name,type) VALUES 
('fs_proofing_order_empty_po',totokenid,insid,'dddd',status);

INSERT INTO t_edge_ins(ins_id,from_po_meta,to_po_meta,from_token_id,to_token_id) 
VALUES(insid,'fs_proofing_order_po','fs_proofing_order_empty_po',fromtokenid,totokenid);

INSERT INTO t_token(token_id,table_name,is_valid,status) VALUES(totokenid,'fs_proofing_bill',true,'DRAFT');


INSERT INTO t_edge_ins(ins_id,from_po_meta,to_po_meta,from_token_id,to_token_id) 
VALUES(insid,'fs_proofing_order_po','fs_proofing_order_empty_po',fromentryid,toentryid);

INSERT INTO t_token(token_id,table_name,is_valid,status) VALUES(toentryid,'fs_proofing_entry',true,'DRAFT');


  RETURN true;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100