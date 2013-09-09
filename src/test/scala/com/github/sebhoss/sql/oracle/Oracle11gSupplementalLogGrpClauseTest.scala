package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gSupplementalLogGrpClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.supplemental_log_grp_clause

  def statements = Oracle11gSupplementalLogGrpClauseTest.statements

}

object Oracle11gSupplementalLogGrpClauseTest {

  val statements = {
    val group = List("GROUP group")
    val column = cartesian(List("(column"), List("NO LOG"), List(")")).toList
    val always = List("", "ALWAYS")
    
    cartesian(group, column, always)
  }

}