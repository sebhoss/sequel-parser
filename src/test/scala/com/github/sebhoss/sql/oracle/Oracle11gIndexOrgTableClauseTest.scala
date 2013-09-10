package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gIndexOrgTableClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.index_org_table_clause

  def statements = Oracle11gIndexOrgTableClauseTest.statements

}

object Oracle11gIndexOrgTableClauseTest {

  val statements = {
    val table = Set("",
        Oracle11gMappingTableClauseTest.statements.head,
        "PCTTHRESHOLD 1",
        Oracle11gKeyCompressionTest.statements.head)
    val overflow = Set("", Oracle11gIndexOrgOverflowClauseTest.statements.head)

    cartesian(table, overflow)
  }

}