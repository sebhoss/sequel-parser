package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.oracle.Oracle11gAllocateExtentClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._
import com.github.sebhoss.sql.oracle.Oracle11gCreateIndexParser._
import com.github.sebhoss.sql.oracle.Oracle11gDeallocateUnusedClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gLoggingClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gParallelClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gPhysicalAttributesClauseParser._

import org.parboiled.scala._

/** Parser for an alter index expression.
  *
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#i2050158 Syntax Definition]]
  */
object Oracle11gAlterIndexParser extends AbstractParser {

  /** Creates a rule which matches multiple `ALTER INDEX` statements.
    */
  def AlterIndexStatements = rule {
    oneOrMore(alter_index) ~ EOI
  }

  /** Creates a rule for a alter index statement.
    *
    * It matches the following grammar:
    *
    * {{{
    * ALTER INDEX [ schema. ]index
    * { { deallocate_unused_clause
    * | allocate_extent_clause
    * | shrink_clause
    * | parallel_clause
    * | physical_attributes_clause
    * | logging_clause
    * } ...
    * | rebuild_clause
    * | PARAMETERS ( 'ODCI_parameters' )
    * )
    * | COMPILE
    * | { ENABLE | DISABLE }
    * | UNUSABLE
    * | VISIBLE | INVISIBLE
    * | RENAME TO new_name
    * | COALESCE
    * | { MONITORING | NOMONITORING } USAGE
    * | UPDATE BLOCK REFERENCES
    * | alter_index_partitioning
    * }
    * ;
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#SQLRF52716 Syntax Definition]]
    */
  def alter_index = rule {
    "ALTER" ~ "INDEX" ~ optional(schema ~ ".") ~ index ~
      (oneOrMore(deallocate_unused_clause |
        allocate_extent_clause |
        shrink_clause |
        parallel_clause |
        physical_attributes_clause |
        logging_clause) |
        rebuild_clause |
        ("PARAMETERS" ~ "(" ~ "'" ~ ODCI_parameters ~ "'" ~ ")") |
        "COMPILE" |
        ("ENABLE" | "DISBALE") |
        "UNUSABLE" |
        ("VISIBLE" | "INVISIBLE") |
        ("RENAME" ~ "TO" ~ new_name) |
        "COALESCE" |
        (("MONITORING" | "NOMONITORING") ~ "USAGE") |
        ("UPDATE" ~ "BLOCK" ~ "REFERENCES") |
        alter_index_partitioning)
  }

  /** Creates a rule for a shrink clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * SHRINK SPACE [ COMPACT ] [ CASCADE ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#BGEFECBA Syntax Definition]]
    */
  def shrink_clause = rule {
    "SHRINK" ~ "SPACE" ~ optional("COMPACT") ~ optional("CASCADE")
  }

  /** Creates a rule for a rebuild clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * REBUILD
    * [ { PARTITION partition
    * | SUBPARTITION subpartition
    * }
    * | { REVERSE | NOREVERSE }
    * ]
    * [ parallel_clause
    * | TABLESPACE tablespace
    * | PARAMETERS ( 'ODCI_parameters' )
    * | XMLIndex_parameters_clause
    * | ONLINE
    * | physical_attributes_clause
    * | key_compression
    * | logging_clause
    * ]...
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#BGEEJJJJ Syntax Definition]]
    */
  def rebuild_clause = rule {
    "REBUILD" ~
      optional((("PARTITION" ~ partition) |
        ("SUBPARTITION" ~ subpartition)) |
        ("REVERSE" | "NOREVERSE")) ~
      zeroOrMore(parallel_clause |
        ("TABLESPACE" ~ tablespace) |
        ("PARAMETERS" ~ "(" ~ "'" ~ ODCI_parameters ~ "'" ~ ")") |
        XMLIndex_parameters_clause |
        "ONLINE" |
        physical_attributes_clause |
        key_compression |
        logging_clause)
  }

  /** Creates a rule for an alter index partitioning.
    *
    * It matches the following grammar:
    *
    * {{{
    * { modify_index_default_attrs
    * | add_hash_index_partition
    * | modify_index_partition
    * | rename_index_partition
    * | drop_index_partition
    * | split_index_partition
    * | coalesce_index_partition
    * | modify_index_subpartition
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#i2128532 Syntax Definition]]
    */
  def alter_index_partitioning = rule {
    (modify_index_default_attrs |
      add_hash_index_partition |
      modify_index_partition |
      rename_index_partition |
      drop_index_partition |
      split_index_partition |
      coalesce_index_partition |
      modify_index_subpartition)
  }

  /** Creates a rule for modify index default attributes.
    *
    * It matches the following grammar:
    *
    * {{{
    * MODIFY DEFAULT ATTRIBUTES
    * [ FOR PARTITION partition ]
    * { physical_attributes_clause
    * | TABLESPACE { tablespace | DEFAULT }
    * | logging_clause
    * }...
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#i2129868 Syntax Definition]]
    */
  def modify_index_default_attrs = rule {
    "MODIFY" ~ "DEFAULT" ~ "ATTRIBUTES" ~
      optional("FOR" ~ "PARTITION" ~ partition) ~
      oneOrMore(physical_attributes_clause |
        ("TABLESPACE" ~ (tablespace | "DEFAULT")) |
        logging_clause)
  }

