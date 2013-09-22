package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gLOBRetentionClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.LOB_retention_clause

  def statements = Oracle11gLOBRetentionClauseTest.statements

}

object Oracle11gLOBRetentionClauseTest {

  val statements = {
    val retention = Set("RETENTION")
    val retentionOps = Set("MAX", "MIN 5", "AUTO", "NONE")
    
    cartesian(retention, retentionOps)
  }

}