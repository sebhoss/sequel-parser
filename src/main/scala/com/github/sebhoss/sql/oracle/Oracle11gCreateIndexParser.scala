package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.common.CreateIndexStatementsParser
import com.github.sebhoss.sql.common.Lexer._
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._
import com.github.sebhoss.sql.oracle.Oracle11gConditionParser._
import com.github.sebhoss.sql.oracle.Oracle11gCreateTableParser.table_compression
import com.github.sebhoss.sql.oracle.Oracle11gCreateTableParser.table_properties
import com.github.sebhoss.sql.oracle.Oracle11gDataTypeParser._
import com.github.sebhoss.sql.oracle.Oracle11gLoggingClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gParallelClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gPhysicalAttributesClauseParser._

import org.parboiled.scala._

object Oracle11gCreateIndexParser extends AbstractParser with CreateIndexStatementsParser {

  /** Creates a rule which matches multiple `CREATE INDEX` statements.
    */
  def CreateIndexStatements = rule {
    oneOrMore(create_index_statement) ~ EOI
  }

  /** Creates a rule for a create index statement.
    *
    * It matches the following grammar:
    *
    * {{{
    * CREATE [ UNIQUE | BITMAP ] INDEX [ schema. ] index
    * ON { cluster_index_clause
    * | table_index_clause
    * | bitmap_join_index_clause
    * }
    * [ UNUSABLE ] ;
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2125762 Syntax Definition]]
    */
  def create_index_statement : Rule0 = rule {
    "CREATE" ~ optional("UNIQUE" | "BITMAP") ~ "INDEX" ~ optional(schema ~ ".") ~ index ~
      "ON" ~ (cluster_index_clause |
        table_index_clause |
        bitmap_join_index_clause) ~
        optional("UNUSABLE") ~ ";"
  }

  /** Creates a rule for a cluster index clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * CLUSTER [ schema. ] cluster index_attributes
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53952 Syntax Definition]]
    */
  def cluster_index_clause = rule {
    "CLUSTER" ~ optional(schema ~ ".") ~ cluster ~ index_attributes
  }

  /** Creates a rule for index attributes.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ { physical_attributes_clause
    * | logging_clause
    * | ONLINE
    * | TABLESPACE { tablespace | DEFAULT }
    * | key_compression
    * | { SORT | NOSORT }
    * | REVERSE
    * | VISIBLE | INVISIBLE
    * | parallel_clause
    * }...
    * ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2182500 Syntax Definition]]
    */
  def index_attributes = rule {
    oneOrMore(physical_attributes_clause |
      logging_clause |
      "ONLINE" |
      ("TABLESPACE" ~ (tablespace | "DEFAULT")) |
      key_compression |
      ("SORT" | "NOSORT") |
      "REVERSE" |
      "VISIBLE" | "INVISIBLE" |
      parallel_clause)
  }

  /** Creates a rule for key compression.
    *
    * It matches the following grammar:
    *
    * {{{
    * { COMPRESS [ integer ]
    * | NOCOMPRESS
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53960 Syntax Definition]]
    */
  def key_compression = rule {
    ("COMPRESS" ~ optional(integer) |
      "NOCOMPRESS")
  }

  /** Creates a rule for a table index clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ schema. ] table [ t_alias ]
    * (index_expr [ ASC | DESC ]
    * [, index_expr [ ASC | DESC ] ]...)
    * [ index_properties ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53953 Syntax Definition]]
    */
  def table_index_clause = rule {
    optional(schema ~ ".") ~ table ~ optional(table_alias) ~
      "(" ~ index_expression ~ optional("ASC" | "DESC") ~
      zeroOrMore("," ~ index_expression ~ optional("ASC" | "DESC")) ~ ")" ~
      index_properties
  }

