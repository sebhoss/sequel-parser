package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.oracle.Oracle11gSizeClauseParser._

/**
 * Parser for a deallocate_unused_clause expression.
 *
 * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses003.htm#i1004660 Syntax Definition]]
 */
object Oracle11gDeallocateUnusedClauseParser extends AbstractParser {

  /**
   * Creates a rule for a deallocate unused clause.
   *
   * It matches the following grammar:
   *
   * {{{
   * DEALLOCATE UNUSED [ KEEP size_clause ]
   * }}}
   *
   * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses003.htm#SQLRF52245 Syntax Definition]]
   */
  def deallocate_unused_clause = rule {
    "DEALLOCATE" ~ "UNUSED" ~ optional("KEEP" ~ size_clause)
  }

}