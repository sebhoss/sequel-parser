package com.github.sebhoss.sql.oracle

class Oracle11gMappingTableClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.mapping_table_clause

  def statements = Oracle11gMappingTableClauseTest.statements

}

object Oracle11gMappingTableClauseTest {

  val statements = Set(
      "MAPPING TABLE",
      "NOMAPPING"
  )

}