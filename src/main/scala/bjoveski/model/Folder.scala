package bjoveski.model

import java.io.File
import scala.collection.immutable.HashSet

/**
 * Created with IntelliJ IDEA.
 * User: bjoveski
 * Date: 6/20/13
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */
class Folder(val name: String,
             val path: String,
             val sets: Set[String],
             val folder: File) {

  def toXml = {
    val setsXml = sets.toList.map(set => {
      <setName>
        {set}
      </setName>
    })


    <folder>
      <name>{name}</name>
      <path>{path}</path>
      <sets>
        {setsXml}
      </sets>
      <photoCount>{getPhotosInFolder.size}</photoCount>
    </folder>
  }



  def getPhotosInFolder = {
    folder.listFiles().filter(file => file.getName.toLowerCase.endsWith("jpg"))
  }

  def getChildrenFolders = {
    folder.listFiles().filter(folder => folder.isDirectory)
  }
}

object Folder {

  def apply(folder: File, sets: Set[String]):Folder = {
    if (!folder.exists()) {
      throw new RuntimeException
    }
    if (!folder.isDirectory) {
      throw new RuntimeException
    }

    val newSets = sets.union(Set(folder.getName))
    new Folder(folder.getName, folder.getAbsolutePath, newSets, folder)
  }

  def apply(folder: File):Folder = {
    val sets = new HashSet[String]()
    Folder.apply(folder, sets)
  }
//
//  def apply(xml: xml.Elem) = {
//    val folder = new File((xml\"path").text)
//
////    val sets = new HashSet[String]()
////    (xml\"sets"\"set").foreach(set => sets.add(set.text))
//    val sets = (xml\"sets"\"set").map(set => set.text).toSet
//
//    Folder.apply(folder, sets)
//  }
}