  /** Creates a rule for index properties.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ { { global_partitioned_index
    * | local_partitioned_index
    * }
    * | index_attributes
    * }...
    * | INDEXTYPE IS { domain_index_clause
    * | XMLTable_index_clause
    * | XMLIndex_clause
    * }
    * ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2138869 Syntax Definition]]
    */
  def index_properties = rule {
    optional(oneOrMore((global_partitioned_index | local_partitioned_index) | index_attributes) |
      ("INDEXTYPE" ~ "IS" ~ (domain_index_clause |
        XMLIndex_clause)))
  }

  /** Creates a rule for a global partitioned index.
    *
    * It matches the following grammar:
    *
    * {{{
    * GLOBAL PARTITION BY
    * { RANGE (column_list)
    * (index_partitioning_clause)
    * | HASH (column_list)
    * { individual_hash_partitions
    * | hash_partitions_by_quantity
    * }
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2126415 Syntax Definition]]
    */
  def global_partitioned_index = rule {
    "GLOBAL" ~ "PARTITION" ~ "BY" ~
      ("RANGE" ~ "(" ~ column_list ~ ")" ~
        "(" ~ index_partitioning_clause ~ ")" |
        "HASH" ~ "(" ~ column_list ~ ")" ~
        (individual_hash_partitions |
          hash_partitions_by_quantity))
  }

  /** Creates a rule for an index partitioning clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * PARTITION [ partition ]
    * VALUES LESS THAN (literal[, literal]... )
    * [ segment_attributes_clause ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2127580 Syntax Definition]]
    */
  def index_partitioning_clause = rule {
    "PARTITION" ~ optional(partition) ~
      "VALUES" ~ "LESS" ~ "THAN" ~ "(" ~ literal ~ zeroOrMore("," ~ literal) ~ ")" ~
      optional(segment_attributes_clause)
  }

  /** Creates a rule for a segment attributes clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * { physical_attributes_clause
    * | TABLESPACE tablespace
    * | logging_clause
    * }...
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2127592 Syntax Definition]]
    */
  def segment_attributes_clause = rule {
    oneOrMore(physical_attributes_clause |
      ("TABLESPACE" ~ tablespace) |
      logging_clause)
  }

  /** Creates a rule for individual has partitions.
    *
    * It matches the following grammar:
    *
    * {{{
    * ( PARTITION
    * [partition] [partitioning_storage_clause  ]]]
    * [, [partition] [partitioning_storage_clause  ]]]]...
    * )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2169764 Syntax Definition]]
    */
  def individual_hash_partitions = rule {
    "(" ~ "PARTITION" ~
      optional(partition) ~ optional(partitioning_storage_clause) ~
      zeroOrMore("," ~ optional(partition) ~ optional(partitioning_storage_clause)) ~
      ")"
  }

  /** Creates a rule for a partitioning storage clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ { TABLESPACE tablespace
    * | OVERFLOW [TABLESPACE tablespace]
    * | table_compression
    * | key_compression
    * | LOB_partitioning_storage
    * | VARRAY varray_item STORE AS [SECUREFILE | BASICFILE] LOB LOB_segname
    * }...
    * ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2169800 Syntax Definition]]
    */
  def partitioning_storage_clause = rule {
    zeroOrMore(("TABLESPACE" ~ tablespace) |
      ("OVERFLOW" ~ optional("TABLESPACE" ~ tablespace)) |
      table_compression |
      key_compression |
      LOB_partitioning_storage |
      ("VARRAY" ~ varray_item ~ "STORE" ~ "AS" ~ optional("SECUREFILE" | "BASICFILE") ~ "LOB" ~ LOB_segname))
  }

  /** Creates a rule for a LOB partitioning storage.
    *
    * It matches the following grammar:
    *
    * {{{
    * LOB (LOB_item) STORE AS [BASICFILE | SECUREFILE]
    * [ LOB_segname [ (TABLESPACE tablespace) ]
    * | (TABLESPACE tablespace)
    * ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53968 Syntax Definition]]
    */
  def LOB_partitioning_storage = rule {
    "LOB" ~ "(" ~ LOB_item ~ ")" ~ "STORE" ~ "AS" ~ optional("BASICFILE" | "SECUREFILE") ~
      optional((LOB_segname ~ optional("(" ~ "TABLESPACE" ~ tablespace ~ ")")) |
        ("(" ~ "TABLESPACE" ~ tablespace ~ ")"))
  }

