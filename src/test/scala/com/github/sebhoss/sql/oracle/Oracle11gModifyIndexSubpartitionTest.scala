package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gModifyIndexSubpartitionTest extends Oracle11gTest {
  
  def rule = Oracle11gAlterIndexParser.modify_index_subpartition

  def statements = Oracle11gModifyIndexSubpartitionTest.statements

}

object Oracle11gModifyIndexSubpartitionTest {

  val statements = {
    val modify = Set("MODIFY SUBPARTITION subpartition")
    val partition = Set("UNUSABLE") ++ Set(Oracle11gAllocateExtentClauseTest.statements.head,
    	Oracle11gDeallocateUnusedClauseTest.statements.head)

    cartesian(modify, partition)
  }

}