package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gVarrayColPropertiesTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.varray_col_properties

  def statements = Oracle11gVarrayColPropertiesTest.statements

}

object Oracle11gVarrayColPropertiesTest {

  val statements = {
    val varray = Set("VARRAY item")
    val columnClause = Set("") ++ Oracle11gSubstitutableColumnClauseTest.statements.take(1)
    val storageClause = Oracle11gVarrayStorageClauseTest.statements.take(1)

    cartesian(varray, cartesian(columnClause, storageClause), Oracle11gSubstitutableColumnClauseTest.statements.take(1))
  }

}