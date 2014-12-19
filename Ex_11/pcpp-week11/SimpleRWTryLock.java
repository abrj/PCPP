import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
public class SimpleRWTryLock{
	private final AtomicReference<Holders> holder = new AtomicReference<Holders>();
	public SimpleRWTryLock(){

	}
	//Ex 11.2.3
	public boolean readerTryLock(){
		
	}

	public void readerUnlock(){

	}
	//Ex 11.2.1
	public boolean writerTryLock(){
	 final Thread current = Thread.currentThread();
	 Writer w = new Writer(current);
	 return holder.compareAndSet(null, w);	 	
	}

	//Ex 11.2.2
	public void writerUnlock(){
		final Thread current = Thread.currentThread();
		Writer w = new Writer(current)
		if(!holder.compareAndSet(w, null)){
			throw new RuntimeException("Not lock holder");
		}

	}
	private static abstract class Holders {
		public final Thread t; 
	}

	private static class ReaderList extends Holders {
		private ReaderList next;
		public ReaderList(Thread t){
			super.t = t;
		}

		public Thread getNext(){
			return next;
		}

		public void setNext(Thread next){
			this.next = next;
		}

	}

	private static class Writer extends Holders {
		public Writer(Thread t){
			super.t = t;
		}
	}
}

