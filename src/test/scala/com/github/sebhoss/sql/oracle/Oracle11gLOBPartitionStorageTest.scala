package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gLOBPartitionStorageTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.LOB_partition_storage

  def statements = Oracle11gLOBPartitionStorageTest.statements

}

object Oracle11gLOBPartitionStorageTest {

  val statements = {
    val partition = Set("PARTITION partition")
    val partitionOps = Oracle11gLOBStorageClauseTest.statements.take(1) ++ Oracle11gVarrayColPropertiesTest.statements.take(1)
    val subpartition = Set("") ++ cartesian(Set("(SUBPARTITION partition"),
        Oracle11gLOBStorageClauseTest.statements.take(1) ++ Oracle11gVarrayColPropertiesTest.statements.take(1), Set(")")) 

    cartesian(partition, partitionOps, subpartition)
  }

}