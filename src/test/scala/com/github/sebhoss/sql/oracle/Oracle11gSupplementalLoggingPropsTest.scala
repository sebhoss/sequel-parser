package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gSupplementalLoggingPropsTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.supplemental_logging_props

  def statements = Oracle11gSupplementalLoggingPropsTest.statements

}

object Oracle11gSupplementalLoggingPropsTest {

  val statements = {
    val log = Set("SUPPLEMENTAL LOG")
    
    cartesian(log, Set(Oracle11gSupplementalLogGrpClauseTest.statements.head) ++
        Set(Oracle11gSupplementalIdKeyClauseTest.statements.head))
  }

}