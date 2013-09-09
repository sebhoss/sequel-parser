package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gDropIndexPartitionTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.drop_index_partition

  def statements = Oracle11gDropIndexPartitionTest.statements

}

object Oracle11gDropIndexPartitionTest {

  val statements = {
    List("DROP PARTITION partition")
  }

}