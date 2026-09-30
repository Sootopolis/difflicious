---
id: zio-test
title: ZIO Test
---

# ZIO Test

Add the Difflicious sbt plugin to `project/plugins.sbt`:

```scala
addSbtPlugin("com.github.jatcwang" % "sbt-difflicious" % "@VERSION@")
```

Then add the ZIO Test integration to `build.sbt`:

```scala
"com.github.jatcwang" %% "difflicious-zio-test" % "@VERSION@" % Test
```

and then mix `ZIODiffliciousSpec` into your specs and call `assertNoDiff` on any `Differ`.

```scala mdoc:nest
import difflicious.Differ
import difflicious.ziotest.ZIODiffliciousSpec
import zio.test.ZIOSpecDefault

object MySpec extends ZIOSpecDefault with ZIODiffliciousSpec {
  def spec = suite("MySpec")(
    test("a == b") {
      Differ[Int].assertNoDiff(1, 2)
    },
  )
}
```

`assertNoDiff` returns a `TestResult`, just like ZIO Test's own assertions such as `assertTrue`. You can combine it
with other assertions using `&&`, use it in a `for` comprehension, or use it inside `check`, where ZIO Test shrinks the
failing input as usual. As with any ZIO Test assertion, the result must be returned from the test: a result that is
neither returned nor combined with another is ignored. Enable the `-Wnonunit-statement` compiler option to have the
compiler warn about such a dropped result.

On the JVM, `ZIODiffliciousSpec` writes reports for failed diffs, which you can explore using
**[Diff Viewer UI / CLI](../CLI.md)**. Each report records the labels of the suites enclosing the failed test.
JSONL report writing is not currently supported on Scala.js or Scala Native.

Reports are written by a test aspect that `ZIODiffliciousSpec` adds to `aspects`. If you override `aspects` in your
spec, build on `super.aspects` rather than replacing it, otherwise no reports are written.
