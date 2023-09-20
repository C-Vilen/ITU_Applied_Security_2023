package AssignmentOne;

import java.math.BigInteger;
import java.util.Random;

public class User {
    private String name;
    private BigInteger g, p, power, pk, friendpk, combinedpk;
    private int[] messages;
    private int messageCount;

    public User(String name, BigInteger g, BigInteger p) {
        this.name = name;
        this.g = g;
        this.p = p;
        this.power = new BigInteger(p.bitLength(), new Random());
        this.pk = g.modPow(power, p);
        this.messages = new int[10];
        this.messageCount = 0;
        this.friendpk = BigInteger.ZERO;
        this.combinedpk = BigInteger.ZERO;
    }

    public void addMessage(String message) {
        BigInteger messageInt = new BigInteger(message);
        BigInteger r = new BigInteger(p.bitLength(), new Random());
        BigInteger ciphertext1 = g.modPow(r, p);
        BigInteger ciphertext2 = messageInt.multiply(pk.modPow(r, p)).mod(p);
        
        if (messageCount < 10) {
            messages[messageCount] = ciphertext1.intValue();
            messages[messageCount + 1] = ciphertext2.intValue();
            messageCount += 2;
        }
    }
        

    public String getMessages() {
        if (messageCount < 2) {
            return "No messages";
        } else {
            BigInteger ciphertext1 = BigInteger.valueOf(messages[0]);
            BigInteger ciphertext2 = BigInteger.valueOf(messages[1]);
            BigInteger s = ciphertext1.modPow(power, p);
            BigInteger decryptedMessage = ciphertext2.multiply(s.modInverse(p)).mod(p);
            String decryptedString = decryptedMessage.toString();
    
            for (int i = 0; i < messageCount - 2; i += 2) {
                messages[i] = messages[i + 2];
                messages[i + 1] = messages[i + 3];
            }
            messageCount -= 2;
    
            return decryptedString;
        }
    }
    
    
    

    public void sendFriendpk(BigInteger friendpk) {
        this.friendpk = friendpk;
        this.combinedpk = friendpk.modPow(power, p);
    }

    public BigInteger getFriendpk() {
        return friendpk;
    }

    public String getName() {
        return name;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getPower() {
        return power;
    }

    public BigInteger getPk() {
        return pk;
    }
}
