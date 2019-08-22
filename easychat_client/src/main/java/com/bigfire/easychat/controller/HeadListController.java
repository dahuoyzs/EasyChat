package com.bigfire.easychat.controller;

import com.bigfire.easychat.util.Storage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/21  19:06
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */

public class HeadListController implements Initializable {
    @FXML ImageView hzwa;
    @FXML ImageView hzwb;
    @FXML ImageView hzwc;
    @FXML ImageView hzwd;
    @FXML ImageView hzwe;
    @FXML ImageView hzwf;
    @FXML ImageView hzwg;
    @FXML ImageView hzwh;
    @FXML ImageView hzwi;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoginController loginController = (LoginController) Storage.controllers.get("loginController");
        hzwa.setOnMouseClicked(e ->loginController.changeHead(hzwa.getImage()));
        hzwb.setOnMouseClicked(e ->loginController.changeHead(hzwb.getImage()));
        hzwc.setOnMouseClicked(e ->loginController.changeHead(hzwc.getImage()));

        hzwd.setOnMouseClicked(e ->loginController.changeHead(hzwd.getImage()));
        hzwe.setOnMouseClicked(e ->loginController.changeHead(hzwe.getImage()));
        hzwf.setOnMouseClicked(e ->loginController.changeHead(hzwf.getImage()));

        hzwg.setOnMouseClicked(e ->loginController.changeHead(hzwg.getImage()));
        hzwh.setOnMouseClicked(e ->loginController.changeHead(hzwh.getImage()));
        hzwi.setOnMouseClicked(e ->loginController.changeHead(hzwi.getImage()));
    }
}
