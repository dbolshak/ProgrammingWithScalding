package slimjar

import com.twitter.scalding._
import org.slf4j.LoggerFactory
import org.apache.hadoop.conf.Configuration

class JobBase(args: Args) extends Job(args) {

  val log = LoggerFactory.getLogger(this.getClass.getName)

  def getOptionalString(key: String): Option[String] = args.m.get(key).map( _.head )

  // Execute at instantiation
  mode match {
    case hadoop: HadoopMode => {
      log.info("-Hadoop Mode-")
      getOptionalString("dependencies") foreach {
        hadoopPath => log.info("dep" + hadoopPath)
                      JobLibLoader.loadJars(hadoopPath, AppConfig.jobConfig)
        AppConfig.jobConfig.addResource(hadoopPath)
      }

    }
    case _ => { log.info("In Local Mode") }
  }

}

object AppConfig {
  implicit var jobConfig : Configuration = new Configuration()
}
