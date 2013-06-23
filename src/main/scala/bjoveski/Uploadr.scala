package bjoveski

import java.io.File
import bjoveski.model.{Folder, Catalog}
import bjoveski.util.{Logging, Manager}
import com.typesafe.config.ConfigFactory

/**
 * Created with IntelliJ IDEA.
 * User: bjoveski
 * Date: 6/19/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
object Uploadr extends Logging {
  val conf = ConfigFactory.load()

  def createSetFromFolder(f: Folder) = {
    val startTime = System.currentTimeMillis()
    val photos = Manager.getPhotosInFolder(f.folder)
    println(s" about to upload photos. [count=${photos.size}] [folderName=${f.folder.getName}]")
    logger.info(s" about to upload photos. [count=${photos.size}] [folderName=${f.folder.getName}]")

    val flickPhotos = photos.map(photoFile => {
        Catalog.uploadPhoto(photoFile)
    })

    val uploaded = flickPhotos.filter(opt => opt.isDefined).map(opt => opt.get)

    println(s"finished uploading. [count=${uploaded.size}] [time=${(System.currentTimeMillis() - startTime)/1000} sec]")
    logger.info(s"finished uploading. [count=${uploaded.size}] [time=${(System.currentTimeMillis() - startTime)/1000} sec]")
    f.sets.foreach(set => {
      Catalog.createSet(set, "", uploaded)
    })
  }


  def uploadPhotosFromXml() {
    Manager.fromXml().foreach(folder => {
      createSetFromFolder(folder)
    })
  }
}
