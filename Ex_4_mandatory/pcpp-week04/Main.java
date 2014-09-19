public class Main{
	public static void main(String[] args) {
		// Mark.SystemInfo();
		// System.out.printf("-----Mark 1-----");
		// System.out.println("");
		// Mark.Mark1();
		// System.out.printf("-----Mark 2-----");
		// System.out.println("");
		// Mark.Mark2();
		// System.out.printf("-----Mark 3-----");
		// System.out.println("");
		// Mark.Mark3();
		// System.out.printf("-----Mark 4-----");
		// System.out.println("");
		// Mark.Mark4();
		// System.out.printf("-----Mark 5-----");
		// System.out.println("");
		// Mark.Mark5();
		// System.out.printf("-----Mark 6-----");
		// System.out.println("");
		// Mark.Mark6("multiply", new IntToDouble() { 
			// public double call(int i) { return Mark.multiply(i); } }); 
		
		/**
		Mark 7 stuff
		*/
		Mark.Mark7("pow", new IntToDouble() {
			public double call(int i) { return Math.pow(10.0, 0.1 * (i & 0xFF)); } });
		Mark.Mark7("exp", new IntToDouble() {
			public double call(int i) { return Math.exp(0.1 * (i & 0xFF)); } });
		Mark.Mark7("log", new IntToDouble() {
			public double call(int i) { return Math.log(0.1 + 0.1 * (i & 0xFF)); } });
		Mark.Mark7("sin", new IntToDouble() {
			public double call(int i) { return Math.sin(0.1 * (i & 0xFF)); } });
		Mark.Mark7("cos", new IntToDouble() {
			public double call(int i) { return Math.cos(0.1 * (i & 0xFF)); } });
		Mark.Mark7("tan", new IntToDouble() {
			public double call(int i) { return Math.tan(0.1 * (i & 0xFF)); } });
		Mark.Mark7("asin", new IntToDouble() {
			public double call(int i) { return Math.asin(1.0/256.0 * (i & 0xFF)); } });
		Mark.Mark7("acos", new IntToDouble() {
			public double call(int i) { return Math.acos(1.0/256.0 * (i & 0xFF)); } });
		Mark.Mark7("atan", new IntToDouble() {
			public double call(int i) { return Math.atan(1.0/256.0 * (i & 0xFF)); } });

	}
}