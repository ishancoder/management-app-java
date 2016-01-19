package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.control.TableColumn;
import java.sql.*;

public class Main extends Application {
    private ObservableList<Manager> data = FXCollections.observableArrayList();
    private Connection connect(String url, String user, String password){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            System.out.println("Can't Connect to the database!");
            e.printStackTrace();
        }
        return conn;
    }

    private int renderTable(TableView tv) {
        int count = 0;
        int temp;
        try {
            data.clear();
            Connection conn = connect("jdbc:mysql://localhost:3306/MoneyManagement","root","ishan");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM MONEY;");
            while(rs.next()) {
                temp = rs.getInt(4);
                count += temp;
                System.out.println(rs.getString(2));
                Manager manager = new Manager(rs.getInt(1), rs.getString(2),
                        rs.getString(3), temp, rs.getDate(5).toString());
                data.add(manager);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        System.out.println("Database Connected!!");

        //User Interface Stuff
        GridPane gp = new GridPane(); //Grid for form
        TableView tableView = new TableView(); //Table of content
        TableColumn idColumn = new TableColumn("id");
        idColumn.setCellValueFactory(new PropertyValueFactory<Manager, Integer>("id"));
        TableColumn givenToColumn = new TableColumn("Given To");
        givenToColumn.setCellValueFactory(new PropertyValueFactory<Manager, String>("givenTo"));
        TableColumn givenForColumn = new TableColumn("Given For");
        givenForColumn.setCellValueFactory(new PropertyValueFactory<Manager,String>("givenFor"));
        TableColumn amountColumn = new TableColumn("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<Manager, Integer>("amount"));
        TableColumn dateColumn = new TableColumn("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<Manager, String>("date"));
        tableView.getColumns().addAll(idColumn, givenToColumn, givenForColumn, amountColumn, dateColumn);
        Label totalLabel = new Label("Total : "+renderTable(tableView));
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(10);
        tableView.setItems(data);
        Text txt = new Text("Money Manager!!");
        txt.setFont(Font.font("Tahoma",FontWeight.NORMAL, 20));
        Label givenToLabel = new Label("Amount Given To : ");
        Label givenForLabel = new Label("Amount Given For : ");
        Label amountLabel = new Label("Amount : ");
        Label dateLabel = new Label("Date : ");
        TextField givenToTextField = new TextField();
        TextField givenForTextField = new TextField();
        TextField amountTextField = new TextField();
        DatePicker datePicker = new DatePicker();
        Button submitButton = new Button("Add!");
        submitButton.setPrefWidth(150);
        gp.add(txt, 0, 0, 2, 1);
        gp.add(givenToLabel, 0, 1);
        gp.add(givenToTextField, 1, 1);
        gp.add(givenForLabel, 0, 2);
        gp.add(givenForTextField, 1, 2);
        gp.add(amountLabel, 0, 3);
        gp.add(amountTextField, 1, 3);
        gp.add(dateLabel, 0, 4);
        gp.add(datePicker, 1, 4);
        gp.add(submitButton, 1, 5, 2, 1);
        gp.add(totalLabel, 0, 5);
        gp.add(tableView, 2, 1, 5, 10);
        primaryStage.setScene(new Scene(gp, 800, 600));

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PreparedStatement stmt = null;
                try {
                    Connection conn = connect("jdbc:mysql://localhost:3306/MoneyManagement","root","ishan");
                    stmt = conn.prepareStatement("INSERT INTO MONEY(givento, givenfor, amount, date) VALUES (?,?,?,?)");
                    String givento = givenToTextField.getText();
                    String givenfor = givenForTextField.getText();
                    String amount = amountTextField.getText().trim();
                    Date date = Date.valueOf(datePicker.getValue());
                    if(!givento.equals("") && !givenfor.equals("") && !amount.equals("") && !date.equals(null)){
                        System.out.println("Submit button Clicked!!!");
                        try{
                            int amnt = Integer.parseInt(amount);
                            stmt.setString(1, givento);
                            stmt.setString(2, givenfor);
                            stmt.setInt(3, amnt);
                            stmt.setDate(4, date);
                            stmt.execute();
                            System.out.println("New Entry of " + givento);
                            givenForTextField.setText("");
                            givenToTextField.setText("");
                            amountTextField.setText("");
                            datePicker.setValue(null);
                            System.out.println("Entering Again!!");
                            totalLabel.setText("Total: " + renderTable(tableView));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        primaryStage.centerOnScreen();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
