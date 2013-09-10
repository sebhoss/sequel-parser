package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gModifyIndexDefaultAttrsTest extends Oracle11gTest {

  def rule = Oracle11gAlterIndexParser.modify_index_default_attrs

  def statements = Oracle11gModifyIndexDefaultAttrsTest.statements

}

object Oracle11gModifyIndexDefaultAttrsTest {

  val statements = {
    val modify = Set("MODIFY DEFAULT ATTRIBUTES")
    val partition = Set("", "FOR PARTITION partition")
    val parameters = Set(Oracle11gPhysicalAttributeClauseTest.statements.head) ++
    	cartesian(Set("TABLESPACE"), Set("tablespace", "DEFAULT")) ++
    	Set(Oracle11gLoggingClauseTest.statements.head)

    cartesian(modify, parameters)
  }

}