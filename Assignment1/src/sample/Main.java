package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws FileNotFoundException {
        TrainFile train = new TrainFile();
        train.parseFile(new File("/home/semilore/Desktop/Assignment1/Assignment1/assignment1_data/data/train"));
        int spamfileSize = train.getFolderSize(new File("/home/semilore/Desktop/Assignment1/Assignment1/assignment1_data/data/train/spam"));// size of spam training directory
        int hamfileSize = train.getFolderSize(new File("/home/semilore/Desktop/Assignment1/Assignment1/assignment1_data/data/train/ham"));// size of ham training directory
        int hamfileSize2 = train.getFolderSize(new File("/home/semilore/Desktop/Assignment1/Assignment1/assignment1_data/data/train/ham2"));// size of ham2 training directory
        hamfileSize += hamfileSize2;
        train.HamProbability(hamfileSize);
        train.SpamProbability(spamfileSize);
        TreeMap sW = train.CalculatePrSW();

        File file = new File("/home/semilore/Desktop/Assignment1/Assignment1/assignment1_data/data/test");

        Testloop(file, sW);
        System.out.println(Controller.testFiles.get(0).getFilename());
        launch(args);
    }

    public static double calculateN(double key){
        double result;
        result = Math.log(1 - key) - Math.log(key);
        return result;
    }
    public static double Sf(double N){
        double sf = 1/(1+ Math.pow(Math.E, N));
        return sf;
    }


    public static void Testloop(File file, Map key) throws FileNotFoundException {
        if (file.isDirectory()) {
            //parse each file inside the directory
            File[] content = file.listFiles();
            for (File current : content) {
                Testloop(current, key);
            }
        } else {
            double N = 0.0;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String token = scanner.next();
                if (key.containsKey(token)) {
                    N += calculateN((Double) key.get(token));
                }
            }
            double spamFileprob = Sf(N);
            Controller.testFiles.add(new TestFile(file.getName(), spamFileprob, file.getParent()));
            N = 0.0;
        }

    }

}


