// app/actors/AccrualActor.scala
package actors

import org.apache.pekko.actor.{Actor, Props}
import play.api.Logger
import domain.LeaveRepository
import javax.inject.Inject
import scala.concurrent.ExecutionContext
import java.time.LocalDate

object AccrualActor {
  case object RunMonthlyAccrual
}

class AccrualActor @Inject() (repository: LeaveRepository)(implicit
    ec: ExecutionContext
) extends Actor {

  private val logger = Logger(this.getClass)
  import AccrualActor._

  def receive: Receive = {
    case RunMonthlyAccrual =>
      println("\n\n>>>>>>>>>> [ACTOR] RUNNING MONTHLY ACCRUAL! <<<<<<<<<<\n\n")
      logger.info(
        "========== Starting monthly leave accrual process... =========="
      )

    case other =>
      println(
        s"\n\n>>>>>>>>>> [ACTOR] RECEIVED UNKNOWN MESSAGE: $other <<<<<<<<<<\n\n"
      )
  }
}
