package instructions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class LoadConstant extends Instruction {
	int r;
	int c;
	LoadConstant(int r, int c) {
		this.r = r;
		this.c = c;
	}
	
	void execute(Machine machine) {
		machine.registers[r] = c;
		machine.pc++;
	}
	
	@Override
	public String toString() {
		return "LoadConstant("+r+", "+c+")";
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof LoadConstant lc &&
				r == lc.r && c == lc.c;
	}
	
	public int hashCode() {
		return r * 31 + c;
	}
}

class Decrement extends Instruction {
	int r;
	Decrement(int r) { this.r = r; }
	
	void execute(Machine machine) { 
		machine.registers[r]--;
		machine.pc++;
	}
}

class Multiply extends Instruction {
	int r1;
	int r2;
	public Multiply(int r1, int r2) {
		super();
		this.r1 = r1;
		this.r2 = r2;
	}
	
	void execute(Machine machine) {
		machine.registers[r1] *= machine.registers[r2];
		machine.pc++;
	}
}

class JumpIfZero extends Instruction {
	int r;
	int a;
	JumpIfZero(int r, int a) {
		this.r = r;
		this.a = a;
	}
	
	void execute(Machine machine) {
		if (machine.registers[r] == 0)
			machine.pc = a;
		else
			machine.pc++;
	}
}

class Jump extends Instruction {
	int a;
	Jump(int a) {
		this.a = a;
	}
	
	void execute(Machine machine) {
		machine.pc = a;
	}
}


class Halt extends Instruction {
	
	void execute(Machine machine) {
		machine.pc = -1;
	}
}

class Machine {
	int[] registers;
	Instruction[] instructions;
	int pc;
	
	Machine(int[] registers, Instruction[] instructions) {
		this.registers = registers;
		this.instructions = instructions;
	}
	
	void run() {
		while (0 <= pc) {
			Instruction i = instructions[pc];
			i.execute(this); // dynamic binding
		}
	}
	
	public static void execute(int[] registers, Instruction[] instructions) {
		new Machine(registers, instructions).run();
	}
}

class MachineTest {

	@Test
	void test() {
		assertEquals(8, power(2, 3));
		assertEquals(9, power(3, 2));
		
		LoadConstant lc1 = new LoadConstant(2, 1);
		LoadConstant lc2 = new LoadConstant(2, 1);
		assertEquals(lc1, lc2);
		assertEquals("LoadConstant(2, 1)", ""+lc1);
	}
	
	int power(int x, int y) {
		Instruction[] powerProgram = {
			new LoadConstant(2, 1),
			new JumpIfZero(1, 5),
			new Multiply(2, 0),
			new Decrement(1),
			new Jump(1),
			new Halt()
		};
		int[] registers = {x, y, 0};
		Machine.execute(registers, powerProgram);
		System.out.println(registers[2]);
		return registers[2];
	}

}
