package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gAllocateExtentClauseTest extends Oracle11gTest {

  def rule = Oracle11gAllocateExtentClauseParser.allocate_extent_clause

  def statements = Oracle11gAllocateExtentClauseTest.statements

}

object Oracle11gAllocateExtentClauseTest {

  val statements = {
    val allocate = Set("ALLOCATE EXTENT")
    val attribute = Set("", "DATAFILE 'filename'", "INSTANCE 5") ++
    	Set("SIZE", Oracle11gSizeClauseTest.statements.head)

    cartesian(allocate, attribute)
  }

}