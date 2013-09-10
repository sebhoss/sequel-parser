package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gRebuildClauseTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.rebuild_clause

  def statements = Oracle11gRebuildClauseTest.statements

}

object Oracle11gRebuildClauseTest {

  val statements = {
    val rebuild = Set("REBUILD")
    val partition = Set("", "PARTITION partition", "SUBPARTITION subpartition", "REVERSE", "NOREVERSE")
    val parameters = Set("") ++
    	Set(Oracle11gParallelClauseTest.statements.head) ++
    	Set("TABLESPACE tablespace") ++
    	Set("PARAMETERS ('params')") ++
    	// FIXME: Include statements for XMLIndex_parameters_clause
    	Set("ONLINE") ++
    	Set(Oracle11gPhysicalAttributeClauseTest.statements.head, 
    	    Oracle11gKeyCompressionTest.statements.head,
    	    Oracle11gLoggingClauseTest.statements.head)
        
    cartesian(rebuild, partition, parameters)
  }

}