  /** Creates a rule for has partitions by quantity.
    *
    * It matches the following grammar:
    *
    * {{{
    * PARTITIONS hash_partition_quantity
    * [ STORE IN (tablespace [, tablespace ]...) ]
    * [ key_compression | table_compression ]
    * [ OVERFLOW STORE IN (tablespace [, tablespace ]...) ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53969 Syntax Definition]]
    */
  def hash_partitions_by_quantity = rule {
    "PARTITIONS" ~ hash_partition_quantity ~
      optional("STORE" ~ "IN" ~ "(" ~ tablespace ~ zeroOrMore("," ~ tablespace) ~ ")") ~
      optional(key_compression | table_compression) ~
      optional("OVERFLOW" ~ "STORE" ~ "IN" ~ "(" ~ tablespace ~ zeroOrMore("," ~ tablespace) ~ ")")
  }

  /** Creates a rule for a local partitioned index.
    *
    * It matches the following grammar:
    *
    * {{{
    * LOCAL
    * [ on_range_partitioned_table
    * | on_list_partitioned_table
    * | on_hash_partitioned_table
    * | on_comp_partitioned_table
    * ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2125897 Syntax Definition]]
    */
  def local_partitioned_index = rule {
    "LOCAL" ~
      optional(on_range_partitioned_table |
        on_list_partitioned_table |
        on_hash_partitioned_table |
        on_comp_partitioned_table)
  }

  /** Creates a rule for a on-range partitioned table.
    *
    * It matches the following grammar:
    *
    * {{{
    * ( PARTITION
    * [ partition ]
    * [ { segment_attributes_clause
    * | key_compression
    * }...
    * ] [ UNUSABLE ]
    * [, PARTITION
    * [ partition ]
    * [ { segment_attributes_clause
    * | key_compression
    * }...
    * ] [ UNUSABLE ]
    * ]...
    * )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2127604 Syntax Definition]]
    */
  def on_range_partitioned_table = rule {
    "(" ~ "PARTITION" ~
      optional(partition) ~
      zeroOrMore(segment_attributes_clause |
        key_compression) ~
      optional("UNUSABLE") ~
      zeroOrMore("," ~ "PARTITION" ~
        optional(partition) ~
        zeroOrMore(segment_attributes_clause |
          key_compression) ~
        optional("UNUSABLE")) ~
      ")"
  }

  /** Creates a rule for a on list partitioned table.
    *
    * It matches the following grammar:
    *
    * {{{
    * ( PARTITION
    * [ partition ]
    * [ { segment_attributes_clause
    * | key_compression
    * }...
    * ] [ UNUSABLE ]
    * [, PARTITION
    * [ partition ]
    * [ { segment_attributes_clause
    * | key_compression
    * }...
    * ] [ UNUSABLE ]
    * ]...
    * )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53973 Syntax Definition]]
    */
  def on_list_partitioned_table = rule {
    "(" ~ "PARTITION" ~
      optional(partition) ~
      zeroOrMore(segment_attributes_clause |
        key_compression) ~
      optional("UNUSABLE") ~
      zeroOrMore("," ~ "PARTITION" ~
        optional(partition) ~
        zeroOrMore(segment_attributes_clause |
          key_compression)) ~
      optional("UNUSABLE") ~
      ")"
  }

