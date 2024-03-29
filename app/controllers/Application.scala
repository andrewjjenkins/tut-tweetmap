package controllers

import actors.UserActor
import akka.actor.Props

import play.api._
import play.api.Play.current
import play.api.libs.json.{JsObject, JsValue, Json, __}
import play.api.mvc._
import play.api.mvc.WebSocket
import play.api.libs.ws.WS

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("This is a different message"))
  }

  def search(query: String) = Action.async {
    fetchTweets(query).map(tweets => Ok(tweets))
  }

  def fetchTweets(query: String): Future[JsValue] = {
    val tweetsFuture =
      WS.url("http://search-twitter-proxy.herokuapp.com/search/tweets")
        .withQueryString("q" -> query)
        .get()
    tweetsFuture.map {
      response => response.json
    } recover {
      case _ => Json.obj("responses" -> Json.arr())
    }
  }

//  def scratch(path1: String, path2: String) = Action {
//    Ok(views.html.scratch(path1, path2, "hello"))
//  }

  def ws = WebSocket.acceptWithActor[JsValue, JsValue] {
    request => out => Props(new UserActor(out))
  }
}
