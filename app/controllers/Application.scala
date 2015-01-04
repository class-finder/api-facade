package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.ws._

import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {
  val svcStudio = Play.configuration.getString("service.studio.hostname").getOrElse(throw new Exception("Failed to load config"))
  val svcEvent = Play.configuration.getString("service.event.hostname").getOrElse(throw new Exception("Failed to load config"))

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  private def OkCORS[C](content: C)(implicit writeable : play.api.http.Writeable[C]): Result = {
    Ok(content).withHeaders("Access-Control-Allow-Origin" -> "*")
  }

  def indexStudios = Action.async { req =>
    val holder : WSRequestHolder = WS.url(svcStudio + "/studios")
    holder.get().map { response =>
      OkCORS(response.json)
    }
  }

  def indexEvents = Action.async {
    val holder : WSRequestHolder = WS.url(svcEvent + "/events")
    holder.get().map { response =>
      OkCORS(response.json)
    }
  }

}