package com.tozzais.baselibrary.util.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tozzais.baselibrary.R;


/**
 * 自定义toast
 *
 * @author dongsy
 * @version 创建时间：2015年10月23日 下午12:08:52
 */
public class ToastCommom {

    private static ToastCommom toastCommom;
    private static Toast toast;


    private ToastCommom() {
    }

    public static ToastCommom createToastConfig() {
        if (toastCommom == null) {
            toastCommom = new ToastCommom();
        }
        return toastCommom;
    }

    public void ToastShow(Context context, String tvString) {
        View layout = LayoutInflater.from(context).inflate(R.layout.base_toast_dialog, null);
        TextView text = layout.findViewById(R.id.toast_des);
        text.setText(tvString);
        if (toast == null){
            toast = new Toast(context.getApplicationContext());
        }else {
            //解决连续点击不显示bug
            toast.cancel();
            toast = new Toast(context.getApplicationContext());
        }
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
