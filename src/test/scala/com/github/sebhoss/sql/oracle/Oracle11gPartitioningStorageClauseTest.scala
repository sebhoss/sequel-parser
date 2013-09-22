package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gPartitioningStorageClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateIndexParser.partitioning_storage_clause

  def statements = Oracle11gPartitioningStorageClauseTest.statements

}

object Oracle11gPartitioningStorageClauseTest {

  val statements = Set("TABLESPACE tablespace") ++
		  cartesian(Set("OVERFLOW"), Set("", "TABLESPACE tablespace")) ++
		  Oracle11gTableCompressionTest.statements.take(1) ++
		  Oracle11gKeyCompressionTest.statements.take(1) ++
		  Oracle11gLOBPartitionStorageTest.statements.take(1) ++
		  cartesian(Set("VARRAY item STORE AS"), Set("SECUREFILE", "BASICFILE"), Set("LOB segname"))

}