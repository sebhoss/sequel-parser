package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gLOBDeduplicateClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.LOB_deduplicate_clause

  def statements = Oracle11gLOBDeduplicateClauseTest.statements

}

object Oracle11gLOBDeduplicateClauseTest {

  val statements = Set("DEDUPLICATE", "KEEP_DUPLICATES")

}