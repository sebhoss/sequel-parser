package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gRenameIndexPartitionTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.rename_index_partition

  def statements = Oracle11gRenameIndexPartitionTest.statements

}

object Oracle11gRenameIndexPartitionTest {

  val statements = {
    val rename = List("RENAME")
    val partitionStart = List("PARTITION", "SUBPARTITION")
    val partition = List("partition")
    val to = List("TO new")

    cartesian(rename, partitionStart, partition, to)
  }

}