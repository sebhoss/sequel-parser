package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gModifyIndexDefaultAttrsTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.modify_index_default_attrs

  def statements = Oracle11gModifyIndexDefaultAttrsTest.statements

}

object Oracle11gModifyIndexDefaultAttrsTest {

  val statements = {
    val modify = List("MODIFY DEFAULT ATTRIBUTES")
    val partition = List("", "FOR PARTITION partition")
    val parameters = Oracle11gPhysicalAttributeClauseTest.statements.toList ++
    	cartesian(List("TABLESPACE"), List("tablespace", "DEFAULT")) ++
    	Oracle11gLoggingClauseTest.statements

    cartesian(modify, parameters)
  }

}