  /** Creates a rule for add hash index partition.
    *
    * It matches the following grammar:
    *
    * {{{
    * ADD PARTITION
    * [ partition_name ]
    * [ TABLESPACE tablespace_name ]
    * [ key_compression ]
    * [ parallel_clause ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#BGEEAJJI Syntax Definition]]
    */
  def add_hash_index_partition = rule {
    "ADD" ~ "PARTITION" ~
      optional(partition_name) ~
      optional("TABLESPACE" ~ tablespace_name) ~
      optional(key_compression) ~
      optional(parallel_clause)
  }

  /** Creates a rule for modify index partition.
    *
    * It matches the following grammar:
    *
    * {{{
    * MODIFY PARTITION partition
    * { { deallocate_unused_clause
    * | allocate_extent_clause
    * | physical_attributes_clause
    * | logging_clause
    * | key_compression
    * }...
    * | PARAMETERS ('ODCI_parameters')
    * | COALESCE
    * | UPDATE BLOCK REFERENCES
    * | UNUSABLE
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#BGECEJHE Syntax Definition]]
    */
  def modify_index_partition = rule {
    "MODIFY" ~ "PARTITION" ~ partition ~
      (oneOrMore(deallocate_unused_clause |
        allocate_extent_clause |
        physical_attributes_clause |
        logging_clause |
        key_compression) |
        ("PARAMETERS" ~ "(" ~ "'" ~ ODCI_parameters ~ "'" ~ ")") |
        "COALESCE" |
        ("UPDATE" ~ "BLOCK" ~ "REFERENCES") |
        "UNUSABLE")
  }

  /** Creates a rule for rename index partition.
    *
    * It matches the following grammar:
    *
    * {{{
    * RENAME
    * { PARTITION partition | SUBPARTITION subpartition }
    * TO new_name
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#BGEFBIII Syntax Definition]]
    */
  def rename_index_partition = rule {
    "RENAME" ~
      (("PARTITION" ~ partition) | ("SUBPARTITION" ~ subpartition)) ~
      "TO" ~ new_name
  }

  /** Creates a rule for drop index partition.
    *
    * It matches the following grammar:
    *
    * {{{
    * DROP PARTITION partition_name
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#BGEDHDBA Syntax Definition]]
    */
  def drop_index_partition = rule {
    "DROP" ~ "PARTITION" ~ partition_name
  }

  /** Creates a rule for split index partition.
    *
    * It matches the following grammar:
    *
    * {{{
    * SPLIT PARTITION partition_name_old
    * AT (literal [, literal ]...)
    * [ INTO (index_partition_description,
    * index_partition_description
    * )
    * ]
    * [ parallel_clause ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#BGEHAAIB Syntax Definition]]
    */
  def split_index_partition = rule {
    "SPLIT" ~ "PARTITION" ~ partition_name_old ~
      "AT" ~ "(" ~ literal ~ zeroOrMore("," ~ literal) ~ ")" ~
      optional("INTO" ~ "(" ~ index_partition_description ~ "," ~ index_partition_description ~
        ")") ~
      optional(parallel_clause)
  }

  /** Creates a rule for a index partition description.
    *
    * It matches the following grammar:
    *
    * {{{
    * PARTITION
    * [ partition
    * [ { segment_attributes_clause
    * | key_compression
    * }...
    * | ( PARAMETERS 'ODCI_parameters' )
    * ]
    * [ UNUSABLE ]
    * ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#SQLRF52733 Syntax Definition]]
    */
  def index_partition_description = rule {
    "PARTITION" ~
      optional(partition ~
        optional(zeroOrMore(segment_attributes_clause |
          key_compression) |
          ("(" ~ "PARAMETERS" ~ "'" ~ ODCI_parameters ~ "'" ~ ")")) ~
        optional("UNUSABLE"))
  }

  /** Creates a rule for a coalesce index partition.
    *
    * It matches the following grammar:
    *
    * {{{
    * COALESCE PARTITION [ parallel_clause ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#BGEBFHBI Syntax Definition]]
    */
  def coalesce_index_partition = rule {
    "COALESCE" ~ "PARTITION" ~ optional(parallel_clause)
  }

  /** Creates a rule for modify index subpartition.
    *
    * It matches the following grammar:
    *
    * {{{
    * MODIFY SUBPARTITION subpartition
    * { UNUSABLE
    * | allocate_extent_clause
    * | deallocate_unused_clause
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_1010.htm#i2128786 Syntax Definition]]
    */
  def modify_index_subpartition = rule {
    "MODIFY" ~ "SUBPARTITION" ~ subpartition ~
      ("UNUSABLE" |
        allocate_extent_clause |
        deallocate_unused_clause)
  }

}