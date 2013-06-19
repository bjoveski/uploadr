package bjoveski.util

import java.io.{File, FileInputStream}
import com.aetrion.flickr.{REST, Flickr, RequestContext}
import com.aetrion.flickr.uploader.UploadMetaData
import com.aetrion.flickr.auth.Permission
import bjoveski.model.FlickrPhoto
import com.typesafe.config.ConfigFactory

/**
 * Created with IntelliJ IDEA.
 * User: bjoveski
 * Date: 6/19/13
 * Time: 1:47 PM
 * To change this template use File | Settings | File Templates.
 */
object Driver {
    val conf = ConfigFactory.load()
    val apiKey = conf.getString("uploadr-app.apiKey")//"a900d0705dc54f91dc78158646847d70"
    val secret = conf.getString("uploadr-app.secret")
//  val apiKey = "a900d0705dc54f91dc78158646847d70"
//  val secret = "e16e06b1d26cb9c3"
  val driver = new Flickr(apiKey, secret, new REST())
  val frob = driver.getAuthInterface.getFrob
  val authUrl = driver.getAuthInterface.buildAuthenticationUrl(Permission.WRITE, frob)

  println(s"please visit ${authUrl.toExternalForm}")
  System.in.read()

  val token = driver.getAuthInterface.getToken(frob)
  RequestContext.getRequestContext.setAuth(token)
  driver.setAuth(token)


  def uploadPhoto(file: File) = {
    val meta = new UploadMetaData
    meta.setTitle(file.getName)
    meta.setHidden(true)
    // meta.setAsync()

    val in = new FileInputStream(file)
    //    logger.info(s"start  uploading photo ${file.getAbsolutePath}")
    val photoId = driver.getUploader.upload(in, meta)
    //    logger.info(s"finish uploading photo ${file.getAbsolutePath}")

    FlickrPhoto(photoId, meta)
  }


  def addPhotoToSet(photosetId: String, photoId: String) {
    driver.getPhotosetsInterface.addPhoto(photosetId, photoId)
  }

  def createSet(title: String, desc: String, primPhotoId: String) = {
    driver.getPhotosetsInterface.create(title, desc, primPhotoId)
  }
}