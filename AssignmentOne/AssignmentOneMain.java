package AssignmentOne;

import java.math.BigInteger;

public class AssignmentOneMain {
    public static void main(String[] args) {
        BigInteger sharedG = BigInteger.valueOf(666);
        BigInteger sharedP = BigInteger.valueOf(6661);
        // --------------------------------------------------------------------------------------------
        // Task 1:
        System.out.println("\n--------------------------\nTask 1:\n--------------------------\n");
        // Alice and Bob computes their public keys
        User Alice = new User("Alice", sharedG, sharedP);
        User Bob = new User("Bob", sharedG, sharedP);
        
        // Alice sends her public key to Bob
        Bob.sendFriendPublicKey(Alice.getPublicKey());
        
        // Bob sends his public key to Alice
        Alice.sendFriendPublicKey(Bob.getPublicKey());

        // Alice creates a message and stores it in her inbox[]
        Alice.addMessage("2000", true);

        // Alice sends the message to Bob
        Bob.addMessage(Alice.getMessage(false), false);
        Alice.pprint();
        Bob.pprint();
        System.out.println("\n");

        // Bob decrypts the message and can read it
        System.out.println("The message to bob is: " + Bob.getMessage(true) + ", and should be 2000");

        // --------------------------------------------------------------------------------------------
        // Task 2:
        System.out.println("\n--------------------------\nTask 2:\n--------------------------\n");

        // Eve computes her public key
        User Eve = new User("Eve", sharedG, sharedP);

        // Eve performs a brute-force attact to find bobs private key with his public key
        BigInteger privateKeyGuess = BigInteger.ONE;
        BigInteger result = sharedG.modPow(privateKeyGuess, sharedP);
        while (!result.equals(Bob.getPublicKey())) {
            privateKeyGuess = privateKeyGuess.add(BigInteger.ONE);
            result = sharedG.modPow(privateKeyGuess, sharedP);
        }
        System.out.println("Eve has found Bob's private key: " + privateKeyGuess+"\n");
        Eve.setPrivateKey(privateKeyGuess);
        
        // Eve intercepts the message from Alice to Bob
        Alice.addMessage("2000", true);
        Bob.addMessage(Alice.getMessage(false), false);
        Eve.interceptEncryptedInbox(Bob);
        System.out.println("Eve has intercepted the message from Alice to Bob");

        Eve.pprint();
        Bob.pprint();
        System.out.println("Eve intercepted bob's message which is: " + Eve.getMessage(true) + ", and should be 2000");
        System.out.println("The message to bob is: " + Bob.getMessage(true) + ", and should be 2000");



        // --------------------------------------------------------------------------------------------
        // Task 3:
        System.out.println("\n--------------------------\nTask 3:\n--------------------------\n");
        // Weave computes her public key
        User Weave = new User("Weave", sharedG, sharedP);
        
        // Weave intercepts the message from Alice to Bob and multiply the amount
        BigInteger multiplyAmount = BigInteger.valueOf(2);

        Alice.addMessage("2000", true);
        Weave.interceptEncryptedInbox(Alice);
        Weave.pprint();
        Weave.changeMessage(multiplyAmount);
        Weave.pprint();
        String str = Weave.getMessage(false);
        System.out.println("weave message: " + str);
        Bob.addMessage(str, false);
        Bob.pprint();
        // Weave intercepts the message from Alice to Bob and multiplies the amount
        System.out.println("Weave has intercepted the message from Alice to Bob and multiplied the amount by " + multiplyAmount);
        System.out.println("The message to bob is: " + Bob.getMessage(true) + ", and should be 2000 * " + multiplyAmount);
        
    }
}
