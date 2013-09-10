package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/** Checks whether the supplied Oracle 11g parser understands reference clauses.
  *
  * Reference clauses are build by the following expression:
  *
  * {{{
  * REFERENCES [ schema. ] { object_table | view }
  * [ (column [, column ]...) ]
  * [ON DELETE { CASCADE | SET NULL } ]
  * [ constraint_state ]
  * }}}
  *
  * @see Oracle11gConstraintStateTest
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJAIHHGC reference clause syntax definition]]
  */
class Oracle11gReferencesClauseTest extends Oracle11gTest {

  def rule = Oracle11gConstraintParser.references_clause
  
  def statements = Oracle11gReferencesClauseTest.statements

}

object Oracle11gReferencesClauseTest {

  val statements = {
    val references = Set("REFERENCES table", "REFERENCES schema.table")
    val column = Set("", "(column)", "(first, second)")
    val delete = Set("", "ON DELETE CASCADE", "ON DELETE SET NULL")
    val constraintState = Set(Oracle11gConstraintStateTest.statements.head)

    cartesian(references, column, delete, constraintState)
  }

}