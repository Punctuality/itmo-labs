package domain

import java.time.LocalDateTime
import java.util.UUID

case class Word(id: UUID = UUID.randomUUID,
                dateTime: LocalDateTime = LocalDateTime.now,
                var position: Int,
                value: String,
                var withSpace: Boolean = true,
                var visible: Boolean = true,
                userId: UUID) {

  override def toString: String = this.value

  override def hashCode: Int = this.value.hashCode + this.position.hashCode + userId.hashCode
}

