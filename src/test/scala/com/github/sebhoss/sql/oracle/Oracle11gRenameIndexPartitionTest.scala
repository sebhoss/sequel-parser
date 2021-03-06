package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gRenameIndexPartitionTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.rename_index_partition

  def statements = Oracle11gRenameIndexPartitionTest.statements

}

object Oracle11gRenameIndexPartitionTest {

  val statements = {
    val rename = Set("RENAME")
    val partitionStart = Set("PARTITION", "SUBPARTITION")
    val partition = Set("partition")
    val to = Set("TO new")

    cartesian(rename, partitionStart, partition, to)
  }

}