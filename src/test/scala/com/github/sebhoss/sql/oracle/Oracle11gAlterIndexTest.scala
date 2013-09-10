package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gAlterIndexTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.alter_index

  def statements = Oracle11gAlterIndexTest.statements

}

object Oracle11gAlterIndexTest {

  val statements = {
    val alter = Set("ALTER INDEX")
    val schema = Set("", "schema.")
    val index = Set("index")
    val params = Set(Oracle11gDeallocateUnusedClauseTest.statements.head,
        Oracle11gAllocateExtentClauseTest.statements.head,
    	Oracle11gShrinkClauseTest.statements.head,
    	Oracle11gParallelClauseTest.statements.head,
    	Oracle11gPhysicalAttributeClauseTest.statements.head,
    	Oracle11gLoggingClauseTest.statements.head,
    	Oracle11gRebuildClauseTest.statements.head) ++
    	Set("PARAMETERS ('params')") ++
    	Set("COMPILE") ++
    	Set("ENABLE", "DISABLE") ++
    	Set("UNUSABLE") ++
    	Set("VISIBLE", "INVISIBLE") ++
    	Set("RENAME TO new") ++
    	Set("COALESCE") ++
    	cartesian(Set("MONITORING", "NOMONITORING"), Set("USAGE")) ++
    	Set("UPDATE BLOCK REFERENCES", Oracle11gAlterIndexPartitioningTest.statements.head)
        
    cartesian(alter, schema, index, params)
  }

}