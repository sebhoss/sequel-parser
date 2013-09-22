package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gRangeValuesClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.range_values_clause

  def statements = Oracle11gRangeValuesClauseTest.statements

}

object Oracle11gRangeValuesClauseTest {

  val statements = {
    val values = Set("VALUES LESS THAN (")
    val opts = Set("literal", "MAXVALUE")

    cartesian(values, opts, Set(")"))
  }

}