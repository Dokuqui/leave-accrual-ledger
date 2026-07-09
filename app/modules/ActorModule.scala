package modules

import com.google.inject.AbstractModule
import play.api.libs.concurrent.PekkoGuiceSupport
import actors.{AccrualActor, AccrualScheduler}

class ActorModule extends AbstractModule with PekkoGuiceSupport {
  override def configure(): Unit = {
    bindActor[AccrualActor]("accrual-actor")

    bind(classOf[AccrualScheduler]).asEagerSingleton()
  }
}
