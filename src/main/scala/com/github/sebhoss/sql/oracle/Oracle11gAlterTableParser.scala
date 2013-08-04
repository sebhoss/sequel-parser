package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._
import com.github.sebhoss.sql.oracle.Oracle11gCreateTableParser._
import com.github.sebhoss.sql.oracle.Oracle11gPhysicalAttributesClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gLoggingClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gAllocateExtentClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gDeallocateUnusedClauseParser._
import com.github.sebhoss.sql.oracle.Oracle11gParallelClauseParser._

import org.parboiled.scala._

object Oracle11gAlterTableParser extends AbstractParser {

  /** Creates a rule which matches multiple `ALTER TABLE` statements.
    */
  def AlterTableStatements = rule {
    oneOrMore(alter_table) ~ EOI
  }

  /** Creates a rule for an alter table statement.
    *
    * It matches the following grammar:
    *
    * {{{
    * ALTER TABLE [ schema. ] table
    * [ alter_table_properties
    * | column_clauses
    * | constraint_clauses
    * | alter_table_partitioning
    * | alter_external_table
    * | move_table_clause
    * ]
    * [ enable_disable_clause
    * | { ENABLE | DISABLE } { TABLE LOCK | ALL TRIGGERS }
    * ] ...
    * ;
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_3001.htm#SQLRF53171 Syntax Definition]]
    */
  def alter_table = rule {
    "ALTER" ~ "TABLE" ~ optional(schema ~ ".") ~ table ~
      optional(alter_table_properties |
        column_clauses |
        constraint_clauses |
        alter_table_partitioning |
        alter_external_table |
        move_table_clause) ~
      oneOrMore(enable_disable_clause |
        (("ENABLE" | "DISABLE") ~ (("TABLE" ~ "LOCK") | ("ALL" ~ "TRIGGERS")))) ~
      ";"
  }

  /** Creates a rule for a alter table properties.
    *
    * It matches the following grammar:
    *
    * {{{
    * { { { physical_attributes_clause
    * | logging_clause
    * | table_compression
    * | supplemental_table_logging
    * | allocate_extent_clause
    * | deallocate_unused_clause
    * | { CACHE | NOCACHE }
    * | RESULT_CACHE ( MODE {DEFAULT | FORCE} )
    * | upgrade_table_clause
    * | records_per_block_clause
    * | parallel_clause
    * | row_movement_clause
    * | flashback_archive_clause
    * }...
    * | RENAME TO new_table_name
    * } [ alter_iot_clauses ] [ alter_XMLSchema_clause ]
    * | { shrink_clause
    * | READ ONLY
    * | READ WRITE
    * | REKEY encryption_spec
    * }
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_3001.htm#i2192749 Syntax Definition]]
    */
  def alter_table_properties = rule {
    oneOrMore(physical_attributes_clause |
      logging_clause |
      table_compression |
      supplemental_table_logging |
      allocate_extent_clause |
      deallocate_unused_clause |
      ("CACHE" | "NOCACHE") |
      ("RESULT" ~ "CACHE" ~ "(" ~ "MODE" ~ ("DEFAULT" | "FORCE") ~ ")") |
      upgrade_table_clause |
      records_per_block_clause |
      parallel_clause |
      row_movement_clause |
      flashback_archive_clause)
  }

  def supplemental_table_logging = rule {
    NOTHING
  }

  def upgrade_table_clause = rule {
    NOTHING
  }

  def records_per_block_clause = rule {
    NOTHING
  }

  /** Creates a rule for column clauses
    *
    * It matches the following grammar:
    *
    * {{{
    * { { add_column_clause
    * | modify_column_clause
    * | drop_column_clause
    * }...
    * | rename_column_clause
    * | { modify_collection_retrieval }...
    * | { modify_LOB_storage_clause }...
    * | { alter_varray_col_properties }...
    * }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_3001.htm#i2103924 Syntax Definition]]
    */
  def column_clauses = rule {
    NOTHING
  }

  def constraint_clauses = rule {
    NOTHING
  }

  def alter_table_partitioning = rule {
    NOTHING
  }

  def alter_external_table = rule {
    NOTHING
  }

  def move_table_clause = rule {
    NOTHING
  }

}