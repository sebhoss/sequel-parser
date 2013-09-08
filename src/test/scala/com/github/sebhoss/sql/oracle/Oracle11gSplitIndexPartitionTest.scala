package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gSplitIndexPartitionTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.split_index_partition
  
  def statements = Oracle11gSplitIndexPartitionTest.statements
  
}

object Oracle11gSplitIndexPartitionTest {

  val statements = {
    val partition = List("SPLIT PARTITION partition AT (literal)")
    val into = List("INTO (") ++ Oracle11gIndexPartitionDescriptionTest.statements ++ List(")")
    
    cartesian(partition, into, Oracle11gParallelClauseTest.statements.toList)
  }
  
}