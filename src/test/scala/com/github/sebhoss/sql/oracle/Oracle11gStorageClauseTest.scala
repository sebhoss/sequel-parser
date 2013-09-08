package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gStorageClauseTest extends Oracle11gTest {

  def rule = Oracle11gStorageClauseParser.storage_clause

  def statements = Oracle11gStorageClauseTest.statements

}

object Oracle11gStorageClauseTest {

  val statements = {
    val storage = List("STORAGE (")
    val attribute = cartesian(List("INITIAL"), Oracle11gSizeClauseTest.statements.toList).toList ++
    	cartesian(List("NEXT"), Oracle11gSizeClauseTest.statements.toList) ++
    	cartesian(List("MINEXTENTS"), List("1", "10", "100", "1000")) ++
    	cartesian(List("MAXEXTENTS"), List("1", "10", "100", "1000", "UNLIMITED")) ++
    	Oracle11gMaxsizeClauseTest.statements ++
    	cartesian(List("PCTINCREASE"), List("1", "10", "100", "1000")) ++
    	cartesian(List("FREELISTS"), List("1", "10", "100", "1000")) ++
    	cartesian(List("FREELIST GROUPS"), List("1", "10", "100", "1000")) ++
    	cartesian(List("OPTIMAL"), Oracle11gSizeClauseTest.statements.toList ++ List("NULL")) ++
    	cartesian(List("BUFFER_POOL"), List("KEEP", "RECYCLE", "DEFAULT")) ++
    	cartesian(List("FLASH_CACHE"), List("KEEP", "NONE", "DEFAULT")) ++
    	List("ENCRYPT")

    cartesian(storage, attribute, List(")"))
  }

}