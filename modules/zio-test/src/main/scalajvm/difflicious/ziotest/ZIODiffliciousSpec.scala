package difflicious.ziotest

import difflicious.reporter.{DifferenceFound, DiffResultJsonlWriter}
import zio.{Chunk, UIO, ZIO}
import zio.test.{TestAspectAtLeastR, TestEnvironment}

trait ZIODiffliciousSpec extends ZIODiffliciousSpecBase {
  private lazy val diffliciousJsonlWriter = new DiffResultJsonlWriter()

  override def aspects: Chunk[TestAspectAtLeastR[Environment & TestEnvironment]] =
    super.aspects :+ ZIODiffliciousSpecBase.reportingAspect(reportDiffFailure)

  private def reportDiffFailure(testHierarchy: Vector[String], failure: DifferenceFound): UIO[Unit] = {
    val className = getClass.getName.stripSuffix("$")
    val testName = testHierarchy.lastOption.getOrElse("")
    ZIO
      .attemptBlocking {
        diffliciousJsonlWriter.write(
          suiteName = className.split('.').lastOption.getOrElse(className),
          suiteId = className,
          suiteClassName = Some(className),
          testName = testName,
          testText = testName,
          testHierarchy = testHierarchy,
        )(failure)
      }
      .catchAllCause(cause => ZIO.logWarningCause("Failed to write Difflicious diff report", cause))
  }
}
