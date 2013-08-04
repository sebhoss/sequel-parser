package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gDropColumnClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateIndexParser.drop_column_clause

  def statements = Oracle11gDropColumnClauseTest.statements

}

object Oracle11gDropColumnClauseTest {
  
  val statements = {
    val dropColumn = Set("DROP_COLUMN")
    val group = Set("", "GROUP identifier")
    val columns = Set("XMLTABLE identifier COLUMNS identifier", "XMLTABLE identifier COLUMNS first, second")
    
    cartesian(dropColumn, group, columns)
  }
  
}