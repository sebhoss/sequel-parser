package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gXMLSchemaSpecTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.XMLSchema_spec

  def statements = Oracle11gXMLSchemaSpecTest.statements

}

object Oracle11gXMLSchemaSpecTest {

  val statements = {
    val schema = Set("", "XMLSCHEMA url")
    val element = cartesian(Set("ELEMENT"), Set("element", "url # element"))
    val opts = Set("", "ALLOW ANYSCHEMA", "ALLOW NONSCHEMA", "DISALLOW NONSCHEMA")

    cartesian(schema, element, opts)
  }

}