package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gVirtualColumnDefinitionTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.virtual_column_definition

  def statements = Oracle11gVirtualColumnDefinitionTest.statements

}

object Oracle11gVirtualColumnDefinitionTest {

  val statements = {
    val column = cartesian(Set("column"), Set("", "GENERATED ALWAYS"), Set("AS (expression)"))
    val virtual = Set("", "VIRTUAL")
    val constraint = Set("", Oracle11gInlineConstraintTest.statements.head)

    cartesian(column, virtual, constraint)
  }

}