package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Controller {
    public static ObservableList<TestFile> testFiles = FXCollections.observableArrayList();
    private int numTruepositives = 0 , numTruenegatives = 0, numFalsePositive =0, numFiles = 0;
    private double accu;
    private double preci;
    //public static double accuracy;
    //public static double precision;
    @FXML
    private TableView<TestFile> table;
    @FXML
    private TableColumn filename;
    @FXML
    private TableColumn actualClass;
    @FXML
    private TableColumn spamProbability;
    @FXML
    private TextField acc;
    @FXML
    private TextField prec;



    @FXML
    public void initialize() throws FileNotFoundException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(null);

        TrainFile train = new TrainFile();
        train.parseFile(new File("/home/semilore/Desktop/Assignment1/Assignment1/assignment1_data/data/train"));
        int spamfileSize = train.getFolderSize(new File("/home/semilore/Desktop/Assignment1/Assignment1/assignment1_data/data/train/spam"));// size of spam training directory
        int hamfileSize = train.getFolderSize(new File("/home/semilore/Desktop/Assignment1/Assignment1/assignment1_data/data/train/ham"));// size of ham training directory
        int hamfileSize2 = train.getFolderSize(new File("/home/semilore/Desktop/Assignment1/Assignment1/assignment1_data/data/train/ham2"));// size of ham2 training directory
        hamfileSize += hamfileSize2;
        train.HamProbability(hamfileSize);
        train.SpamProbability(spamfileSize);
        TreeMap sW = train.CalculatePrSW();

        File file = mainDirectory;//new File("/home/semilore/Desktop/Assignment1/Assignment1/assignment1_data/data/test");
        Testloop(file, sW);
        numFiles = Controller.testFiles.size();
        CalculateAcc();
        CalculatePrecision();

        filename.setCellValueFactory(new PropertyValueFactory<>("filename"));
        actualClass.setCellValueFactory(new PropertyValueFactory<>("actualClass"));
        spamProbability.setCellValueFactory(new PropertyValueFactory<>("spamProbability"));
        table.setItems(testFiles);
        DecimalFormat df = new DecimalFormat("0.00000");
        acc.setText(df.format(accu));
        prec.setText(df.format(preci));

    }

    public double calculateN(double key){
        double result;
        result = Math.log(1 - key) - Math.log(key);
        return result;
    }
    public double Sf(double N){
        double sf = 1/(1+ Math.pow(Math.E, N));
        return sf;
    }


    public void Testloop(File file, Map key) throws FileNotFoundException {
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

            if (spamFileprob >= 0.500 && file.getParentFile().getName().toString().equals("spam")){
                numTruepositives++;
            }
            if (spamFileprob < 0.500 && file.getParentFile().getName().toString().equals("ham")){
                numTruenegatives++;

            }
            if (spamFileprob >= 0.500 && file.getParentFile().getName().toString().equals("ham")){
                numFalsePositive++;
            }
            Controller.testFiles.add(new TestFile(file.getName(), spamFileprob, file.getParentFile().getName()));

        }

    }
    public void CalculateAcc()
    {
        accu = ((numTruepositives + numTruenegatives)/ (double) numFiles);
    }
    public void CalculatePrecision()
    {
        preci = (numTruepositives/(double)(numFalsePositive + numTruepositives));
    }



}
