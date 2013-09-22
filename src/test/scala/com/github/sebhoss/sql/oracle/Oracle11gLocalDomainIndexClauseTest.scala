package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gLocalDomainIndexClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateIndexParser.local_domain_index_clause

  def statements = Oracle11gLocalDomainIndexClauseTest.statements

}

object Oracle11gLocalDomainIndexClauseTest {

  val statements = {
    val local = Set("LOCAL")
    val partition = Set("") ++ cartesian(Set("(PARTITION partition"), Set("", "PARAMETERS ( 'params' )"), Set(")"))

    cartesian(local, partition)
  }

}