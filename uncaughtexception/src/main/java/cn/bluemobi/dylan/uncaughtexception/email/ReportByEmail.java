package cn.bluemobi.dylan.uncaughtexception.email;

import android.util.Log;

import cn.bluemobi.dylan.uncaughtexception.CustomActivityOnCrash;


public class ReportByEmail {
    public static boolean sendEmail2(String title, String reportContent) {
        MyMail m = new MyMail("android2dev@163.com", "android");
        m.set_host("smtp.163.com");
        m.set_port("25");
        m.set_debuggable(true);
        String[] toArr = CustomActivityOnCrash.getEmailTo();
        if ((toArr == null) || (toArr.length <= 0)) {
            return false;
//       toArr = new String[] {"13468857714@qq.com" };
        }
        m.set_to(toArr);
        m.set_from("android2dev@163.com");
        m.set_subject("【" + title + "】异常崩溃");
        m.setBody(reportContent.toString());
        try {
            if (m.send()) {
                Log.i(title, "邮件发送成功");
                return true;
            }

            Log.i(title, "邮件发送失败");
            return false;
        } catch (Exception e) {
            Log.e(title, "邮件发送异常", e);
        }
        return false;
    }
}

