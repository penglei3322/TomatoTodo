package com.example.dllo.tomatotodo.main;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.dllo.tomatotodo.countdowndetail.CountdownDetailActivity;
import com.example.dllo.tomatotodo.history.HistoryFragment;
import com.example.dllo.tomatotodo.potatolist.PotatoListFragment;
import com.example.dllo.tomatotodo.potatolist.activity.PotatoListDetailActivity;
import com.example.dllo.tomatotodo.preferences.PreferencesActivity;
import com.example.dllo.tomatotodo.service.CompleteTimerActivity;
import com.example.dllo.tomatotodo.service.CountDownEvent;
import com.example.dllo.tomatotodo.service.TomatoService;
import com.example.dllo.tomatotodo.statistics.StatisticsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private TabLayout tabLayout;
    private MainAdapter adapter;
    private ArrayList<Fragment> fragments;
    private ViewPager viewPager;
    private TextView titleTimer;
    private CheckBox startCb;
    private ImageView acceptBtn;
    private ServiceConnection serviceConnection;
    private TomatoService.MyBinder myBinder;
    private RelativeLayout relativeLayout;
    private NotificationManager notificationManager;
    private boolean isShowing = false;
    int allHeight;


    private boolean isActive;


    private ImageView popIv;
    private PopupWindow popupWindow;
    private int pos = 0;

    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public int initView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        tabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        adapter = new MainAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        titleTimer = (TextView) findViewById(R.id.title_timer);
        startCb = (CheckBox) findViewById(R.id.title_action_checkbox);

        acceptBtn = (ImageView) findViewById(R.id.title_action_accept);

        popIv = (ImageView) findViewById(R.id.title_action_pop);

        fragments = new ArrayList<>();
        fragments.add(new PotatoListFragment());
        fragments.add(new HistoryFragment());
        fragments.add(new StatisticsFragment());
        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        titleTimer.setOnClickListener(this);

        popIv.setOnClickListener(this);

        acceptBtn.setOnClickListener(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_title);
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
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (TomatoService.MyBinder) service;
                if (myBinder.isTick()) {
                    startCb.setChecked(true);
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
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        startCb.setOnCheckedChangeListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
        int workTime = sharedPreferences.getInt("workTime", 25);
        titleTimer.setText(workTime + ":00");


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                titleTimer.setTextColor(Color.GREEN);
                acceptBtn.setVisibility(View.GONE);
                startCb.setVisibility(View.VISIBLE);
            } else {
                titleTimer.setTextColor(Color.BLACK);
            }
            titleTimer.setText(time);
            startCb.setChecked(true);
        } else if (countDownEvent.getMillisUntilFinished() == -1) { // 工作结束
            titleTimer.setText("番茄已完成");
            acceptBtn.setVisibility(View.VISIBLE);
            startCb.setVisibility(View.GONE);
        }
        if (countDownEvent.getMillisUntilFinished() == 0) { // 休息结束
            SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
            int workTime = sharedPreferences.getInt("workTime", 25);
            titleTimer.setText(workTime + ":00");
            titleTimer.setTextColor(Color.BLACK);
            isShowing = true;
            startCb.setChecked(false);
            isShowing = false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isActive) {
            if (myBinder.isTick()) {
                startCb.setChecked(true);
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
                int workTime = sharedPreferences.getInt("workTime", 25);
                titleTimer.setText(workTime + ":00");
                titleTimer.setTextColor(Color.BLACK);
                isShowing = true;
                startCb.setChecked(false);
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
        unbindService(serviceConnection);
        EventBus.getDefault().unregister(this);
        isActive = false;
    }

    // checkBox状态监听
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!myBinder.isTick()) {
                myBinder.startCountDown();
                titleTimer.setTextColor(Color.BLACK);
            }
        } else {
            if (!isShowing && !myBinder.isRest()) {
                showDelAlert();
                startCb.setChecked(true);
            } else { // 取消休息
                myBinder.cancelCountDown();
                SharedPreferences sharedPreferences = getSharedPreferences("titleTime", MODE_PRIVATE);
                int workTime = sharedPreferences.getInt("workTime", 25);
                titleTimer.setText(workTime + ":00");
                titleTimer.setTextColor(Color.BLACK);
                isShowing = true;
                startCb.setChecked(false);
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
                titleTimer.setText(workTime + ":00");
                titleTimer.setTextColor(Color.BLACK);
                isShowing = true;
                startCb.setChecked(false);
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
        }
    }

    private void showPopupWindow() {

        TextView finishPopTv, recordPopTv, interruptPopTv, goalPopTv, sharePopTv, preferencesPopTv, helpPopTv;

        View view = LayoutInflater.from(this).inflate(R.layout.popup_window, null);

        popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        finishPopTv = (TextView) view.findViewById(R.id.pop_finish);
        recordPopTv = (TextView) view.findViewById(R.id.pop_record);
        interruptPopTv = (TextView) view.findViewById(R.id.pop_interrupt);
        goalPopTv = (TextView) view.findViewById(R.id.pop_goal);
        sharePopTv = (TextView) view.findViewById(R.id.pop_share);
        preferencesPopTv = (TextView) view.findViewById(R.id.pop_preferences);
        helpPopTv = (TextView) view.findViewById(R.id.pop_help);
        popupWindow.setContentView(view);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(tabLayout, tabLayout.getWidth(), -tabLayout.getHeight());

        // //在dismiss中恢复透明度
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
    }

    public int getHeight() {
        // 状态栏高度

        ViewTreeObserver observer = tabLayout.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int height = tabLayout.getMeasuredHeight();
                int relHei = relativeLayout.getMeasuredHeight();
                allHeight = height + relHei;
                return true;
            }
        });


        return allHeight;
    }
}
