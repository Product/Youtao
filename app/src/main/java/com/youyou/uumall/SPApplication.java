package com.youyou.uumall;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.youyou.uumall.base.BaseConstants;
import com.youyou.uumall.manager.LocationManager_;
import com.youyou.uumall.system.CrashHandler_;
import com.youyou.uumall.utils.MyLogger;
import com.youyou.uumall.utils.MyUtils;
import com.youyou.uumall.utils.ScreenUtils;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class SPApplication extends MultiDexApplication {

    private static SPApplication mInstance;
    private Context currentContext;
    private MyLogger log = MyLogger.getLogger("SPApplication");

    public static SPApplication getInstance() {
        return mInstance;
    }

    /**
     * 设置当前context的句柄
     */
    public void setCurrentContext(Context currentContext) {
        this.currentContext = currentContext;
    }

    public Context getCurrentContext() {
        return currentContext;
    }

    /**
     * 完全退出
     */
    public void exit() {
        LocationManager_.getInstance_(this).stopLocation();
        MobclickAgent.onKillProcess(this);
        System.exit(0);
    }

    @Override
    public void onCreate() {
        //MultiDex 支持 65535 方法数量限制
        MultiDex.install(getApplicationContext());
        super.onCreate(); // 注意：不能删，否则会崩。from baiduPush
        mInstance = this;

        log.d("Application Created");
        initDirInThread();
        CrashHandler_.getInstance_(this).init(getApplicationContext());

        initImageLoader();
        initUmeng();
        initFresco();
        //    initGlide();//初始化
    }

    private void initFresco(){
        Fresco.initialize(mInstance);
    }

    private void initDirInThread() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    String[] dirs = {BaseConstants.path.FILE_DIR,
                            BaseConstants.path.IMAGE_DIR, BaseConstants.path.LOG_DIR,
                            BaseConstants.path.PHOTO_DIR, BaseConstants.path.STICKER_DIR};
                    File dir;
                    for (String path : dirs) {
                        dir = new File(path);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                    }
                }

            }
        }).start();
    }

    private void initUmeng() {
        MobclickAgent.setDebugMode(true); //开启调试模式，在发布版本时应关闭日志
        MobclickAgent.setCatchUncaughtExceptions(false); //关闭友盟的错误统计功能，通过CrashHandler上传错误日志
        log.e("umeng添加设备需要：" + MyUtils.getDeviceInfo(this));

        PushAgent mPushAgent = PushAgent.getInstance(this);
        //开启推送并设置注册的回调处理
        mPushAgent.enable();
        String device_token = UmengRegistrar.getRegistrationId(this);
        MyUtils.savePara(this,BaseConstants.preferencesFiled.DEVICE_TOKEN,device_token);
    }

    /**
     * 根据pID获取App的包名
     *
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private void initImageLoader(){
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, BaseConstants.path.IMAGE_DIR);
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 6;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this)) // maxwidth, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                //      .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(3 * 1024 * 1024)
                .diskCacheSize(cacheSize)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .build();//开始构建
        ImageLoader.getInstance().init(config);
    }

//    private void initGlide() {
//        if (!Glide.isSetup()) {
//            int maxMemory = (int) Runtime.getRuntime().maxMemory();
//            int cacheSize = maxMemory / 6;
//            GlideBuilder glideBuilder = new GlideBuilder(this);
//            glideBuilder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888); //暂时不要求画质
//            // 设置图片内存缓存大小为程序最大可用内存的1/4
//            glideBuilder.setMemoryCache(new LruResourceCache(cacheSize));
//            glideBuilder.setBitmapPool(new LruBitmapPool(cacheSize));
//            // 设置图片磁盘缓存大小100M
//            DiskCache dlw = DiskLruCacheWrapper.get(new File(BaseConstants.path.IMAGE_DIR), 100 * 1024 * 1024);
//            glideBuilder.setDiskCache(dlw);
//            Glide.setup(glideBuilder);
//        } else {
//            log.e("初始化Glide设置 失败~");
//        }
//    }
}
