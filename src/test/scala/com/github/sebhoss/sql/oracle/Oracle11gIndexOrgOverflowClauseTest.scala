package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gIndexOrgOverflowClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.index_org_overflow_clause

  def statements = Oracle11gIndexOrgOverflowClauseTest.statements

}

object Oracle11gIndexOrgOverflowClauseTest {

  val statements = {
    val column = Set("", "INCLUDING column")
    val overflow = Set("OVERFLOW")
    val attributes = Set("", Oracle11gSegmentAttributesClauseTest.statements.head)

    cartesian(column, overflow, attributes)
  }

}