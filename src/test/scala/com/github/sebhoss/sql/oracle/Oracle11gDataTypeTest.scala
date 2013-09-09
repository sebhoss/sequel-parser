package com.github.sebhoss.sql.oracle

class Oracle11gDataTypeTest extends Oracle11gTest {

  def rule = Oracle11gDataTypeParser.datatype

  def statements = Oracle11gDataTypeTest.statements

}

object Oracle11gDataTypeTest {

  val statements = List(
    /* Oracle built-in datatypes */
    "CHAR",
    "CHAR(123)",
    "CHAR(123 BYTE)",
    "CHAR(123 CHAR)",
    "VARCHAR2(123)",
    "VARCHAR2(123 BYTE)",
    "VARCHAR2(123 CHAR)",
    "NCHAR",
    "NCHAR(123)",
    "NVARCHAR2(123)",
    "NUMBER",
    "NUMBER(123)",
    "NUMBER(123, 4)",
    "NUMBER(10, 2)",
    "NUMBER(10,2)",
    "NUMBER(10,2 )",
    "NUMBER( 10,2 )",
    "NUMBER( 10 , 2 )",
    "FLOAT",
    "FLOAT(123)",
    "BINARY_FLOAT",
    "BINARY_DOUBLE",
    "LONG",
    "LONG RAW",
    "RAW(123)",
    "DATE",
    "TIMESTAMP",
    "TIMESTAMP(123)",
    "WITH TIME ZONE",
    "WITH LOCAL TIME ZONE",
    // FIXME: Enable interval testing
//    "INTERVAL YEAR TO MONTH",
//    "INTERVAL YEAR(2011) TO MONTH",
//    "INTERVAL DAY TO SECOND",
    "BLOB",
    "CLOB",
    "NCLOB",
    "BFILE",
    "ROWID",
    "UROWID",
    "UROWID(123)",

    /* ANSI datatypes */
    "CHARACTER(123)",
    "CHARACTER VARYING (123)",
    "CHAR VARYING (123)",
    "NCHAR VARYING (123)",
    "VARCHAR(123)",
    "NATIONAL CHARACTER(123)",
    "NATIONAL CHARACTER VARYING (123)",
    "NATIONAL CHAR(123)",
    "NATIONAL CHAR VARYING (123)",
    "NUMERIC",
    "DECIMAL",
    "DEC",
    "NUMERIC(10)",
    "DECIMAL(10)",
    "DEC(10)",
    "NUMERIC(10,2)",
    "DECIMAL(10,2)",
    "DEC(10,2)",
    "INTEGER",
    "INT",
    "SMALLINT",
    "DOUBLE PRECISION",
    "REAL",

    /* Oracle supplied datatypes */
    "SYS.AnyDataSet",
    "SYS.AnyData",
    "SYS.AnyType",
    "XMLType",
    "URIType",
    "SDO_Geometry",
    "SDO_Topo_Geometry",
    "SDO_GeoRaster",
    "ORDAudio",
    "ORDImage",
    "ORDVideo",
    "ORDDoc",
    "ORDDicom",
    "SI_StillImage",
    "SI_AverageColor",
    "SI_PositionalColor",
    "SI_ColorHistogram",
    "SI_Texture",
    "SI_FeatureList",
    "SI_Color"
  )

}