package com.example

/**
 * @author yy
 */
import akka.actor.{Actor, ActorRef}

object EventSource {
    case class RegisterListener(listener: ActorRef)
    case class UnregisterListener(listener: ActorRef)
}

trait EventSource { this: Actor =>

    import EventSource._

    var listeners = Vector.empty[ActorRef]
    def notify[T](event: T) = listeners foreach (_ ! event)

    def eventSourceReceive: Receive = {
        case RegisterListener(listener) => {
            listeners = listeners :+ listener 
        }
        case UnregisterListener(listener) => {
            listeners = listeners filter (_ != listener)
        }
    }
}
