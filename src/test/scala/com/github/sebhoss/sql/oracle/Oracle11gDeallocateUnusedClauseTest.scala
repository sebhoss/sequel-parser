package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gDeallocateUnusedClauseTest extends Oracle11gTest {

  def rule = Oracle11gDeallocateUnusedClauseParser.deallocate_unused_clause

  def statements = Oracle11gDeallocateUnusedClauseTest.statements

}

object Oracle11gDeallocateUnusedClauseTest {

  val statements = {
    val deallocate = List("DEALLOCATE UNUSED")
    val keep = List("") ++ cartesian(List("KEEP"), Oracle11gSizeClauseTest.statements.toList).toList

    cartesian(deallocate, keep)
  }

}