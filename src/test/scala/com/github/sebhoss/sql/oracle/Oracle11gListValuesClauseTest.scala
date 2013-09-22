package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gListValuesClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.list_values_clause

  def statements = Oracle11gListValuesClauseTest.statements

}

object Oracle11gListValuesClauseTest {

  val statements = {
    val values = Set("VALUES (")
    val literal = Set("literal", "NULL", "DEFAULT")

    cartesian(values, literal, Set(")"))
  }

}