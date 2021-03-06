package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gIndexPartitionDescriptionTest extends Oracle11gTest {
  
  def rule = Oracle11gAlterIndexParser.index_partition_description

  def statements = Oracle11gIndexPartitionDescriptionTest.statements

}

object Oracle11gIndexPartitionDescriptionTest {

  val statements = {
    val partition = Set("PARTITION")
    val odci = Set("( PARAMETERS 'odci')")
    val unusable = Set("", "UNUSABLE")
    val options = cartesian(Set("partition"), Set(Oracle11gSegmentAttributesClauseTest.statements.head,
        Oracle11gKeyCompressionTest.statements.head) ++ odci, unusable)

    cartesian(partition, options)
  }

}