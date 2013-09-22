package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.common.CreateTableStatementsParser
import com.github.sebhoss.sql.common.Lexer._
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._
import com.github.sebhoss.sql.oracle.Oracle11gConstraintParser._
import com.github.sebhoss.sql.oracle.Oracle11gCreateIndexParser._
import com.github.sebhoss.sql.oracle.Oracle11gDataTypeParser._
import com.github.sebhoss.sql.oracle.Oracle11gExpressionParser._
import com.github.sebhoss.sql.oracle.Oracle11gLoggingClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gParallelClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gPhysicalAttributesClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gSelectParser._

import org.parboiled.scala._

/**
 * Parboiled-based SQL parser for Oracle 11g syntax.
 *
 * @see [[https://github.com/sirthias/parboiled Parboiled]]
 * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2095331 Oracle 11g CREATE
 * TABLE syntax definition]]
 */
object Oracle11gCreateTableParser extends AbstractParser with CreateTableStatementsParser {

  /**
   * Creates a rule which matches multiple `CREATE TABLE` statements.
   */
  def CreateTableStatements = rule {
    oneOrMore(CreateTableStatement) ~ EOI
  }

  /**
   * Creates a rule which matches `CREATE TABLE` statements.
   *
   * It matches the following grammar:
   *
   * {{{
   * CREATE
   * [ GLOBAL TEMPORARY ]
   * TABLE
   * [ schema. ] table
   * { relational_table | object_table | XMLType_table }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2201774 Syntax Definition]]
   */
  def CreateTableStatement = rule {
    "CREATE" ~
      optional("GLOBAL" ~ "TEMPORARY") ~
      "TABLE" ~
      optional(schema ~ ".") ~ table ~
      (relational_table | object_table | XMLType_table)
  }

  /**
   * Creates a rule for a relational table definition.
   *
   * It matches the following grammar:
   *
   * {{{
   * [ (relational_properties) ]
   * [ ON COMMIT { DELETE | PRESERVE } ROWS ]
   * [ physical_properties ]
   * [ table_properties ]
   * ;
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2149006 Syntax Definition]]
   *
   */
  def relational_table = rule {
    optional("(" ~ relational_properties ~ ")") ~
      optional("ON" ~ "COMMIT" ~ ("DELETE" | "PRESERVE") ~ "ROWS") ~
      optional(physical_properties) ~
      optional(table_properties) ~
      ";"
  }

  /**
   * Creates a rule for relational table properties.
   *
   * It matches the following grammar:
   *
   * {{{
   * { column_definition
   * | virtual_column_definition
   * | { out_of_line_constraint
   * | out_of_line_ref_constraint
   * | supplemental_logging_props
   * }
   * }
   * [, { column_definition
   * | virtual_column_definition
   * | { out_of_line_constraint
   * | out_of_line_ref_constraint
   * | supplemental_logging_props
   * }
   * }
   * ]...
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2146032 Syntax Definition]]
   */
  def relational_properties : Rule0 = rule {
    (column_definition |
      virtual_column_definition |
      (out_of_line_constraint | out_of_line_ref_constraint | supplemental_logging_props)) ~
      zeroOrMore("," ~ (column_definition |
        virtual_column_definition |
        (out_of_line_constraint | out_of_line_ref_constraint | supplemental_logging_props)))
  }

  /**
   * Creates a rule for column definitions.
   *
   * It matches the following grammar:
   *
   * {{{
   * column datatype [ SORT ]
   * [ DEFAULT expr ]
   * [ ENCRYPT encryption_spec ]
   * [ ( { inline_constraint }... )
   * | inline_ref_constraint
   * ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#CEGEDHJE Syntax Definition]]
   */
  def column_definition = rule {
    column ~ datatype ~ optional("SORT") ~
      optional("DEFAULT" ~ expr) ~
      optional("ENCRYPT" ~ encryption_spec) ~
      optional(("(" ~ oneOrMore(inline_constraint) ~ ")") |
        inline_ref_constraint)
  }

  /**
   * Creates a rule for an encryption spec.
   *
   * It matches the following grammar:
   *
   * {{{
   * [ USING 'encrypt_algorithm' ]
   * [ IDENTIFIED BY password ]
   * [ 'integrity_algorithm' ]
   * [ [ NO ] SALT ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#CEGJIIIH Syntax Definition]]
   */
  def encryption_spec = rule {
    optional("USING" ~ "'" ~ encrypt_algorithm ~ "'") ~
      optional("IDENTIFIED" ~ "BY" ~ password) ~
      optional("'" ~ integrity_algorithm ~ "'") ~
      optional(optional("NO") ~ "SALT")
  }

  def encrypt_algorithm = rule {
    Letters
  }

  def password = rule {
    Letters
  }

  def integrity_algorithm = rule {
    Letters
  }

  /**
   * Creates a rule for a virtual column definition.
   *
   * It matches the following grammar:
   *
   * {{{
   * column [datatype] [GENERATED ALWAYS] AS (column_expression)
   * [VIRTUAL]
   * [ inline_constraint [inline_constraint]... ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABCHBHE Syntax Definition]]
   */
  def virtual_column_definition = rule {
    column ~ optional(datatype) ~ optional("GENERATED" ~ "ALWAYS") ~ "AS" ~ "(" ~ column_expression ~ ")" ~
      optional("VIRTUAL") ~
      zeroOrMore(inline_constraint)
  }

  def column_expression = rule {
    objectName
  }

  /**
   * Creates a rule for supplemental logging props.
   *
   * It matches the following grammar:
   *
   * {{{
   * SUPPLEMENTAL LOG { supplemental_log_grp_clause
   * | supplemental_id_key_clause
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2126822 Syntax Definition]]
   */
  def supplemental_logging_props = rule {
    "SUPPLEMENTAL" ~ "LOG" ~ (supplemental_log_grp_clause |
      supplemental_id_key_clause)
  }

