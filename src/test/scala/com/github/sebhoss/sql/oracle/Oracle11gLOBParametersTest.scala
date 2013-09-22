package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gLOBParametersTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.LOB_parameters

  def statements = Oracle11gLOBParametersTest.statements

}

object Oracle11gLOBParametersTest {

  val statements = {
    val storage = cartesian(Set("ENABLE", "DISABLE"), Set("STORAGE IN ROW"))
    val chunk = Set("CHUNK 5")
    val pctVersion = Set("PCTVERSION 5")
    val freePools = Set("FREEPOOLS 5")
    val retention = Oracle11gLOBRetentionClauseTest.statements.take(1)
    val deduplicate = Oracle11gLOBDeduplicateClauseTest.statements.take(1)
    val compression = Oracle11gLOBCompressionClauseTest.statements.take(1)
    val encrypt = Set("ENCRYPT " ++ Oracle11gEncryptionSpecTest.statements.head, "DECRYPT")
    val cache = cartesian(Set("CACHE", "NOCACHE", "CACHE READS"), Set("") ++ Oracle11gLoggingClauseTest.statements.take(1))

    storage ++ chunk ++ pctVersion ++ freePools ++ retention ++ deduplicate ++ compression ++ encrypt ++ cache
  }

}