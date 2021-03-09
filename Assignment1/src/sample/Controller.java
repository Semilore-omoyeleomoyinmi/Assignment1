package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {
    @FXML
    private TableView<TestFile> table;
    @FXML
    private TableColumn file;
    @FXML
    private TableColumn actualClass;
    @FXML
    private TableColumn spamProb;
    @FXML
    private TextField accuracy;
    @FXML
    private TextField precision;



    @FXML
    public void initialize() {
        file.setCellValueFactory(new PropertyValueFactory<>("file"));
        actualClass.setCellValueFactory(new PropertyValueFactory<>("actualClass"));
        spamProb.setCellValueFactory(new PropertyValueFactory<>("spamProb"));
    }


}
