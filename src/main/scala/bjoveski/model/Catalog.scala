package bjoveski.model

import scala.collection.mutable
import java.io.File
import bjoveski.util.Driver

/**
 * Created with IntelliJ IDEA.
 * User: bjoveski
 * Date: 6/19/13
 * Time: 1:47 PM
 * To change this template use File | Settings | File Templates.
 */
object Catalog {
  val photoid2Photo = new mutable.HashMap[String, FlickrPhoto]()
  val setId2Photos = new mutable.HashMap[String, mutable.HashSet[FlickrPhoto]]()


  def uploadPhoto(file: File) = {
    val photo = Driver.uploadPhoto(file)
    photoid2Photo.put(photo.photoId, photo)
    photo
  }

  def addPhotoToSet(photoSetId: String, photoId: String) {
    Driver.addPhotoToSet(photoSetId, photoId)
    val photos = setId2Photos.getOrElseUpdate(photoSetId, new mutable.HashSet[FlickrPhoto]())
    photos.add(photoid2Photo(photoId))
  }

  def createSet(title: String, desc: String, photos: Iterable[FlickrPhoto]) = {

    val photoSet = Driver.createSet(title, desc, photos.head.photoId)
    photos.tail.foreach(photo => Driver.addPhotoToSet(photoSet.getId, photo.photoId))

    val catalogSet = new mutable.HashSet[FlickrPhoto]()
    photos.foreach(photo => catalogSet.add(photo))

    setId2Photos.put(photoSet.getId, catalogSet)
  }

}
