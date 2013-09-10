package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/** Checks whether the supplied Oracle 11g parser understands segment attributes clauses.
  *
  * Segment attributes clauses are build by the following expression:
  *
  * {{{
  * { physical_attributes_clause
  * | TABLESPACE tablespace
  * | logging_clause
  * }...
  * }}}
  *
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2127592 segment attributes clause syntax definition]]
  */
class Oracle11gSegmentAttributesClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateIndexParser.segment_attributes_clause

  def statements = Oracle11gSegmentAttributesClauseTest.statements

}

object Oracle11gSegmentAttributesClauseTest {

  val statements = {
    val physicalAttributes = Set(Oracle11gPhysicalAttributeClauseTest.statements.head)
    val tablespace = Set("TABLESPACE tablespace")
    val logging = Set(Oracle11gLoggingClauseTest.statements.head)

    cartesian(physicalAttributes, tablespace, logging)
  }

}