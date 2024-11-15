import chisel3._
import chisel3.iotesters.PeekPokeTester

class RegisterFileTester(dut: RegisterFile) extends PeekPokeTester(dut) {

  // Testing writing values to specific registers and then reading them back

  poke(dut.io.writeEnable, true)
  poke(dut.io.writeSel, 1) //Choosing to write to register 1
  poke(dut.io.writeData, 42) // Writing the data
  step(1)

  // Write 99 to register 2
  poke(dut.io.writeSel, 2)
  poke(dut.io.writeData, 99)
  step(1)

  // Write 123 to register 10
  poke(dut.io.writeSel, 10)
  poke(dut.io.writeData, 123)
  step(1)

  // Disable write
  poke(dut.io.writeEnable, false)

  // Reading from registers and verifying results
  // Read from register 1
  poke(dut.io.aSel, 1)
  step(1)

  // Read from register 2
  poke(dut.io.aSel, 2)
  step(1)

  // Read from register 10
  poke(dut.io.aSel, 10)
  step(1)
}

object RegisterFileTester {
  def main(args: Array[String]): Unit = {
    println("Testing RegisterFile")
    iotesters.Driver.execute(
      Array("--generate-vcd-output", "on",
        "--target-dir", "generated",
        "--top-name", "RegisterFileTest"),
      () => new RegisterFile
    ) { c => new RegisterFileTester(c) }
  }
}
