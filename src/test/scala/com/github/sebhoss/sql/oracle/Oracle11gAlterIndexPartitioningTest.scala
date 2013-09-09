package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gAlterIndexPartitioningTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.alter_index_partitioning

  def statements = Oracle11gAlterIndexPartitioningTest.statements

}

object Oracle11gAlterIndexPartitioningTest {

  val statements = {
    Oracle11gModifyIndexDefaultAttrsTest.statements ++
    Oracle11gAddHashIndexPartitionTest.statements ++
    Oracle11gModifyIndexPartitionTest.statements ++
    Oracle11gRenameIndexPartitionTest.statements ++
    Oracle11gDropIndexPartitionTest.statements ++
    Oracle11gSplitIndexPartitionTest.statements ++
    Oracle11gCoalesceIndexPartitionTest.statements ++
    Oracle11gModifyIndexSubpartitionTest.statements
  }

}