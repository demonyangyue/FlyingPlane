package com.example

/**
 * @author yy
 */
import akka.actor.{Actor, Props, ActorLogging}

object Plane{
    case object GiveMeControl
}

class Plane extends Actor with ActorLogging{
    import Altimeter._
    import ControlSurfaces._
    import Plane._

    val altimeter = context.actorOf(Props[Altimeter])
    val controls = context.actorOf(Props(classOf[ControlSurfaces], altimeter))

    def receive = {
        case GiveMeControl => {
            log.info("Plane giving control")
            sender ! controls
        }
    }
}

