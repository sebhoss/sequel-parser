package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gModifyIndexPartitionTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.modify_index_partition

  def statements = Oracle11gModifyIndexPartitionTest.statements

}

object Oracle11gModifyIndexPartitionTest {

  val statements = {
    val modify = Set("MODIFY PARTITION partition")
    val modifiers = Set(Oracle11gDeallocateUnusedClauseTest.statements.head,
    	Oracle11gAllocateExtentClauseTest.statements.head,
    	Oracle11gPhysicalAttributeClauseTest.statements.head,
    	Oracle11gLoggingClauseTest.statements.head,
    	Oracle11gKeyCompressionTest.statements.head)
    	Set("PARAMETERS ('params')") ++
    	Set("COALESCE") ++
    	Set("UPDATE BLOCK REFERENCES") ++
    	Set("UNUSABLE")

    cartesian(modify, modifiers)
  }

}