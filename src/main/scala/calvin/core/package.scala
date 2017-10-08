package calvin

import akka.actor.{ActorSystem, Props}
import calvin.core.actor.{DispatcherActor, EchoActor, PrinterActor, WebActor}

package object core {

  val calvin = "calvin"

  val system = ActorSystem(calvin)

  val echo = system.actorOf(Props[EchoActor], "echo")

  val printer = system.actorOf(Props[PrinterActor], "printer")

  val dispatcher = system.actorOf(Props[DispatcherActor], "dispatcher")

  val web = system.actorOf(Props[WebActor], "web")
}