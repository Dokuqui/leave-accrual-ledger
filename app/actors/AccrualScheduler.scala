package actors

import org.apache.pekko.actor.{ActorRef, ActorSystem}
import play.api.Logger
import javax.inject.{Inject, Named}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class AccrualScheduler @Inject() (
    system: ActorSystem,
    @Named("accrual-actor") accrualActor: ActorRef
)(implicit ec: ExecutionContext) {

  private val logger = Logger(this.getClass)

  println("\n\n>>>>>>>>>> [SYSTEM] ACCRUAL SCHEDULER IS ALIVE! <<<<<<<<<<\n\n")
  logger.info(
    "--> AccrualScheduler initialized! Counting down 10 seconds... <--"
  )

  system.scheduler.scheduleWithFixedDelay(
    initialDelay = 10.seconds,
    delay = 1.minute,
    receiver = accrualActor,
    message = AccrualActor.RunMonthlyAccrual
  )
}
