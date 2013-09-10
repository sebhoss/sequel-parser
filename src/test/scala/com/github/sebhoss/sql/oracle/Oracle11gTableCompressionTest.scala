package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

class Oracle11gTableCompressionTest extends Oracle11gTest {

  def rule = Oracle11gCreateTableParser.table_compression

  def statements = Oracle11gTableCompressionTest.statements

}

object Oracle11gTableCompressionTest {

  val statements = {
    val compress = Set("COMPRESS")
    val compressOpts = Set("", "BASIC") ++ cartesian(Set("FOR"), Set("OLTP", "QUERY", "ARCHIVE"), Set("", "LOW", "HIGH"))
    val nocompress = Set("NOCOMPRESS")

    cartesian(compress, compressOpts) ++ nocompress
  }

}