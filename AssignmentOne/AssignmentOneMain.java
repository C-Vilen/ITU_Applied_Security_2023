package AssignmentOne;

import java.math.BigInteger;

public class AssignmentOneMain {
    public static void main(String[] args) {
        BigInteger sharedG = BigInteger.valueOf(666);
        BigInteger sharedP = BigInteger.valueOf(6661);

        // Alice and Bob computes their public keys
        User Alice = new User("Alice", sharedG, sharedP);
        User Bob = new User("Bob", sharedG, sharedP);
        
        // Alice sends her public key to Bob
        Bob.sendFriendpk(Alice.getPk());
        
        // Bob sends his public key to Alice
        Alice.sendFriendpk(Bob.getPk());
        
        // Alice creates a message and send it to Bob
        Alice.addMessage("2000");
        Bob.addMessage(Alice.getMessages());
        System.out.println("The message is: " + Bob.getMessages() + ", and it should be 2000");
    }
}