  /** Creates a rule for a on hash partitioned table.
    *
    * It matches the following grammar:
    *
    * {{{
    * { STORE IN (tablespace[, tablespace ]...)
    * | (PARTITION [ partition ] [ TABLESPACE tablespace ] [key_compression]
    * [, PARTITION [ partition ] [ TABLESPACE tablespace ] [key_compression]] ...
    * )
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53975 Syntax Definition]]
    */
  def on_hash_partitioned_table = rule {
    ("STORE" ~ "IN" ~ "(" ~ tablespace ~ zeroOrMore("," ~ tablespace) |
      "(" ~ "PARTITION" ~ optional(partition) ~ optional("TABLESPACE" ~ tablespace) ~ optional(key_compression) ~
      zeroOrMore("PARTITION" ~ optional(partition) ~ optional("TABLESPACE" ~ tablespace) ~ optional(key_compression)))
  }

  /** Creates a rule for a on comp partitioned table.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ STORE IN ( tablespace [, tablespace ]... ) ]
    * ( PARTITION
    * [ partition ]
    * [ { segment_attributes_clause
    * | key_compression
    * }...
    * ] [ UNUSABLE ] [ index_subpartition_clause ]
    * [, PARTITION
    * [ partition ]
    * [ { segment_attributes_clause
    * | key_compression
    * }...
    * ] [ UNUSABLE ] [ index_subpartition_clause ]
    * ]...
    * )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53976 Syntax Definition]]
    */
  def on_comp_partitioned_table = rule {
    optional("STORE" ~ "IN" ~ "(" ~ tablespace ~ zeroOrMore("," ~ tablespace) ~ ")") ~
      "(" ~ "PARTITION" ~
      optional(partition) ~
      zeroOrMore(segment_attributes_clause |
        key_compression) ~
      optional("UNUSABLE") ~ optional(index_subpartition_clause) ~
      zeroOrMore("," ~ "PARTITION") ~
      optional(partition) ~
      zeroOrMore(segment_attributes_clause |
        key_compression) ~
      optional("UNUSABLE") ~ optional(index_subpartition_clause) ~
      ")"
  }

  /** Creates a rule for an index subpartition clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * { STORE IN (tablespace[, tablespace ]...)
    * | (SUBPARTITION
    * [ subpartition ][ TABLESPACE tablespace ] [ key_compression ] [ UNUSABLE ]
    * [, SUBPARTITION
    * [ subpartition ][ TABLESPACE tablespace ] [ key_compression ] [ UNUSABLE ]
    * ]...
    * )
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2127707 Syntax Definition]]
    */
  def index_subpartition_clause = rule {
    ("STORE" ~ "IN" ~ "(" ~ tablespace ~ zeroOrMore("," ~ tablespace) ~ ")" |
      "(" ~ "SUBPARTITION" ~
      optional(subpartition) ~ optional("TABLESPACE" ~ tablespace) ~ optional(key_compression) ~ optional("UNUSABLE") ~
      zeroOrMore("," ~ "SUBPARTITION" ~
        optional(subpartition) ~ optional("TABLESPACE" ~ tablespace) ~ optional(key_compression) ~ optional("UNUSABLE")) ~
      ")")
  }

  /** Creates a rule for a domain index clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * indextype
    * [ local_domain_index_clause ]
    * [ parallel_clause ]
    * [ PARAMETERS ('ODCI_parameters') ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53961 Syntax Definition]]
    */
  def domain_index_clause = rule {
    indextype ~
      optional(local_domain_index_clause) ~
      optional(parallel_clause) ~
      optional("PARAMETERS" ~ "(" ~ "'" ~ ODCI_parameters ~ "'" ~ ")")
  }

  /** Creates a rule for a local domain index clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * LOCAL
    * [ ( PARTITION partition [ PARAMETERS ( 'ODCI_parameters' ) ]
    * [,  PARTITION partition [ PARAMETERS ('ODCI_parameters') ]]...
    * )
    * ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53962 Syntax Definition]]
    */
  def local_domain_index_clause = rule {
    "LOCAL" ~
      optional(("(" ~ "PARTITION" ~ partition ~ optional("PARAMETERS" ~ "(" ~ "'" ~ ODCI_parameters ~ "'" ~ ")")) ~
        zeroOrMore("," ~ "PARTITION" ~ partition ~ optional("PARAMETERS" ~ "(" ~ "'" ~ ODCI_parameters ~ "'" ~ ")")) ~
        ")")
  }

