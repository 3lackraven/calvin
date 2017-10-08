package calvin.core.actor

import akka.actor.{Actor, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import calvin.core.request.{StartRequest, StopRequest}
import calvin.core.{printer, system}

import scala.concurrent.Future

class WebActor extends Actor {

  implicit val actorSystem = system
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher
  var bindingFuture = null.asInstanceOf[Future[Http.ServerBinding]]


  val route =
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    }

  def receive = {

    case request: StartRequest =>
      printer ! "dispatcher received: " + request
      bindingFuture = Http().bindAndHandle(route, "localhost", request.port)
      printer ! s"Server online at http://localhost:${request.port}"


    case request: StopRequest =>
      bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => printer ! s"Server at http://localhost:${request.port} offline")


  }
}
