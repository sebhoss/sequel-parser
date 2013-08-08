package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/** Checks whether the supplied Oracle 11g parser understands inline ref constraints.
  *
  * Inline ref constraints are build by the following expression:
  *
  * {{{
  * { SCOPE  IS [ schema. ] scope_table
  * | WITH ROWID
  * | [ CONSTRAINT constraint_name ]
  * references_clause
  * [ constraint_state ]
  * }
  * }}}
  *
  * @see Oracle11gReferencesClauseTest
  * @see Oracle11gConstraintStateTest
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJAHIEIJ inline ref constraints syntax definition]]
  */
class Oracle11gInlineRefConstraintTest extends Oracle11gTest {

  def rule = Oracle11gConstraintParser.inline_ref_constraint
  
  def statements = Oracle11gInlineRefConstraintTest.statements

}

object Oracle11gInlineRefConstraintTest {

  val statements = {
    val scope = Set("SCOPE IS table", "SCOPE IS schema.table")
    val attribute = Set("WITH ROWID") ++ cartesian(Set("CONSTRAINT constraint", ""),
        Oracle11gReferencesClauseTest.statements)

    cartesian(scope, attribute)
  }

}