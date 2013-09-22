package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gVarrayStorageClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.varray_storage_clause

  def statements = Oracle11gVarrayStorageClauseTest.statements

}

object Oracle11gVarrayStorageClauseTest {

  val statements = {
    val store = Set("STORE AS")
    val where = Set("SECUREFILE", "BASICFILE")
    val lob = Set("LOB")
    val params = cartesian(Set("", "segname"), Set("("), Oracle11gLOBStorageParameters.statements.take(1), Set(")"))
    val segname = Set("segname")

    cartesian(cartesian(store, where, lob), params, segname)
  }

}