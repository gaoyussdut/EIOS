/*
 * Copyright (C) 2006-2018 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement along with this program; if not, write to Talend SA 9 rue Pages
 * 92150 Suresnes, France
 */
package com.amalto.core.storage.record;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.mdm.commmon.metadata.*;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GaoyuTestCase extends DataRecordDataWriterTestCase {

    DataRecordJSONWriter writer;

    @Before
    public void setup() throws Exception {
        super.setup();
        writer = new DataRecordJSONWriter();
        writer.setSecurityDelegator(delegate);
        repository.load(this.getClass().getResourceAsStream("test.xsd"));
    }

    @Test
    public void testSimpleComplexType() throws Exception {
        MetadataRepository repository = new MetadataRepository();

        InputStream stream = getClass().getResourceAsStream("test.xsd");
        repository.load(stream);
//        ComplexTypeMetadata tt = repository.getComplexType("Customer");
//        ComplexTypeMetadata rr = repository.getComplexType("RR");
//        assertEquals(2, rr.getKeyFields().size());

        repository.getUserComplexTypes().forEach(complexTypeMetadata -> {
            System.out.println("+++++++++++++++++++++++++");
            System.out.println("表名：" + complexTypeMetadata.getName());
            complexTypeMetadata.getFields().forEach(fieldMetadata -> {
                System.out.print(
                        "表名：" + fieldMetadata.getEntityTypeName()
                                + "，字段名：" + fieldMetadata.getPath()
                                + ",字段类型：" + fieldMetadata.getType().getName()
                );


                try {
                    FieldMetadata reference = ((ReferenceFieldMetadata) fieldMetadata).getReferencedField();
                    if (null != reference) {
                        System.out.print(
                                "   ,关联键:" + reference.getPath()
                                        + ",关联表:" + reference.getEntityTypeName()
                        );

                        System.out.println("卧槽：" + JSON.toJSONString(((ComplexTypeMetadata) fieldMetadata)));
                    }
                } catch (Exception e) {

                }

                System.out.println();
            });
        });


//
//        tt.getLookupFields().forEach(fieldMetadata -> {
//
//        });
//        FieldMetadata e3 = tt.getField("name");
//        FieldMetadata reference = ((ReferenceFieldMetadata) e3).getReferencedField();
//        System.out.println("关联键:" + reference.getPath() + ",关联表:" + reference.getEntityTypeName());
//        int foreignKeyFields = ((CompoundFieldMetadata) reference).getFields().length;

//        assertEquals(2, foreignKeyFields);

    }

    @Test
    public void testSimpleComplexTypeWithDifferentLanguage() throws Exception {
        DataRecord record = createDataRecord(repository.getComplexType("SimpleProduct"));
        setDataRecordField(record, "Id", "12345");
        setDataRecordField(record, "Name", "1jGTpu é #dwe # é  BCLS #237 28TH & MAIN &&&& > < ,>= привет 안녕 하세요  こんにちは 你好");
        setDataRecordField(record, "Description", "Desc");
        setDataRecordField(record, "Availability", Boolean.FALSE);
        String result = toJSON(record);
        assertEquals(
                "{\"simpleproduct\":{\"id\":\"12345\",\"name\":\"1jGTpu é #dwe # é  BCLS #237 28TH & MAIN &&&& > < ,>= привет 안녕 하세요  こんにちは 你好\",\"description\":\"Desc\",\"availability\":\"false\"}}",
                result);
    }

    @Test
    public void testSimpleTypeWithArray() throws Exception {
        DataRecord record = createDataRecord(repository.getComplexType("WithArray"));
        setDataRecordField(record, "Id", "12345");
        setDataRecordField(record, "Repeat", "ABC");
        setDataRecordField(record, "Repeat", "DEF");
        String result = toJSON(record);
        assertEquals("{\"witharray\":{\"id\":\"12345\",\"repeat\":[\"ABC\",\"DEF\"]}}", result);
    }

    @Test
    public void testSimpleTypeWithNullArray() throws Exception {
        DataRecord record = createDataRecord(repository.getComplexType("WithArray"));
        setDataRecordField(record, "Id", "12345");
        String result = toJSON(record);
        assertEquals("{\"witharray\":{\"id\":\"12345\"}}", result);
    }

    @Test
    public void testSimpleTypeWithEmptyArray() throws Exception {
        DataRecord record = createDataRecord(repository.getComplexType("WithArray"));
        setDataRecordField(record, "Id", "12345");
        setDataRecordField(record, "Repeat", new ArrayList<String>());
        String result = toJSON(record);
        assertEquals("{\"witharray\":{\"id\":\"12345\",\"repeat\":[]}}", result);
    }

    @Test
    public void testTypeWithComplexType() throws Exception {
        DataRecord record = createDataRecord(repository.getComplexType("Customer"));
        setDataRecordField(record, "Id", "12345");
        setDataRecordField(record, "Name", "Name");
        String result = toJSON(record);
        assertEquals("{\"customer\":{\"id\":\"12345\",\"name\":\"Name\",\"address\":{}}}", result);
    }

    @Test
    public void testContainedType() throws Exception {
        ComplexTypeMetadata type = repository.getComplexType("WithContained");
        DataRecord record = createDataRecord(type);
        setDataRecordField(record, "Id", "ABCD");

        FieldMetadata field = type.getField("Contained");
        Assert.assertTrue(field instanceof ContainedTypeFieldMetadata);
        ContainedTypeFieldMetadata containedField = (ContainedTypeFieldMetadata) field;
        ContainedComplexTypeMetadata tm = (ContainedComplexTypeMetadata) containedField.getType();

        DataRecord contained = createDataRecord(tm);
        setDataRecordField(contained, "ContainedId", "CID");
        setDataRecordField(contained, "ContainedName", "CName");

        setDataRecordField(record, "Contained", contained);

        String result = toJSON(record);

        assertEquals("{\"withcontained\":{\"id\":\"ABCD\",\"contained\":{\"containedid\":\"CID\",\"containedname\":\"CName\"}}}", result);
    }

    @Test
    public void testMultiContainedType() throws Exception {
        ComplexTypeMetadata type = repository.getComplexType("WithMultiContained");
        DataRecord record = createDataRecord(type);
        setDataRecordField(record, "Id", "ABCD");
        FieldMetadata field = type.getField("Contained");
        Assert.assertTrue(field instanceof ContainedTypeFieldMetadata);
        ContainedTypeFieldMetadata containedField = (ContainedTypeFieldMetadata) field;
        ContainedComplexTypeMetadata tm = (ContainedComplexTypeMetadata) containedField.getType();
        List<DataRecord> list = new ArrayList<DataRecord>();
        DataRecord contained1 = createDataRecord(tm);
        setDataRecordField(contained1, "ContainedId", "CID1");
        setDataRecordField(contained1, "ContainedName", "CName1");
        list.add(contained1);

        DataRecord contained2 = createDataRecord(tm);
        setDataRecordField(contained2, "ContainedId", "CID2");
        setDataRecordField(contained2, "ContainedName", "CName2");
        list.add(contained2);

        setDataRecordField(record, "Contained", list);

        String result = toJSON(record);

        assertEquals("{\"withmulticontained\":{\"id\":\"ABCD\",\"contained\":"
                + "[{\"containedid\":\"CID1\",\"containedname\":\"CName1\"},"
                + "{\"containedid\":\"CID2\",\"containedname\":\"CName2\"}]}}", result);
    }

    @Test
    public void testMultiContainedTypeWithNullList() throws Exception {
        ComplexTypeMetadata type = repository.getComplexType("WithMultiContained");
        DataRecord record = createDataRecord(type);
        setDataRecordField(record, "Id", "ABCD");
        FieldMetadata field = type.getField("Contained");
        Assert.assertTrue(field instanceof ContainedTypeFieldMetadata);
        ContainedTypeFieldMetadata containedField = (ContainedTypeFieldMetadata) field;
        ContainedComplexTypeMetadata tm = (ContainedComplexTypeMetadata) containedField.getType();

        String result = toJSON(record);

        assertEquals("{\"withmulticontained\":{\"id\":\"ABCD\",\"contained\":"
                + "[]}}", result);
    }

    @Test
    public void testMultiContainedTypeWithEmptyList() throws Exception {
        ComplexTypeMetadata type = repository.getComplexType("WithMultiContained");
        DataRecord record = createDataRecord(type);
        setDataRecordField(record, "Id", "ABCD");
        FieldMetadata field = type.getField("Contained");
        Assert.assertTrue(field instanceof ContainedTypeFieldMetadata);
        ContainedTypeFieldMetadata containedField = (ContainedTypeFieldMetadata) field;
        ContainedComplexTypeMetadata tm = (ContainedComplexTypeMetadata) containedField.getType();

        setDataRecordField(record, "Contained", new ArrayList<DataRecord>());

        String result = toJSON(record);

        assertEquals("{\"withmulticontained\":{\"id\":\"ABCD\",\"contained\":"
                + "[]}}", result);
    }

    private String toJSON(DataRecord record) throws Exception {
        Writer w = new StringWriter();
        writer.write(record, w);
        return w.toString();
    }

}
