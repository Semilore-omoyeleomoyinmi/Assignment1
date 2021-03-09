package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataSource {
    public static ObservableList<TestFile> getAllFiles(File file) throws FileNotFoundException {
        ObservableList<TestFile> testFiles = FXCollections.observableArrayList();
        Scanner scanner = new Scanner( file);
        boolean valid = false; // valid checks to see if the word has been seen once
        while (scanner.hasNext() && valid == false) {
            String token = scanner.next();

        }
        return testFiles;
    }
}