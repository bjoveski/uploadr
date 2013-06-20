package bjoveski.util

import java.io.{PrintWriter, File}
import com.typesafe.config.ConfigFactory
import scala.xml.XML

/**
 * Created with IntelliJ IDEA.
 * User: bjoveski
 * Date: 6/19/13
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
object Manager {
  var folders: Iterable[File] = null
  val conf = ConfigFactory.load()


  def apply(pathToRoot: String) {
    val root = new File(pathToRoot)
    folders = root.listFiles().filter(dir => dir.isDirectory)

  }

  def getPhotosInFolder(folder: File) = {
    if (!folder.isDirectory) {
      throw new RuntimeException()
    }

    folder.listFiles().filter(file => file.getName.toLowerCase.endsWith("jpg"))
  }

  def generateXml() = {
    val folders = Manager.folders.map(f => <folder><name>{f.getName}</name><path>{f.getAbsolutePath}</path><count>{Manager.getPhotosInFolder(f).size}</count></folder>)

    val writer = new PrintWriter(conf.getString("uploadr-app.outputXmlFile"))
    writer.write("<folders>")
    folders.foreach(folder => writer.write(folder.toString()))
    writer.write("</folders>")
    writer.close()
  }

  def fromXml() = {
    val folders = XML.loadFile(conf.getString("uploadr-app.outputXmlFile"))
    folders.child.map(folder => {
      new File((folder\"path").text)
    })
   }

}
