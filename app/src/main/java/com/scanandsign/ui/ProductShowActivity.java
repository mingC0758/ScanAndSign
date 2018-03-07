package com.scanandsign.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scanandsign.R;
import com.scanandsign.bean.Product;

import org.litepal.crud.DataSupport;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 已存储的货品列表展示界面
 */
public class ProductShowActivity extends AppCompatActivity {

	private android.support.v7.widget.RecyclerView mRecyclerView;
	MultiTypeAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_show);
		this.mRecyclerView = (RecyclerView) findViewById(R.id.rv_product_show);
		mAdapter = new MultiTypeAdapter();
		mAdapter.register(Product.class, new ProductViewBinder());
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setAdapter(mAdapter);
		List<Product> productList = DataSupport.findAll(Product.class, false);
		mAdapter.setItems(productList);
		mAdapter.notifyDataSetChanged();
	}
}
