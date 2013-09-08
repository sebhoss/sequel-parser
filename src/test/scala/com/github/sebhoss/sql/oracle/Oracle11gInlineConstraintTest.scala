package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/**
 * Checks whether the supplied Oracle 11g parser understands inline constraints.
 *
 * Inline constraints are build by the following expression:
 *
 * {{{
 * [ CONSTRAINT constraint_name ]
 * { [ NOT ] NULL
 * | UNIQUE
 * | PRIMARY KEY
 * | references_clause
 * | CHECK (condition)
 * }
 * [ constraint_state ]
 * }}}
 *
 * @see Oracle11gReferencesClauseTest
 * @see Oracle11gConstraintStateTest
 * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#SQLRF52183 inline constraint syntax definition]]
 */
class Oracle11gInlineConstraintTest extends Oracle11gTest {

  def rule = Oracle11gConstraintParser.inline_constraint

  def statements = {
    val constraint = List("CONSTRAINT constraint", "")
    val attributes = List("NULL", "NOT NULL", "UNIQUE", "PRIMARY KEY", "CHECK (abc)") ++ Oracle11gReferencesClauseTest.statements.toList

    cartesian(constraint, attributes)
  }

}