package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.AbstractParser
import com.github.sebhoss.sql.oracle.Oracle11gCommonParser._

/** Parser for a parallel_clause expression.
  *
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses006.htm#i77131 Syntax Definition]]
  */
object Oracle11gParallelClauseParser extends AbstractParser {

  /** Creates a rule for a parallel clause.
    *
    * It matches the following grammar:
    *
    * {{{
    * { NOPARALLEL | PARALLEL [ integer ] }
    * }}}
    *
    * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses006.htm#SQLRF52286 Syntax Definition]]
    */
  def parallel_clause = rule {
    ("NOPARALLEL" | "PARALLEL" ~ optional(integer))
  }

}