  /**
   * Creates a rule for a supplemental log group clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * GROUP log_group
   * (column [ NO LOG ]
   * [, column [ NO LOG ] ]...)
   * [ ALWAYS ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#SQLRF54410 Syntax Definition]]
   */
  def supplemental_log_grp_clause = rule {
    "GROUP" ~ log_group ~
      "(" ~ column ~ optional("NO" ~ "LOG") ~
      zeroOrMore("," ~ column ~ optional("NO" ~ "LOG")) ~ ")" ~
      optional("ALWAYS")
  }

  def log_group = rule {
    objectName
  }

  /**
   * Creates a rule for a supplemental log group clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * DATA
   * ( { ALL | PRIMARY KEY | UNIQUE | FOREIGN KEY }
   * [, { ALL | PRIMARY KEY | UNIQUE | FOREIGN KEY } ]...
   * )
   * COLUMNS
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#SQLRF54411 Syntax Definition]]
   */
  def supplemental_id_key_clause = rule {
    "DATA" ~
      "(" ~ ("ALL" | ("PRIMARY" ~ "KEY") | "UNIQUE" | ("FOREIGN" ~ "KEY")) ~
      zeroOrMore("," ~ ("ALL" | ("PRIMARY" ~ "KEY") | "UNIQUE" | ("FOREIGN" ~ "KEY"))) ~
      ")" ~
      "COLUMNS"
  }

  /**
   * Creates a rule for physical properties.
   *
   * It matches the following grammar:
   *
   * {{{
   * { [deferred_segment_creation] segment_attributes_clause [ table_compression ]
   * | [deferred_segment_creation] ORGANIZATION
   * { HEAP [ segment_attributes_clause ] [ table_compression ]
   * | INDEX [ segment_attributes_clause ] index_org_table_clause
   * | EXTERNAL external_table_clause
   * }
   * | CLUSTER cluster (column [, column ]...)
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2126711 Syntax Definition]]
   */
  def physical_properties = rule {
    ((optional(deferred_segment_creation) ~ segment_attributes_clause ~ optional(table_compression)) |
      (optional(deferred_segment_creation) ~ "ORGANIZATION" ~
        (("HEAP" ~ optional(segment_attributes_clause) ~ optional(table_compression)) |
          ("INDEX" ~ optional(segment_attributes_clause) ~ index_org_table_clause) |
          ("EXTERNAL" ~ external_table_clause))) |
          ("CLUSTER" ~ cluster ~ "(" ~ column ~ zeroOrMore("," ~ column) ~ ")"))
  }

  /**
   * Creates a rule for a deferred segment creation.
   *
   * It matches the following grammar:
   *
   * {{{
   * SEGMENT CREATION { IMMEDIATE | DEFERRED }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#CEGBDDBC Syntax Definition]]
   */
  def deferred_segment_creation = rule {
    "SEGMENT" ~ "CREATION" ~ ("IMMEDIATE" | "DEFERRED")
  }

  /**
   * Creates a rule for an index org table clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * [ { mapping_table_clause
   * | PCTTHRESHOLD integer
   * | key_compression
   * }...
   * ]
   * [ index_org_overflow_clause ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2129638 Syntax Definition]]
   */
  def index_org_table_clause = rule {
    zeroOrMore(mapping_table_clause |
      ("PCTTHRESHOLD" ~ integer) |
      key_compression) ~
      optional(index_org_overflow_clause)
  }

  /**
   * Creates a rule for a mapping table clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * { MAPPING TABLE | NOMAPPING }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2146256 Syntax Definition]]
   */
  def mapping_table_clause = rule {
    ("MAPPING" ~ "TABLE") | "NOMAPPING"
  }

  /**
   * Creates a rule for an index org overflow clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * [ INCLUDING column_name ]
   * OVERFLOW [ segment_attributes_clause ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2146263 Syntax Definition]]
   */
  def index_org_overflow_clause = rule {
    optional("INCLUDING" ~ column_name) ~
      "OVERFLOW" ~ optional(segment_attributes_clause)
  }

  /**
   * Creates a rule for an external table clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * ([ TYPE access_driver_type ]
   * external_data_properties
   * )
   * [ REJECT LIMIT { integer | UNLIMITED } ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2129649 Syntax Definition]]
   */
  def external_table_clause = rule {
    "(" ~ optional("TYPE" ~ access_driver_type) ~
      external_data_properties ~
      ")" ~
      optional("REJECT" ~ "LIMIT" ~ (integer | "UNLIMITED"))
  }

  /**
   * Creates a rule for external data properties.
   *
   * It matches the following grammar:
   *
   * {{{
   * DEFAULT DIRECTORY directory
   * [ ACCESS PARAMETERS
   * { (opaque_format_spec)
   * | USING CLOB subquery
   * }
   * ]
   * LOCATION
   * ([ directory: ] 'location_specifier'
   * [, [ directory: ] 'location_specifier' ]...
   * )
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2146275 Syntax Definition]]
   */
  def external_data_properties = rule {
    "DEFAULT" ~ "DIRECTORY" ~ directory ~
      optional("ACCESS" ~ "PARAMETERS" ~
        (("(" ~ opaque_format_spec ~ ")") |
          ("USING" ~ "CLOB" ~ subquery)))
      "LOCATION" ~ "(" ~ optional(directory ~ ":") ~ "'" ~ oneOrMore(Letters | Digits | Symbols) ~ "'" ~ 
          zeroOrMore("," ~ optional(directory ~ ":") ~ "'" ~ oneOrMore(Letters | Digits | Symbols) ~ "'")
  }

