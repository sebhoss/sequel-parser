package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gIndexPartitionDescriptionTest extends Oracle11gTest {
  
  def rule = Oracle11gAlterIndexParser.index_partition_description

  def statements = Oracle11gIndexPartitionDescriptionTest.statements

}

object Oracle11gIndexPartitionDescriptionTest {

  val statements = {
    val partition = List("PARTITION")
    val odci = List("( PARAMETERS 'odci')")
    val unusable = List("", "UNUSABLE")
    val options = cartesian(List("partition"), Oracle11gSegmentAttributesClauseTest.statements.toList ++
        Oracle11gKeyCompressionTest.statements ++ odci, unusable).toList

    cartesian(partition, options)
  }

}