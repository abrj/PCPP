// COMPILE:
// javac -cp scala.jar:akka-actor.jar Ecco.java 
// RUN:
// java -cp scala.jar:akka-actor.jar:akka-config.jar:. Ecco

import java.io.*;
import akka.actor.*;

// -- MESSAGES --------------------------------------------------

class StartTransferMessage implements Serializable {
}

class TransferMessage implements Serializable {

}

class DepositMessage implements Serializable{

}

class PrintBalanceMessage implements Serializable{

}

// -- ACTORS --------------------------------------------------

class AccountActor extends UntypedActor {

}

class BankActor extends UntypedActor {

}

class ClerkActor extends UntypedActor{

}

// -- MAIN --------------------------------------------------

public class ABC {
    public static void main(String[] args) {
    	//CREATE ACTORS AND SEND START MESSAGE

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
