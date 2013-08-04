package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gSizeClauseTest extends Oracle11gTest {
  
  def rule = Oracle11gSizeClauseParser.size_clause

  def statements = Oracle11gSizeClauseTest.statements

}

object Oracle11gSizeClauseTest {
  
  val statements = {
    val integers = Set("0", "1", "10", "100", "1000", "12345")
    val units = Set("K", "M", "G", "T", "P", "E")
    
    cartesian(integers, units)
  }
  
}