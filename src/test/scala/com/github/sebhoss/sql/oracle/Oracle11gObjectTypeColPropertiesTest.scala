package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gObjectTypeColPropertiesTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.object_type_col_properties

  def statements = Oracle11gObjectTypeColPropertiesTest.statements

}

object Oracle11gObjectTypeColPropertiesTest {

  val statements = {
    cartesian(List("COLUMN column"), Oracle11gSubstitutableColumnClauseTest.statements.toList)
  }

}