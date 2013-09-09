package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gSubstitutableColumnClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.substitutable_column_clause

  def statements = Oracle11gSubstitutableColumnClauseTest.statements

}

object Oracle11gSubstitutableColumnClauseTest {

  val statements = {
    val element = List("", "ELEMENT")
    val isOf = List("IS OF")
    val typ = List("", "TYPE")
    val typeName = List("(ONLY name)", "(name)")
    
    val not = List("", "NOT")
    val subs = List("SUBSTITUTABLE AT ALL LEVELS")
    
    cartesian(element, isOf, typ, typeName) ++ cartesian(not, subs)
  }

}