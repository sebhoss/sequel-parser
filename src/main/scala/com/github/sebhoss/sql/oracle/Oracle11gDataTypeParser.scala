package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.common.Lexer._
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._

import org.parboiled.scala._

object Oracle11gDataTypeParser extends AbstractParser {

  /** Creates a rule for Oracle schema data types.
    *
    * It matches the following grammar:
    *
    * {{{
    * { Oracle_built_in_datatypes
    * | ANSI_supported_datatypes
    * | user_defined_types
    * | Oracle_supplied_types
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm Data Types]]
    */
  def datatype = rule {
    (Oracle_built_in_datatypes |
      ANSI_supported_datatypes |
      user_defined_types |
      Oracle_supplied_types)
  }

  /** Creates a rule for built-in datatypes by Oracle.
    *
    * It matches the following grammar:
    *
    * {{{
    * { character_datatypes
    * | number_datatypes
    * | long_and_raw_datatypes
    * | datetime_datatypes
    * | large_object_datatypes
    * | rowid_datatypes
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#i54330 Built-in Datatypes]]
    */
  def Oracle_built_in_datatypes = rule {
    (character_datatypes |
      number_datatypes |
      long_and_raw_datatypes |
      datetime_datatypes |
      large_object_datatypes |
      rowid_datatypes)
  }

  /** Creates a rule for character datatypes.
    *
    * It matches the following grammar:
    *
    * {{{
    * { CHAR [ (size [ BYTE | CHAR ]) ]
    * | VARCHAR2 (size [ BYTE | CHAR ])
    * | NCHAR [ (size) ]
    * | NVARCHAR2 (size)
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#BABDAGJF Syntax Definition]]
    */
  def character_datatypes = rule {
    (("CHAR" ~ optional("(" ~ size ~ optional("BYTE" | "CHAR") ~ ")")) |
      ("VARCHAR2" ~ "(" ~ size ~ optional("BYTE" | "CHAR") ~ ")") |
      ("NCHAR" ~ optional("(" ~ size ~ ")")) |
      ("NVARCHAR2" ~ "(" ~ size ~ ")"))
  }

  /** Creates a rule for number datatypes.
    *
    * It matches the following grammar:
    *
    * {{{
    * { NUMBER [ (precision [, scale ]) ]
    * | FLOAT [ (precision) ]
    * | BINARY_FLOAT
    * | BINARY_DOUBLE
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50960 Syntax Definition]]
    */
  def number_datatypes = rule {
    (("NUMBER" ~ optional("(" ~ precision ~ optional("," ~ scale) ~ ")")) |
      ("FLOAT" ~ optional("(" ~ precision ~ ")")) |
      "BINARY_FLOAT" |
      "BINARY_DOUBLE")
  }

  /** Creates a rule for long and raw datatypes.
    *
    * It matches the following grammar:
    *
    * {{{
    * { LONG | LONG RAW | RAW (size) }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50961 Syntax Definition]]
    */
  def long_and_raw_datatypes = rule {
    (("LONG" ~ optional("RAW")) | ("RAW" ~ "(" ~ size ~ ")"))
  }

  /** Creates a rule for datetime datatypes.
    *
    * It matches the following grammar:
    *
    * {{{
    * { DATE
    * | TIMESTAMP [ (fractional_seconds_precision) ]
    * [ WITH [ LOCAL ] TIME ZONE ])
    * | INTERVAL YEAR [ (year_precision) ] TO MONTH
    * | INTERVAL DAY [ (day_precision) ] TO SECOND
    * [ (fractional_seconds_precision) ]
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50962 Syntax Definition]]
    */
  def datetime_datatypes = rule {
    ("DATE" |
      ("TIMESTAMP" ~ optional("(" ~ fractional_seconds_precision ~ ")")) |
      ("WITH" ~ optional("LOCAL") ~ "TIME" ~ "ZONE") |
      ("INTERVAL" ~ "YEAR" ~ optional("(" ~ year_precision ~ ")") ~ "TO" ~ "MONTH") |
      ("INTERVAL" ~ "DAY" ~ optional("(" ~ day_precision ~ ")") ~ "TO" ~ "SECOND" ~
        optional("(" ~ fractional_seconds_precision ~ ")")))
  }

  /** Creates a rule for large object datatypes.
    *
    * It matches the following grammar:
    *
    * {{{
    * { BLOB | CLOB | NCLOB | BFILE }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50963 Syntax Definition]]
    */
  def large_object_datatypes = rule {
    ("BLOB" | "CLOB" | "NCLOB" | "BFILE")
  }

  /** Creates a rule for rowid datatypes.
    *
    * It matches the following grammar:
    *
    * {{{
    * { ROWID | UROWID [ (size) ] }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50964 Syntax Definition]]
    */
  def rowid_datatypes = rule {
    ("ROWID" | ("UROWID" ~ optional("(" ~ size ~ ")")))
  }

