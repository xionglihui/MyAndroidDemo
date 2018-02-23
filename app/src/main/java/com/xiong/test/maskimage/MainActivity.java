package com.xiong.test.maskimage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author xionglh
 * @version MainActivity Created by xionglh on 2018/2/23 10:40 v1.0.0
 */
public class MainActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Maskimage txtImg = (Maskimage) findViewById(R.id.mimg_main_show);
        Integer[] imgs = {R.mipmap.icon_rongzu_fanxian, R.mipmap.icon_rongzu_baoxiang};
        txtImg.setImageResoucre(imgs);
        findViewById(R.id.btn_onlicl_agin).setOnClickListener(t -> txtImg.setImageResoucre(imgs));
    }


}
