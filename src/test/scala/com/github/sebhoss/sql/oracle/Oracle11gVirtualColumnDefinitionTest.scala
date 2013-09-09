package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gVirtualColumnDefinitionTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.virtual_column_definition

  def statements = Oracle11gVirtualColumnDefinitionTest.statements

}

object Oracle11gVirtualColumnDefinitionTest {

  val statements = {
    val column = cartesian(List("column"), List("", "GENERATED ALWAYS"), List("AS (expression)")).toList
    val virtual = List("", "VIRTUAL")
    val constraint = List("") ++ Oracle11gInlineConstraintTest.statements

    cartesian(column, virtual, constraint)
  }

}