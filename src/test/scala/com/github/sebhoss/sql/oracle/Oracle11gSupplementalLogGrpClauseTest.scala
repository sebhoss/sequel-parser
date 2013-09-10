package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gSupplementalLogGrpClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.supplemental_log_grp_clause

  def statements = Oracle11gSupplementalLogGrpClauseTest.statements

}

object Oracle11gSupplementalLogGrpClauseTest {

  val statements = {
    val group = Set("GROUP group")
    val column = cartesian(Set("(column"), Set("NO LOG"), Set(")"))
    val always = Set("", "ALWAYS")
    
    cartesian(group, column, always)
  }

}