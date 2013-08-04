package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser

/** Parser for a logging_clause expression.
  *
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses005.htm#i999782 Syntax Definition]]
  */
object Oracle11gLoggingClauseParser extends AbstractParser {

  /** Creates a rule for a logging clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * { LOGGING | NOLOGGING |  FILESYSTEM_LIKE_LOGGING }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses005.htm#SQLRF52283 Syntax Definition]]
    */
  def logging_clause = rule {
    ("LOGGING" | "NOLOGGING" | "FILESYSTEM_LIKE_LOGGING")
  }

}