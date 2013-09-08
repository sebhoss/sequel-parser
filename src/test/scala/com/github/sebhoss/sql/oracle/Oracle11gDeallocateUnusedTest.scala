package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gDeallocateUnusedTest extends Oracle11gTest {

  def rule = Oracle11gDeallocateUnusedClauseParser.deallocate_unused_clause

  def statements = Oracle11gDeallocateUnusedTest.statements

}

object Oracle11gDeallocateUnusedTest {

  val statements = {
    List("DEALLOCATE UNUSED") ++ cartesian(List("DEALLOCATE UNUSED KEEP"), Oracle11gSizeClauseTest.statements.toList).toList
  }

}