package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gPhysicalAttributeClauseTest extends Oracle11gTest {

  def rule = Oracle11gPhysicalAttributesClauseParser.physical_attributes_clause

  def statements = Oracle11gPhysicalAttributeClauseTest.statements

}

object Oracle11gPhysicalAttributeClauseTest {

  val statements = {
    val pctfree = Set("") ++ cartesian(Set("PCTFREE"), Set("1", "10", "100"))
    val pctused = Set("") ++ cartesian(Set("PCTUSED"), Set("1", "10", "100"))
    val initrans = Set("") ++ cartesian(Set("INITRANS"), Set("1", "10", "100"))

    cartesian(pctfree, pctused, initrans, Oracle11gStorageClauseTest.statements)
  }

}