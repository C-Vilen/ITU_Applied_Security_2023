package AssignmentOne;

import java.math.BigInteger;
import java.util.Random;

public class User {
    private final String name;
    private final BigInteger g, p, publicKey;
    private volatile BigInteger privateKey, friendsPublicKey;
    private volatile BigInteger[] inbox;
    private volatile int messageCount;

    public User(String name, BigInteger g, BigInteger p) {
        this.name = name;
        this.g = g;
        this.p = p;
        this.privateKey = new BigInteger(p.bitLength(), new Random());
        this.publicKey = g.modPow(privateKey, p);
        this.inbox = new BigInteger[10];
        this.messageCount = 0;
        this.friendsPublicKey = BigInteger.ZERO;
    }

    public void addMessage(String message, boolean encrypt) {
        if (messageCount < 10) {
            BigInteger ciphertext2;
            // Encrypting the message if encrypt is true
            if (encrypt) {
                BigInteger[] ciphertext = encryptMessage(message);
                ciphertext2 = ciphertext[1];
                inbox[messageCount] = publicKey;
            } else {
                ciphertext2 = new BigInteger(message);
                inbox[messageCount] = friendsPublicKey;
            }
            // Adding the message to the inbox
            inbox[messageCount + 1] = ciphertext2;
            messageCount += 2;
        } else {
            System.out.println("Inbox is full");
        }
    }

        private BigInteger[] encryptMessage(String message){
        BigInteger messageInt = new BigInteger(message);
        BigInteger ciphertext2 = messageInt.multiply(friendsPublicKey.modPow(privateKey, p)).mod(p);
        return new BigInteger[]{publicKey, ciphertext2};
    }
        
    public String getMessage(boolean decrypt) {
        if (messageCount < 2) {
            return "No messages in inbox";
        } else {
            String decryptedString;
            if (decrypt) {
                decryptedString = decryptMessage(inbox);
            } else {
                decryptedString = inbox[1].toString();
            }
    
            for (int i = 0; i < messageCount - 2; i += 2) {
                inbox[i] = inbox[i + 2];
                inbox[i + 1] = inbox[i + 3];
            }
            messageCount -= 2;
    
            return decryptedString;
        }
    }

    private String decryptMessage(BigInteger[] ciphertext){
        BigInteger ciphertext1 = ciphertext[0];
        BigInteger ciphertext2 = ciphertext[1];
        BigInteger s = ciphertext1.modPow(privateKey, p);
        BigInteger decryptedMessage = ciphertext2.multiply(s.modInverse(p)).mod(p);
        return decryptedMessage.toString();
    }
    
    public void interceptEncryptedInbox(User user) {
        this.messageCount = user.messageCount;
        for (int i = 0; i < user.messageCount; i++) {
            this.inbox[i] = user.inbox[i];
        }
    }
    
    public void changeMessage(BigInteger multiplier) {
        for (int i = 1; i < messageCount; i += 2) {
            inbox[i] = inbox[i].multiply(multiplier);
        }
    }        
    
    public void pprint(){
        System.out.println("------------------------");
        System.out.println("Pretty print of: " + name);
        System.out.println("------------------------");
        System.out.println("g:                " + g);
        System.out.println("p:                " + p);
        System.out.println("privateKey:       " + privateKey);
        System.out.println("publicKey:        " + publicKey);
        System.out.println("friendsPublicKey: " + friendsPublicKey);
        for (int i = 0; i < messageCount; i++) {
            System.out.println("inbox[" + i + "]:         " + inbox[i]);
        }
        System.out.println("------------------------");
    }

    public void sendFriendPublicKey(BigInteger friendsPublicKey) {
        this.friendsPublicKey = friendsPublicKey;
    }

    public void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }
}
