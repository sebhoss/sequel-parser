package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gSupplementalIdKeyClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.supplemental_id_key_clause

  def statements = Oracle11gSupplementalIdKeyClauseTest.statements

}

object Oracle11gSupplementalIdKeyClauseTest {

  val statements = {
    val data = List("DATA (")
    val opts = List("ALL", "PRIMARY KEY", "UNIQUE", "FOREIGN KEY")
    val columns = List(") COLUMNS")
    
    cartesian(data, opts, columns)
  }

}