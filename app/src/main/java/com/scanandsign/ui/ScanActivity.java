package com.scanandsign.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mei_husky.library.view.QRCodeScannerView;
import com.mei_husky.library.view.QRCoverView;
import com.scanandsign.R;

public class ScanActivity extends AppCompatActivity {

	private QRCodeScannerView mScannerView;
	private QRCoverView mCoverView;

	//手电筒是否开启
	private boolean torchopen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		//控件绑定
		mScannerView = (QRCodeScannerView) findViewById(R.id.scan_scanner_view);
		mCoverView = (QRCoverView) findViewById(R.id.scan_cover_view);

		//初始化界面
		initView();

		//相机：7.0及以上需要进行动态权限申请
		if (Build.VERSION.SDK_INT >=23) {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
					Toast.makeText(this, "缺乏使用相机权限，本应用无法工作！", Toast.LENGTH_SHORT).show();
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
				} else {
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
				}
			}else {
				startScan();
			}
		}
		else {
			//7.0及以下
			startScan();
		}
	}

	private void initView() {
		//扫描框外部颜色
		mCoverView.setCoverViewOutsideColor(R.color.alphablack);
		//扫描框角框颜色
		mCoverView.setCoverViewCornerColor(R.color.greenyellow);
		//设置角框大小
		//mCoverView.setCoverViewCorner(10, 20, true);
		//设置角框为包裹状态
		mCoverView.setCoverViewConnerFace(true);
		//设置扫描框大小
		mCoverView.setCoverViewScanner(250, 250);

		//轻触打开/关闭手电筒
		mCoverView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switchTorch();
			}
		});
	}

	/**
	 * 开始扫描
	 */
	private void startScan() {
		mScannerView.setOnCheckCameraPermissionListener(new QRCodeScannerView.OnCheckCameraPermissionListener() {
			@Override
			public boolean onCheckCameraPermission() {
				return true;
			}
		});
		//自动聚焦间隔2s
		mScannerView.setAutofocusInterval(2000L);
		//扫描结果监听处理
		mScannerView.setOnQRCodeReadListener(new QRCodeScannerView.OnQRCodeScannerListener() {
			@Override
			public void onDecodeFinish(String text, PointF[] points) {
				//判断扫描结果
				judgeResult(text, points);
			}
		});
		//开启后置摄像头
		mScannerView.setBackCamera();
	}

	/**
	 * 开关手电筒
	 */
	private void switchTorch() {
		if (torchopen)  {
			//关闭手电筒
			mScannerView.setTorchEnabled(true);
		} else {
			//打开手电筒
			mScannerView.setTorchEnabled(false);
		}
		torchopen = !torchopen;
	}

	/**
	 * 判断二维码是否在框内，若是，则进入新的Activity
	 * @param result 扫描结果
	 * @param points 二维码点
	 */
	private void judgeResult(String result, PointF[] points) {
		//接下来是处理二维码是否在扫描框中的逻辑
		RectF finderRect = mCoverView.getViewFinderRect();
		boolean isContain = true;
		//依次判断扫描结果的每个point是否都在扫描框内
		for (int i = 0, length = points.length; i < length; i++) {
			if (!finderRect.contains(points[i].x, points[i].y)) {
				isContain = false;  //只要有一个不在，说明二维码不完全在扫描框中
				break;
			}
		}
		if (isContain) {
			Toast.makeText(ScanActivity.this, "扫描结果：" + result, Toast.LENGTH_SHORT).show();
			//停止扫描，进入签名界面
			mScannerView.setQRDecodingEnabled(false);
			mCoverView.setShowLaser(false);
			Intent intent = new Intent(this, SignActivity.class);
			intent.putExtra("url", result);
			startActivity(intent);
		} else {
			Log.d("ScanActivity", "二维码不在框内");
		}
	}

	@Override
	protected void onResume() {
		//闪光灯重置为关闭状态
		torchopen = false;
		mScannerView.startCamera();
		mScannerView.setQRDecodingEnabled(true);
		super.onResume();
	}

	@Override
	protected void onPause() {
		mScannerView.stopCamera();
		super.onPause();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
	                                       @NonNull int[] grantResults) {
		if (requestCode == 1) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				startScan();
			}else {
				Toast.makeText(this, "缺乏使用相机权限，本应用无法工作！", Toast.LENGTH_SHORT).show();
				this.finish();
			}
		}
	}
}
