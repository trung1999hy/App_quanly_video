package com.example.valut.ui.chargecoin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.ProductDetails;
import com.example.valut.R;
import com.example.valut.ui.ultis.Constant;

import java.util.ArrayList;
import java.util.List;


public class PurchaseInAppAdapter extends RecyclerView.Adapter<PurchaseInAppAdapter.ViewHolder> {

    private OnClickListener onClickListener;
    private List<ProductDetails> productDetailsList = new ArrayList<>();
    private Context context;

    public void setData(Context context, List<ProductDetails> productDetailsList) {
        this.productDetailsList = productDetailsList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.
                item_subscription, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.display(productDetailsList.get(position));
    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSubName;
        private ConstraintLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubName = itemView.findViewById(R.id.tvSubName);
            item = itemView.findViewById(R.id.item);
        }

        public void display(ProductDetails productDetails) {
            if (productDetails == null) return;
            String price = productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice();
            tvSubName.setText(setTitleValue(productDetails.getProductId(), price));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClickItem(productDetails);
                }
            });
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClickItem(ProductDetails item);
    }

    private String setTitleValue(String productId, String price) {
        switch (productId) {
            case Constant.KEY_5_COIN:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/5 coin");
            case Constant.KEY_15_COIN:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/15 coin");
            case Constant.KEY_30_COIN:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/30 coin");
            case Constant.KEY_100_COIN:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/100 coin");
            case Constant.KEY_200_COIN:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/200 coin");
            case Constant.KEY_300_COIN:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/300 coin");
            case Constant.KEY_500_COIN:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/500 coin");

            default:
                return String.format(context.getResources().getString(R.string.message_purchase_one), "/0 coin");
        }
    }
}
