package bjoveski.util

import org.apache.log4j.Logger

/**
 * Created with IntelliJ IDEA.
 * User: bjoveski
 * Date: 6/19/13
 * Time: 7:48 PM
 * To change this template use File | Settings | File Templates.
 */
trait Logging {
  val loggerName = this.getClass.getName
  lazy val logger = Logger.getLogger(loggerName)
}
