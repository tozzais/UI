package com.gc.ui.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CommonUtils {
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */

        String telRegex = "[1]\\d{10}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    public static void callPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    private static String ROOT_PATH;



    public static void callKeFu(Context context,String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context.startActivity(intent);
    }

    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        } else if (birthDay.getTime() > cal.getTimeInMillis()) {
            return -1;
        }


        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        System.out.println("age:" + age);
        return age;
    }

    public static String getRankTimes(String timeStr) {
        try {
            BigDecimal bigDecimal = new BigDecimal(timeStr);
            timeStr = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).toString();

            String hours = new SimpleDateFormat("HH:mm:ss").format(Long.parseLong(timeStr.substring(0, timeStr.length() - 4)) * 1000);
            String ms = timeStr.substring(timeStr.length() - 3, timeStr.length());
            return hours + "(" + ms + ")";

        } catch (Exception e) {
            e.printStackTrace();
            return "0:0:0(000)";
        }
    }

    public static String getPriceDecimal(String priceStr) {
        try {
            BigDecimal bigDecimal = new BigDecimal(priceStr);
            priceStr = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();

            String ms = priceStr.substring(priceStr.length() - 2, priceStr.length());
            return ms;

        } catch (Exception e) {
            e.printStackTrace();
            return "00";
        }
    }

    public static String getEncryPhone(String phone) {
        if (!isMobileNO(phone)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(phone);
        sb.replace(3, 3 + 4, "****");
        return sb.toString();
    }






}
