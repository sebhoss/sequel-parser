package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/** Checks whether the supplied Oracle 11g parser understands using index clauses.
  *
  * Using index clauses are build by the following expression:
  *
  * {{{
  * USING INDEX
  * { [ schema. ] index
  * | (create_index_statement)
  * | index_properties
  * }
  * }}}
  *
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#SQLRF52189 using index clause syntax definition]]
  */
class Oracle11gUsingIndexClauseTest extends Oracle11gTest {

  def rule = Oracle11gConstraintParser.using_index_clause
  
  def statements = Oracle11gUsingIndexClauseTest.statements

}

object Oracle11gUsingIndexClauseTest {
  
  val statements = {
    val using = Set("USING INDEX")
    val index = Set("index", "schema.index")

    cartesian(using, index)
  }
  
}