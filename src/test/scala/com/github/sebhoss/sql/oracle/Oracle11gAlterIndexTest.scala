package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gAlterIndexTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.alter_index

  def statements = Oracle11gAlterIndexTest.statements

}

object Oracle11gAlterIndexTest {

  val statements = {
    val alter = List("ALTER INDEX")
    val schema = List("", "schema.")
    val index = List("index")
    val params = Oracle11gDeallocateUnusedClauseTest.statements.toList ++
    	Oracle11gAllocateExtentClauseTest.statements ++
    	Oracle11gShrinkClauseTest.statements ++
    	Oracle11gParallelClauseTest.statements ++
    	Oracle11gPhysicalAttributeClauseTest.statements ++
    	Oracle11gLoggingClauseTest.statements ++
    	Oracle11gRebuildClauseTest.statements ++
    	List("PARAMETERS ('params')") ++
    	List("COMPILE") ++
    	List("ENABLE", "DISABLE") ++
    	List("UNUSABLE") ++
    	List("VISIBLE", "INVISIBLE") ++
    	List("RENAME TO new") ++
    	List("COALESCE") ++
    	cartesian(List("MONITORING", "NOMONITORING"), List("USAGE")) ++
    	List("UPDATE BLOCK REFERENCES") ++
    	Oracle11gAlterIndexPartitioningTest.statements
        
    cartesian(alter, schema, index, params)
  }

}