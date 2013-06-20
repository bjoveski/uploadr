package bjoveski

import java.io.File
import bjoveski.model.Catalog
import bjoveski.util.{Logging, Manager}

/**
 * Created with IntelliJ IDEA.
 * User: bjoveski
 * Date: 6/19/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
object Uploadr extends Logging {

  def createSetFromFolder(folder: File) = {
    val startTime = System.currentTimeMillis()
    val photos = Manager.getPhotosInFolder(folder)
    println(s" about to upload photos. [count=${photos.size}] [folderName=${folder.getName}]")
    logger.info(s" about to upload photos. [count=${photos.size}] [folderName=${folder.getName}]")

    val flickPhotos = photos.map(photoFile => {
        Catalog.uploadPhoto(photoFile)
    })

    val uploaded = flickPhotos.filter(opt => opt.isDefined).map(opt => opt.get)

    println(s"finished uploading. [count=${uploaded.size}] [time=${(System.currentTimeMillis() - startTime)/1000} sec]")
    logger.info(s"finished uploading. [count=${uploaded.size}] [time=${(System.currentTimeMillis() - startTime)/1000} sec]")
    Catalog.createSet(folder.getName, "", uploaded)
  }

}
