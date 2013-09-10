package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gSubstitutableColumnClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.substitutable_column_clause

  def statements = Oracle11gSubstitutableColumnClauseTest.statements

}

object Oracle11gSubstitutableColumnClauseTest {

  val statements = {
    val element = Set("", "ELEMENT")
    val isOf = Set("IS OF")
    val typ = Set("", "TYPE")
    val typeName = Set("(ONLY name)", "(name)")

    val not = Set("", "NOT")
    val subs = Set("SUBSTITUTABLE AT ALL LEVELS")

    cartesian(element, isOf, typ, typeName).toList ++ cartesian(not, subs)
  }

}