package sample;
import java.io.*;
import java.util.*;

public class TrainFile {
    private TreeMap<String, Double> hamprobabilities;
    private TreeMap<String, Double> spamprobabilities;
    private TreeMap<String, Double> prSW;
    private Map<String, Integer> trainHamFreq;// this holds the amount of files the code thinks is ham
    private Map<String, Integer> trainSpamFreq;// this holds the amount of files the code thinks is spam
    public TrainFile(){
        trainSpamFreq = new TreeMap<>();
        trainHamFreq = new TreeMap<>();
        hamprobabilities = new TreeMap<>();
        spamprobabilities = new TreeMap<>();
        prSW = new TreeMap<>();
    }
    public void parseFile (File file) throws FileNotFoundException {
        if(file.isDirectory()){
            //parse each file inside the directory
            File[] content = file.listFiles();
            for(File current: content){
                parseFile(current);
            }
        }else {
            // create a scanner
            Scanner scanner = new Scanner(file);
            // scan [A-Z]+ \\W*each word in each line
            boolean valid = false; // valid checks to see if the word has been seen once
            while (scanner.hasNext() && valid == false) {
                String token = scanner.next();

                // check to see if token is in the trainSpamFreq map, then increase the count
                if (tokenCheck(token) && file.getParentFile().getName().toString().equals("spam")) {
                    countSpamFreq(token);
                    // change valid to true
                    valid = true;
                }

                // check to see if token is in the trainHamFreq map, then increase the count
                if (tokenCheck(token)){
                    if(file.getParentFile().getName().toString().equals("ham") || file.getParentFile().getName().toString().equals("ham2")) {
                        countHamFreq(token);
                    }
                    // change valid to true
                    valid = true;
                }
            }

        }

    }
    private void countHamFreq(String token){
        //put the word into trainHamFreq
        if(trainHamFreq.containsKey(token)){
            int numfile = trainHamFreq.get(token);
            trainHamFreq.put(token,numfile +1);
        }else{
            trainHamFreq.put(token,1);
        }
    }
    private void countSpamFreq(String token){
        //put the word into trainSpamFreq
        if(trainSpamFreq.containsKey(token)){
            int numfile = trainSpamFreq.get(token);
            trainSpamFreq.put(token,numfile+1);
        }else{
            trainSpamFreq.put(token,1);
        }
    }

    private boolean tokenCheck(String token) {
        // create pattern to check if file is spam
        String pattern = "^[a-zA-Z]+";
        /*String pattern = "GET";
        String pattern2 = "FREE";
         */
        return (token.matches(pattern));

    }
    public TreeMap CalculatePrSW(){
        Set<String> keyspam = spamprobabilities.keySet();
        for(String keys: keyspam) {
            //System.out.println(keys);
            if(hamprobabilities.containsKey(keys))
            {
               // System.out.println("yay");
                cPrSW(keys);
            }
            //System.out.println("nay");
        }
        return prSW;
    }
    public void cPrSW(String token){
        // Pr(S|W)
        double prb = (spamprobabilities.get(token))/(spamprobabilities.get(token) + hamprobabilities.get(token));
        prSW.put(token, prb);
    }

    // get the directory size
    public static int getFolderSize(File dir) {
        int size = 0;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                size += file.length();
            }
            else
                size += getFolderSize(file);
        }
        return size;
    }


    public void SpamProbability (int fileSize) {
        Set<String> keyspam = trainSpamFreq.keySet();// the keys in a set then loop through to calculate probability
        for(String keys: keyspam){
           double calcProb = calProWS(fileSize, keys);// calculate Pr(W|S)
            spamprobabilities.put(keys, calcProb);
        }
    }

    public void HamProbability (int fileSize) {
        Set<String> keyham = trainHamFreq.keySet();// get the keys in a set then loop through to calculate probability
        for(String keys: keyham){
            double calcProb = calProWH(fileSize, keys );
            hamprobabilities.put(keys, calcProb);
        }
    }




    public double calProWS(double fileSize, String token) {
        double prob = (trainSpamFreq.get(token))/fileSize;
        return prob;
    }

    public double calProWH(double fileSize, String token) {
        double prob = (trainHamFreq.get(token))/fileSize;
        return prob;
    }




}
