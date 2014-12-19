import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
public class SimpleRWTryLock{
	public final AtomicReference<Holders> holder = new AtomicReference<Holders>();
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

	//Ex 11.2.4
	public void readerUnlock(){
		final Thread current = Thread.currentThread();
		if(holder.get() == null){
			throw new RuntimeException("Not lock holder.");
		}
		else if(holder.get() instanceof ReaderList){
			ReaderList rl = (ReaderList) holder.get();
			if(!rl.contains(current))
				throw new RuntimeException("Not lock holder");
			holder.set(rl.remove(current));
		}
		else{
			throw new RuntimeException("Not lock holder");
		}
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
		if(holder.get().t != current){
			throw new RuntimeException("Not lock holder");
		}
		else{
			holder.set(null);
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

		public boolean contains(Thread t){
			if(super.t != t){
				ReaderList nxt = next;
				while(nxt.t != null){
					if(nxt.t == t)
						return true;
					nxt = nxt.getNext();
				}
				return false;
			}
			return true;
		}

		public ReaderList remove(Thread t){
			
			if(super.t != t){
				ReaderList nxt = next;
				while(nxt.t != null){
					if(nxt.getNext().t == t){
						nxt.setNext(nxt.getNext().getNext());
						return this;
					}
					nxt = nxt.getNext();
				}
				throw new RuntimeException("Not lock holder");
			}
			return next;
		}

	}

	private static class Writer extends Holders {
		public Writer(Thread t){
			super(t);
		}
	}
}

class TestTryLock {
	public static void main(String[] args) {
		testTryLock();
	}

	private static void testTryLock(){
		SimpleRWTryLock lock = new SimpleRWTryLock();
		
		// assert lock.holder == null;
		assert lock.writerTryLock() == true;
		assert lock.writerTryLock() == false;
		//Make sure lock didn't get freed up just because we tried to take it.
		assert lock.writerTryLock() == false;
		//Make sure we can't get reader
		assert lock.readerTryLock() == false;
		//No error should be thrown here
		lock.writerUnlock();
		boolean thrown = false;
		try{
			lock.writerUnlock();
		}catch(RuntimeException e){
			thrown = true;
		}
		assert thrown == true;


		assert lock.readerTryLock() == true;
		assert lock.readerTryLock() == true;
		lock.readerUnlock();
		lock.readerUnlock();

		thrown = false;
		try{
			lock.readerUnlock();
		}catch(RuntimeException e){
			thrown = true;
		}
		assert thrown == true;

	}
}

