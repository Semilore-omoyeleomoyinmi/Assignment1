package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.util.TreeMap;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws FileNotFoundException {
        TrainFile train = new TrainFile();
        train.parseFile(new File("/home/semilore/Desktop/Soft-Sys-Dev/Assignment1/assignment1_data/data/train"));
        int spamfileSize = train.getFolderSize(new File("/home/semilore/Desktop/Soft-Sys-Dev/Assignment1/assignment1_data/data/train/spam"));// size of spam training directory
        int hamfileSize = train.getFolderSize(new File("/home/semilore/Desktop/Soft-Sys-Dev/Assignment1/assignment1_data/data/train/ham"));// size of ham training directory
        int hamfileSize2 = train.getFolderSize(new File("/home/semilore/Desktop/Soft-Sys-Dev/Assignment1/assignment1_data/data/train/ham2"));// size of ham2 training directory
        hamfileSize += hamfileSize2;
        train.HamProbability(hamfileSize);
        train.SpamProbability(spamfileSize);
        TreeMap sW = train.CalculatePrSW();
        System.out.println(sW.toString());



     launch(args);
    }
}
