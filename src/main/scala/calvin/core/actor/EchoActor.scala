package calvin.core.actor

import akka.actor.Actor
import calvin.core.request.EchoRequest
import calvin.core.response.EchoResponse

class EchoActor extends Actor {

  def receive = {

    case request: EchoRequest =>
      sender() ! EchoResponse(request)
  }
}