  /**
   * Creates a rule for object type column properties.
   *
   * It matches the following grammar:
   *
   * {{{
   * COLUMN column substitutable_column_clause
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2129742 Syntax Definition]]
   */
  def object_type_col_properties = rule {
    "COLUMN" ~ column ~ substitutable_column_clause
  }

  /**
   * Creates a rule for a substitutable column clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * { [ ELEMENT ] IS OF [ TYPE ] ( [ONLY] type)
   * | [ NOT ] SUBSTITUTABLE AT ALL LEVELS
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2215008 Syntax Definition]]
   */
  def substitutable_column_clause = rule {
    (optional("ELEMENT") ~ "IS" ~ "OF" ~ optional("TYPE") ~ "(" ~ optional("ONLY") ~ type_name ~ ")") |
      (optional("NOT") ~ "SUBSTITUTABLE" ~ "AT" ~ "ALL" ~ "LEVELS")
  }

  /**
   * Creates a rule for nested table col properties.
   *
   * It matches the following grammar:
   *
   * {{{
   * NESTED TABLE
   * { nested_item | COLUMN_VALUE }
   * [ substitutable_column_clause ]
   * [ LOCAL | GLOBAL ]
   * STORE AS storage_table
   * [ ( { (object_properties)
   * | [ physical_properties ]
   * | [ column_properties ]
   * }...
   * )
   * ]
   * [ RETURN [ AS ]  { LOCATOR | VALUE } ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2215015 Syntax Definition]]
   */
  def nested_table_col_properties = rule {
    "NESTED" ~ "TABLE" ~
      (nested_item | "COLUMN_VALUE") ~
      optional(substitutable_column_clause) ~
      optional("LOCAL" | "GLOBAL") ~
      "STORE" ~ "AS" ~ storage_table
    optional("(" ~ oneOrMore(object_properties |
      optional(physical_properties) |
      optional(column_properties)) ~ ")") ~
      optional("RETURN" ~ optional("AS") ~ ("LOCATOR" | "VALUE"))
  }

  /**
   * Creates a rule for object properties.
   *
   * It matches the following grammar:
   *
   * {{{
   * { { column | attribute }
   * [ DEFAULT expr ]
   * [ { inline_constraint }...  | inline_ref_constraint ]
   * | { out_of_line_constraint
   * | out_of_line_ref_constraint
   * | supplemental_logging_props
   * }
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2126768 Syntax Definition]]
   */
  def object_properties = rule {
    ((column | attribute) ~
      optional("DEFAULT" ~ expr) ~
      optional(oneOrMore(inline_constraint) | inline_ref_constraint)) |
      (out_of_line_constraint |
        out_of_line_ref_constraint |
        supplemental_logging_props)
  }

  /**
   * Creates a rule for varray col properties.
   *
   * It matches the following grammar:
   *
   * {{{
   * VARRAY varray_item
   * { [ substitutable_column_clause ] varray_storage_clause
   * | substitutable_column_clause
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2149052 Syntax Definition]]
   */
  def varray_col_properties = rule {
    "VARRAY" ~ varray_item ~
      ((optional(substitutable_column_clause) ~ varray_storage_clause) |
        substitutable_column_clause)
  }

  /**
   * Creates a rule for a varray storage clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * STORE AS [SECUREFILE | BASICFILE] LOB
   * { [LOB_segname] ( LOB_storage_parameters )
   * | LOB_segname
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABCACAI Syntax Definition]]
   */
  def varray_storage_clause = rule {
    "STORE" ~ "AS" ~ ("SECUREFILE" | "BASICFILE") ~ "LOB" ~
      ((optional(LOB_segname) ~ "(" ~ LOB_storage_parameters ~ ")") |
        LOB_segname)
  }

  /**
   * Creates a rule for LOB storage parameters.
   *
   * It matches the following grammar:
   *
   * {{{
   * { { ENABLE | DISABLE } STORAGE IN ROW
   * | CHUNK integer
   * | PCTVERSION integer
   * | FREEPOOLS integer
   * | LOB_retention_clause
   * | LOB_deduplicate_clause
   * | LOB_compression_clause
   * | { ENCRYPT encryption_spec | DECRYPT }
   * | { CACHE | NOCACHE | CACHE READS } [ logging_clause ]
   * }...
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2149089 Syntax Definition]]
   */
  def LOB_storage_parameters = rule {
    oneOrMore((("ENABLE" | "DISABLE") ~ "STORAGE" ~ "IN" ~ "ROW") |
      ("CHUNK" ~ integer) |
      ("PCTVERSION" ~ integer) |
      ("FREEPOOLS" ~ integer) |
      LOB_retention_clause |
      LOB_deduplicate_clause |
      LOB_compression_clause |
      (("ENCRYPT" ~ encryption_spec) | "DECRYPT") |
      (("CACHE" | "NOCACHE" | "CACHE" ~ "READS") ~ optional(logging_clause)))
  }

  /**
   * Creates a rule for a LOB retention clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * RETENTION [ MAX | MIN integer | AUTO | NONE ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#SQLRF54391 Syntax Definition]]
   */
  def LOB_retention_clause = rule {
    "RETENTION" ~ optional("MAX" | ("MIN" ~ integer) | "AUTO" | "NONE")
  }

  /**
   * Creates a rule for a LOB deduplicate clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * { DEDUPLICATE
   * | KEEP_DUPLICATES
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABFCEJE Syntax Definition]]
   */
  def LOB_deduplicate_clause = rule {
    ("DEDUPLICATE" |
      "KEEP_DUPLICATES")
  }

