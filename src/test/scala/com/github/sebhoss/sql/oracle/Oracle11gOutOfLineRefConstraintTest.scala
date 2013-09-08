package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/** Checks whether the supplied Oracle 11g parser understands out of line ref constraints.
  *
  * Out of line ref constraints are build by the following expression:
  *
  * {{{
  * { SCOPE FOR ({ ref_col | ref_attr })
  * IS [ schema. ] scope_table
  * | REF ({ ref_col | ref_attr }) WITH ROWID
  * | [ CONSTRAINT constraint_name ] FOREIGN KEY
  * ( { ref_col | ref_attr } ) references_clause
  * [ constraint_state ]
  * }
  * }}}
  *
  * @see Oracle11gReferencesClauseTest
  * @see Oracle11gConstraintStateTest
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#CJADJGEC out of line ref constraints syntax definition]]
  */
class Oracle11gOutOfLineRefConstraintTest extends Oracle11gTest {

  def rule = Oracle11gConstraintParser.out_of_line_ref_constraint

  def statements = Oracle11gOutOfLineRefConstraintTest.statements

}

object Oracle11gOutOfLineRefConstraintTest {

  val statements = {
    val scope = List("SCOPE FOR (column) IS table", "SCOPE FOR (column) IS schema.table", "")
    val ref = List("REF (column) WITH ROWID")
    val constraint = List("CONSTRAINT constraint FOREIGN KEY (column)", "FOREIGN KEY (column)") ++ 
    	Oracle11gReferencesClauseTest.statements.toList

    cartesian(scope, ref, constraint)
  }

}