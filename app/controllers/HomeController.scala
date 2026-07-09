package controllers

import javax.inject._
import play.api.mvc._
import actors.AccrualScheduler

@Singleton
class HomeController @Inject() (
    val controllerComponents: ControllerComponents,
    scheduler: AccrualScheduler
) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
