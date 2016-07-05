package com.youyou.uumall.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.youyou.uumall.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class ShareDialog {
    private AlertDialog dialog;
    private GridView gridView;
    private RelativeLayout cancelButton;
    private SimpleAdapter saImageItems;
    private int[] image = {R.drawable.ssdk_oks_classic_wechat, R.drawable.ssdk_oks_classic_wechatmoments};
    private String[] name = {"微信好友", "朋友圈"};

    public ShareDialog(Context context) {
        dialog = new AlertDialog.Builder(context,R.style.dialog).create();
        dialog.show();
//        dialog.
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
//        ViewGroup content = (ViewGroup) window.findViewById(android.R.id.content);
//        content.removeAllViews();
        dialog.setContentView(R.layout.dialog_share);
        gridView = (GridView) window.findViewById(R.id.share_gridView);
        cancelButton = (RelativeLayout) window.findViewById(R.id.share_cancel);
        List<HashMap<String, Object>> shareList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < image.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", image[i]);//添加图像资源的ID
            map.put("ItemText", name[i]);//按序号做ItemText
            shareList.add(map);
        }

        saImageItems = new SimpleAdapter(context, shareList, R.layout.item_share, new String[]{"ItemImage", "ItemText"}, new int[]{R.id.item_share_iv, R.id.item_share_tv});
        gridView.setAdapter(saImageItems);
    }

    public void setCancelButtonOnClickListener(View.OnClickListener Listener) {
        cancelButton.setOnClickListener(Listener);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        gridView.setOnItemClickListener(listener);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }
}
