package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._

/** Parser for a size_clause expression.
  *
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses008.htm#SQLRF30012 Syntax Definition]]
  */
object Oracle11gSizeClauseParser extends AbstractParser {

  /** Creates a rule for a size clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * integer [ K | M | G | T | P | E ]
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses008.htm#CHDEAIID Syntax Definition]]
    */
  def size_clause = rule {
    integer ~ ("K" | "M" | "G" | "T" | "P" | "E")
  }

}