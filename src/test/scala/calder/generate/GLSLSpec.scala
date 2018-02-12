package calder.generate

import org.scalatest._

import calder.generate._

class GenerateSpec extends FunSpec {
  def normalize(str: String): String = str.replaceAll("\\s+", " ").trim

  it("supports literals") {
    assert(
      normalize(
        new MultiplicativeExpression(
          new MultiplicativeExpression(
            new UnaryExpression(new PostfixExpression(new PrimaryExpression(new Intconstant("123"))))
          ),
          new Slash,
          new UnaryExpression(new PostfixExpression(new PrimaryExpression(new Intconstant("456"))))
        ).source()
      ) == normalize("123 / 456")
    )
  }
}
