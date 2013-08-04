package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gAsyncClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateIndexParser.async_clause

  def statements = Oracle11gAsyncClauseTest.statements

}

object Oracle11gAsyncClauseTest {
  
  val statements = {
    val async = Set("ASYNC (SYNC")
    val sync = Set("ALWAYS", "MANUAL", "EVERY 5", "ON COMMIT")
    val stale = Set("", "STALE (FALSE)", "STALE (TRUE)")
    
    cartesian(async, sync, stale, Set(")"))
  }
  
}