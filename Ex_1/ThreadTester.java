//Exercise 1.3
public class ThreadTester{
	public static void main(String[] args) {
		Thread t1 = 
 			new Thread( 
	 		new Runnable() { 
	 			public void run() { 
	 				while (true)
	 					Printer.print();
	 			} 
	 		} 
	 		);
	 	Thread t2 = 
 		new Thread( 
	 	new Runnable() { 
	 		public void run() { 
	 			while (true)
	 				Printer.print();
	 		} 
	 	} 
	 	);
	 t1.start();
	 t2.start();
	 try{
	 	t1.join();
	 	t2.join();
	 }
	 catch(Exception e){
	 	//
	 }

	}
}
//Exercise 1.3 
class Printer {
	public static void print() {
		synchronized(Printer.class){
		System.out.print("-");
		try { Thread.sleep(50); } catch (InterruptedException exn) { }
		System.out.print("|");
		}
	}
}