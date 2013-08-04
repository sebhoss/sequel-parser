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
    val physicalAttributesClause = powerset("PCTFREE 10", "PCTUSED 10", "INITRANS 10")
    val loggingClause = Set("", "LOGGING", "NOLOGGING", "FILESYSTEM_LIKE_LOGGING")
    val online = Set("ONLINE")
    val tablespace = Set("TABLESPACE tablespace", "TABLESPACE DEFAULT")
    val keyCompress = Set("COMPRESS", "COMPRESS 10", "NOCOMPRESS")
    val sort = Set("SORT", "NOSORT")
    val reverse = Set("REVERSE")
    val visible = Set("VISIBLE")
    val insivible = Set("INVISIBLE")
    val parallel = Set("NOPARALLEL", "PARALLEL", "PARALLEL 10")
    
    cartesian(physicalAttributesClause, loggingClause, online, tablespace, keyCompress, sort, reverse, visible,
        insivible, parallel)
  }
  
}