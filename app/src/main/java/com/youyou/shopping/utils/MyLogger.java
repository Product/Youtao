
package com.youyou.shopping.utils;


import android.util.Log;


import com.youyou.shopping.base.BaseConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

/**
 * MyLogger
 *
 * @author Zaffy
 * @version 1.1
 */
public class MyLogger {

    private final static boolean sIsLoggerEnable = true;//log类可用
    private final static String tag = "[xp]";//TODO tag要改
    public static int logLevel = Log.VERBOSE;//log类型
    private static Hashtable<String, MyLogger> sLoggerTable;//log存放集合
    private PrintWriter pw = null;

    private static boolean logFlag = true; //禁用log ，禁用false ，启用true
    private static boolean logWriteToFile = true;//log写入文件

    static {//构造代码块做初始化的操作
        sLoggerTable = new Hashtable<String, MyLogger>();
    }

    private String mClassName;
    //
    public static MyLogger getLogger(String className) {
        MyLogger classLogger = sLoggerTable.get(className);
        //讲log统一管理在集合中
        if (classLogger == null) {
            classLogger = new MyLogger(className);
            sLoggerTable.put(className, classLogger);
        }
        return classLogger;
    }
    //私有构造
    private MyLogger(String name) {
        mClassName = name;
    }
    //获取到了方法名
    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();

        if (sts == null) {
            return null;
        }

        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {//自然方法,跳出
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {//
                continue;
            }

            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            //最后返回的是线程名,方法名,行号
            return "[ " + Thread.currentThread().getName() + ": "
                    + st.getFileName() + ":" + st.getLineNumber() + " ]";
        }

        return null;
    }
    //打印信息的方法,有开关,可以写到本地
    public void info(Object str) {
        if (!logFlag)
            return;
        if (logWriteToFile) {
            writeLogToFile(str);
        }
        if (logLevel <= Log.INFO) {
            String name = getFunctionName();
            if (name != null) {
                Log.i(tag, name + " - " + str);
            } else {
                Log.i(tag, str.toString());
            }
        }
    }

    public void i(Object str) {
        if (!logFlag)
            return;
        if (logWriteToFile) {
            writeLogToFile(str);
        }
        info(str);
    }

    public void verbose(Object str) {
        if (!logFlag)
            return;
        if (logLevel <= Log.VERBOSE) {
            String name = getFunctionName();
            if (name != null) {
                Log.v(tag, name + " - " + str);
            } else {
                Log.v(tag, str.toString());
            }

        }
    }

    public void v(Object str) {
        if (!logFlag)
            return;
        if (logWriteToFile) {
            writeLogToFile(str);
        }
        verbose(str);
    }

    public void warn(Object str) {
        if (!logFlag)
            return;
        if (logLevel <= Log.WARN) {
            String name = getFunctionName();

            if (name != null) {
                Log.w(tag, name + " - " + str);
            } else {
                Log.w(tag, str.toString());
            }

        }
    }

    public void w(Object str) {
        if (!logFlag)
            return;
        if (logWriteToFile) {
            writeLogToFile(str);
        }
        warn(str);
    }

    public void error(Object str) {
        if (!logFlag)
            return;
        if (logLevel <= Log.ERROR) {

            String name = getFunctionName();
            if (name != null) {
                Log.e(tag, name + " - " + str);
            } else {
                Log.e(tag, str.toString());
            }
        }
    }

    public void error(Exception ex) {
        if (!logFlag)
            return;
        if (logLevel <= Log.ERROR) {
            Log.e(tag, "error", ex);
        }
    }

    public void e(Object str) {
        if (!logFlag)
            return;
        if (logWriteToFile) {
            writeLogToFile(str);
        }
        error(str);
    }

    public void e(Exception ex) {
        if (!logFlag)
            return;
        if (logWriteToFile) {
            writeLogToFile(ex);
        }
        error(ex);
    }

    public void e(String log, Throwable tr) {
        if (!logFlag)
            return;
        String line = getFunctionName();
        if (sIsLoggerEnable) {
            Log.e(tag, "{Thread:" + Thread.currentThread().getName() + "}"
                    + "[" + mClassName + line + ":] " + log + "\n", tr);
            if (logWriteToFile) {
                writeLogToFile("{Thread:" + Thread.currentThread().getName()
                        + "}" + "[" + mClassName + line + ":] " + log + "\n"
                        + Log.getStackTraceString(tr));
            }
        }
    }

    public void debug(Object str) {
        if (!logFlag)
            return;
        if (logLevel <= Log.DEBUG) {
            String name = getFunctionName();
            if (name != null) {
                Log.d(tag, name + " - " + str);
            } else {
                Log.d(tag, str.toString());
            }

        }
    }

    public void d(Object str) {
        if (!logFlag)
            return;
        if (logWriteToFile) {
            writeLogToFile(str);
        }
        debug(str);
    }

    private void writeLogToFile(Object str) {
        File fil = new File(BaseConstants.path.LOG_DIR + File.separator + "log" + getFormatDate("yyyy-MM-dd") + ".txt");
        if (!fil.exists()) {
            try {
                fil.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            pw = new PrintWriter(new FileOutputStream(fil, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (pw != null) {
            pw.print(str + "\r\n");
            pw.flush();
        }
    }

    private String getFormatDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINESE);
        return sdf.format(new Date());
    }
}
