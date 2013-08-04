package com.github.sebhoss.sql.oracle

import com.github.sebhoss.sql.common.StringHelper._

/**
 * Checks whether the supplied Oracle 11g parser understands cluster index clauses.
 *
 * Cluster index clauses are build by the following expression:
 *
 * {{{
 * CLUSTER [ schema. ] cluster index_attributes
 * }}}
 *
 * @see Oracle11gIndexAttributesTest
 * @see [[http://docs.oracle.com/cd/E11882_01/server.112/e26088/clauses002.htm#SQLRF52189 cluster index clause syntax definition]]
 */
class Oracle11gClusterIndexClauseTest extends Oracle11gTest {

  def rule = Oracle11gCreateIndexParser.cluster_index_clause

  def statements = {
    val cluster = Set("CLUSTER")
    val clusterName = Set("cluster", "schema.cluster")
    val index_attributes = Oracle11gIndexAttributesTest.statements

    cartesian(cluster, clusterName, index_attributes)
  }

}