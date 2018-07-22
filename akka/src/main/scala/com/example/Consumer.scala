package com.example

import akka.actor.{ Actor, ActorLogging, Props }

class Consumer extends Actor with ActorLogging {
  override def receive: Receive = {
    case msg: String =>
      log.info(self.path.name + " received " + msg)
  }
}

object Consumer {
  def props: Props = Props[Consumer]
}
