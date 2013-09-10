package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gDeferredSegmentCreationTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.deferred_segment_creation

  def statements = Oracle11gDeferredSegmentCreationTest.statements

}

object Oracle11gDeferredSegmentCreationTest {

  val statements = {
    val segment = Set("SEGMENT CREATION")
    val opts = Set("IMMEDIATE", "DEFERRED")

    cartesian(segment, opts)
  }

}