package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gColumnClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateIndexParser.column_clause

  def statements = Oracle11gColumnClauseTest.statements

}

object Oracle11gColumnClauseTest {
  
  val statements = {
    val column = Set("column")
    val attribute = Set("FOR ORDINALITY") // ++ 
//    	cartesian(Oracle11gDataTypeTest.statements, Set("PATH \"path\"", "PATH \"path\" VIRTUAL"))
    
    cartesian(column, attribute)
  }
  
}