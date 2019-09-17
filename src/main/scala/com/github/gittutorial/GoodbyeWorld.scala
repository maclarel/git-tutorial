package com.github.gittutorial

import cats.Applicative
import cats.implicits._
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe._

trait GoodbyeWorld[F[_]]{
  def goodbye(n: GoodbyeWorld.Name): F[GoodbyeWorld.Farewell]
}

object GoodbyeWorld {
  implicit def apply[F[_]](implicit ev: GoodbyeWorld[F]): GoodbyeWorld[F] = ev

  final case class Name(name: String) extends AnyVal
  /**
    * More generally you will want to decouple your edge representations from
    * your internal data structures, however this shows how you can
    * create encoders for your data.
    **/
  final case class Farewell(farewell: String) extends AnyVal
  object Farewell {
    implicit val farewellEncoder: Encoder[Farewell] = new Encoder[Farewell] {
      final def apply(a: Farewell): Json = Json.obj(
        ("message", Json.fromString(a.farewell)),
      )
    }
    implicit def farewellEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Farewell] =
      jsonEncoderOf[F, Farewell]
  }

  def impl[F[_]: Applicative]: GoodbyeWorld[F] = new GoodbyeWorld[F]{
    def goodbye(n: GoodbyeWorld.Name): F[GoodbyeWorld.Farewell] =
        Farewell("Goodbye, " + n.name).pure[F]
  }
}