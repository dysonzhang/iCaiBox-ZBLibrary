package com.icaihe.activity_fragment;

import com.icaihe.R;
import com.icaihe.application.ICHApplication;

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

	public static Intent createIntent(Context context) {
		return new Intent(context, MainTabActivity.class);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab_activity, this);

		initView();
		initData();
		initEvent();
	}

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
			long boxId = ICHApplication.getInstance().getCurrentUser().getBoxId();
			if (boxId == 0) {
				return FristBoxFragment.createInstance();
			} else {
				return BoxFragment.createInstance();
			}
		case 2:
			return ContractListFragment.createInstance();
		case 3:
			return MySettingFragment.createInstance();
		default:
			return NoticeListFragment.createInstance();
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

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void initEvent() {// 必须调用
		super.initEvent();
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {

	}

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
}