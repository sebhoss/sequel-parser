package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gAddHashIndexPartitionTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.add_hash_index_partition

  def statements = Oracle11gAddHashIndexPartitionTest.statements

}

object Oracle11gAddHashIndexPartitionTest {

  val statements = {
    val addPartition = List("ADD PARTITION")
    val partition = List("", "partition")
    val tablespace = List("", "TABLESPACE tablespace")
    val key = List("") ++ Oracle11gKeyCompressionTest.statements
    val parallel = List("") ++ Oracle11gParallelClauseTest.statements

    cartesian(addPartition, partition, tablespace, key, parallel)
  }

}