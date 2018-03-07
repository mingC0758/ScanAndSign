package com.scanandsign.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scanandsign.R;
import com.scanandsign.bean.Product;
import com.scanandsign.bean.ProductResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 给货品签名界面
 */
public class SignActivity extends AppCompatActivity {
	private RecyclerView recyclerView;
	private MultiTypeAdapter adapter;

	private DrawView drawView;

	private List<Product> productList = new ArrayList<>(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);

		//界面初始化
		drawView = (DrawView) findViewById(R.id.sign_draw_view);
		findViewById(R.id.btn_submit_sign).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				submitSign();
			}
		});
		recyclerView = (RecyclerView) findViewById(R.id.sign_recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new MultiTypeAdapter();
		adapter.register(Product.class, new ProductViewBinder());
		recyclerView.setAdapter(adapter);
		adapter.setItems(productList);
		//拉取数据
		final String url = getIntent().getStringExtra("url");
		//final String url = "http://192.168.1.103:8080/GetProducts";
		Observable.create(new ObservableOnSubscribe<List<Product>>() {
			@Override
			public void subscribe(@NonNull ObservableEmitter<List<Product>> e) throws Exception {
					OkHttpClient client = new OkHttpClient();
					Request request = new Request.Builder()
							.url(url)
							.build();
					Response response = client.newCall(request).execute();
					String message = response.message();
					String responseData = response.body().string();
					Log.d("SignActivity", message);
					Log.d("SignActivity", responseData);
					Gson gson = new Gson();
					ProductResult productResult = gson.fromJson(responseData, ProductResult.class);
					e.onNext(productResult.getProductList());
			}
		}).observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Consumer<List<Product>>() {
					@Override
					public void accept(List<Product> products) throws Exception {
						//显示数据
						productList.addAll(products);
						adapter.notifyDataSetChanged();
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						//AlertDialog.Builder builder = new AlertDialog.Builder();
						Toast.makeText(SignActivity.this, "出错：" + throwable.getMessage(),
								Toast.LENGTH_SHORT).show();
						throwable.printStackTrace();
					}
				});
	}



	/**
	 * 提交签名，弹出对话框显示签名图片
	 */
	private void submitSign() {
	//	File file = new File(Environment.getExternalStorageDirectory() + "/scan_and_sign.jpg");
		//创建签名的bitmap
		Bitmap bitmap = drawView.createBitmap();
//		//保存bitmap
//		try {
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 60, new FileOutputStream(file));
//		}catch (FileNotFoundException e) {
//			e.printStackTrace();
//			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//		}
		//对话框显示
		ImageView imageView = new ImageView(this);
		imageView.setImageBitmap(bitmap);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setView(imageView).show();

		//存储货品列表到数据库
		for (Product product : productList) {
			product.save();
		}

		//上传图片到服务器
//		OkHttpClient mOkHttpClent = new OkHttpClient();
//		MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
//				.setType(MultipartBody.FORM)
//				.addFormDataPart("img", "sign_pic", RequestBody.create(MediaType.parse("image/png"), file));
//		RequestBody requestBody = bodyBuilder.build();
//		Request request = new Request.Builder()
//				.url("http://192.168.1.103:8080/UploadSign")
//				.post(requestBody)
//				.build();
//		Call call = mOkHttpClent.newCall(request);
//		call.enqueue(new Callback() {
//			@Override
//			public void onFailure(Call call, IOException e) {
//				Toast.makeText(SignActivity.this, "上传签名出错：" + e.getMessage(),
//						Toast.LENGTH_SHORT).show();
//				e.printStackTrace();
//			}
//
//			@Override
//			public void onResponse(Call call, Response response) throws IOException {
//				Toast.makeText(SignActivity.this, "上传签名成功！", Toast.LENGTH_SHORT).show();
//			}
//		});
	}
}
