package bjoveski

import java.io.File
import bjoveski.model.Catalog
import bjoveski.util.Manager

/**
 * Created with IntelliJ IDEA.
 * User: bjoveski
 * Date: 6/19/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
object Uploadr {
  def createSetFromFolder(folderPath: String) = {
    val folder = new File(folderPath)
    val photos = Manager.getPhotosInFolder(folder)
    println(s"${photos.size} photos about to be uploaded from ${folder.getName}")
    //    logger.info(s"${photos.size} photos about to be uploaded from ${folder.getName}")
    //
    //    for ( i <- 0 until photos.size) {
    //      Catalog.uploadPhoto(photos(i))
    //
    //      if (i % 10 == 0) {
    //        println(s"uploaded $i photos")
    //      }
    //    }

    val flickPhotos = photos.map(photoFile => {
      Catalog.uploadPhoto(photoFile)

    })

    println(s"finished uploading photos ${flickPhotos.size}")
    //    logger.info(s"finished uploading photos ${flickPhotos.size}")
    Catalog.createSet(folder.getName, "test", flickPhotos)
  }
}
