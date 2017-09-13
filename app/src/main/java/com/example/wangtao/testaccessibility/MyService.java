package com.example.wangtao.testaccessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Path;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import com.blankj.utilcode.util.ScreenUtils;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by wangtao on 2017/9/5.
 */

public class MyService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtils.i("onServiceConnected===========");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!TextUtils.equals(event.getClassName(), "com.tencent.mm.plugin.wallet.pay.ui.WalletPayUI")) {
            return;
        }
        LogUtils.i("=========" + event.getClassName());
        LinkedHashSet<AccessibilityNodeInfo> listSource = new LinkedHashSet<>();
        LinkedHashSet<AccessibilityNodeInfo> listRoot = new LinkedHashSet<>();
        AccessibilityNodeInfo rootWindow = getRootInActiveWindow();
        AddAllToListSource(listSource, event.getSource());
        AddAllToListSource(listRoot, rootWindow);
        AccessibilityNodeInfo source = event.getSource();

        if (rootWindow != null) {
            List<AccessibilityNodeInfo> listSearch = rootWindow.findAccessibilityNodeInfosByText("2");
            LogUtils.i("====查找到多少条：" + listSearch);
            for (AccessibilityNodeInfo info : listSearch) {
                LogUtils.i("====root:" + info.toString());
                boolean isSUccess = info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                AccessibilityNodeInfo parent = info.getParent();

                LogUtils.i("==isSUccess:" + isSUccess);
                while (parent != null) {
                    if (parent.isClickable()) {
                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        break;
                    }
                    parent = parent.getParent();
                }


            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            List<AccessibilityWindowInfo> list = getWindows();
            for (AccessibilityWindowInfo info : list) {

                LogUtils.i("====window:" + info.toString());
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

            GestureDescription.Builder builder = new GestureDescription.Builder();
            Path path = new Path();
            path.lineTo(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()*0.8f);
            builder.addStroke(new GestureDescription.StrokeDescription(path, 10, 30));
            boolean isDispatched = dispatchGesture(builder.build(), new GestureResultCallback() {
                @Override
                public void onCompleted(GestureDescription gestureDescription) {
                    super.onCompleted(gestureDescription);
                    LogUtils.i("======点击回调==" + gestureDescription);
                }
            }, null);
            LogUtils.i("=====手势点击：" + isDispatched);
        }
    }


    private void AddAllToListSource(LinkedHashSet<AccessibilityNodeInfo> lsit, AccessibilityNodeInfo node) {
        if (node == null) {
            return;
        }
        for (int index = 0; index < node.getChildCount(); index++) {
            AccessibilityNodeInfo nodeChild = node.getChild(index);
//            if (isCheckNode(nodeChild)) {

            lsit.add(nodeChild);
//            }
            AddAllToListSource(lsit, nodeChild);
        }
    }


    @Override
    public void onInterrupt() {

    }

    private void inputHello(String text) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        //找到当前获取焦点的view
        AccessibilityNodeInfo target = nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (target == null) {
            LogUtils.i("input: null====");
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            target.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }

    public void findText() {

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        //接收按键事件
        LogUtils.i("======" + event);
        return super.onKeyEvent(event);
    }

}
