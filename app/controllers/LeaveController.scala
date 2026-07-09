package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import domain.{LeaveRepository, LeaveError}
import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

import cats.effect.unsafe.implicits.global

@Singleton
class LeaveController @Inject() (
    val controllerComponents: ControllerComponents,
    repository: LeaveRepository
)(implicit ec: ExecutionContext)
    extends BaseController {
  case class LeaveRequestPayload(employeeId: UUID, daysToTake: BigDecimal)

  implicit val payloadFormat: OFormat[LeaveRequestPayload] =
    Json.format[LeaveRequestPayload]

  def requestLeave() = Action.async(parse.json) { request =>
    request.body
      .validate[LeaveRequestPayload]
      .fold(
        errors => {
          Future.successful(
            BadRequest(
              Json.obj("error" -> "Invalid JSON format or missing fields.")
            )
          )
        },
        payload => {
          repository
            .deductLeaveTransactionally(payload.employeeId, payload.daysToTake)
            .unsafeToFuture()
            .map {
              case Right(_) =>
                Ok(
                  Json.obj(
                    "status" -> "success",
                    "message" -> s"Successfully deducted ${payload.daysToTake} days for employee ${payload.employeeId}."
                  )
                )
              case Left(error) =>
                BadRequest(Json.obj("error" -> error.message))
            }
        }
      )
  }
}
