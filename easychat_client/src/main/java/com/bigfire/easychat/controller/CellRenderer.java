package com.bigfire.easychat.controller;

import com.alibaba.fastjson.JSONObject;
import com.bigfire.easychat.entity.User;
import com.bigfire.easychat.util.StrUtil;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/25  10:35
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class CellRenderer extends ListCell<User> {
    @Override
    public void updateItem(User user, boolean empty){
        super.updateItem(user, empty);
        setGraphic(null);
        setText(null);
        if (user!=null){
            String username = user.getUsername();
            String fileName = user.getHeadUrl();
//            System.out.println(username+":"+fileName);
            HBox hBox = new HBox();
//            Hlink
            Hyperlink hyperlink = new Hyperlink();
            Text name = new Text(username);
            ImageView pictureImageView = new ImageView();
            Image image = new Image(getClass().getClassLoader().getResource("images/hzw/" + fileName).toString(),50,50,true,true);
            pictureImageView.setImage(image);
            hBox.getChildren().addAll(pictureImageView, name);
            hBox.setAlignment(Pos.CENTER_LEFT);
            setGraphic(hBox);
        }
    }

}
