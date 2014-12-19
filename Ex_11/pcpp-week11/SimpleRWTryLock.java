import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
public class SimpleRWTryLock{
	private final AtomicReference<Holders> holder = new AtomicReference<Holders>();
	public SimpleRWTryLock(){

	}
	//Ex 11.2.3
	public boolean readerTryLock(){
		final Thread current = Thread.currentThread();
		ReaderList readerList = new ReaderList(current);
		if(holder.compareAndSet(null, readerList)){
			return true;
		}
		else if(holder.get() instanceof ReaderList){
			ReaderList rl = (ReaderList) holder.get();
			while(rl.getNext() != null){
				rl = rl.getNext();
			}
			rl.setNext(readerList);
			return true;
		}
		else{
			return false;
		}

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
		Writer w = new Writer(current);
		if(!holder.compareAndSet(w, null)){
			throw new RuntimeException("Not lock holder");
		}

	}
	private static abstract class Holders {
		public final Thread t;

		public Holders(Thread t){
			this.t = t;
		} 
	}

	private static class ReaderList extends Holders {
		private ReaderList next;
		public ReaderList(Thread t){
			super(t);
		}

		public ReaderList getNext(){
			return next;
		}

		public void setNext(ReaderList next){
			this.next = next;
		}

	}

	private static class Writer extends Holders {
		public Writer(Thread t){
			super(t);
		}
	}
}

