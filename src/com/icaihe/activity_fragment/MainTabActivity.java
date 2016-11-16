package com.icaihe.activity_fragment;

import com.icaihe.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import zuo.biao.library.interfaces.OnBottomDragListener;

/**
 * 应用主页
 * 
 * @use MainTabActivity.createIntent(...)
 */
public class MainTabActivity extends BaseBottomTabActivity implements OnBottomDragListener {
	private static final String TAG = "MainTabActivity";

	// 启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**
	 * 启动这个Activity的Intent
	 * 
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return new Intent(context, MainTabActivity.class);
	}
	// 启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab_activity, this);
		// 功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		// 功能归类分区方法，必须调用>>>>>>>>>>
	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	@Override
	public void initView() {// 必须调用
		super.initView();
		exitAnim = R.anim.bottom_push_out;
	}

	@Override
	protected int[] getTabClickIds() {
		return new int[] { R.id.ll_tab_notice, R.id.ll_tab_box, R.id.ll_tab_contract, R.id.ll_tab_my };
	}

	@Override
	protected int[][] getTabSelectIds() {
		return new int[][] { new int[] { R.id.iv_tab_notice, R.id.iv_tab_box, R.id.iv_tab_contract, R.id.iv_tab_my }, // 顶部图标
				new int[] { R.id.tv_tab_notice, R.id.tv_tab_box, R.id.tv_tab_contract, R.id.tv_tab_my }// 底部文字
		};
	}

	@Override
	protected Fragment getFragment(int position) {
		switch (position) {
		case 1:
			return BoxFragment.createInstance();
		case 2:
			return ContractFragment.createInstance(0);
		case 3:
			return MySettingFragment.createInstance();
		default:
			return NoticeListFragment.createInstance(0, "dysonzhang");
		}
	};

	private static final String[] TAB_NAMES = { "动态", "财盒", "通讯录", "我的" };

	@Override
	protected void selectTab(int position) {
		// 导致切换时闪屏，建议去掉BottomTabActivity中的topbar，在fragment中显示topbar
		// rlBottomTabTopbar.setVisibility(position == 2 ? View.GONE :
		// View.VISIBLE);

		tvBaseTitle.setText(TAB_NAMES[position]);

	}

	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {// 必须调用
		super.initData();
	}

	// Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {// 必须调用
		super.initEvent();
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {

	}

	// 系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 双击手机返回键退出<<<<<<<<<<<<<<<<<<<<<
	private long firstTime = 0;// 第一次返回按钮计时

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) {
				showShortToast("再按一次退出");
				firstTime = secondTime;
			} else {// 完全退出
				moveTaskToBack(false);// 应用退到后台
				System.exit(0);
			}
			return true;
		}

		return super.onKeyUp(keyCode, event);
	}
	// 双击手机返回键退出>>>>>>>>>>>>>>>>>>>>>

	// 类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}