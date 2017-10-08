package calvin.core.actor

import akka.actor.Actor

class PrinterActor extends Actor {

  def receive = {

    case throwable: Throwable =>
      println(throwable.getMessage)

    case option: Option[_] =>
      option .foreach(value => println(value))

    case result: Any =>
      println(result)
  }
}