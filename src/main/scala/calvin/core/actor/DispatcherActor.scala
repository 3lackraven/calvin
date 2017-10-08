package calvin.core.actor

import akka.actor.Actor
import calvin.core.request.{EchoRequest, StartRequest, StopRequest}
import calvin.core.{printer, echo, web}

class DispatcherActor extends Actor {

  def receive = {

    case request: EchoRequest =>
      echo.forward(request)

    case request@(_: StartRequest | _: StopRequest) =>
      web.forward(request)

    case any: Any =>
      printer.forward(any)

  }
}
