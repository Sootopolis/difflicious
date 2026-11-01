package difflicious.ziotest

import difflicious.Differ
import difflicious.differ.SeqDiffer
import difflicious.utils.{SeqLike, TypeName}
import zio.NonEmptyChunk

trait ZIOInstances {
  implicit val nonEmptyChunkAsSeq: SeqLike[NonEmptyChunk] = new SeqLike[NonEmptyChunk] {
    override def asSeq[A](f: NonEmptyChunk[A]): Seq[A] = f.toChunk
  }

  implicit def nonEmptyChunkDiffer[A](implicit
    aDiffer: Differ[A],
    typeName: TypeName[NonEmptyChunk[A]],
  ): SeqDiffer[NonEmptyChunk, A] =
    SeqDiffer.create(
      itemDiffer = aDiffer,
      typeName = typeName.copy(long = "zio.NonEmptyChunk", short = "NonEmptyChunk"),
      asSeq = nonEmptyChunkAsSeq,
    )
}
