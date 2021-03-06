package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gShrinkClauseTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.shrink_clause

  def statements = Oracle11gShrinkClauseTest.statements

}

object Oracle11gShrinkClauseTest {

  val statements = {
    val shrink = Set("SHRINK SPACE")
    val opts = Set("COMPACT", "CASCADE")
        
    cartesian(shrink, opts)
  }

}