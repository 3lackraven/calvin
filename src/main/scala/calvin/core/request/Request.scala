package calvin.core.request

trait Request

case class EchoRequest(
                       text: String = null
                     )
  extends Request

case class StartRequest(
                         name: String = null,
                         port: Int = 0
                       )
  extends Request

case class StopRequest(
                        name: String = null,
                        port: Int = 0
                      )
  extends Request
