package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gSupplementalLoggingPropsTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.supplemental_logging_props

  def statements = Oracle11gSupplementalLoggingPropsTest.statements

}

object Oracle11gSupplementalLoggingPropsTest {

  val statements = {
    val log = List("SUPPLEMENTAL LOG")
    
    cartesian(log, Oracle11gSupplementalLogGrpClauseTest.statements.toList ++ Oracle11gSupplementalIdKeyClauseTest.statements)
  }

}