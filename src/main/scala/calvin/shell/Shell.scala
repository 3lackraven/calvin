package calvin.shell

import java.net.InetAddress

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.io.StdIn
import scala.language.postfixOps


object Shell extends App {

  val prompt = "[" + System.getProperty("user.name") + "@" + InetAddress.getLocalHost.getCanonicalHostName + "]$ "
  val calvin = "calvin"
  val exit = "exit"

  implicit val timeout = Timeout(10 seconds)
  implicit val executionContext = ExecutionContext.global

  val system = ActorSystem("calvin")
  val parser = system.actorOf(Props[Parser], "parser")
  val printer = system.actorOf(Props[Printer], "printer")
  var line = StdIn.readLine(prompt)

  /*while (line != exit) {

    parser ? line onComplete {
      case Success(result) => println(result)
      case Failure(error) => print(error.getMessage)
    }

    Thread.sleep(500)
    line = StdIn.readLine(prompt)
  }*/

  while (line != exit) {
    val command = parser ? line
    pipe(command) to printer
    Thread.sleep(500)
    line = StdIn.readLine(prompt)
  }

  system.terminate
}

object Action {
  val wtf = "WTF"
  val echo = "echo"
  val start = "start"
  val stop = "stop"
}

case class Action(
                   name: String = Action.wtf,
                   text: String = "???"
                 )

class Parser extends Actor {

  def receive = {
    case arg: Any =>

      val action = optionParser.parse(arg.toString.split(" "), Action()).getOrElse(Action())

      for (i <- 0 to 10000000) { print("")}

      sender() ! action
  }

  val optionParser = new scopt.OptionParser[Action]("") {

    cmd("echo")
      .action((_, c) => c.copy(name = Action.echo))
      .text("return the target text")
      .children(
        arg[String]("<text>")
          .action((value, c) => c.copy(text = value))
          .text("the target text")
      )

    /*opt[String]('n', "name")
      .text("target name (e.g. WebServer)")
      .action((value, command) => command.copy(name = value))

    opt[String]('a', "action")
      .text("target action (e.g echo, start, stop)")
      .action((value, command) => command.copy(action = value))*/
  }
}

class Printer extends Actor {

  def receive = {

    case throwable: Throwable => println(throwable.getMessage)

    case result: Any => println(result)
  }
}