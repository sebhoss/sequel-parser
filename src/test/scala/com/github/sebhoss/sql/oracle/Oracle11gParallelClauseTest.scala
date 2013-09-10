package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gParallelClauseTest extends Oracle11gTest {

  def rule = Oracle11gParallelClauseParser.parallel_clause

  def statements = Oracle11gParallelClauseTest.statements

}

object Oracle11gParallelClauseTest {

  val statements = {
    cartesian(Set("NOPARALLEL", "PARALLEL"), Set("", "1", "10", "100"))
  }

}