  /**
   * Creates a rule for a LOB compression clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * { COMPRESS [HIGH | MEDIUM | LOW ]
   * | NOCOMPRESS
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABFHIFJ Syntax Definition]]
   */
  def LOB_compression_clause = rule {
    (("COMPRESS" ~ optional("HIGH" | "MEDIUM" | "LOW")) |
      "NOCOMPRESS")
  }

  /**
   * Creates a rule for a LOB storage clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * LOB
   * { (LOB_item [, LOB_item ]...)
   * STORE AS { {SECUREFILE | BASICFILE}
   * | (LOB_storage_parameters)
   * }...
   * | (LOB_item)
   * STORE AS { {SECUREFILE | BASICFILE}
   * | LOB_segname
   * | (LOB_storage_parameters)
   * }...
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2218765 Syntax Definition]]
   */
  def LOB_storage_clause = rule {
    "LOB" ~
      (("(" ~ LOB_item ~ zeroOrMore("," ~ LOB_item) ~ ")" ~
        "STORE" ~ "AS" ~ oneOrMore(("SECUREFILE" | "BASICFILE") | "(" ~ LOB_storage_parameters ~ ")")) |
        "(" ~ LOB_item ~ ")" ~
        "STORE" ~ "AS" ~ oneOrMore(("SECUREFILE" | "BASICFILE") |
          LOB_segname |
          ("(" ~ LOB_storage_parameters ~ ")")))
  }

  /**
   * Creates a rule for a LOB partition storage.
   *
   * It matches the following grammar:
   *
   * {{{
   * PARTITION partition
   * { LOB_storage_clause | varray_col_properties }...
   * [ (SUBPARTITION subpartition
   * { LOB_partitioning_storage | varray_col_properties }...
   * )
   * ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2129787 Syntax Definition]]
   */
  def LOB_partition_storage = rule {
    "PARTITION" ~ partition ~
      oneOrMore(LOB_storage_clause | varray_col_properties) ~
      optional("(" ~ "SUBPARTITION" ~ subpartition ~
        oneOrMore(LOB_partitioning_storage | varray_col_properties) ~
        ")")
  }

  /**
   * Creates a rule for XMLType column properties.
   *
   * It matches the following grammar:
   *
   * {{{
   * XMLTYPE [ COLUMN ] column
   * [ XMLType_storage ]
   * [ XMLSchema_spec ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2129798 Syntax Definition]]
   */
  def XMLType_column_properties = rule {
    "XMLTYPE" ~ optional("COLUMN") ~ column ~
      optional(XMLType_storage) ~
      optional(XMLSchema_spec)
  }

  /**
   * Creates a rule for a XMLType storage.
   *
   * It matches the following grammar:
   *
   * {{{
   * STORE
   * { AS
   * { OBJECT RELATIONAL
   * | [SECUREFILE | BASICFILE]
   * { CLOB | BINARY XML }
   * [ { LOB_segname [ (LOB_parameters) ]
   * | (LOB_parameters)
   * }
   * ]
   * }
   * | { ALL VARRAYS AS { LOBS | TABLES } }
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2218838 Syntax Definition]]
   */
  def XMLType_storage = rule {
    "STORE" ~
      ("AS" ~
        (("OBJECT" ~ "RELATIONAL") |
          optional("SECUREFILE" | "BASICFILE") ~
          ("CLOB" | "BINARY" ~ "XML") ~
          optional((LOB_segname ~ optional("(" ~ LOB_parameters ~ ")")) | "(" ~ LOB_parameters ~ ")")) |
          ("ALL" ~ "VARRAYS" ~ "AS" ~ ("LOBS" | "TABLES")))
  }

  /**
   * Creates a rule for LOB parameters.
   *
   * It matches the following grammar:
   *
   * {{{
   * { { ENABLE | DISABLE } STORAGE IN ROW
   * | CHUNK integer
   * | PCTVERSION integer
   * | FREEPOOLS integer
   * | LOB_retention_clause
   * | LOB_deduplicate_clause
   * | LOB_compression_clause
   * | { ENCRYPT encryption_spec | DECRYPT }
   * | { CACHE | NOCACHE | CACHE READS } [ logging_clause ]
   * }...
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2149089 Syntax Definition]]
   */
  def LOB_parameters = rule {
    oneOrMore((("ENABLE" | "DISABLE") ~ "STORAGE" ~ "IN" ~ "ROW") |
      ("CHUNK" ~ integer) |
      ("PCTVERSION" ~ integer) |
      ("FREEPOOLS" ~ integer) |
      LOB_retention_clause |
      LOB_deduplicate_clause |
      LOB_compression_clause |
      ("ENCRYPT" ~ encryption_spec | "DECRYPT") |
      ("CACHE" | "NOCACHE" | ("CACHE" ~ "READS")) ~ optional(logging_clause))
  }

  /**
   * Creates a rule for a XMLSchema spec.
   *
   * It matches the following grammar:
   *
   * {{{
   * [ XMLSCHEMA XMLSchema_URL ]
   * ELEMENT { element | XMLSchema_URL # element }
   * [ ALLOW ANYSCHEMA
   * | ALLOW NONSCHEMA
   * | DISALLOW NONSCHEMA
   * ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABJDCGH Syntax Definition]]
   */
  def XMLSchema_spec = rule {
    optional("XMLSCHEMA" ~ XMLSchema_URL) ~
      "ELEMENT" ~ (element | (XMLSchema_URL ~ "#" ~ element)) ~
      optional(("ALLOW" ~ "ANYSCHEMA") |
        ("ALLOW" ~ "NONSCHEMA") |
        ("DISALLOW" ~ "NONSCHEMA"))
  }