  /** Creates a rule for an XMLIndex clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * [XDB.] XMLINDEX [ local_XMLIndex_clause ]
    * [ parallel_clause ]
    * [ XMLIndex_parameters_clause ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53963 Syntax Definition]]
    */
  def XMLIndex_clause = rule {
    optional("XDB.") ~ "XMLINDEX" ~ optional(local_XMLIndex_clause) ~
      optional(parallel_clause) ~
      optional(XMLIndex_parameters_clause)
  }

  /** Creates a rule for a local XMLIndex clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * LOCAL
    * [ ( PARTITION partition [ XMLIndex_parameters_clause ]
    * [, PARTITION partition [ XMLIndex_parameters)clause ]]...
    * )
    * ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53964 Syntax Definition]]
    */
  def local_XMLIndex_clause = rule {
    "LOCAL" ~
      optional("(" ~ "PARTITION" ~ partition ~ optional(XMLIndex_parameters_clause) ~
        zeroOrMore("," ~ "PARTITION" ~ partition ~ optional(XMLIndex_parameters_clause)) ~
        ")")
  }

  /** Creates a rule for a bitmap join index clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ schema.]table
    * ( [ [ schema. ]table. | t_alias. ]column
    * [ ASC | DESC  ]
    * [, [ [ schema. ]table. | t_alias. ]column
    * [ ASC | DESC ]
    * ]...
    * )
    * FROM [ schema. ]table [ t_alias ]
    * [, [ schema. ]table [ t_alias ]
    * ]...
    * WHERE condition
    * [ local_partitioned_index ] index_attributes
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#SQLRF53954 Syntax Definition]]
    */
  def bitmap_join_index_clause = rule {
    optional(schema ~ ".") ~ table ~
      "(" ~ optional(optional(schema ~ ".") ~ (table | table_alias) ~ ".") ~ column ~
      optional("ASC" | "DESC") ~
      zeroOrMore("," ~ optional(optional(schema ~ ".") ~ (table | table_alias) ~ ".") ~ column ~
        optional("ASC" | "DESC")) ~
      ")" ~
      "FROM" ~ optional(schema ~ ".") ~ table ~ optional(table_alias) ~
      zeroOrMore("," ~ optional(schema ~ ".") ~ table ~ optional(table_alias)) ~
      "WHERE" ~ condition ~
      optional(local_partitioned_index) ~ index_attributes
  }

  /** Creates a rule for an XMLIndex parameters clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * PARAMETERS ( ' { XMLIndex_parameters | PARAM identifier } ' )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4409 Syntax Definition]]
    */
  def XMLIndex_parameters_clause = rule {
    "PARAMETERS" ~ "(" ~ "'" ~ (XMLIndex_parameters | ("PARAM" ~ identifier)) ~ "'" ~ ")"
  }

  /** Creates a rule for XMLIndex parameters.
    *
    * It matches the following grammar:
    *
    * {{{
    * XMLIndex_parameter_clause [ XMLIndex_parameter_clause ] ...
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4410 Syntax Definition]]
    */
  def XMLIndex_parameters = rule {
    oneOrMore(XMLIndex_parameter_clause)
  }

  /** Creates a rule for an XMLIndex parameter clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * unstructured_clause | structured_clause | async_clause
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4411 Syntax Definition]]
    */
  def XMLIndex_parameter_clause = rule {
    unstructured_clause | structured_clause | async_clause
  }

  /** Creates a rule for an unstructured clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * {   PATHS { create_index_paths_clause | alter_index_paths_clause } }
    * | { path_table_clause | pikey_clause | path_id_clause |
    * order_key_clause | value_clause | drop_path_table_clause }
    * [parallel_clause] }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB5832 Syntax Definition]]
    */
  def unstructured_clause = rule {
    (("PATHS" ~ (create_index_paths_clause | alter_index_paths_clause)) |
      (path_table_clause | pikey_clause | path_id_clause |
        order_key_clause | value_clause | drop_path_table_clause)) ~
        optional(parallel_clause)
  }

