package com.youyou.uumall.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.youyou.uumall.R;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.model.ShopCartBean;
import com.youyou.uumall.secure.DES;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义工具类： 方法：
 *
 * @author liuj
 */

public class MyUtils {

    public static final String SHARED_PREFERENCES_NAME = "yt";

    public static final int K = 1024;
    public static final int M = 1024 * K;
    public static final int G = 1024 * M;
    public static final long EXTRA_SPACE = 200 * M;
    public static final String YOU_TAO = "youtao";
    private static String VERSION = null;
    private static final Byte lock[] = new Byte[0];
    public static int min = 60 * 1000;
    public static int hour = 60 * min;
    public static int day = 24 * hour;

    public static final long INTERVAL = 3000L; //防止连续点击的时间间隔
    private static long lastClickTime = 0L; //上一次点击的时间


    /**
     * 保存键值对到SharedPreferences
     *
     * @param key
     * @param value
     */
    public static void savePara(Context context, String key, String value) {
        synchronized (lock) {
            String secret = key;
            if (key.length() < 8) {
                secret += YOU_TAO;
            }
            secret = secret.substring(0, 8);
            try {
                String saveValue = DES.encryptDES(value, secret);
                SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                settings.edit().putString(key, saveValue).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 根据key从SharedPreferences取对应的value
     *
     * @param key
     * @param context
     */
    public static String getPara(String key, Context context) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String secret = key;
        if (key.length() < 8) {
            secret += YOU_TAO;
        }
        secret = secret.substring(0, 8);
        try {
            String saveValue = settings.getString(key, "");
            if (TextUtils.isEmpty(saveValue)) {
                return "";
            } else {
                return DES.decryptDES(saveValue, secret);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return settings.getString(key, "");
        }
    }

    /**
     * getVersion:系统版本 <br/>
     *
     * @param context
     * @return String
     * @throws
     * @author Zaffy
     * @since JDK 1.6
     */
    public static String getVersion(Context context) {
        if (VERSION != null) {
            return VERSION;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            VERSION = pi.versionName;
            return VERSION;
        } catch (NameNotFoundException e) {
            return "0.0.0";
        }
    }

    /**
     * 判断应用是否存在
     *
     * @param context
     * @param targetPackage
     * @return
     */
    public static boolean isPackageExisted(Context context, String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    /**
     * 获取当前应用程序的版本号。
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    // return the number of bytes available on the filesystem rooted at the
    // given File
    @SuppressWarnings("deprecation")
    public static long getAvailableBytes(File root) throws Exception {
        StatFs stat = new StatFs(root.getPath());
        // put a bit of margin (in case creating the file grows the system by a
        // few blocks)
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    public static boolean isStorageUseful() {
        // 获取SdCard状态
        String state = Environment.getExternalStorageState();
        // 判断SdCard是否存在并且是可用的
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Environment.getExternalStorageDirectory().canWrite()) {
                return true;
            }
        }
        return false;
    }

    /**
     * haveEnoughSpace:是否有足够的空间 <br/>
     *
     * @param root
     * @param min
     * @return boolean
     * @throws
     * @author Zaffy
     * @since JDK 1.6
     */
    public static boolean haveEnoughSpace(File root, long min /* byte */) {
        long available;
        try {
            available = getAvailableBytes(root);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (min < EXTRA_SPACE) {
            min = EXTRA_SPACE;
        }
        return available >= min;
    }

    /**
     * 友盟添加测试设备所用
     *
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void copyToClipBoard(Context contex, String content) {
        ClipboardManager clipboard = (ClipboardManager) contex.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(content);
    }

    public static boolean filter() {
        if (lastClickTime == 0) {
            lastClickTime = System.currentTimeMillis();
            return false;
        }
        if ((System.currentTimeMillis() - lastClickTime) > INTERVAL) {
            lastClickTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }

    /**
     * 获取文件夹总大小
     *
     * @param file
     * @return
     */
    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children) {
                    size += getDirSize(f);
                }
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                return (double) file.length() / 1024 / 1024;
            }
        } else {
            return 0.0;
        }
    }

    public static Map[] insertOneGoods(String goodsId) {
        Map dataArray = new HashMap();
        dataArray.put("goodsId", goodsId);
        dataArray.put("count", 1);
        Map[] array = {dataArray};
        return array;
    }

    public static Map[] deleteAllGoods(List<ShopCartBean> mData, String tag) {
        List<ShopCartBean> list = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            ShopCartBean shopCartBean = mData.get(i);//拿到每一件商品
            if (TextUtils.equals(shopCartBean.goodsId, tag)) {
                continue;
            }
            ShopCartBean temp = new ShopCartBean();
            temp.goodsId = shopCartBean.goodsId;
            temp.count = shopCartBean.count;
            list.add(temp);
        }
        Map[] array;
        if (list.size() == 0) {
//            Map dataArray = new HashMap();
//            dataArray.put("goodsId", "");
//            dataArray.put("count", 0);
//            array = new Map[1];
//            array[0] = dataArray;
//            int[] a = {1,2};
            array = null;
        } else {
            array = new Map[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Map map = new HashMap();
                ShopCartBean shopCartBean = list.get(i);
                map.put("goodsId", shopCartBean.goodsId);
                map.put("count", shopCartBean.count);
                array[i] = map;
            }
        }
        return array;
    }