  /**
   * Creates a rule for table partitioning clauses.
   *
   * It matches the following grammar:
   *
   * {{{
   * { range_partitions
   * | hash_partitions
   * | list_partitions
   * | reference_partitioning
   * | composite_range_partitions
   * | composite_hash_partitions
   * | composite_list_partitions
   * | system_partitioning
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2129707 Syntax Definition]]
   */
  def table_partitioning_clauses = rule {
    (range_partitions |
      hash_partitions |
      list_partitions |
      reference_partitioning |
      composite_range_partitions |
      composite_hash_partitions |
      composite_list_partitions |
      system_partitioning)
  }

  /**
   * Creates a rule for range partitions.
   *
   * It matches the following grammar:
   *
   * {{{
   * PARTITION BY RANGE (column[, column ]...)
   * [ INTERVAL expr [ STORE IN ( tablespace [, tablespace]...) ]]
   * ( PARTITION [ partition ]
   * range_values_clause table_partition_description
   * [, PARTITION [ partition ]
   * range_values_clause table_partition_description
   * ]...
   * )
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2146287 Syntax Definition]]
   */
  def range_partitions = rule {
    "PARTITION" ~ "BY" ~ "RANGE" ~ "(" ~ column ~ zeroOrMore("," ~ column) ~ ")" ~
      optional("INTERVAL" ~ expr ~ optional("STORE" ~ "IN" ~ "(" ~ tablespace ~ zeroOrMore("," ~ tablespace) ~ ")")) ~
      "(" ~ "PARTITION" ~ optional(partition) ~
      range_values_clause ~ table_partition_description ~
      zeroOrMore("," ~ "PARTITION" ~ optional(partition) ~
        range_values_clause ~ table_partition_description) ~
      ")"
  }

  /**
   * Creates a rule for a range values clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * VALUES LESS THAN
   * ({ literal | MAXVALUE }
   * [, { literal | MAXVALUE } ]...
   * )
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2125922 Syntax Definition]]
   */
  def range_values_clause = rule {
    "VALUES" ~ "LESS" ~ "THAN" ~
      "(" ~ (literal | "MAXVALUE") ~
      zeroOrMore("," ~ (literal | "MAXVALUE")) ~
      ")"
  }

  /**
   * Creates a rule for a table partition description.
   *
   * It matches the following grammar:
   *
   * {{{
   * [ deferred_segment_creation ]
   * [ segment_attributes_clause ]
   * [ table_compression | key_compression ]
   * [ OVERFLOW [ segment_attributes_clause ] ]
   * [ { LOB_storage_clause
   * | varray_col_properties
   * | nested_table_col_properties
   * }...
   * ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2127282 Syntax Definition]]
   */
  def table_partition_description = rule {
    optional(deferred_segment_creation) ~
      optional(segment_attributes_clause) ~
      optional(table_compression | key_compression) ~
      optional("OVERFLOW" ~ optional(segment_attributes_clause)) ~
      zeroOrMore((LOB_storage_clause |
        varray_col_properties |
        nested_table_col_properties))
  }

  /**
   * Creates a rule for hash partitions.
   *
   * It matches the following grammar:
   *
   * {{{
   * PARTITION BY HASH (column [, column ] ...)
   * { individual_hash_partitions
   * | hash_partitions_by_quantity
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2146298 Syntax Definition]]
   */
  def hash_partitions = rule {
    "PARTITION" ~ "BY" ~ "HASH" ~ "(" ~ column ~ zeroOrMore("," ~ column) ~ ")" ~
      (individual_hash_partitions |
        hash_partitions_by_quantity)
  }

  /**
   * Creates a rule for list partitions.
   *
   * It matches the following grammar:
   *
   * {{{
   * PARTITION BY LIST (column)
   * (PARTITION [ partition ]
   * list_values_clause table_partition_description
   * [, PARTITION [ partition ]
   * list_values_clause table_partition_description
   * ]...
   * )
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2146309 Syntax Definition]]
   */
  def list_partitions = rule {
    "PARTITION" ~ "BY" ~ "LIST" ~ "(" ~ column ~ ")" ~
      "(" ~ "PARTITION" ~ optional(partition) ~
      list_values_clause ~ table_partition_description ~
      zeroOrMore("," ~ "PARTITION" ~ optional(partition) ~
        list_values_clause ~ table_partition_description) ~
      ")"
  }

  /**
   * Creates a rule for a list values clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * VALUES ({ literal | NULL }
   * [, { literal | NULL }]...
   * | DEFAULT
   * )
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2125928 Syntax Definition]]
   */
  def list_values_clause = rule {
    "VALUES" ~ "(" ~ (literal | "NULL") ~
      zeroOrMore("," ~ (literal | "NULL")) |
      "DEFAULT" ~
      ")"
  }

  /**
   * Creates a rule for reference partitioning.
   *
   * It matches the following grammar:
   *
   * {{{
   * PARTITION BY REFERENCE ( constraint )
   * [ (reference_partition_desc...) ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABDECCA Syntax Definition]]
   */
  def reference_partitioning = rule {
    "PARTITION" ~ "BY" ~ "REFERENCE" ~ "(" ~ constraint ~ ")" ~
      optional("(" ~ oneOrMore(reference_partition_desc) ~ ")")
  }

  /**
   * Creates a rule for a reference partition desc.
   *
   * It matches the following grammar:
   *
   * {{{
   * PARTITION [partition] [table_partition_description] )
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABEJDCI Syntax Definition]]
   */
  def reference_partition_desc = rule {
    "PARTITION" ~ optional(partition) ~ optional(table_partition_description)
  }

