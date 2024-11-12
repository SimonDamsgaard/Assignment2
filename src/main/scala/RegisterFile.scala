import chisel3._
import chisel3.util._

class RegisterFile extends Module {
  val io = IO(new Bundle {
    val aSel = Input(UInt(log2Ceil(11).W))      // Selector for output 'a'
    val bSel = Input(UInt(log2Ceil(11).W))      // Selector for output 'b'
    val writeSel = Input(UInt(log2Ceil(11).W))  // Selector for writing
    val writeData = Input(UInt(32.W))           // Data to write, 32 bits
    val writeEnable = Input(Bool())             // Write enable signal
    val a = Output(UInt(32.W))                  // Output for selected register 'a'
    val b = Output(UInt(32.W))                  // Output for selected register 'b'
  })

  // Define registers as a vector of 11 registers 32 bits initialized to 0
  val registers = RegInit(VecInit(Seq.fill(11)(0.U(32.W))))

  // Read operations
  io.a := registers(io.aSel)
  io.b := registers(io.bSel)

  // Write operation
  when(io.writeEnable) {
    registers(io.writeSel) := io.writeData
  }
}
