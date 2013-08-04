package com.github.sebhoss.sql.oracle

class Oracle11gLoggingClauseTest extends Oracle11gTest {

  def rule = Oracle11gLoggingClauseParser.logging_clause

  def statements = Oracle11gLoggingClauseTest.statements

}

object Oracle11gLoggingClauseTest {

  val statements = {
    Set("LOGGING", "NOLOGGING", "FILESYSTEM_LIKE_LOGGING")
  }

}