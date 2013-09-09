package com.github.sebhoss.sql.oracle

class Oracle11gConstraintTest extends Oracle11gTest {

  def rule = Oracle11gConstraintParser.constraint

  def statements = Oracle11gConstraintTest.statements

}

object Oracle11gConstraintTest {

  val statements = {
    Oracle11gInlineConstraintTest.statements ++
    Oracle11gOutOfLineConstraintTest.statements ++
    Oracle11gInlineRefConstraintTest.statements ++
    Oracle11gOutOfLineRefConstraintTest.statements
  }

}