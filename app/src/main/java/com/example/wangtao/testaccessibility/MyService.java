package com.example.wangtao.testaccessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.Instrumentation;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Message;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.LinkedHashSet;

/**
 * Created by wangtao on 2017/9/5.
 */

public class MyService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        times=0;
        LogUtils.i("onServiceConnected===========");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        LinkedHashSet<AccessibilityNodeInfo> listSource = new LinkedHashSet<>();
        LinkedHashSet<AccessibilityNodeInfo> listRoot = new LinkedHashSet<>();
        AddAllToListSource(listSource, event.getSource());
        AddAllToListSource(listRoot, getRootInActiveWindow());


        for (AccessibilityNodeInfo info : listSource) {
            LogUtils.i("标签：" + Utils.toNodeString(info));
        }
        for (AccessibilityNodeInfo info : listRoot) {
            LogUtils.i("window：" + Utils.toNodeString(info));
        }
//        handler.sendEmptyMessageDelayed(0,3000);

    }
    private int times=0;
    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(times>=10){
                return;
            }
            inputHello("1");
            Instrumentation m_Instrumentation = new Instrumentation();
            m_Instrumentation.sendKeyDownUpSync( KeyEvent.KEYCODE_B );
            times++;
        }
    };

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

    public void findText(){

    }
    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        //接收按键事件

        return super.onKeyEvent(event);
    }
}