  /** Creates a rule for an create index paths clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * { INCLUDE  | EXCLUDE } (XPaths_list) [namespace_mapping_clause]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4412 Syntax Definition]]
    */
  def create_index_paths_clause = rule {
    ("INCLUDE" | "EXCLUDE") ~ "(" ~ XPaths_list ~ ")" ~ optional(namespace_mapping_clause)
  }

  /** Creates a rule for a namespace mapping clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * NAMESPACE MAPPING ( { namespace } ... )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4414 Syntax Definition]]
    */
  def namespace_mapping_clause = rule {
    "NAMESPACE" ~ "MAPPING" ~ "(" ~ oneOrMore(namespace) ~ ")"
  }

  /** Creates a rule for an alter index paths clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * ( INDEX_ALL_PATHS
    * | {INCLUDE | EXCLUDE} {ADD | REMOVE} ( XPaths_list ) [namespace_mapping_clause]
    * )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4413 Syntax Definition]]
    */
  def alter_index_paths_clause = rule {
    "(" ~ "INDEX_ALL_PATHS" |
      (("INCLUDE" | "EXCLUDE") ~ ("ADD" | "REMOVE") ~ "(" ~ XPaths_list ~ ")" ~ optional(namespace_mapping_clause)) ~
      ")"
  }

  /** Creates a rule for a path table clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * PATH TABLE [identifier] [ (segment_attributes_clause table_properties) ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4415 Syntax Definition]]
    */
  def path_table_clause = rule {
    "PATH" ~ "TABLE" ~ optional(identifier) ~ optional("(" ~ segment_attributes_clause ~ table_properties ~ ")")
  }

  /** Creates a rule for a pikey clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * PIKEY [INDEX [identifier] [(index_attributes)]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB5833 Syntax Definition]]
    */
  def pikey_clause = rule {
    "PIKEY" ~ optional("INDEX" ~ optional(identifier) ~ optional("(" ~ index_attributes ~ ")"))
  }

  /** Creates a rule for a path id clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * PATH ID [INDEX [identifier] [(index_attributes)]]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4416 Syntax Definition]]
    */
  def path_id_clause = rule {
    "PATH" ~ "ID" ~ optional("INDEX" ~ optional(identifier) ~ optional("(" ~ index_attributes ~ ")"))
  }

  /** Creates a rule for an order key clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * ORDER KEY [INDEX [identifier] [(index_attributes)]]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4417 Syntax Definition]]
    */
  def order_key_clause = rule {
    "ORDER" ~ "KEY" ~ optional("INDEX" ~ optional(identifier) ~ optional("(" ~ index_attributes ~ ")"))
  }

  /** Creates a rule for a value clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * VALUE [INDEX [identifier] [(index_attributes)]]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4418 Syntax Definition]]
    */
  def value_clause = rule {
    "VALUE" ~ optional("INDEX" ~ optional(identifier) ~ optional("(" ~ index_attributes ~ ")"))
  }

  /** Creates a rule for a drop path table clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * DROP PATH TABLE
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB5834 Syntax Definition]]
    */
  def drop_path_table_clause = rule {
    "DROP" ~ "PATH" ~ "TABLE"
  }

  /** Creates a rule for a structured clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * groups_clause | alter_index_group_clause
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4420 Syntax Definition]]
    */
  def structured_clause = rule {
    groups_clause | alter_index_group_clause
  }

  /** Creates a rule for a groups clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * group_clause ...
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4421 Syntax Definition]]
    */
  def groups_clause = rule {
    oneOrMore(group_clause)
  }