  /** Creates a rule for ANSI supported datatypes.
    *
    * It matches the following grammar:
    *
    * {{{
    * { CHARACTER [VARYING] (size)
    * | { CHAR | NCHAR } VARYING (size)
    * | VARCHAR (size)
    * | NATIONAL { CHARACTER | CHAR }
    * [VARYING] (size)
    * | { NUMERIC | DECIMAL | DEC }
    * [ (precision [, scale ]) ]
    * | { INTEGER | INT | SMALLINT }
    * | FLOAT [ (size) ]
    * | DOUBLE PRECISION
    * | REAL
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50965 Syntax Definition]]
    */
  def ANSI_supported_datatypes = rule {
    (("CHARACTER" ~ optional("VARYING") ~ "(" ~ size ~ ")") |
      (("CHAR" | "NCHAR") ~ "VARYING" ~ "(" ~ size ~ ")") |
      ("VARCHAR" ~ "(" ~ size ~ ")") |
      ("NATIONAL" ~ ("CHARACTER" | "CHAR") ~
        optional("VARYING") ~ "(" ~ size ~ ")") |
        (("NUMERIC" | "DECIMAL" | "DEC") ~
          optional("(" ~ precision ~ optional("," ~ scale) ~ ")")) |
          ("INTEGER" | "INT" | "SMALLINT") |
          ("FLOAT" ~ optional("(" ~ size ~ ")")) |
          ("DOUBLE" ~ "PRECISION") |
          "REAL")
  }

  /** Creates a rule for user defined datatypes.
    *
    * It does not match anything yet.
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#i46376 Online documentation]]
    */
  def user_defined_types = rule {
    NOTHING
  }

  /** Creates a rule for Oracle supplied datatypes.
    *
    * It matches the following grammar:
    *
    * {{{
    * { any_types
    * | XML_types
    * | spatial_types
    * | media_types
    * | expression_filter_type
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50966 Syntax Definition]]
    */
  def Oracle_supplied_types = rule {
    (any_types |
      XML_types |
      spatial_types |
      media_types |
      expression_filter_type)
  }

  /** Creates a rule for an AnyType.
    *
    * It matches the following grammar:
    *
    * {{{
    * { SYS.AnyData | SYS.AnyType | SYS.AnyDataSet }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50967 Syntax Definition]]
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#i107578 Online Documentation]]
    */
  def any_types = rule {
    ("SYS.AnyDataSet" | "SYS.AnyData" | "SYS.AnyType")
  }

  /** Creates a rule for XML datatypes.
    *
    * It matches the following grammar:
    *
    * {{{
    * { XMLType | URIType }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50968 Syntax Definition]]
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#i160550 Online Documentation]]
    */
  def XML_types = rule {
    ("XMLType" | "URIType")
  }

  /** Creates a rule for spatial datatypes.
    *
    * It matches the following grammar:
    *
    * {{{
    * { SDO_Geometry | SDO_Topo_Geometry |SDO_GeoRaster }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50969 Syntax Definition]]
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#i107588 Online Documentation]]
    */
  def spatial_types = rule {
    ("SDO_Geometry" | "SDO_Topo_Geometry" | "SDO_GeoRaster")
  }

  /** Creates a rule for media types.
    *
    * It matches the following grammar:
    *
    * {{{
    * { ORDAudio
    * | ORDImage
    * | ORDVideo
    * | ORDDoc
    * | ORDDicom
    * | still_image_object_types
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50970 Syntax Definition]]
    */
  def media_types = rule {
    ("ORDAudio" |
      "ORDImage" |
      "ORDVideo" |
      "ORDDoc" |
      "ORDDicom" |
      still_image_object_types)
  }

  /** Creates a rule for still image object types.
    *
    * It matches the following grammar:
    *
    * {{{
    * { SI_StillImage
    * | SI_AverageColor
    * | SI_PositionalColor
    * | SI_ColorHistogram
    * | SI_Texture
    * | SI_FeatureList
    * | SI_Color
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e17118/sql_elements001.htm#SQLRF50971 Syntax Definition]]
    */
  def still_image_object_types = rule {
    ("SI_StillImage" |
      "SI_AverageColor" |
      "SI_PositionalColor" |
      "SI_ColorHistogram" |
      "SI_Texture" |
      "SI_FeatureList" |
      "SI_Color")
  }

  /** Creates a rule for expression filter types.
    *
    * It does not match anything yet.
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e14919/exprn_expconcepts.htm#EXPRN007 Online Documentation]]
    */
  def expression_filter_type = rule {
    NOTHING
  }

}