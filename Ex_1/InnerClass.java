//Exercise 1.2

public class InnerClass {
	public static void main(String[] args) {
		Runnable r = new Runnable(){
			public void run(){
				System.out.println("Hello, World!");
			}
		};
		doTwice(r);
		doNTimes(r, 5);
		write14Times("Ex 1.2 - 3 : Hello World");
	}

	//Exercise 1.2 - 1 
	public static void doTwice(Runnable r){
		System.out.println(" ## Ex 1.2 - 1");
		r.run();
		r.run();
	}
	//Exercise 1.2 - 2 
	public static void doNTimes(Runnable r, int n){
		System.out.println(" ## Ex 1.2 - 2");
		for(int i=0; i<n; i++){
			r.run();
		}
	}
	//Exercise 1.2 - 3
	public static void write14Times(String s){
		System.out.println(" ## Ex 1.3 - 3");
		Runnable r = new Runnable(){
			public void run(){
				System.out.println(s);
			}
		};
		doNTimes(r, 14);		
	}

}