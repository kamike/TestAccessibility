package com.example.wangtao.testaccessibility;

import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */

public class Utils {
    public static String toNodeString(final AccessibilityNodeInfo node) {
        if (node == null) {
            return "node 为空";
        }
        Rect rect = new Rect();
        node.getBoundsInScreen(rect);
        if (rect.centerX() == 0 && rect.centerY() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean isClickSuccess = node.performAction(node.getActions());
                  node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                  node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    node.performAction(AccessibilityNodeInfo.ACTION_CLEAR_FOCUS);
                    node.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
                    //AccessibilityNodeInfo.ACTION_SELECT

                    AccessibilityNodeInfo parent = node.getParent();
                    LogUtils.i("点击数字是否成功：" + isClickSuccess + "," + node.isClickable() + "," + node.isLongClickable());

                    if (parent != null) {
                        boolean isParent = parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        LogUtils.i("parent点击成功：" + "isParent," + parent.getParent());
                    } else {
                        LogUtils.i("parent：===null," + node.getChildCount());
                    }
                }
            }, 5000);


        }
        return node.getText() + "," + node.getContentDescription() + "," + rect.centerX() + "," + rect.centerY() + "," + node.getClassName();
    }

    public static void toNodeString(List<AccessibilityNodeInfo> list) {
        if (list == null) {
            return;
        }
        for (AccessibilityNodeInfo node : list) {
            Rect rect = new Rect();
            node.getBoundsInScreen(rect);
            LogUtils.i("node：" + node.getText() + "," + node.getContentDescription() + "," + rect.centerX() + "," + rect.centerY() + "," + node.getClassName());
        }
    }

    /**
     * 得到node的文字描述
     *
     * @param nodeTarget
     * @return
     */
    public static String getNodeInfoString(AccessibilityNodeInfo nodeTarget) {
        if (nodeTarget == null) {
            return null;
        }
        if (TextUtils.isEmpty(nodeTarget.getText())) {
            if (TextUtils.isEmpty(nodeTarget.getContentDescription())) {
                return null;
            } else {
                return nodeTarget.getContentDescription().toString();
            }
        } else {
            return nodeTarget.getText().toString();
        }
    }
}
