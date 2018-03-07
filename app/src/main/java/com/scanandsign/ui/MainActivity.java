package com.scanandsign.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.scanandsign.R;
import com.scanandsign.bean.Product;

import org.litepal.crud.DataSupport;

public class MainActivity extends AppCompatActivity {

	private android.widget.Button btnLookStoreProducts;
	private android.widget.Button btnScanAndSign;
	private Button btnProductClear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.btnScanAndSign = (Button) findViewById(R.id.btn_main_scan_and_sign);
		this.btnLookStoreProducts = (Button) findViewById(R.id.btn_main_look_store_products);
		this.btnProductClear = (Button) findViewById(R.id.btn_main_product_clear);

		btnProductClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//清空数据库货品列表
				DataSupport.deleteAll(Product.class, "");
			}
		});

		btnLookStoreProducts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//显示已存储货品列表
				Intent intent = new Intent(MainActivity.this, ProductShowActivity.class);
				startActivity(intent);
			}
		});

		btnScanAndSign.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//进入扫码签收界面
				Intent intent = new Intent(MainActivity.this, ScanActivity.class);
				startActivity(intent);
			}
		});


	}
}
