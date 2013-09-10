package com.github.sebhoss.sql.oracle

class Oracle11gConstraintTest extends Oracle11gTest {

  def rule = Oracle11gConstraintParser.constraint

  def statements = Oracle11gConstraintTest.statements

}

object Oracle11gConstraintTest {

  val statements = {
    Set(Oracle11gInlineConstraintTest.statements.head,
        Oracle11gOutOfLineConstraintTest.statements.head,
        Oracle11gInlineRefConstraintTest.statements.head,
        Oracle11gOutOfLineRefConstraintTest.statements.head)
  }

}