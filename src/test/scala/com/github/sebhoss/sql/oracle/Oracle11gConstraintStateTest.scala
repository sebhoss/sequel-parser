package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/** Checks whether the supplied Oracle 11g parser understands constraint states.
  *
  * Constraint states are build by the following expression:
  *
  * {{{
  * [ [ [ NOT ] DEFERRABLE ]
  * [ INITIALLY { IMMEDIATE | DEFERRED } ]
  * | [ INITIALLY { IMMEDIATE | DEFERRED } ]
  * [ [ NOT ] DEFERRABLE ]
  * ]
  * [ RELY | NORELY ]
  * [ using_index_clause ]
  * [ ENABLE | DISABLE ]
  * [ VALIDATE | NOVALIDATE ]
  * [ exceptions_clause ]
  * }}}
  *
  * @see Oracle11gUsingIndexClauseTest
  * @see Oracle11gExceptionsClauseTest
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJAFFBAA constraint state syntax definition]]
  */
class Oracle11gConstraintStateTest extends Oracle11gTest {

  def rule = Oracle11gConstraintParser.constraint_state

  def statements = Oracle11gConstraintStateTest.statements

}

object Oracle11gConstraintStateTest {

  val statements = {
    val deferable = cartesian(Set("NOT DEFERABLE", "DEFERABLE", ""), Set("INITIALLY IMMEDIATE", "INITIALLY DEFERED", ""))
    val rely = Set("RELY", "NORELY", "")
    val usingIndexClause = Oracle11gUsingIndexClauseTest.statements ++ Set("")
    val enable = Set("ENABLE", "DISABLE", "")
    val validate = Set("VALIDATE", "NOVALIDATE", "")
    val exception = Oracle11gExceptionsClauseTest.statements

    cartesian(deferable, rely, usingIndexClause, enable, validate, exception)
  }

}