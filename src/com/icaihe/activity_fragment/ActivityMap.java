package com.icaihe.activity_fragment;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.icaihe.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import zuo.biao.library.base.BaseActivity;

public class ActivityMap extends BaseActivity implements OnGetPoiSearchResultListener {

	private MapView mMapView = null;
	private BaiduMap mBaiduMap;

	private Button mCompleteButton;
	private Button mCancleButton;
	private Button mRequestLocation;
	private ListView mListView;

	// 搜索周边相关
	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;

	/** 定位SDK的核心类 */
	public LocationClient mLocationClient = null;

	/** 当前标志 */
	private Marker mCurrentMarker;
	// 定位图标描述
	private BitmapDescriptor currentMarker = null;

	public BDLocationListener myListener = new MyLocationListener();

	private List<PoiInfo> dataList;
	private ListAdapter adapter;

	private int locType;
	private double longitude;// 精度
	private double latitude;// 维度
	private float radius;// 定位精度半径，单位是米
	private String addrStr;// 反地理编码
	private String province;// 省份信息
	private String city;// 城市信息
	private String district;// 区县信息
	private float direction;// 手机方向信息

	private int checkPosition;

	public static String RESULT_LOCATION_STRING = "RESULT_LOCATION_STRING";

	public static Intent createIntent(Context context) {
		return new Intent(context, ActivityMap.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		initView();
		initData();
		initEvent();
		initLocation();
	}

	@Override
	public void toWarnActivity(boolean isBattery) {
		Intent intent = ActivityWran.createIntent(context);
		intent.putExtra("isBattery", isBattery ? 1 : 0);
		toActivity(intent);
	}

	@Override
	public void initView() {
		super.initView();
		dataList = new ArrayList<PoiInfo>();
		mMapView = (MapView) findViewById(R.id.bmapView);
		mCompleteButton = (Button) findViewById(R.id.chat_publish_complete_publish);
		mCancleButton = (Button) findViewById(R.id.chat_publish_complete_cancle);
		mRequestLocation = (Button) findViewById(R.id.request);
		mListView = (ListView) findViewById(R.id.lv_location_nearby);
		checkPosition = 0;
		adapter = new ListAdapter(0);
		mListView.setAdapter(adapter);
	}

	@Override
	public void initData() {
		super.initData();
	}

	/**
	 * 事件初始化
	 */
	@Override
	public void initEvent() {
		super.initEvent();
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				checkPosition = position;
				adapter.setCheckposition(position);
				adapter.notifyDataSetChanged();
				PoiInfo ad = (PoiInfo) adapter.getItem(position);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ad.location);
				mBaiduMap.animateMapStatus(u);
				mCurrentMarker.setPosition(ad.location);
			}
		});

		mRequestLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showShortToast("正在定位。。。");
				initLocation();
			}
		});

		mCompleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				double lat = dataList.get(checkPosition).location.latitude;
				double lon = dataList.get(checkPosition).location.longitude;

				ActivityCreateGroup.addressX = lon + "";
				ActivityCreateGroup.addressY = lat + "";
				ActivityCreateGroup.location = dataList.get(checkPosition).address;
				// showShortToast(
				// "名称是: " + dataList.get(checkPosition).name + " 地址是：" +
				// dataList.get(checkPosition).address);

				// setResult(RESULT_OK, new
				// Intent().putExtra(RESULT_BOX_BLE_STRING, device.getName()));
				finish();
			}
		});

		mCancleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 定位
	 */
	private void initLocation() {
		// 重新设置
		checkPosition = 0;
		adapter.setCheckposition(0);

		mBaiduMap = mMapView.getMap();
		mBaiduMap.clear();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build())); // 设置级别

		// 定位初始化
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener);// 注册定位监听接口

		/**
		 * 设置定位参数
		 */
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		// option.setScanSpan(5000);// 设置发起定位请求的间隔时间,ms
		option.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		mLocationClient.setLocOption(option);
		mLocationClient.start(); // 调用此方法开始定位
	}

	/**
	 * 定位SDK监听函数
	 * 
	 * @author
	 * 
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || mMapView == null) {
				return;
			}

			locType = location.getLocType();
			Log.i("mybaidumap", "当前定位的返回值是：" + locType);

			longitude = location.getLongitude();
			latitude = location.getLatitude();
			if (location.hasRadius()) {// 判断是否有定位精度半径
				radius = location.getRadius();
			}

			if (locType == BDLocation.TypeNetWorkLocation) {
				addrStr = location.getAddrStr();// 获取反地理编码(文字描述的地址)
				Log.i("mybaidumap", "当前定位的地址是：" + addrStr);
			}

			direction = location.getDirection();// 获取手机方向，【0~360°】,手机上面正面朝北为0°
			province = location.getProvince();// 省份
			city = location.getCity();// 城市
			district = location.getDistrict();// 区县

			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

			// 将当前位置加入List里面
			PoiInfo info = new PoiInfo();
			info.address = location.getAddrStr();
			info.city = location.getCity();
			info.location = ll;
			info.name = location.getAddrStr();
			dataList.add(info);
			adapter.notifyDataSetChanged();
			Log.i("mybaidumap", "province是：" + province + " city是" + city + " 区县是: " + district);

			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);

			// 画标志
			CoordinateConverter converter = new CoordinateConverter();
			converter.coord(ll);
			converter.from(CoordinateConverter.CoordType.COMMON);
			LatLng convertLatLng = converter.convert();

			OverlayOptions ooA = new MarkerOptions().position(ll)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka));
			mCurrentMarker = (Marker) mBaiduMap.addOverlay(ooA);

			MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
			mBaiduMap.animateMapStatus(u);

			// 画当前定位标志
			MapStatusUpdate uc = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(uc);

			mMapView.showZoomControls(false);
			// poi 搜索周边
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Looper.prepare();
					searchNeayBy();
					Looper.loop();
				}
			}).start();

		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	/**
	 * 搜索周边
	 */
	private void searchNeayBy() {
		// POI初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();

		poiNearbySearchOption.keyword("公司");
		poiNearbySearchOption.location(new LatLng(latitude, longitude));
		poiNearbySearchOption.radius(100); // 检索半径，单位是米
		poiNearbySearchOption.pageCapacity(20); // 默认每页10条
		mPoiSearch.searchNearby(poiNearbySearchOption); // 发起附近检索请求
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Log.i("----------------", "---------------------");
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	};

	/*
	 * 接受周边地理位置结果
	 */
	@Override
	public void onGetPoiResult(PoiResult result) {
		// 获取POI检索结果
		if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
			Toast.makeText(ActivityMap.this, "未找到结果", Toast.LENGTH_LONG).show();
			return;
		}

		if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
			// mBaiduMap.clear();
			if (result != null) {
				if (result.getAllPoi() != null && result.getAllPoi().size() > 0) {
					dataList.addAll(result.getAllPoi());
					// adapter.notifyDataSetChanged();
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}
			}
		}
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {

	}

	/**
	 * 用于显示poi的overly
	 * 
	 * @author Administrator
	 *
	 * 
	 *         private class MyPoiOverlay extends PoiOverlay {
	 * 
	 *         public MyPoiOverlay(BaiduMap baiduMap) { super(baiduMap); }
	 * 
	 * @Override public boolean onPoiClick(int index) { super.onPoiClick(index);
	 *           PoiInfo poiInfo = getPoiResult().getAllPoi().get(index);
	 *           mPoiSearch.searchPoiDetail(new
	 *           PoiDetailSearchOption().poiUid(poiInfo.uid)); return true; } }
	 */

	class ListAdapter extends BaseAdapter {

		private int checkPosition;

		public ListAdapter(int checkPosition) {
			this.checkPosition = checkPosition;
		}

		public void setCheckposition(int checkPosition) {
			this.checkPosition = checkPosition;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(ActivityMap.this).inflate(R.layout.item_map, null);

				holder.textView = (TextView) convertView.findViewById(R.id.text_name);
				holder.textAddress = (TextView) convertView.findViewById(R.id.text_address);
				holder.imageLl = (ImageView) convertView.findViewById(R.id.image);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Log.i("mybaidumap", "name地址是：" + dataList.get(position).name);
			Log.i("mybaidumap", "address地址是：" + dataList.get(position).address);

			holder.textView.setText(dataList.get(position).name);
			holder.textAddress.setText(dataList.get(position).address);
			if (checkPosition == position) {
				holder.imageLl.setVisibility(View.VISIBLE);
			} else {
				holder.imageLl.setVisibility(View.GONE);
			}

			return convertView;
		}

	}

	class ViewHolder {
		TextView textView;
		TextView textAddress;
		ImageView imageLl;
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		if (mLocationClient != null) {
			mLocationClient.stop();
		}

		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mPoiSearch.destroy();
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
		// TODO Auto-generated method stub
	}

}
