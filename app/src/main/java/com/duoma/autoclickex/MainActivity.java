package com.duoma.autoclickex;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.duoma.autoclickex.floatviewfinal.MyConstance;
import com.duoma.autoclickex.floatviewfinal.service.FloatViewService;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import static com.duoma.autoclickex.EXApplication.Rows;
import static com.duoma.autoclickex.EXApplication.longX1;
import static com.duoma.autoclickex.EXApplication.longX2;
import static com.duoma.autoclickex.EXApplication.longX3;
import static com.duoma.autoclickex.EXApplication.screenX;
import static com.duoma.autoclickex.EXApplication.sheet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AUTOCLICKEX";
    private Button btn_start;
    private boolean isClick;


    private MyThread myThread;

    private FloatViewService mFloatViewService;
    private NotificationManager manager;
    private ControReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initReceiver();

        initUI();
    }

    private void initReceiver() {

        //service注册广播，添加多个过滤条件
        receiver = new ControReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyConstance.ACTION_START);
        filter.addAction(MyConstance.ACTION_STOP);

        registerReceiver(receiver, filter);
    }

    private void initUI() {
        btn_start = (Button) findViewById(R.id.main_btn_start);
        btn_start.setOnClickListener(this);

        TextView tv_1 = (TextView) findViewById(R.id.tv_1);
        TextView tv_2 = (TextView) findViewById(R.id.tv_2);

        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = screenWidth = display.getWidth();
        int screenHeight = screenHeight = display.getHeight();
        screenX = screenWidth / 720f;
        tv_1.setText("当前设备分辨率为：" + screenWidth + "x" + screenHeight);

        Workbook book = null;
        try {
//            InputStream is = new FileInputStream("mnt/sdcard/user.xls");
            File file = new File(getSDPath() + "/user.xls");
            InputStream is = new FileInputStream(file);
            book = Workbook.getWorkbook(is);
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }
        if (book != null) {
            Log.e("工作表数量", "" + book.getNumberOfSheets());
            // 获得第一个工作表对象
            sheet = book.getSheet(0);
            Rows = sheet.getRows();//总行数
            int Cols = sheet.getColumns();//总列数
            tv_2.setText("获取的Excel工作表的信息：" + "行数为：" + Rows + "  " + "列数为：" + Cols);
        } else {

        }

        try {
            Intent intent = new Intent(this, FloatViewService.class);
            startService(intent);
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_start:
                if (!isClick) {
                    isClick = true;
                    btn_start.setText("关闭");

                    startAutoClick();
                } else {
                    isClick = false;
                    btn_start.setText("启动");

                    stopAutoClick();
                }

                break;
        }
    }

    private void startAutoClick() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);//点击HOME键
//        startActivity(intent);

        execShellCmd("input keyevent 3");//hong

        showNotification();

//        myThread = new MyThread();
//        myThread.start();

