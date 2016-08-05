package com.example.dllo.tomatotodo.main;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.countdowndetail.CompleteTimerActivity;
import com.example.dllo.tomatotodo.countdowndetail.CountdownDetailActivity;
import com.example.dllo.tomatotodo.history.HistoryFragment;
import com.example.dllo.tomatotodo.potatolist.PotatoListFragment;
import com.example.dllo.tomatotodo.potatolist.activity.PotatoListDetailActivity;
import com.example.dllo.tomatotodo.preferences.PreferencesActivity;
import com.example.dllo.tomatotodo.service.CountDownEvent;
import com.example.dllo.tomatotodo.service.TomatoService;
import com.example.dllo.tomatotodo.statistics.StatisticsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private TabLayout mTabLayout;
    private MainAdapter mAdapter;
    private ArrayList<Fragment> mFragmentsList;
    private ViewPager mViewPager;
    private TextView mTitleTimer;
    private CheckBox mStartCb;
    private ImageView mAcceptBtn;
    private ServiceConnection mServiceConnection;
    private TomatoService.MyBinder myBinder;
    private RelativeLayout mRelativeLayout;
    private NotificationManager notificationManager;
    private boolean isShowing = false;
    int allHeight;
    private boolean isActive;
    private ImageView mPopIv;
    private PopupWindow mPopupWindow;
    private int pos = 0;

    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public int initView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        mTabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        mAdapter = new MainAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mTitleTimer = (TextView) findViewById(R.id.title_timer);
        mStartCb = (CheckBox) findViewById(R.id.title_action_checkbox);
        mAcceptBtn = (ImageView) findViewById(R.id.title_action_accept);
        mPopIv = (ImageView) findViewById(R.id.title_action_pop);
        mFragmentsList = new ArrayList<>();
        mFragmentsList.add(new PotatoListFragment());
        mFragmentsList.add(new HistoryFragment());
        mFragmentsList.add(new StatisticsFragment());
        mAdapter.setFragments(mFragmentsList);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTitleTimer.setOnClickListener(this);

        mPopIv.setOnClickListener(this);

        mAcceptBtn.setOnClickListener(this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_title);
        getHeight();
        // 跳转到土豆详情列表
        findViewById(R.id.activity_intent_historylist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PotatoListDetailActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });

        // 绑定服务
        Intent serviceIntent = new Intent(this, TomatoService.class);
        startService(serviceIntent);
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (TomatoService.MyBinder) service;
                if (myBinder.isTick()) {
                    mStartCb.setChecked(true);
                } else {
                    if (myBinder.isWorkFinish()) {
                        setTitleTimer(new CountDownEvent(-1));
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(serviceIntent, mServiceConnection, BIND_AUTO_CREATE);

        mStartCb.setOnCheckedChangeListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
        int workTime = sharedPreferences.getInt("workTime", 25);
        mTitleTimer.setText(workTime + ":00");

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                Log.d("pos", pos + "```");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Subscribe
    public void setTitleTimer(CountDownEvent countDownEvent) {
        if (countDownEvent.getMillisUntilFinished() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
            String time = simpleDateFormat.format(new Date(countDownEvent.getMillisUntilFinished()));
            if (myBinder.isRest()) {
                mTitleTimer.setTextColor(Color.GREEN);
                mAcceptBtn.setVisibility(View.GONE);
                mStartCb.setVisibility(View.VISIBLE);
            } else {
                mTitleTimer.setTextColor(Color.BLACK);
            }
            mTitleTimer.setText(time);
            mStartCb.setChecked(true);
        } else if (countDownEvent.getMillisUntilFinished() == -1) { // 工作结束
            mTitleTimer.setText("番茄已完成");
            mAcceptBtn.setVisibility(View.VISIBLE);
            mStartCb.setVisibility(View.GONE);
        }
        if (countDownEvent.getMillisUntilFinished() == 0) { // 休息结束
            SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
            int workTime = sharedPreferences.getInt("workTime", 25);
            mTitleTimer.setText(workTime + ":00");
            mTitleTimer.setTextColor(Color.BLACK);
            isShowing = true;
            mStartCb.setChecked(false);
            isShowing = false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isActive) {
            if (myBinder.isTick()) {
                mStartCb.setChecked(true);
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
                int workTime = sharedPreferences.getInt("workTime", 25);
                mTitleTimer.setText(workTime + ":00");
                mTitleTimer.setTextColor(Color.BLACK);
                isShowing = true;
                mStartCb.setChecked(false);
                isShowing = false;
            }
            if (myBinder.isWorkFinish()) {
                setTitleTimer(new CountDownEvent(-1));
            }
        }
        isActive = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        EventBus.getDefault().unregister(this);
        isActive = false;
    }

    // checkBox状态监听
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!myBinder.isTick()) {
                myBinder.startCountDown();
                mTitleTimer.setTextColor(Color.BLACK);
            }
        } else {
            if (!isShowing && !myBinder.isRest()) {
                showDelAlert();
                mStartCb.setChecked(true);
            } else { // 取消休息
                myBinder.cancelCountDown();
                SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
                int workTime = sharedPreferences.getInt("workTime", 25);
                mTitleTimer.setText(workTime + ":00");
                mTitleTimer.setTextColor(Color.BLACK);
                isShowing = true;
                mStartCb.setChecked(false);
                isShowing = false;
            }

        }
    }


    // 显示dialog
    public void showDelAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("放弃番茄");
        builder.setMessage("真的要放弃这个番茄吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myBinder.cancelCountDown();
                SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
                int workTime = sharedPreferences.getInt("workTime", 25);
                mTitleTimer.setText(workTime + ":00");
                mTitleTimer.setTextColor(Color.BLACK);
                isShowing = true;
                mStartCb.setChecked(false);
                isShowing = false;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_timer:
                Intent intent = new Intent(MainActivity.this, CountdownDetailActivity.class);
                startActivity(intent);
                break;

            case R.id.title_action_accept:
                Intent intentComplete = new Intent(MainActivity.this, CompleteTimerActivity.class);
                startActivity(intentComplete);
                break;


            case R.id.title_action_pop:

                // PopupWindow
                showPopupWindow();

                break;

            case R.id.pop_share:
                Intent intentShare = new Intent("share");
                sendBroadcast(intentShare);
                File sdRoot = Environment.getExternalStorageDirectory();
                Log.d("MainActivity", "sdRoot:" + sdRoot);
                shareMsg("分享至","我是分享内容","我是分享",sdRoot.getPath()+"/test.PNG");

                break;

        }
    }

    private void showPopupWindow() {

        TextView finishPopTv, recordPopTv, interruptPopTv, goalPopTv, sharePopTv, preferencesPopTv, helpPopTv;

        View view = LayoutInflater.from(this).inflate(R.layout.popup_window, null);

        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        finishPopTv = (TextView) view.findViewById(R.id.pop_finish);
        recordPopTv = (TextView) view.findViewById(R.id.pop_record);
        interruptPopTv = (TextView) view.findViewById(R.id.pop_interrupt);
        goalPopTv = (TextView) view.findViewById(R.id.pop_goal);
        sharePopTv = (TextView) view.findViewById(R.id.pop_share);
        preferencesPopTv = (TextView) view.findViewById(R.id.pop_preferences);
        helpPopTv = (TextView) view.findViewById(R.id.pop_help);
        mPopupWindow.setContentView(view);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAsDropDown(mTabLayout, mTabLayout.getWidth(), -mTabLayout.getHeight());

        // //在dismiss中恢复透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        if (pos == 1) {
            finishPopTv.setVisibility(View.GONE);
            recordPopTv.setVisibility(View.VISIBLE);
            interruptPopTv.setVisibility(View.VISIBLE);
        }
        if (pos == 2) {
            finishPopTv.setVisibility(View.GONE);
            recordPopTv.setVisibility(View.GONE);
            interruptPopTv.setVisibility(View.GONE);
            goalPopTv.setVisibility(View.VISIBLE);
            sharePopTv.setVisibility(View.VISIBLE);

        }
        preferencesPopTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
            }
        });

        sharePopTv.setOnClickListener(this);


    }

    public int getHeight() {
        // 状态栏高度

        ViewTreeObserver observer = mTabLayout.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int height = mTabLayout.getMeasuredHeight();
                int relHei = mRelativeLayout.getMeasuredHeight();
                allHeight = height + relHei;
                return true;
            }
        });


        return allHeight;
    }
     //安卓原生分享
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
            Log.d("MainActivity", "111:" + 111);
        } else {
            Log.d("MainActivity", "222:" + 222);
            File f = new File(imgPath);
            Log.d("MainActivity", "f:" + f);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/PNG");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }


}
