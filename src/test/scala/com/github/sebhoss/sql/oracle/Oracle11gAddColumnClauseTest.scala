package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gAddColumnClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateIndexParser.add_column_clause

  def statements = Oracle11gAddColumnClauseTest.statements

}

object Oracle11gAddColumnClauseTest {

  val statements = {
    val addColumn = List("ADD_COLUMN")
    val group = List("", "GROUP identifier")
    val columns = cartesian(List("XMLTABLE identifier COLUMNS"), Oracle11gColumnClauseTest.statements.toList).toList

    cartesian(addColumn, group, columns)
  }

}