package bjoveski.util

import java.io.{PrintWriter, File}
import com.typesafe.config.ConfigFactory
import scala.xml.XML
import bjoveski.model.Folder
import scala.collection.mutable
import scala.collection.immutable.HashSet

/**
 * Created with IntelliJ IDEA.
 * User: bjoveski
 * Date: 6/19/13
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
object Manager {
  var folders: mutable.HashSet[Folder] = new mutable.HashSet()
  val conf = ConfigFactory.load()


  def apply(pathToRoot: String) {
    val root = new File(pathToRoot)

    val dirs = root.listFiles().filter(dir => dir.isDirectory)
    recurse(dirs, Set())

  }

  def recurse(in: Traversable[File], sets: Set[String]) {
    in.toList.foreach(f => {
      val folder = Folder(f, sets)
      folders.add(folder)
      recurse(folder.getChildrenFolders, sets.union(Set(folder.name)))
    })
}

  def getPhotosInFolder(folder: File) = {
    if (!folder.isDirectory) {
      throw new RuntimeException()
    }

    folder.listFiles().filter(file => file.getName.toLowerCase.endsWith("jpg"))
  }

  def generateXml() = {
    val folders = Manager.folders.map(f => {
      f.toXml
    })

    val writer = new PrintWriter(conf.getString("uploadr-app.outputXmlFile"))
    writer.write("<folders>\n")
    folders.foreach(folder => {
      writer.write(folder.toString())
      writer.write("\n")
    })
    writer.write("</folders>\n")
    writer.close()
  }

  def fromXml() = {
    val folders = XML.loadFile(conf.getString("uploadr-app.outputXmlFile"))\"folder"
    folders.map(folder => {
      val f = new File((folder\"path").text)
      val sets = (folder\"sets"\"setName").map(set => set.text.trim).toSet
      Folder(f, sets)
    })
   }

}
