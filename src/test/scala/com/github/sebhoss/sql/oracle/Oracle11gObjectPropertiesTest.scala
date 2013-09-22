package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gObjectPropertiesTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.object_properties

  def statements = Oracle11gObjectPropertiesTest.statements

}

object Oracle11gObjectPropertiesTest {

  val statements = {
    val column = Set("column", "attribute")
    val expr = Set("", "DEFAULT expression")
    val inlineConstraint = Set("", Oracle11gInlineConstraintTest.statements.head, Oracle11gInlineRefConstraintTest.statements.head)
    val outOfLineConstraint = Oracle11gOutOfLineConstraintTest.statements.take(1) ++
    	Oracle11gOutOfLineRefConstraintTest.statements.take(1) ++
    	Oracle11gSupplementalLoggingPropsTest.statements.take(1)

    cartesian(column, expr, inlineConstraint) ++ outOfLineConstraint
  }

}