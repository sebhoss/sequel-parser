package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gAddColumnClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateIndexParser.add_column_clause

  def statements = Oracle11gAddColumnClauseTest.statements

}

object Oracle11gAddColumnClauseTest {

  val statements = {
    val addColumn = Set("ADD_COLUMN")
    val group = Set("", "GROUP identifier")
    val columns = Set("XMLTABLE identifier COLUMNS")
    val columnClause = Oracle11gColumnClauseTest.statements.take(1)

    cartesian(addColumn, group, columns, columnClause)
  }

}