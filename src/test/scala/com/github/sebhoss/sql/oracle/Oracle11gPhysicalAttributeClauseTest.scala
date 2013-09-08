package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gPhysicalAttributeClauseTest extends Oracle11gTest {

  def rule = Oracle11gPhysicalAttributesClauseParser.physical_attributes_clause

  def statements = Oracle11gPhysicalAttributeClauseTest.statements

}

object Oracle11gPhysicalAttributeClauseTest {

  val statements = {
    val pctfree = List("") ++ cartesian(List("PCTFREE"), List("1", "10", "100"))
    val pctused = List("") ++ cartesian(List("PCTUSED"), List("1", "10", "100"))
    val initrans = List("") ++ cartesian(List("INITRANS"), List("1", "10", "100"))

    cartesian(pctfree, pctused, initrans, Oracle11gStorageClauseTest.statements.toList)
  }

}