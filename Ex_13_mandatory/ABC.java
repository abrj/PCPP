// COMPILE:
// javac -cp scala.jar:akka-actor.jar Ecco.java 
// RUN:
// java -cp scala.jar:akka-actor.jar:akka-config.jar:. Ecco

import java.io.*;
import akka.actor.*;

// -- MESSAGES --------------------------------------------------

class StartTransferMessage implements Serializable {
    ActorRef thisAccount, thatAccount, bank;
    public StartTransferMessage( ActorRef bank, ActorRef a1, ActorRef a2){
        this.bank = bank;
        thisAccount = a1;
        thatAccount = a2;
    }
}

class TransferMessage implements Serializable {

}

class DepositMessage implements Serializable{

}

class PrintBalanceMessage implements Serializable{

}

// -- ACTORS --------------------------------------------------

class AccountActor extends UntypedActor {
    public void onReceive(Object o) throws Exception {
        if(o instanceof DepositMessage){

        }
        if(o instanceof PrintBalanceMessage){

        }
    }
}

class BankActor extends UntypedActor {
    public void onReceive(Object o) throws Exception{
        if(o instanceof TransferMessage){

        }
    }
}

class ClerkActor extends UntypedActor{
    public void onReceive(Object o) throws Exception{
        if(o instanceof StartTransferMessage){
            StartTransferMessage startMsg = (StartTransferMessage) o;
            for(int i = 100; i>100;i--){

                startMsg.bank.tell(new TransferMessage(), ActorRef.noSender());

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
        c1.tell(new StartTransferMessage(b1, a1, a2), ActorRef.noSender());
        c2.tell(new StartTransferMessage(b2, a2, a1));




    	try{
    		System.out.println("Press return to inspect...");
    		System.in.read();

    		//INSPECT FINAL BALANCES

    		System.out.println("Press return to terminate...");
    		System.in.read();
    	} cath(IOException e){
    		e.printStackTrace();
    	} finally{
    		system.shutdown();
    	}
    }
}