  /**
   * Creates a rule for composite range partitions.
   *
   * It matches the following grammar:
   *
   * {{{
   * PARTITION BY RANGE ( column [, column]... )
   * [ INTERVAL ( expr ) [ STORE IN ( tablespace [, tablespace]... ) ]]
   * { subpartition_by_range
   * | subpartition_by_list
   * | subpartition_by_hash
   * }
   * ( { PARTITION [partition] range_partition_desc }... )
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2146320 Syntax Definition]]
   */
  def composite_range_partitions = rule {
    "PARTITION" ~ "BY" ~ "RANGE" ~ "(" ~ column ~ zeroOrMore("," ~ column) ~ ")" ~
      optional("INTERVAL" ~ "(" ~ expr ~ ")" ~ optional("STORE" ~ "IN" ~ "(" ~ tablespace ~ zeroOrMore("," ~ tablespace) ~ ")")) ~
      (subpartition_by_range |
        subpartition_by_list |
        subpartition_by_hash) ~
        "(" ~ oneOrMore("PARTITION" ~ optional(partition) ~ range_partition_desc) ~ ")"
  }

  /**
   * Creates a rule for a subpartition by range.
   *
   * It matches the following grammar:
   *
   * {{{
   * SUBPARTITION BY RANGE ( column [, column]... ) [subpartition_template]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABDDHAA Syntax Definition]]
   */
  def subpartition_by_range = rule {
    "SUBPARTITION" ~ "BY" ~ "RANGE" ~ "(" ~ column ~ zeroOrMore("," ~ column) ~ ")" ~ optional(subpartition_template)
  }

  /**
   * Creates a rule for a subpartition template.
   *
   * It matches the following grammar:
   *
   * {{{
   * SUBPARTITION TEMPLATE
   * ( { range_subpartition_desc [, range_subpartition_desc] ...
   * | list_subpartition_desc [, list_subpartition_desc] ...
   * | individual_hash_subparts [, individual_hash_subparts] ...
   * }
   * ) | hash_subpartition_quantity
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2129934 Syntax Definition]]
   */
  def subpartition_template = rule {
    "SUBPARTITION" ~ "TEMPLATE" ~
      "(" ~ ((range_subpartition_desc ~ zeroOrMore("," ~ range_subpartition_desc)) |
        (list_subpartition_desc ~ zeroOrMore("," ~ list_subpartition_desc)) |
        (individual_hash_subparts ~ zeroOrMore("," ~ individual_hash_subparts))) ~
        ")" | hash_subparts_by_quantity
  }

  /**
   * Creates a rule for a range subpartition desc.
   *
   * It matches the following grammar:
   *
   * {{{
   * SUBPARTITION [subpartition] range_values_clause
   * [partitioning_storage_clause]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABEGHJB Syntax Definition]]
   */
  def range_subpartition_desc = rule {
    "SUBPARTITION" ~ optional(subpartition) ~ range_values_clause ~
      optional(partitioning_storage_clause)
  }

  /**
   * Creates a rule for a list subpartition desc.
   *
   * It matches the following grammar:
   *
   * {{{
   * SUBPARTITION [subpartition]
   * list_values_clause
   * [partitioning_storage_clause]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2146352 Syntax Definition]]
   */
  def list_subpartition_desc = rule {
    "SUBPARTITION" ~ optional(subpartition) ~
      list_values_clause ~
      optional(partitioning_storage_clause)
  }

  /**
   * Creates a rule for individual hash subparts.
   *
   * It matches the following grammar:
   *
   * {{{
   * SUBPARTITION [subpartition] [partitioning_storage_clause]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2215074 Syntax Definition]]
   */
  def individual_hash_subparts = rule {
    "SUBPARTITION" ~ optional(subpartition) ~ optional(partitioning_storage_clause)
  }

  /**
   * Creates a rule for a hash subpartition by quantity.
   *
   * It matches the following grammar:
   *
   * {{{
   * SUBPARTITIONS integer [STORE IN ( tablespace [, tablespace]... )]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABIDCDG Syntax Definition]]
   */
  def hash_subparts_by_quantity = rule {
    "SUBPARTITIONS" ~ integer ~ optional("STORE" ~ "IN" ~ "(" ~ tablespace ~ zeroOrMore("," ~ tablespace) ~ ")")
  }

  /**
   * Creates a rule for a subpartitions by list.
   *
   * It matches the following grammar:
   *
   * {{{
   * SUBPARTITION BY LIST (column) [ subpartition_template ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2153057 Syntax Definition]]
   */
  def subpartition_by_list = rule {
    "SUBPARTITION" ~ "BY" ~ "LIST" ~ "(" ~ column ~ ")" ~ optional(subpartition_template)
  }

  /**
   * Creates a rule for a subpartition by hash.
   *
   * It matches the following grammar:
   *
   * {{{
   * SUBPARTITION BY HASH (column [, column ]...)
   * [ SUBPARTITIONS integer
   * [ STORE IN (tablespace [, tablespace ]...) ]
   * | subpartition_template
   * ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2129906 Syntax Definition]]
   */
  def subpartition_by_hash = rule {
    "SUBPARTITION" ~ "BY" ~ "HASH" ~ "(" ~ column ~ zeroOrMore("," ~ column) ~ ")" ~
      optional("SUBPARTITIONS" ~ integer ~
        (optional("STORE" ~ "IN" ~ "(" ~ tablespace ~ zeroOrMore("," ~ tablespace) ~ ")") |
          subpartition_template))
  }

  /**
   * Creates a rule for a range partition desc.
   *
   * It matches the following grammar:
   *
   * {{{
   * range_values_clause
   * table_partition_description
   * [ ( { range_subpartition_desc [, range_subpartition_desc] ...
   * | list_subpartition_desc [, list_subpartition_desc] ...
   * | individual_hash_subparts [, individual_hash_subparts] ...
   * }
   * ) | hash_subparts_by_quantity ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABJIIJB Syntax Definition]]
   */
  def range_partition_desc = rule {
    range_values_clause ~
      table_partition_description ~
      optional("(" ~ (range_subpartition_desc ~ zeroOrMore("," ~ range_subpartition_desc)) |
        (list_subpartition_desc ~ zeroOrMore("," ~ list_subpartition_desc)) |
        (individual_hash_subparts ~ zeroOrMore("," ~ individual_hash_subparts))) ~
      ")" | hash_subparts_by_quantity
  }

