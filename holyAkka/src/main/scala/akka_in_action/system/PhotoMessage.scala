package akka_in_action.system

import java.util.Date

case class PhotoMessage(id: String,
                        photo: String,
                        creationTime: Option[Date] = None,
                        speed: Option[Int] = None)

case class TimeoutMessage(msg:PhotoMessage)
