package com.ubirch

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn

object ActorServer {
  def main(args: Array[String]): Unit = {
    implicit val actorSystem: ActorSystem = ActorSystem("actorSystem")
    implicit val ec = actorSystem.dispatcher

    val route =
      path("newMsgPackByBin") {
        post {
          entity(as[Array[Byte]]) { binData =>
            complete(UPPFactory.createNewUPP(binData))
          }
        }
      } ~ path("newMsgPackString") {
        post {
          entity(as[String]) { msgPack =>
            complete(UPPFactory.createNewUPP(msgPack))
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => actorSystem.terminate())
  }
}
