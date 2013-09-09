package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gModifyIndexPartitionTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.modify_index_partition

  def statements = Oracle11gModifyIndexPartitionTest.statements

}

object Oracle11gModifyIndexPartitionTest {

  val statements = {
    val modify = List("MODIFY PARTITION partition")
    val modifiers = Oracle11gDeallocateUnusedClauseTest.statements.toList ++
    	Oracle11gAllocateExtentClauseTest.statements ++
    	Oracle11gPhysicalAttributeClauseTest.statements ++
    	Oracle11gLoggingClauseTest.statements ++
    	Oracle11gKeyCompressionTest.statements ++
    	List("PARAMETERS ('params')") ++
    	List("COALESCE") ++
    	List("UPDATE BLOCK REFERENCES") ++
    	List("UNUSABLE")

    cartesian(modify, modifiers)
  }

}