package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gAddHashIndexPartitionTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.add_hash_index_partition

  def statements = Oracle11gAddHashIndexPartitionTest.statements

}

object Oracle11gAddHashIndexPartitionTest {

  val statements = {
    val addPartition = Set("ADD PARTITION")
    val partition = Set("", "partition")
    val tablespace = Set("", "TABLESPACE tablespace")
    val key = Set("", Oracle11gKeyCompressionTest.statements.head) 
    val parallel = Set("", Oracle11gParallelClauseTest.statements.head)

    cartesian(addPartition, partition, tablespace, key, parallel)
  }

}