//        showFloatingView();
    }

    private void stopAutoClick() {

        if (manager != null) {
            // 清除id为NOTIFICATION_FLAG的通知
            manager.cancel(NOTIFICATION_FLAG);
        }

//        myThread.interrupt();
//        myThread.stopThread();

//        hideFloatingView();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Toast toast = Toast.makeText(MainActivity.this, "" + msg.what, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    };

    class MyThread extends Thread {
        private boolean _run = true;

        void stopThread() {
            _run = false;
        }

        @Override
        public void run() {
            while (_run) {
                repaint();
            }
        }

        private void repaint() {
            try {
//                execShellCmd("input keyevent 3");//hong

                /*for (int i = 5; i > 0; i--) {
                    Message msg = new Message();
                    msg.what = i;
                    mHandler.sendMessage(msg);

                    Thread.sleep(1000);
                }*/

                for (int i = 1; i <= Rows; i++) {
                    String phone = (sheet.getCell(0, 1)).getContents();
                    String password = (sheet.getCell(1, 1)).getContents();
                    Log.e("phone", "phone:" + phone + "password:" + password);

                    Thread.sleep(longX2);
                    Log.e(TAG, "点击账号");
                    execShellCmd(getString(600, 340));//账号
//                    Thread.sleep(longX2);
                    /*for (int k = 0; k < 11; k++) {
                        execShellCmd("input keyevent 67");//删除键
                    }*/
//                    Thread.sleep(longX2);
//                    execShellCmd("input tap 600 340");//账号
                    Thread.sleep(longX2);
                    Log.e(TAG, "黏贴账号");
                    execShellCmd("input text " + phone);
                    Thread.sleep(longX1);
                    Log.e(TAG, "点击密码");
                    execShellCmd(getString(600, 450));//密码
//                    Thread.sleep(longX2);
                    /*for (int k = 0; k < 21; k++) {
                        execShellCmd("input keyevent 67");//删除键
                    }*/
//                    Thread.sleep(longX2);
//                    execShellCmd("input tap 600 450");//密码
                    Thread.sleep(longX2);
                    Log.e(TAG, "黏贴密码");
                    execShellCmd("input text " + password);
                    Thread.sleep(longX1);
                    Log.e(TAG, "点击登录");
                    execShellCmd(getString(600, 690));//登录

                    Thread.sleep(longX3);//3000太低
                    Log.e(TAG, "点击我");
                    execShellCmd(getString(645, 1212));//我

                    Thread.sleep(longX2);
                    Log.e(TAG, "点击设置");
                    execShellCmd(getString(686, 93));//设置
                    Thread.sleep(longX2);
//                    execShellCmd(getSwipe(350, 1000, 350, 300));///滑动
                    execShellCmd("input keyevent 20");///滑动
                    execShellCmd("input keyevent 20");///滑动
                    execShellCmd("input keyevent 20");///滑动
                    execShellCmd("input keyevent 20");///滑动
                    execShellCmd("input keyevent 20");///滑动
                    execShellCmd("input keyevent 20");///滑动
                    Thread.sleep(longX3);
                    Log.e(TAG, "点击退出");
                    execShellCmd(getString(360, 980));//退出
                    Thread.sleep(longX1);
                }

                Toast toast = Toast.makeText(MainActivity.this, "执行完毕", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getString(int x, int y) {
        int x1 = (int) (x * screenX);
        int y1 = (int) (y * screenX);
        return "input tap " + x1 + " " + y1;
    }

    private String getSwipe(int x, int y, int a, int b) {
        int x1 = (int) (x * screenX);
        int y1 = (int) (y * screenX);
        int a1 = (int) (a * screenX);
        int b1 = (int) (b * screenX);
        return "input swipe " + x1 + " " + y1 + " " + a1 + " " + b1;
    }

    private void sleepTime(int number) {
        TimerTask task = new TimerTask() {
            public void run() {

            }
        };
        Timer timer = new Timer();
        timer.schedule(task, number);
    }

    /**
     * 此方法只是关闭软键盘
     */
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 执行shell命令
     */
    private DataOutputStream dataOutputStream;

    private void execShellCmd(String cmd) {
        try {
            if (dataOutputStream == null) {
                Process process = Runtime.getRuntime().exec("su");
                OutputStream outputStream = process.getOutputStream();
                dataOutputStream = new DataOutputStream(outputStream);
            }

            dataOutputStream.writeBytes(cmd + "\n");
            dataOutputStream.flush();
//            dataOutputStream.close();
//            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        destroy();
        if (manager != null) {
            // 清除id为NOTIFICATION_FLAG的通知
            manager.cancel(NOTIFICATION_FLAG);
        }

        if (receiver != null) {
            unregisterReceiver(receiver);
        }

        super.onDestroy();
    }

    /**
     * 显示悬浮图标
     */
    public void showFloatingView() {
        if (mFloatViewService != null) {
            mFloatViewService.showFloat();
        }
    }

    /**
     * 隐藏悬浮图标
     */
    public void hideFloatingView() {
        if (mFloatViewService != null) {
            mFloatViewService.hideFloat();
        }
    }

    /**
     * 释放PJSDK数据
     */
    public void destroy() {
        try {
            stopService(new Intent(this, FloatViewService.class));
            unbindService(mServiceConnection);
        } catch (Exception e) {
        }
    }

    /**
     * 连接到Service
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mFloatViewService = ((FloatViewService.FloatViewServiceBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mFloatViewService = null;
        }
    };

    private int NOTIFICATION_FLAG = 1;

    private void showNotification() {

        //第一步：获取状态通知栏管理
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //第二步：实例化通知栏构造器NotificationCompat.Builder
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        //第三步：设置通知栏PendingIntent（点击动作事件等都包含在这里）
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        /**
         * 自定义布局 开始
         */
        //自定义布局
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_control);
        mRemoteViews.setImageViewResource(R.id.notifi_img_ic, R.mipmap.ic_launcher);
        mRemoteViews.setImageViewResource(R.id.notifi_img_start, R.drawable.ic_start);
        mRemoteViews.setImageViewResource(R.id.notifi_img_stop, R.drawable.ic_stop);

        //设置点击事件延时意图
        Intent startIntent = new Intent(MyConstance.ACTION_START);
        PendingIntent pendingStart = PendingIntent.getBroadcast(this, 0, startIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notifi_img_start, pendingStart);

        Intent stopIntent = new Intent(MyConstance.ACTION_STOP);
        PendingIntent pendingStop = PendingIntent.getBroadcast(this, 0, stopIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notifi_img_stop, pendingStop);
        /**
         * 自定义布局 结束
         */

        //第四步：对Builder进行配置：
        mBuilder.setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                .setTicker("开启模拟登录")// 设置在status bar上显示的提示文字
//                .setContentTitle("Notification Title")// 设置在下拉status
//                .setContentText("This is the notification message")// TextView中显示的详细内容
                .setPriority(Notification.PRIORITY_MAX)//设置置顶
                .setContent(mRemoteViews)
                .setContentIntent(pendingIntent);//设置通知栏点击意图

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;//不被清除
//        notification.bigContentView = mRemoteViews;//设置Notification高度为布局高度

        //第五步：发送通知请求
        manager.notify(NOTIFICATION_FLAG, notification);
    }

    class ControReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case MyConstance.ACTION_START:
                    execShellCmd("input keyevent 4");//back

                    TimerTask task = new TimerTask() {
                        public void run() {
                            myThread = new MyThread();
                            myThread.start();
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 1000);

                    break;

                case MyConstance.ACTION_STOP:
                    execShellCmd("input keyevent 4");//back

                    myThread.interrupt();
                    myThread.stopThread();
                    break;

            }
        }
    }
}