package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gAlterIndexPartitioningTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.alter_index_partitioning

  def statements = Oracle11gAlterIndexPartitioningTest.statements

}

object Oracle11gAlterIndexPartitioningTest {

  val statements = Set(
      Oracle11gModifyIndexDefaultAttrsTest.statements.head,
      Oracle11gAddHashIndexPartitionTest.statements.head,
      Oracle11gModifyIndexPartitionTest.statements.head,
      Oracle11gRenameIndexPartitionTest.statements.head,
      Oracle11gDropIndexPartitionTest.statements.head,
      Oracle11gSplitIndexPartitionTest.statements.head,
      Oracle11gCoalesceIndexPartitionTest.statements.head,
      Oracle11gModifyIndexSubpartitionTest.statements.head
  )

}