  /**
   * Creates a rule for composite hash partitions.
   *
   * It matches the following grammar:
   *
   * {{{
   * PARTITION BY HASH (column [, column ] ...)
   * { subpartition_by_range
   * | subpartition_by_hash
   * | subpartition_by_list
   * }
   * { individual_hash_partitions
   * | hash_partitions_by_quantity
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#CJABBBAI Syntax Definition]]
   */
  def composite_hash_partitions = rule {
    "PARTITION" ~ "BY" ~ "HASH" ~ "(" ~ column ~ zeroOrMore("," ~ column) ~ ")" ~
      (subpartition_by_range |
        subpartition_by_hash |
        subpartition_by_list) ~
        (individual_hash_partitions |
          hash_partitions_by_quantity)
  }

  /**
   * Creates a rule for composite list partitions.
   *
   * It matches the following grammar:
   *
   * {{{
   * PARTITION BY LIST ( column )
   * { subpartition_by_range
   * | subpartition_by_hash
   * | subpartition_by_list
   * }
   * ( { PARTITION [partition] list_partition_desc }... )
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABFDCFF Syntax Definition]]
   */
  def composite_list_partitions = rule {
    "PARTITION" ~ "BY" ~ "LIST" ~ "(" ~ column ~ ")" ~
      (subpartition_by_range |
        subpartition_by_hash |
        subpartition_by_list) ~
        "(" ~ oneOrMore("PARTITION" ~ optional(partition) ~ list_partition_desc) ~ ")"
  }

  /**
   * Creates a rule for a list partition desc.
   *
   * It matches the following grammar:
   *
   * {{{
   * list_values_clause
   * table_partition_description
   * [ ( range_subpartition_desc [, range_subpartition_desc])
   * | ( list_subpartition_desc [, list_subpartition_desc])
   * | ( individual_hash_subparts [, individual_hash_subparts])
   * ) | hash_subparts_by_quantity
   * ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABIBDHH Syntax Definition]]
   */
  def list_partition_desc = rule {
    list_values_clause ~
      table_partition_description ~
      optional((("(" ~ range_subpartition_desc ~ zeroOrMore("," ~ range_subpartition_desc) ~ ")") |
        ("(" ~ list_subpartition_desc ~ zeroOrMore("," ~ list_subpartition_desc) ~ ")") |
        ("(" ~ individual_hash_subparts ~ zeroOrMore("," ~ individual_hash_subparts) ~ ")")) |
        hash_subparts_by_quantity)
  }

  /**
   * Creates a rule for system partitioning.
   *
   * It matches the following grammar:
   *
   * {{{
   * PARTITION BY SYSTEM [ PARTITIONS integer
   * | reference_partition_desc
   * [, reference_partition_desc ...]
   * ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABFAEHI Syntax Definition]]
   */
  def system_partitioning = rule {
    "PARTITION" ~ "BY" ~ "SYSTEM" ~ optional(("PARTITIONS" ~ integer) |
      (reference_partition_desc ~ zeroOrMore("," ~ reference_partition_desc)))
  }

  /**
   * Creates a rule for an enable/disable clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * { ENABLE | DISABLE }
   * [ VALIDATE | NOVALIDATE ]
   * { UNIQUE (column [, column ]...)
   * | PRIMARY KEY
   * | CONSTRAINT constraint
   * }
   * [ using_index_clause ]
   * [ exceptions_clause ]
   * [ CASCADE ]
   * [ { KEEP | DROP } INDEX ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2129729 Syntax Definition]]
   */
  def enable_disable_clause = rule {
    ("ENABLE" | "DISABLE") ~
      optional("VALIDATE" | "NOVALIDATE") ~
      (("UNIQUE" ~ "(" ~ column ~ zeroOrMore("," ~ column) ~ ")") |
        ("PRIMARY" ~ "KEY") |
        ("CONSTRAINT" ~ constraint)) ~
        optional(using_index_clause) ~
        optional(exceptions_clause) ~
        optional("CASCADE") ~
        optional("KEEP" | "DROP" | "INDEX")
  }

  /**
   * Creates a rule for a row movement clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * { ENABLE | DISABLE } ROW MOVEMENT
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2204697 Syntax Definition]]
   */
  def row_movement_clause = rule {
    ("ENABLE" | "DISABLE") ~ "ROW" ~ "MOVEMENT"
  }

  /**
   * Creates a rule for an flashback archive clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * FLASHBACK ARCHIVE [flashback_archive] | NO FLASHBACK ARCHIVE
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABDGEHC Syntax Definition]]
   */
  def flashback_archive_clause = rule {
    ("FLASHBACK" ~ "ARCHIVE" ~ optional(flashback_archive)) | ("NO" ~ "FLASHBACK" ~ "ARCHIVE")
  }

  /**
   * Creates a rule for an object table.
   *
   * It matches the following grammar:
   *
   * {{{
   * OF
   * [ schema. ] object_type
   * [ object_table_substitution ]
   * [ (object_properties) ]
   * [ ON COMMIT { DELETE | PRESERVE } ROWS ]
   * [ OID_clause ]
   * [ OID_index_clause ]
   * [ physical_properties ]
   * [ table_properties ]
   * ;
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2214901 Syntax Definition]]
   */
  def object_table = rule {
    "OF" ~
      optional(schema ~ ".") ~ object_type ~
      optional(object_table_substitution) ~
      optional("(" ~ object_properties ~ ")") ~
      optional("ON" ~ "COMMIT" ~ ("DELETE" | "PRESERVE") ~ "ROWS") ~
      optional(OID_clause) ~
      optional(OID_index_clause) ~
      optional(physical_properties) ~
      optional(table_properties) ~
      ";"
  }

