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
object Driver extends Logging {
  val conf = ConfigFactory.load()
  val apiKey = conf.getString("uploadr-app.apiKey")//"a900d0705dc54f91dc78158646847d70"
  val secret = conf.getString("uploadr-app.secret")
  val driver = new Flickr(apiKey, secret, new REST())
  val frob = driver.getAuthInterface.getFrob
  val authUrl = driver.getAuthInterface.buildAuthenticationUrl(Permission.WRITE, frob)

  logger.info("Connecting to filckr")
  println(s"please visit ${authUrl.toExternalForm}")
  System.in.read()

  val token = driver.getAuthInterface.getToken(frob)
  RequestContext.getRequestContext.setAuth(token)
  driver.setAuth(token)
  logger.info("initialization done")

  def uploadPhoto(file: File) = {
    val startTime = System.currentTimeMillis()
    val meta = new UploadMetaData
    meta.setTitle(file.getName)
    meta.setHidden(true)
    // meta.setAsync()

    val in = new FileInputStream(file)
    logger.info(s"start  uploading photo=[${file.getAbsolutePath}]")
    val photoId = driver.getUploader.upload(in, meta)
    logger.info(s"finish uploading photo=[${file.getAbsolutePath}] time=${System.currentTimeMillis() - startTime}")

    FlickrPhoto(photoId, meta)
  }


  def addPhotoToSet(photosetId: String, photoId: String) {
    driver.getPhotosetsInterface.addPhoto(photosetId, photoId)
  }

  def createSet(title: String, desc: String, primPhotoId: String) = {
    driver.getPhotosetsInterface.create(title, desc, primPhotoId)
  }
}
