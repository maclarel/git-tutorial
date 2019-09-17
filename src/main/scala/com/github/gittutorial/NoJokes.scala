package com.github.gittutorial

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import io.circe.{Encoder, Decoder, Json, HCursor}
import io.circe.generic.semiauto._
import org.http4s._
import org.http4s.implicits._
import org.http4s.{EntityDecoder, EntityEncoder, Method, Uri, Request}
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.Method._
import org.http4s.circe._

trait NoJokes[F[_]]{
  def noJoke(): F[NoJokes.Message]
}

object NoJokes {
  implicit def apply[F[_]](implicit ev: NoJokes[F]): NoJokes[F] = ev
  /**
    * More generally you will want to decouple your edge representations from
    * your internal data structures, however this shows how you can
    * create encoders for your data.
    **/
  final case class Message(message: String) extends AnyVal
  object Message {
    implicit val messageEncoder: Encoder[Message] = new Encoder[Message] {
      final def apply(a: Message): Json = Json.obj(
        ("message", Json.fromString(a.message)),
      )
    }
    implicit def messageEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Message] =
      jsonEncoderOf[F, Message]
  }

  def impl[F[_]: Applicative]: NoJokes[F] = new NoJokes[F]{
    def noJoke(): F[NoJokes.Message] =
      Message("No joke for you.").pure[F]
  }
}