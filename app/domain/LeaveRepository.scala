package domain

import doobie._
import doobie.implicits._
import doobie.implicits.javatimedrivernative._
import cats.effect.IO
import cats.syntax.applicative._
import java.util.UUID
import javax.inject.{Inject, Singleton}
import play.api.Configuration

object CustomPutGet {

  implicit val uuidMeta: Meta[UUID] =
    Meta[String].timap(UUID.fromString)(_.toString)

  implicit val contractTypeMeta: Meta[ContractType] =
    Meta[String].timap[ContractType] {
      case "FullTime"   => ContractType.FullTime
      case "PartTime"   => ContractType.PartTime
      case "Contractor" => ContractType.Contractor
    } {
      case ContractType.FullTime   => "FullTime"
      case ContractType.PartTime   => "PartTime"
      case ContractType.Contractor => "Contractor"
    }
}

@Singleton
class LeaveRepository @Inject() (config: Configuration) {
  import CustomPutGet._

  private val dbUrl = config.get[String]("db.default.url")
  private val dbUser = config.get[String]("db.default.username")
  private val dbPass = config.get[String]("db.default.password")

  val transactor = Transactor.fromDriverManager[IO](
    "com.mysql.cj.jdbc.Driver",
    dbUrl,
    dbUser,
    dbPass
  )

  def findEmployeeQuery(id: UUID): ConnectionIO[Option[Employee]] =
    sql"""
      SELECT id, first_name, last_name, hire_date, contract_type 
      FROM employees 
      WHERE id = ${id.toString}
    """.query[Employee].option

  def updateBalanceQuery(
      employeeId: UUID,
      newTaken: BigDecimal
  ): ConnectionIO[Int] =
    sql"""
      UPDATE leave_balances 
      SET taken_days = taken_days + $newTaken 
      WHERE employee_id = ${employeeId.toString}
    """.update.run

  def deductLeaveTransactionally(
      employeeId: UUID,
      daysToTake: BigDecimal
  ): IO[Either[LeaveError, Unit]] = {

    val transactionProgram: ConnectionIO[Either[LeaveError, Unit]] = for {
      employeeOpt <- findEmployeeQuery(employeeId)

      result <- employeeOpt match {
        case None =>
          Left[LeaveError, Unit](LeaveError.EmployeeNotFound(employeeId))
            .pure[ConnectionIO]

        case Some(_) =>
          updateBalanceQuery(employeeId, daysToTake).map(_ => Right(()))
      }
    } yield result

    transactionProgram.transact(transactor)
  }
}
