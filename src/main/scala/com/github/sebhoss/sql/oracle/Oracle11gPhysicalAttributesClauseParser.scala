package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._
import com.github.sebhoss.sql.oracle.Oracle11gStorageClauseParser._

/**
 * Parser for a physical_attributes_clause expression.
 *
 * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses007.htm#i77584 Syntax Definition]]
 */
object Oracle11gPhysicalAttributesClauseParser extends AbstractParser {

  /**
   * Creates a rule for a physical attributes clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * [ { PCTFREE integer
   * | PCTUSED integer
   * | INITRANS integer
   * | storage_clause
   * }...
   * ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses007.htm#SQLRF52291 Syntax Definition]]
   */
  def physical_attributes_clause = rule {
    oneOrMore(("PCTFREE" ~ integer) |
      ("PCTUSED" ~ integer) |
      ("INITRANS" ~ integer) |
      storage_clause)
  }

}