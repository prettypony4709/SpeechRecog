package com.ViktorVano.SpeechRecognitionAI.GUI;

import com.ViktorVano.SpeechRecognitionAI.Miscellaneous.WordCommand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.ViktorVano.SpeechRecognitionAI.Miscellaneous.WordCommandsFile.saveWordCommands;

public class WordCommandSettings {
    private int wordCommandIndex = -1;
    private Button buttonUpdateWordCommand;
    private Button buttonRemoveWordCommand;

    private Button buttonAddWordCommand;
    private TextField txtEditWord;
    private TextField txtEditCommand;

    private final TextField txtNewWord;
    private TextField txtNewCommand;

    public WordCommandSettings(Stage stageReference, ObservableList<WordCommand> wordCommandsDatabase, ListView<String> wordCommandsList)
    {
        final int dialogWidth = 700;
        final int dialogHeight = 620;
        final Stage dialog = new Stage();
        dialog.setTitle("Word Commands");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stageReference);
        BorderPane borderPane = new BorderPane();

        ObservableList<String> wordCommandItem = FXCollections.observableArrayList();
        for (WordCommand wordCommand : wordCommandsDatabase)
            wordCommandItem.add(wordCommand.word + "\t\t-->\t\t" + wordCommand.command);
        wordCommandsList.setItems(wordCommandItem);
        wordCommandsList.setOnMouseClicked(event -> {
            if(wordCommandsList.getSelectionModel().getSelectedIndex() != -1)
            {
                wordCommandIndex = wordCommandsList.getSelectionModel().getSelectedIndex();
                buttonRemoveWordCommand.setDisable(false);
                String[] strings = wordCommandsList.getItems().get(wordCommandIndex).split("\t\t-->\t\t");
                txtEditWord.setText(strings[0]);
                txtEditCommand.setText(strings[1]);
            }else
            {
                wordCommandIndex = -1;
                buttonRemoveWordCommand.setDisable(true);
                txtEditWord.setText("");
                txtEditCommand.setText("");
                buttonUpdateWordCommand.setDisable(true);
            }
        });

        buttonRemoveWordCommand = new Button("Remove Word Command");
        buttonRemoveWordCommand.setDisable(wordCommandIndex == -1);
        buttonRemoveWordCommand.setOnAction(event -> {
            if(wordCommandsList.getSelectionModel().getSelectedIndex() != -1)
            {
                wordCommandsList.getItems().remove(wordCommandIndex);
                wordCommandsDatabase.remove(wordCommandIndex);
                wordCommandIndex = wordCommandsList.getSelectionModel().getSelectedIndex();
                buttonRemoveWordCommand.setDisable(wordCommandIndex == -1);
                if(wordCommandIndex == -1)
                {
                    txtEditWord.setText("");
                    txtEditCommand.setText("");
                    buttonUpdateWordCommand.setDisable(true);
                }
                saveWordCommands(wordCommandsDatabase);
            }
        });

        Label labelNewWordCommand = new Label("\n New Word Command \n\n");
        labelNewWordCommand.setFont(Font.font("Arial", 20));
        labelNewWordCommand.setStyle("-fx-font-weight: bold");

        txtNewWord = new TextField();
        txtNewWord.setPromptText("Word/Phrase");
        txtNewWord.textProperty().addListener((observable, oldValue, newValue) ->
                buttonAddWordCommand.setDisable(txtNewWord.getText().length() == 0 ||
                        txtNewCommand.getText().length() == 0));

        txtNewCommand = new TextField();
        txtNewCommand.setPromptText("Command");
        txtNewCommand.textProperty().addListener((observable, oldValue, newValue) ->
                buttonAddWordCommand.setDisable(txtNewWord.getText().length() == 0 ||
                        txtNewCommand.getText().length() == 0));

