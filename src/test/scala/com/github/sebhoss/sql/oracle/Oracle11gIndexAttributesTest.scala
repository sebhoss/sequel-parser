package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/** Checks whether the supplied Oracle 11g parser understands index attributes.
  *
  * Index attributes clauses are build by the following expression:
  *
  * {{{
  * [ { physical_attributes_clause
  * | logging_clause
  * | ONLINE
  * | TABLESPACE { tablespace | DEFAULT }
  * | key_compression
  * | { SORT | NOSORT }
  * | REVERSE
  * | VISIBLE | INVISIBLE
  * | parallel_clause
  * }...
  * ]
  * }}}
  *
  * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/statements_5012.htm#i2182500 index attributes syntax definition]]
  */
class Oracle11gIndexAttributesTest extends Oracle11gTest {

  def rule = Oracle11gCreateIndexParser.index_attributes
  
  def statements = Oracle11gIndexAttributesTest.statements

}

object Oracle11gIndexAttributesTest {

  val statements = {
    val physicalAttributesClause = powerset("PCTFREE 10", "PCTUSED 10", "INITRANS 10").toList
    val loggingClause = List("", "LOGGING", "NOLOGGING", "FILESYSTEM_LIKE_LOGGING")
    val online = List("ONLINE")
    val tablespace = List("TABLESPACE tablespace", "TABLESPACE DEFAULT")
    val keyCompress = List("COMPRESS", "COMPRESS 10", "NOCOMPRESS")
    val sort = List("SORT", "NOSORT")
    val reverse = List("REVERSE")
    val visible = List("VISIBLE")
    val insivible = List("INVISIBLE")
    val parallel = List("NOPARALLEL", "PARALLEL", "PARALLEL 10")

    cartesian(physicalAttributesClause, loggingClause, online, tablespace, keyCompress, sort, reverse, visible,
        insivible, parallel)
  }

}