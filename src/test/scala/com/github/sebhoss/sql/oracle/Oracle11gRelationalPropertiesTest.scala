package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gRelationalPropertiesTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.relational_properties

  def statements = Oracle11gRelationalPropertiesTest.statements

}

object Oracle11gRelationalPropertiesTest {

  val statements = Set(
      Oracle11gColumnDefinitionTest.statements.head,
      Oracle11gVirtualColumnDefinitionTest.statements.head,
      Oracle11gOutOfLineConstraintTest.statements.head,
      Oracle11gOutOfLineRefConstraintTest.statements.head,
      Oracle11gSupplementalLoggingPropsTest.statements.head
  )

}