  /** Creates a rule for a group clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ GROUP identifier ] XMLIndex_xmltable_clause
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4422 Syntax Definition]]
    */
  def group_clause = rule {
    optional("GROUP" ~ identifier) ~ XMLIndex_xmltable_clause
  }

  /** Creates a rule for an XMLIndex xml table clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * XMLTABLE identifier [ (segment_attributes_clause table_properties) ]
    * [ XML_namespaces_clause , ] XQuery_string
    * [ PASSING identifier ] COLUMNS column_clause [ , column_clause ] ...
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4423 Syntax Definition]]
    */
  def XMLIndex_xmltable_clause = rule {
    "XMLTABLE" ~ identifier ~ optional("(" ~ segment_attributes_clause ~ table_properties ~ ")") ~
      optional(XML_namespaces_clause ~ ",") ~ XQuery_string ~
      optional("PASSING" ~ identifier) ~ "COLUMNS" ~ column_clause ~ zeroOrMore("," ~ column_clause)
  }

  /** Creates a rule for a column clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * column { FOR ORDINALITY } | { datatype PATH string [ VIRTUAL ] }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4424 Syntax Definition]]
    */
  def column_clause = rule {
    column ~ (("FOR" ~ "ORDINALITY") | (datatype ~ "PATH" ~ string ~ optional("VIRTUAL")))
  }

  /** Creates a rule for an alter index group clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * { ADD_GROUP groups_clause } |
    * { DROP_GROUP [ GROUP identifier ] ... |
    * add_column_clause |
    * drop_column_clause
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4425
    */
  def alter_index_group_clause = rule {
    ("ADD_GROUP" ~ groups_clause) |
      ("DROP_GROUP" ~ optional("GROUP" ~ identifier ~ zeroOrMore("," ~ "GROUP" ~ identifier))) |
      add_column_clause |
      drop_column_clause
  }

  /** Creates a rule for an add column clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * ADD_COLUMN add_column_options
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4426 Syntax Definition]]
    */
  def add_column_clause = rule {
    "ADD_COLUMN" ~ add_column_options
  }

  /** Creates a rule for add column options.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ GROUP identifier ] XMLTABLE identifier [XML_namespaces_clause , ]
    * COLUMNS column_clause [ , column_clause ] ...
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4427 Syntax Definition]]
    */
  def add_column_options = rule {
    optional("GROUP" ~ identifier) ~ "XMLTABLE" ~ identifier ~ optional(XML_namespaces_clause ~ ",") ~
      "COLUMNS" ~ column_clause ~ zeroOrMore("," ~ column_clause)
  }

  /** Creates a rule for a drop column clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * DROP_COLUMN drop_column_options
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4428 Syntax Definition]]
    */
  def drop_column_clause = rule {
    "DROP_COLUMN" ~ drop_column_options
  }

  /** Creates a rule for drop column options.
    *
    * It matches the following grammar:
    *
    * {{{
    * [ GROUP identifier ] XMLTABLE identifier
    * COLUMNS identifier [ , identifier ] ...
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB4429 Syntax Definition]]
    */
  def drop_column_options = rule {
    optional("GROUP" ~ identifier) ~ "XMLTABLE" ~ identifier ~
      "COLUMNS" ~ identifier ~ zeroOrMore("," ~ identifier)
  }

  /** Creates a rule for an async clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * ASYNC ( SYNC { ALWAYS | MANUAL | EVERY repeat_interval | ON COMMIT }
    * [ STALE ( {FALSE | TRUE} ) ] )
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/appdev.112/e23094/xdb_indexing.htm#ADXDB5835 Syntax Definition]]
    */
  def async_clause = rule {
    "ASYNC" ~ "(" ~ "SYNC" ~ ("ALWAYS" | "MANUAL" | ("EVERY" ~ repeat_interval) | ("ON" ~ "COMMIT")) ~
      optional("STALE" ~ "(" ~ ("FALSE" | "TRUE") ~ ")") ~ ")"
  }

}