        buttonAddWordCommand = new Button("Add Word Command");
        buttonAddWordCommand.setDisable(true);
        buttonAddWordCommand.setOnAction(event -> {
            WordCommand tempWordCommand = new WordCommand();
            tempWordCommand.word = txtNewWord.getText();
            tempWordCommand.command = txtNewCommand.getText();
            txtNewWord.setText("");
            txtNewCommand.setText("");
            String tempString =  tempWordCommand.word + "\t\t-->\t\t" +
                    tempWordCommand.command;
            wordCommandsList.getItems().add(tempString);
            wordCommandsDatabase.add(wordCommandsDatabase.size(), tempWordCommand);
            saveWordCommands(wordCommandsDatabase);
        });

        Label labelEditWordCommand = new Label("\n Edit Word Command \n\n");
        labelEditWordCommand.setFont(Font.font("Arial", 20));
        labelEditWordCommand.setStyle("-fx-font-weight: bold");

        txtEditWord = new TextField();
        txtEditWord.setPromptText("Word/Phrase");
        txtEditWord.textProperty().addListener((observable, oldValue, newValue) ->
                buttonUpdateWordCommand.setDisable(txtEditWord.getText().length() == 0 ||
                        txtEditCommand.getText().length() == 0 ||
                        wordCommandIndex == -1));

        txtEditCommand = new TextField();
        txtEditCommand.setPromptText("Command");
        txtEditCommand.textProperty().addListener((observable, oldValue, newValue) ->
                buttonUpdateWordCommand.setDisable(txtEditWord.getText().length() == 0 ||
                        txtEditCommand.getText().length() == 0 ||
                        wordCommandIndex == -1));

        buttonUpdateWordCommand = new Button("Update Word Command");
        buttonUpdateWordCommand.setDisable(true);
        buttonUpdateWordCommand.setOnAction(event -> {
            WordCommand tempWordCommand = new WordCommand();
            tempWordCommand.word = txtEditWord.getText();
            tempWordCommand.command = txtEditCommand.getText();
            txtEditWord.setText("");
            txtEditCommand.setText("");
            String tempString =  tempWordCommand.word + "\t\t-->\t\t" +
                    tempWordCommand.command;
            wordCommandsList.getItems().set(wordCommandIndex, tempString);
            wordCommandsDatabase.set(wordCommandIndex, tempWordCommand);
            saveWordCommands(wordCommandsDatabase);
        });

        StackPane stackPaneCenter = new StackPane();
        stackPaneCenter.getChildren().add(wordCommandsList);

        VBox vBoxRight = new VBox();
        vBoxRight.getChildren().add(labelNewWordCommand);
        vBoxRight.getChildren().add(txtNewWord);
        vBoxRight.getChildren().add(txtNewCommand);
        vBoxRight.getChildren().add(buttonAddWordCommand);
        vBoxRight.getChildren().add(labelEditWordCommand);
        vBoxRight.getChildren().add(txtEditWord);
        vBoxRight.getChildren().add(txtEditCommand);
        vBoxRight.getChildren().add(buttonUpdateWordCommand);
        vBoxRight.getChildren().add(buttonRemoveWordCommand);

        borderPane.setCenter(stackPaneCenter);
        borderPane.setRight(vBoxRight);
        Scene dialogScene = new Scene(borderPane, dialogWidth, dialogHeight);
        dialog.setMinWidth(borderPane.getWidth());
        dialog.setMinHeight(borderPane.getHeight());
        dialog.setResizable(true);
        dialog.setScene(dialogScene);
        dialog.show();

        try
        {
            Image icon = new Image(getClass().getResourceAsStream("../images/icon3.png"));
            dialog.getIcons().add(icon);
            System.out.println("Icon loaded from IDE...");
        }catch(Exception e)
        {
            try
            {
                Image icon = new Image("com/ViktorVano/SpeechRecognitionAI/images/icon3.png");
                dialog.getIcons().add(icon);
                System.out.println("Icon loaded from exported JAR...");
            }catch(Exception e1)
            {
                System.out.println("Icon failed to load...");
            }
        }
    }
}
