package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gIndividualHashPartitionsTest extends Oracle11gTest {

  def rule = Oracle11gCreateIndexParser.individual_hash_partitions

  def statements = Oracle11gIndividualHashPartitionsTest.statements

}

object Oracle11gIndividualHashPartitionsTest {

  val statements = {
    val partition = Set("( PARTITION")
    val part = Set("", "partition")
    val storage = Set("", "OVERFLOW TABLESPACE tablespace")

    cartesian(partition, part, storage, Set(")"))
  }
  
}