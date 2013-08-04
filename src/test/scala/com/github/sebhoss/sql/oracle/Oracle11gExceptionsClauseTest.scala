package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/** Checks whether the supplied Oracle 11g parser understands exception clauses.
  *
  * Exceptions clauses are build by the following expression:
  *
  * {{{
  * EXCEPTIONS INTO [ schema. ] table
  * }}}
  *
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJAEHIDE exception clause syntax definition]]
  */
class Oracle11gExceptionsClauseTest extends Oracle11gTest {

  def rule = Oracle11gConstraintParser.exceptions_clause

  def statements = Oracle11gExceptionsClauseTest.statements

}

object Oracle11gExceptionsClauseTest {
  
  val statements = {
    val exception = Set("EXCEPTIONS INTO")
    val table = Set("table", "schema.table")
    
    cartesian(exception, table)
  }
  
}