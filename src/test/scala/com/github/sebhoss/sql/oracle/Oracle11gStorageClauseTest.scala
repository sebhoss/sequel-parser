package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gStorageClauseTest extends Oracle11gTest {

  def rule = Oracle11gStorageClauseParser.storage_clause

  def statements = Oracle11gStorageClauseTest.statements

}

object Oracle11gStorageClauseTest {

  val statements = {
    val storage = Set("STORAGE (")
    val attribute = cartesian(Set("INITIAL"), Oracle11gSizeClauseTest.statements) ++
    	cartesian(Set("NEXT"), Oracle11gSizeClauseTest.statements) ++
    	cartesian(Set("MINEXTENTS"), Set("1", "10", "100", "1000")) ++
    	cartesian(Set("MAXEXTENTS"), Set("1", "10", "100", "1000", "UNLIMITED")) ++
    	Oracle11gMaxsizeClauseTest.statements ++
    	cartesian(Set("PCTINCREASE"), Set("1", "10", "100", "1000")) ++
    	cartesian(Set("FREELISTS"), Set("1", "10", "100", "1000")) ++
    	cartesian(Set("FREELIST GROUPS"), Set("1", "10", "100", "1000")) ++
    	cartesian(Set("OPTIMAL"), Oracle11gSizeClauseTest.statements ++ Set("NULL")) ++
    	cartesian(Set("BUFFER_POOL"), Set("KEEP", "RECYCLE", "DEFAULT")) ++
    	cartesian(Set("FLASH_CACHE"), Set("KEEP", "NONE", "DEFAULT")) ++
    	Set("ENCRYPT")
    
    cartesian(storage, attribute, Set(")"))
  }

}