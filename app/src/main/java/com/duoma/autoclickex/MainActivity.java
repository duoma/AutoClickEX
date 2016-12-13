package com.duoma.autoclickex;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AUTOCLICKEX";
    private Button btn_start;
    private boolean isClick;
    private Sheet sheet;
    private int Rows;
    private float screenX;

    private int longX1 = 1500;
    private int longX2 = 2000;
    private int longX3 = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initUI();
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
        MyThread.start();
    }

    private void stopAutoClick() {
        MyThread.stop();
        System.exit(0);
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

    private Thread MyThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                execShellCmd("input keyevent 3");//hong

                for (int i = 5; i > 0; i--) {
                    Message msg = new Message();
                    msg.what = i;
                    mHandler.sendMessage(msg);

                    Thread.sleep(1000);
                }

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
    });

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
}
