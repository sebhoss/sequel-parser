package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gMaxsizeClauseTest extends Oracle11gTest {

  def rule = Oracle11gStorageClauseParser.maxsize_clause

  def statements = Oracle11gMaxsizeClauseTest.statements

}

object Oracle11gMaxsizeClauseTest {

  val statements = {
    cartesian(List("MAXSIZE"), List("UNLIMITED") ++ Oracle11gSizeClauseTest.statements.toList)
  }

}