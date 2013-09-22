package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gXMLTypeStorageTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.XMLType_storage

  def statements = Oracle11gXMLTypeStorageTest.statements

}

object Oracle11gXMLTypeStorageTest {

  val statements = {
    val store = Set("STORE")
    val as = cartesian(Set("AS"), Set("OBJECT RELATIONAL") ++ Set("SECUREFILE", "BASICFILE"), Set("CLOB", "BINARY XML")) ++
    	cartesian(Set("ALL VARRAYS AS"), Set("LOBS", "TABLES"))

    cartesian(store, as)
  }

}