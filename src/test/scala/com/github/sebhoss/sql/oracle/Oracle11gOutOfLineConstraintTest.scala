package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/** Checks whether the supplied Oracle 11g parser understands out of line constraints.
  *
  * Out of line constraints are build by the following expression:
  *
  * {{{
  * [ CONSTRAINT constraint_name ]
  * { UNIQUE (column [, column ]...)
  * | PRIMARY KEY (column [, column ]...)
  * | FOREIGN KEY (column [, column ]...) references_clause
  * | CHECK (condition)
  * } [ constraint_state ]
  * }}}
  *
  * @see Oracle11gReferencesClauseTest
  * @see Oracle11gConstraintStateTest
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJADJGEC out of line constraints syntax definition]]
  */
class Oracle11gOutOfLineConstraintTest extends Oracle11gTest {

  def rule = Oracle11gConstraintParser.out_of_line_constraint

  def statements = Oracle11gOutOfLineConstraintTest.statements

}

object Oracle11gOutOfLineConstraintTest {

  val statements = {
    val constraint = List("CONSTRAINT constraint", "")
    val attributes = List("UNIQUE (column)", "UNIQUE (first, second)", "PRIMARY KEY (column)",
        "PRIMARY KEY (first, second)", "CHECK (condition)") ++ cartesian(List("FOREIGN KEY (column)",
            "FOREIGN KEY (first, second)"), Oracle11gReferencesClauseTest.statements.toList).toList

    cartesian(constraint, attributes)
  }

}