package com.bigfire.easychat;

import com.bigfire.easychat.util.Storage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  18:36
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("EasyChat0.3.0");
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon/wx.png").toString()));

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/LoginLayout.fxml"));
        Scene mainScene = new Scene(root, 504, 400);
        mainScene.setRoot(root);
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        Storage.stageViews.put("loginStage",primaryStage);
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
