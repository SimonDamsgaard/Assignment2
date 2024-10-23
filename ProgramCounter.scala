import chisel3._
import chisel3.util._

class ProgramCounter extends Module {
  val io = IO(new Bundle {
    val stop = Input(Bool())
    val jump = Input(Bool())
    val run = Input(Bool())
    val programCounterJump = Input(UInt(16.W))
    val programCounter = Output(UInt(16.W))
  })

  // Define the program counter register with an initial value of 0
  val programCounterReg = RegInit(0.U(16.W))

  // Logic for incrementing, jumping, or holding the program counter
  when (io.run && !io.stop && !io.jump) {
    // Increment the program counter when running and not stopped or jumping
    programCounterReg := programCounterReg + 1.U
  }

  // Mux to decide whether to jump to a specific value or keep the current counter
  val Lmux = Mux(io.jump, io.programCounterJump, programCounterReg)

  // Selector for the stop condition, if stop is high or run is low, hold the value
  val selecterRmux = io.stop || !io.run

  // Mux to decide the final output based on stop and run conditions
  val Rmux = Mux(selecterRmux, programCounterReg, Lmux)

  // Assign the final value to the output
  io.programCounter := Rmux
}