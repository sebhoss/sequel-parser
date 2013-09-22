package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gLOBStorageClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.LOB_storage_clause

  def statements = Oracle11gLOBStorageClauseTest.statements

}

object Oracle11gLOBStorageClauseTest {

  val statements = {
    val lob = Set("LOB")
    val lobItem = Set("( item ) STORE AS")
    val lobParams = Set("SECUREFILE", "BASICFILE", "(" ++ Oracle11gLOBStorageParameters.statements.head ++ ")") ++
    	Set("segname (" ++ Oracle11gLOBStorageParameters.statements.head ++ ")")

    cartesian(lob, lobItem, lobParams)
  }

}