    public static Map[] deleteOneGoods(List<ShopCartBean> mData, String tag) {
        String[] goodsId = new String[mData.size()];//建立一个商品id的集合
        int[] count = new int[mData.size()];//建立一个商品数量的集合
        for (int i = 0; i < mData.size(); i++) {
            ShopCartBean shopCartBean = mData.get(i);//拿到每一件商品
            goodsId[i] = shopCartBean.goodsId; //
            count[i] = shopCartBean.count;
        }
        Map[] array = new Map[goodsId.length];
        for (int i = 0; i < goodsId.length; i++) {
            Map dataArray = new HashMap();
            dataArray.put("goodsId", goodsId[i]);
            if (TextUtils.equals(goodsId[i], tag)) {
                dataArray.put("count", count[i] - 1);
            } else {
                dataArray.put("count", count[i]);
            }
            array[i] = dataArray;
        }
        return array;
    }

    /**
     * @param data       商品id及数量，内容为JSONObject{goodsId,count}
     * @param name       取货人姓名
     * @param linkTel    联系方式
     * @param pickupTime 取货时间
     * @param address    自提点Id
     * @param remarks    备注
     * @return
     */
    public static Map orderSubmit(List<ShopCartBean> data, String name, String linkTel, String pickupTime, String address, String remarks) {
        Map order = new HashMap();
        order.put("name", name);
        order.put("linkTel", linkTel);
        order.put("pickupTime", pickupTime);
        order.put("address", address);
        order.put("remarks", remarks == null ? "" : remarks);
        Map[] goodsList = new Map[data.size()];
        for (int i = 0; i < data.size(); i++) {
            ShopCartBean shopCartBean = data.get(i);
            Map goods = new HashMap();
            goods.put("goodsId", shopCartBean.goodsId);
            goods.put("count", shopCartBean.count);
            goodsList[i] = goods;
        }
        order.put("goodsList", goodsList);
        return order;
    }

    public static String getCountryCode(Context context) {
        String countryCode = "";
        String country = MyUtils.getPara(BaseConstants.preferencesFiled.DEFAULT_COUNTRY, context);
        String dictList = MyUtils.getPara("dictList", context);
        if (!TextUtils.isEmpty(dictList)) {
            String[] split = dictList.split(";");
            for (String s : split) {
                if (s.contains(country)) {
                    countryCode = s.split(",")[1];
                }
            }
        } else {
            countryCode = "SG";// TODO: 2016/6/22 逻辑问题
        }
        return countryCode;
    }

    public static DisplayImageOptions getImageOptions() {
        return  new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.order_empty_3x)
                .showImageForEmptyUri(R.color.bg_settings_gap)
                .showImageOnFail(R.color.bg_settings_gap)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public static String formatURl(String url) {
        String decode = "";
        try {
            decode = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            MyLogger.getLogger("MyUtils").e(e.toString());
        }
        return decode;
    }

    public static void setSmsCodeAnimator(final TextView textView, final Context context) {
        textView.setEnabled(false);
        ValueAnimator animator = ValueAnimator.ofInt(30, 1);
        animator.setDuration(30000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer animatedValue = (Integer) animation.getAnimatedValue();
                textView.setText(animatedValue+"秒后重试");
                textView.setTextColor(context.getResources().getColor(R.color.font_gray));

            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setEnabled(true);
                textView.setText(context.getResources().getString(R.string.register_get_smscode));
                textView.setTextColor(context.getResources().getColor(R.color.font_register_smscode));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public static void setWebViewSetting(WebView webView ,Context context) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
//        webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = context.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        //设置数据库缓存路径
//        webView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
//        webView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
//        webView.getSettings().setAppCacheEnabled(true);
    }

    public static void clearWebViewCeche(Context context) {
        //WebView 缓存文件
        String cacheDirPath = context.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        File appCacheDir = new File(cacheDirPath);
        try {
            long folderSize = FileUtils.getFolderSize(appCacheDir);
            String s = FileUtils.formatFileSize(folderSize);
//            Log.e("haha",s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //删除webview 缓存 缓存目录
        if(appCacheDir.exists()){
            deleteFile(appCacheDir);
        }
    }
    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {

//        Log.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
//            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }

//    public static void setHeight(ImageView imageView) {
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        DisplayMetrics metric = new DisplayMetrics();
//        Activity context = (Activity) this.context;
//        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int mScreenWidth = metric.widthPixels;
//        int width = mScreenWidth/2- PixelUtil.dp2px(20);
////        int mScreenHeight = metric.heightPixels;
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, (int) (width/1.28));
//        imageView.setLayoutParams(params);
//    }
}
