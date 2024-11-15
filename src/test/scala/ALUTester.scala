import chisel3._
import chisel3.iotesters.PeekPokeTester

class ALUTester(dut: ALU) extends PeekPokeTester(dut) {

  //Program Counter running for 5 clock cycles
  poke(dut.io.sel, 0)
  poke(dut.io.in1, 3)
  poke(dut.io.in2, 7)
  step(2)

  poke(dut.io.sel, 1)
  poke(dut.io.in1, 3)
  poke(dut.io.in2, 11)
  step(2)

  poke(dut.io.sel, 2)
  poke(dut.io.in1, 7)
  poke(dut.io.in2, 3)
  step(2)

  poke(dut.io.sel, 3)
  poke(dut.io.in1, 3)
  poke(dut.io.in2, 2)
  step(2)

  poke(dut.io.sel, 4)
  poke(dut.io.in1, 8)
  poke(dut.io.in2, 353)
  step(2)

  poke(dut.io.sel, 5)
  poke(dut.io.in1, 6)
  poke(dut.io.in2, 353)
  step(2)

  poke(dut.io.sel, 6)
  poke(dut.io.in1, 7)
  poke(dut.io.in2, 29)
  step(2)

  poke(dut.io.sel, 7)
  poke(dut.io.in1, 6)
  poke(dut.io.in2, 6)
  step(2)


}

object ALUTester {
  def main(args: Array[String]): Unit = {
    println("Testing ALU")
    iotesters.Driver.execute(
      Array("--generate-vcd-output", "on",
        "--target-dir", "generated",
        "--top-name", "ALUTest"),
      () => new ALU
    ) { c => new ALUTester(c) }
  }
}

