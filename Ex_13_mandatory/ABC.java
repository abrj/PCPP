// COMPILE:
// javac -cp scala.jar:akka-actor.jar Ecco.java 
// RUN:
// java -cp scala.jar:akka-actor.jar:akka-config.jar:. Ecco

import java.io.*;
import akka.actor.*;
import java.util.*;

// -- MESSAGES --------------------------------------------------

class StartTransferMessage implements Serializable {
    public final ActorRef from, to, bank;
    public final int count;
    public StartTransferMessage(ActorRef bank, ActorRef from, ActorRef to, int count){
        this.bank = bank;
        this.from = from;
        this.to = to;
        this.count = count;
    }
}

class TransferMessage implements Serializable {
    public final ActorRef from, to;
    public final int amount;
    public TransferMessage(int amount, ActorRef from, ActorRef to){
        this.amount = amount;
        this.from = from;
        this.to = to;
    }
}

class DepositMessage implements Serializable{
    public final int value;
    public DepositMessage (int value){
        this.value = value;
    }
}

class PrintBalanceMessage implements Serializable{

}

// -- ACTORS --------------------------------------------------

class AccountActor extends UntypedActor {
    private int balance = 0;
    public void onReceive(Object o) throws Exception {
        if(o instanceof DepositMessage){
            DepositMessage depositMsg = (DepositMessage) o;
            this.balance += depositMsg.value;
        }
        if(o instanceof PrintBalanceMessage){
            System.out.println("Balance: " + this.balance);
        }
    }
}

class BankActor extends UntypedActor {
    public void onReceive(Object o) throws Exception{
        if(o instanceof TransferMessage){
            TransferMessage tm = (TransferMessage) o;
            tm.from.tell(new DepositMessage(-tm.amount), ActorRef.noSender());
            tm.to.tell(new DepositMessage(tm.amount), ActorRef.noSender());
        }
    }
}

class ClerkActor extends UntypedActor{
    public void onReceive(Object o) throws Exception{
        if(o instanceof StartTransferMessage){
            StartTransferMessage startMsg = (StartTransferMessage) o;
            Random rnd = new Random();
            for(int i = 0; i < startMsg.count; i++){
                int amount = rnd.nextInt(100);
                startMsg.bank.tell(new TransferMessage(amount, startMsg.from, startMsg.to), ActorRef.noSender());

            }
         }
    }

}

// -- MAIN --------------------------------------------------

public class ABC {
    public static void main(String[] args) {
    	//CREATE ACTORS
        final ActorSystem system = ActorSystem.create("ABC_system");
        final ActorRef a1 = system.actorOf(Props.create(AccountActor.class), "Account1");
        final ActorRef a2 = system.actorOf(Props.create(AccountActor.class), "Account2");

        final ActorRef b1 = system.actorOf(Props.create(BankActor.class), "Bank1");
        final ActorRef b2 = system.actorOf(Props.create(BankActor.class), "Bank2");

        final ActorRef c1 = system.actorOf(Props.create(ClerkActor.class), "Clerk1");
        final ActorRef c2 = system.actorOf(Props.create(ClerkActor.class), "Clerk2");

        //SEND STARTMESSAGE
        c1.tell(new StartTransferMessage(b1, a1, a2, 100), ActorRef.noSender());
        c2.tell(new StartTransferMessage(b2, a2, a1, 100), ActorRef.noSender());




    	try{
            Thread.sleep(100);
    		System.out.println("Press return to inspect...");
    		System.in.read();

    		//INSPECT FINAL BALANCES
            a1.tell(new PrintBalanceMessage(), ActorRef.noSender());
            a2.tell(new PrintBalanceMessage(), ActorRef.noSender());

            
            Thread.sleep(100);
    		System.out.println("Press return to terminate...");
    		System.in.read();
    	}
        catch(Exception e){
    		e.printStackTrace();
    	} 
        finally{
    		system.shutdown();
    	}
    }
}
