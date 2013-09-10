package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gColumnDefinitionTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.column_definition

  def statements = Oracle11gColumnDefinitionTest.statements

}

object Oracle11gColumnDefinitionTest {

  val statements = {
    val column = Set("column")
    val datatype = Set(Oracle11gDataTypeTest.statements.head)
    val sort = Set("", "SORT")
    val expression = Set("", "DEFAULT expression") // TODO: Reference Oracle11gExpressionTest.statements
    val constraint = Set("(" + Oracle11gInlineConstraintTest.statements.head + ")") ++
    	Set(Oracle11gInlineRefConstraintTest.statements.head)

    cartesian(column, datatype, sort, expression, constraint)
  }

}