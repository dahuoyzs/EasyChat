package com.bigfire.easychat.testui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  21:30
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class TitledPaneSample extends Application {

    final String[] imageNames = new String[]{"Apples", "Flowers", "Leaves"};
    final Image[] images = new Image[imageNames.length];
    final ImageView[] pics = new ImageView[imageNames.length];
    final TitledPane[] tps = new TitledPane[imageNames.length];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("TitledPane");
        Scene scene = new Scene(new Group(), 80, 180);
        final Accordion accordion = new Accordion();
//        for (int i = 0; i < imageNames.length; i++) {
//            images[i] = icon Image(getClass().getResourceAsStream(imageNames[i] + ".jpg"));
//            pics[i] = icon ImageView(images[i]);
//            tps[i] = icon TitledPane(imageNames[i], pics[i]);
//        }

        accordion.getPanes().addAll(tps);
        accordion.setExpandedPane(tps[0]);

        Group root = (Group) scene.getRoot();
        root.getChildren().add(accordion);
        stage.setScene(scene);
        stage.show();
    }
}
