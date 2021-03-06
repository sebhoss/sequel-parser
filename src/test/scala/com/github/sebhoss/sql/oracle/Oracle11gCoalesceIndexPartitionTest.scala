package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gCoalesceIndexPartitionTest extends Oracle11gTest {
  
  def rule = Oracle11gAlterIndexParser.coalesce_index_partition

  def statements = Oracle11gCoalesceIndexPartitionTest.statements

}

object Oracle11gCoalesceIndexPartitionTest {

  val statements = {
    val coalesce = Set("COALESCE PARTITION")
    val parallel = Set("", Oracle11gParallelClauseTest.statements.head)

    cartesian(coalesce, parallel)
  }

}