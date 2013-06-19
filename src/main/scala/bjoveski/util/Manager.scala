package bjoveski.util

import java.io.File

/**
 * Created with IntelliJ IDEA.
 * User: bjoveski
 * Date: 6/19/13
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
object Manager {
  var folders: Iterable[File] = null

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
}
