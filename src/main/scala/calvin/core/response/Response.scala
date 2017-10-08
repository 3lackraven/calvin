package calvin.core.response

import calvin.core.request.{EchoRequest, Request, StartRequest, StopRequest}


trait Response {
  val request: Request
}

case class EchoResponse(
                         request: EchoRequest = null
                       )
  extends Response {
  override def toString = request.text
}

case class StartResponse(
                          request: StartRequest = null,
                          name: String = null,
                          port: Int = 0
                        )
  extends Response

case class StopResponse(
                         request: StopRequest = null,
                         name: String = null,
                         port: Int = 0
                       )
  extends Response