  /**
   * Creates a rule for an object table substitution.
   *
   * It matches the following grammar:
   *
   * {{{
   * [ NOT ] SUBSTITUTABLE AT ALL LEVELS
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2126754 Syntax Definition]]
   */
  def object_table_substitution = rule {
    optional("NOT") ~ "SUBSTITUTABLE" ~ "AT" ~ "ALL" ~ "LEVELS"
  }

  /**
   * Creates a rule for an OID clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * OBJECT IDENTIFIER IS
   * { SYSTEM GENERATED | PRIMARY KEY }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2149035 Syntax Definition]]
   */
  def OID_clause = rule {
    "OBJECT" ~ "IDENTIFIER" ~ "IS" ~
      (("SYSTEM" ~ "GENERATED") | ("PRIMARY" ~ "KEY"))
  }

  /**
   * Creates a rule for an OID index clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * OIDINDEX [ index ]
   * ({ physical_attributes_clause
   * | TABLESPACE tablespace
   * }...
   * )
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2149044 Syntax Definition]]
   */
  def OID_index_clause = rule {
    "OIDINDEX" ~ optional(index) ~
      "(" ~ oneOrMore(physical_attributes_clause |
        ("TABLESPACE" ~ tablespace)) ~
      ")"
  }

  /**
   * Creates a rule for a XMLType table.
   *
   * It matches the following grammar:
   *
   * {{{
   * OF XMLTYPE
   * [ (oject_properties) ]
   * [ XMLTYPE XMLType_storage ]
   * [ XMLSchema_spec ]
   * [ XMLType_virtual_columns ]
   * [ ON COMMIT { DELETE | PRESERVE } ROWS ]
   * [ OID_clause ]
   * [ OID_index_clause ]
   * [ physical_properties ]
   * [ table_properties ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2234923 Syntax Definition]]
   */
  def XMLType_table = rule {
    "OF" ~ "XMLTYPE" ~
      optional("(" ~ object_properties ~ ")") ~
      optional("XMLTYPE" ~ XMLType_storage) ~
      optional(XMLSchema_spec) ~
      optional(XMLType_virtual_columns) ~
      optional("ON" ~ "COMMIT" ~ ("DELETE" | "PRESERVE") ~ "ROWS") ~
      optional(OID_clause) ~
      optional(OID_index_clause) ~
      optional(physical_properties) ~
      optional(table_properties)
  }

  /**
   * Creates a rule for XMLType virtual columns.
   *
   * It matches the following grammar:
   *
   * {{{
   * VIRTUAL COLUMNS ( column AS (expr) [, column AS (expr) ]... )
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#BABFIGHD Syntax Definition]]
   */
  def XMLType_virtual_columns = rule {
    "VIRTUAL" ~ "COLUMNS" ~ "(" ~ column ~ "AS" ~ "(" ~ expr ~ ")" ~ zeroOrMore("," ~ column ~ "AS" ~ "(" ~ expr ~ ")")
  }

  /**
   * Creates a rule for table compression.
   *
   * It matches the following grammar:
   *
   * {{{
   * { COMPRESS [ BASIC
   * | FOR { OLTP
   * | { QUERY | ARCHIVE } [ LOW | HIGH ]
   * }
   * ]
   * | NOCOMPRESS
   * }
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2129627 Syntax Definition]]
   */
  def table_compression = rule {
    ("COMPRESS" ~ optional("BASIC" |
      ("FOR" ~ ("OLTP" |
        ("QUERY" | "ARCHIVE") ~ optional("LOW" | "HIGH")))) |
      "NOCOMPRESS")
  }

  /**
   * Creates a rule for table properties.
   *
   * It matches the following grammar:
   *
   * {{{
   * [ column_properties ]
   * [ table_partitioning_clauses ]
   * [ CACHE | NOCACHE ]
   * [ RESULT_CACHE ( MODE {DEFAULT | FORCE } ) ]
   * [ parallel_clause ]
   * [ ROWDEPENDENCIES | NOROWDEPENDENCIES ]
   * [ enable_disable_clause ]...
   * [ row_movement_clause ]
   * [ flashback_archive_clause ]
   * [ AS subquery ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2126725 Syntax Definition]]
   */
  def table_properties : Rule0 = rule {
    optional(column_properties) ~
      optional(table_partitioning_clauses) ~
      optional("CACHE" | "NOCACHE") ~
      optional("RESULT_CACHE" ~ "(" ~ "MODE" ~ ("DEFAULT" | "FORCE") ~ ")") ~
      optional(parallel_clause) ~
      optional("ROWDEPENDENCIES" | "NOROWDEPENDENCIES") ~
      optional(enable_disable_clause) ~
      optional(row_movement_clause) ~
      optional(flashback_archive_clause) ~
      optional("AS" ~ subquery)
  }

  /**
   * Creates a rule for column properties.
   *
   * It matches the following grammar:
   *
   * {{{
   * { object_type_col_properties
   * | nested_table_col_properties
   * | { varray_col_properties | LOB_storage_clause }
   * [ (LOB_partition_storage [, LOB_partition_storage ]...) ]
   * | XMLType_column_properties
   * }...
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_7002.htm#i2146083 Syntax Definition]]
   */
  def column_properties : Rule0 = rule {
    oneOrMore(object_type_col_properties |
      nested_table_col_properties |
      ((varray_col_properties | LOB_storage_clause) ~
        optional(LOB_partition_storage ~ zeroOrMore("," ~ LOB_partition_storage))) |
        XMLType_column_properties)
  }

}