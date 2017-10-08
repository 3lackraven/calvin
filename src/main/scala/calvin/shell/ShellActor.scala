package calvin.shell

import java.net.InetAddress

import akka.actor.{Actor, Props}
import calvin.core.{dispatcher, printer, system}
import calvin.core.request.{EchoRequest, Request, StartRequest, StopRequest}
import calvin.core.response.Response
import scopt.OptionParser
import scala.io.StdIn
//import scala.language.postfixOps


object Shell extends App {

  val prompt =
    "[" +
      System.getProperty("user.name") +
      "@" +
      InetAddress.getLocalHost.getCanonicalHostName +
      "]$ "

  val exit = "exit"

  val shell = system.actorOf(Props[ShellActor], "shell")

  var line = ""

  while (line != exit) {

    shell ! line

    Thread.sleep(500)

    if(line.isEmpty)
      println()

    line = StdIn.readLine(prompt)
  }

  system.terminate
}


class ShellActor extends Actor {

  def receive: Receive = {

    case response: Response =>
      printer.forward(response)

    case any: Any =>

      val line = any.toString

      if (!line.isEmpty)
        optionParser
          .parse(line.split(" "), RequestWrapper())
          .foreach(dispatcher ! _.request)
  }

  private case class RequestWrapper(request: Request = null) {
    def apply[T] = request.asInstanceOf[T]
  }

  private val optionParser = new OptionParser[RequestWrapper]("") {

    cmd("echo")
      .text("return a target text")
      .action((_, wrapper) => wrapper.copy(request = EchoRequest()))
      .children(
        arg[String]("<text>")
          .text("target text")
          .action((value, wrapper) => RequestWrapper(wrapper[EchoRequest].copy(text = value)))
      )

    cmd("start")
      .text("start the target service")
      .action((_, command) => command.copy(request = StartRequest()))
      .children(

        arg[String]("<name>")
          .text("service name")
          .action((value, wrapper) => RequestWrapper(wrapper[StartRequest].copy(name = value))),

        opt[Int]('p', "port")
          .text("service port")
          .action((value, wrapper) => RequestWrapper(wrapper[StartRequest].copy(port = value)))
      )

    cmd("stop")
      .text("stop the target service")
      .action((_, command) => command.copy(request = StopRequest()))
      .children(

        arg[String]("<name>")
          .text("service name")
          .action((value, wrapper) => RequestWrapper(wrapper[StopRequest].copy(name = value))),

        opt[Int]('p', "port")
          .text("service port")
          .action((value, wrapper) => RequestWrapper(wrapper[StopRequest].copy(port = value)))
      )

    /*cmd("server")
      .text("execute command on server")
      .action((_, wrapper) => wrapper.copy(action = ServerAction()))
      .children(
        arg[Command]("start|stop")
          .text("start a server")
          .action((_, wrapper) => ActionWrapper(wrapper[ServerAction].copy(command = Command.start))),

        opt[Unit]('x', "stop")
          .text("stop a server")
          .action((_, wrapper) => ActionWrapper(wrapper[ServerAction].copy(command = Command.stop))),

        opt[Int]('p', "port")
          .text("service port")
          .action((value, wrapper) => ActionWrapper(wrapper[ServerAction].copy(port = value)))
      )*/
  }
}