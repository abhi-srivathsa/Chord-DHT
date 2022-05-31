import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Utility functions
public class Helper implements Runnable {

    private final Node node;

    public Helper(Node node){
        this.node = node;
    }

    @Override
    public void run() {
        if ( !node.isSingle()) {
            node.stabilization();
            node.fixFTable();
            node.checkPredecessor();
            node.fixSList();
        }
    }


    public static int numberOfBit(){
        return 16;
    }

    public static int getPeriod(){
        return 500;
    }

    public static int getTimer(){
        return 1000;
    }

    public static String calculateHash(String key){
        MessageDigest digest;
        byte[] hash;
        StringBuffer hexHash = new StringBuffer();
        try {
            digest = MessageDigest.getInstance("SHA-1");
            hash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
            int j = 0;
            while(j<numberOfBit()/8){
                String hex = Integer.toHexString(0xff &  hash[j]);
                if (hex.length() == 1) hexHash.append('0');
                hexHash.append(hex);
                j++;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexHash.toString();
    }


    public static String computefinger(String nodeidentifier, int finger){
        int[] hash = new int[numberOfBit()/8];
        int j=0;
        for (int i =0; i< (numberOfBit()/8) *2; i= i+2){
            String number = "" + nodeidentifier.charAt(i) + nodeidentifier.charAt(i+1);
            hash[j] = Integer.parseInt(number,16);
            j++;

        }
        finger = numberOfBit()-finger;
        recursion(hash, (int) Math.pow(2,(numberOfBit()-finger)%8), (finger-1)/8);
        StringBuffer hexHash = new StringBuffer();
        for (int h = 0; h < numberOfBit()/8; h++) {
            String hex = Integer.toHexString(hash[h]);
            if (hex.length() == 1) hexHash.append('0');
            hexHash.append(hex);
        }
        return hexHash.toString();
    }
    private static void recursion (int hash[], int tosum, int i){
        if (hash[i] + tosum <= 255){
            hash[i] = hash[i] + tosum;
        }
        else{
            hash[i] =  (hash[i] + tosum -256);
            if(i >0){
                recursion(hash,1, i-1);
            }
        }
    }





}
