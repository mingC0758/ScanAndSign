package com.scanandsign.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scanandsign.R;
import com.scanandsign.bean.Product;

import me.drakeet.multitype.ItemViewBinder;

/**
 * @author mingC
 * @date 2018/1/30
 */
public class ProductViewBinder extends ItemViewBinder<Product, ProductViewBinder.ViewHolder> {

	@NonNull
	@Override
	protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
	                                        @NonNull ViewGroup parent) {
		View root = inflater.inflate(R.layout.item_product, parent, false);
		return new ViewHolder(root);
	}

	@Override
	protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Product product) {
		holder.tvName.setText(product.getName());
		holder.tvNum.setText("" + product.getNum());
		holder.tvUnit.setText("" + product.getUnit());

		//添加长按菜单编辑和删除
		holder.productLayout.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(final View v) {
				PopupMenu menu = new PopupMenu(v.getContext(), v);
				holder.productLayout.setBackground(new ColorDrawable(Color.YELLOW));
				menu.setOnDismissListener(new PopupMenu.OnDismissListener() {
					@Override
					public void onDismiss(PopupMenu menu) {
						holder.productLayout.setBackground(null);
					}
				});
				menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						if (item.getItemId() == R.id.menu_pro_delete) {
							//删除操作
							getAdapter().getItems().remove(product);
							getAdapter().notifyItemRemoved(holder.getAdapterPosition());
						} else {
							//修改操作
							final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_product_edit, null);
							new AlertDialog.Builder(v.getContext())
									.setNegativeButton("取消", null)
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									try {
										String name = ((TextView) dialogView.findViewById(R.id.et_pro_edit_name)).getText().toString();
										int num = Integer.valueOf(((TextView) dialogView.findViewById(R.id.et_pro_edit_num)).getText().toString());
										String unit = ((TextView) dialogView.findViewById(R.id.et_pro_edit_unit)).getText().toString();
										product.setName(name);
										product.setNum(num);
										product.setUnit(unit);
									} catch (NumberFormatException e) {
										e.printStackTrace();
									}
									getAdapter().notifyItemChanged(holder.getAdapterPosition());
								}
							}).setView(dialogView)
							.setTitle("修改")
							.show();
						}
						holder.productLayout.setBackground(null);
						return true;
					}
				});
				menu.inflate(R.menu.menu_product);
				menu.show();
				return false;
			}
		});
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		View productLayout;
		TextView tvName;
		TextView tvNum;
		TextView tvUnit;

		ViewHolder(final View itemView) {
			super(itemView);
			productLayout = itemView.findViewById(R.id.layout_product_item);
			tvName = (TextView) itemView.findViewById(R.id.tv_name);
			tvNum = (TextView) itemView.findViewById(R.id.tv_num);
			tvUnit = (TextView) itemView.findViewById(R.id.tv_unit);
		}
	}
}
