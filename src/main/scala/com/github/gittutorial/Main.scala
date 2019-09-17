package com.github.gittutorial

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Main extends IOApp {
  def run(args: List[String]) =
    GittutorialServer.stream[IO].compile.drain.as(ExitCode.Success)
}