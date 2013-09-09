package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gRebuildClauseTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.rebuild_clause

  def statements = Oracle11gRebuildClauseTest.statements

}

object Oracle11gRebuildClauseTest {

  val statements = {
    val rebuild = List("REBUILD")
    val partition = List("", "PARTITION partition", "SUBPARTITION subpartition", "REVERSE", "NOREVERSE")
    val parameters = List("") ++
    	Oracle11gParallelClauseTest.statements ++
    	List("TABLESPACE tablespace") ++
    	List("PARAMETERS ('params')") ++
    	// FIXME: Include statements for XMLIndex_parameters_clause
    	List("ONLINE") ++
    	Oracle11gPhysicalAttributeClauseTest.statements ++
    	Oracle11gKeyCompressionTest.statements ++
    	Oracle11gLoggingClauseTest.statements
        
    cartesian(rebuild, partition, parameters)
  }

}