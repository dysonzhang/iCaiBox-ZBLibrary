package com.icaihe.view;

import com.icaihe.R;
import com.icaihe.model.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.ImageLoaderUtil;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

/**用户View
 * @use
	UserView userView = new UserView(context, resources);
	adapter中使用[具体参考.DemoAdapter2(getView使用自定义View的写法)]
	convertView = userView.createView(inflater);
	userView.setView(position, data);
    或  其它类中使用 
    containerView.addView(userView.createView(inflater));
    userView.setView(data);
    然后
	userView.setOnDataChangedListener(onDataChangedListener);data = userView.getData();//非必需
	userView.setOnClickListener(onClickListener);//非必需
 */
public class UserView extends BaseView<User> implements OnClickListener {
	private static final String TAG = "UserView";
	
	public UserView(Activity context, Resources resources) {
		super(context, resources);
	}

	public ImageView ivUserViewHead;
	public ImageView ivUserViewStar;

	public TextView tvUserViewSex;

	public TextView tvUserViewName;
	public TextView tvUserViewId;
	public TextView tvUserViewNumber;
	@SuppressLint("InflateParams")
	@Override
	public View createView(LayoutInflater inflater) {
		convertView = inflater.inflate(R.layout.user_view, null);

		ivUserViewHead = findViewById(R.id.ivUserViewHead, this);
		ivUserViewStar = findViewById(R.id.ivUserViewStar, this);

		tvUserViewSex = findViewById(R.id.tvUserViewSex, this);

		tvUserViewName = findViewById(R.id.tvUserViewName);
		tvUserViewId = findViewById(R.id.tvUserViewId);
		tvUserViewNumber = findViewById(R.id.tvUserViewNumber);

		return convertView;
	}

	@Override
	public void setView(User data){
		if (data == null) {
			Log.e(TAG, "setView  data == null >> return;");
			return;
		}
		this.data = data;

		ImageLoaderUtil.loadImage(ivUserViewHead, data.getHead(), ImageLoaderUtil.TYPE_OVAL);
		ivUserViewStar.setImageResource(data.getStarred() ? R.drawable.star_light : R.drawable.star);

		tvUserViewSex.setBackgroundResource(data.getSex() == User.SEX_FEMAIL
				? R.drawable.circle_pink : R.drawable.circle_blue);
		tvUserViewSex.setText(data.getSex() == User.SEX_FEMAIL ?  "女" : "男");
		tvUserViewSex.setTextColor(getColor(data.getSex() == User.SEX_FEMAIL ? R.color.pink : R.color.blue));

		tvUserViewName.setText(StringUtil.getTrimedString(data.getName()));
		tvUserViewId.setText("ID:" + data.getId());
		tvUserViewNumber.setText("Phone:" + StringUtil.getNoBlankString(data.getPhone()));
	}

	@Override
	public void onClick(View v) {
		if (BaseModel.isCorrect(data) == false) {
			return;
		}
		switch (v.getId()) {
		case R.id.ivUserViewHead:
			toActivity(WebViewActivity.createIntent(context, data.getName(), data.getHead()));
			break;
		case R.id.ivUserViewStar:
			data.setStarred(! data.getStarred());
			setView(data);
			break;
		case R.id.tvUserViewSex:
			data.setSex(data.getSex() == User.SEX_FEMAIL ? User.SEX_MAIL : User.SEX_FEMAIL);
			setView(data);
			break;
		default:
			break;
		}		
	}
}