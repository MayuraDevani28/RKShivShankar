package com.shivshankar.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shivshankar.CartActivityBuyer;
import com.shivshankar.R;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.classes.CartItem;
import com.shivshankar.utills.AlertDialogManager;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;
import com.shivshankar.viewpager.ViewPagerActivity;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

@SuppressLint({"NewApi", "ResourceAsColor"})
public class CartAdapterBuyer extends RecyclerView.Adapter<CartAdapterBuyer.MyViewHolder> implements OnResult {

    private final AppCompatActivity activity;
    private final ArrayList<CartItem> list;
    private static int posit;

    Dialog dialog;
    private EditText mTv_Brand, mTv_Top_Fabrics, mTv_Bottom_Fabrics, mTv_Dupatta, mTv_All_Fabrics, mTv_Category, mTv_Type, mTv_Price, mTv_Min_Qty, mTv_Product_Code;
    private TextInputLayout mEdt_Dupatta, mEdt_All_Fabrics;
    private LinearLayout mLL_Fabrics, mLl_fabric;
    String[] SP_BODY, VAL_BODY;
    Resources res;

    public CartAdapterBuyer(AppCompatActivity activity, ArrayList<CartItem> list, String[] SP_body, String[] VAL_body) {
        this.activity = activity;
        this.list = list;
        SP_BODY = SP_body;
        VAL_BODY = VAL_body;
        res = activity.getResources();
    }

    public List<CartItem> getItems() {
        return list;
    }

    public boolean isQtyRemaining() {
        boolean b = false;
        for (int i = 0; i < list.size(); i++) {
            CartItem item = list.get(i);
            if (item.getSuitFbricId() == 2) {
                if (item.getBodyPart().equalsIgnoreCase("Top")) {
                    if (isAllCutZero(item) || iaAllQtyZero(item)) {
                        b = true;
                    }
                } else if (item.getCartQuantity() == 0 || item.getFabricCuts() == 0) {
                    b = true;
                }
                break;
            }
        }
        return b;

    }

    private boolean iaAllQtyZero(CartItem item) {
        if (item.getFabric_FrontQty() == 0 && item.getFabric_BackQty() == 0 && item.getFabric_BajuQty() == 0 && item.getFabric_ExtraQty() == 0)
            return true;
        else
            return false;
    }

    private boolean isAllCutZero(CartItem item) {
        if (item.getFabric_FrontCut() == 0 && item.getFabric_BackCut() == 0 && item.getFabric_BajuCut() == 0 && item.getFabric_ExtraCut() == 0)
            return true;
        else
            return false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher {
        boolean isChanging = false, isChanging2 = false;
        private ImageView mIv_product_image, mIv_delete;
        private TextView mTv_product_code, mTv_price, mTv_price_fabric, mTv_total, mTv_total_fabric, mBtn_update;
        EditText mTv_qty;

        private LinearLayout mLl_suit, mLl_fabric, mLl_qty_cut, mLl_front;
        private EditText mTv_qty_fabric, mTv_cut_fabric, mTv_qty_fabric_back, mTv_cut_fabric_back, mTv_qty_fabric_baju, mTv_cut_fabric_baju, mTv_qty_fabric_extra, mTv_cut_fabric_extra;

        private TextView mBtn_update_fabric, mTv_total_cut, mTv_total_qty;

        private AppCompatSpinner mSp_body;
        private RadioGroup mRadioGroup1, mRadioGroup2;
        RecyclerView mRv_products;
        GalleryAdapter horizontalAdapter;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                mTv_product_code = (TextView) itemView.findViewById(R.id.tv_product_code);
                mIv_product_image = (ImageView) itemView.findViewById(R.id.iv_product_image);
                mIv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
                mTv_price = (TextView) itemView.findViewById(R.id.tv_price);
                mTv_price_fabric = (TextView) itemView.findViewById(R.id.tv_price_fabric);
                mTv_qty = (EditText) itemView.findViewById(R.id.tv_qty);
                mTv_qty.addTextChangedListener(this);


                mRv_products = (RecyclerView) itemView.findViewById(R.id.rv_products);
                int i = 6;
                if (res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    i = 8;
                }
                mRv_products.setLayoutManager(new GridLayoutManager(activity, i));
                horizontalAdapter = new GalleryAdapter(activity);
                mRv_products.setAdapter(horizontalAdapter);

                mLl_qty_cut = (LinearLayout) itemView.findViewById(R.id.ll_qty_cut);
                mLl_front = (LinearLayout) itemView.findViewById(R.id.ll_front);
                mTv_total_qty = (TextView) itemView.findViewById(R.id.tv_total_qty);
                mTv_total_cut = (TextView) itemView.findViewById(R.id.tv_total_cut);
                mTv_total = (TextView) itemView.findViewById(R.id.tv_total);
                mTv_total_fabric = (TextView) itemView.findViewById(R.id.tv_total_fabric);
                mIv_delete.setOnClickListener(this);
                mBtn_update = (TextView) itemView.findViewById(R.id.btn_update);
                mBtn_update.setOnClickListener(this);
                mIv_product_image.setOnClickListener(this);

                mLl_suit = (LinearLayout) itemView.findViewById(R.id.ll_suit);
                mLl_fabric = (LinearLayout) itemView.findViewById(R.id.ll_fabric);
                mTv_qty_fabric = (EditText) itemView.findViewById(R.id.tv_qty_fabric);
                TextWatcher textListener = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        isChanging2 = true;
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        try {
                            mBtn_update_fabric.setVisibility(View.VISIBLE);
                            isChanging2 = false;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                };
                mTv_qty_fabric.addTextChangedListener(textListener);
                mBtn_update_fabric = (TextView) itemView.findViewById(R.id.btn_update_fabric);
                mBtn_update_fabric.setOnClickListener(this);
                mTv_cut_fabric = (EditText) itemView.findViewById(R.id.tv_cut_fabric);
                mTv_cut_fabric.addTextChangedListener(textListener);

                mTv_cut_fabric_back = (EditText) itemView.findViewById(R.id.tv_cut_fabric_back);
                mTv_cut_fabric_back.addTextChangedListener(textListener);
                mTv_cut_fabric_baju = (EditText) itemView.findViewById(R.id.tv_cut_fabric_baju);
                mTv_cut_fabric_baju.addTextChangedListener(textListener);
                mTv_cut_fabric_extra = (EditText) itemView.findViewById(R.id.tv_cut_fabric_extra);
                mTv_cut_fabric_extra.addTextChangedListener(textListener);

                mTv_qty_fabric_back = (EditText) itemView.findViewById(R.id.tv_qty_fabric_back);
                mTv_qty_fabric_back.addTextChangedListener(textListener);
                mTv_qty_fabric_baju = (EditText) itemView.findViewById(R.id.tv_qty_fabric_baju);
                mTv_qty_fabric_baju.addTextChangedListener(textListener);
                mTv_qty_fabric_extra = (EditText) itemView.findViewById(R.id.tv_qty_fabric_extra);
                mTv_qty_fabric_extra.addTextChangedListener(textListener);

                mSp_body = (AppCompatSpinner) itemView.findViewById(R.id.sp_body);
                mRadioGroup1 = (RadioGroup) itemView.findViewById(R.id.radioGroup1);
                mRadioGroup2 = (RadioGroup) itemView.findViewById(R.id.radioGroup2);

                mRadioGroup1.setOnCheckedChangeListener(listener1);
                mRadioGroup2.setOnCheckedChangeListener(listener2);

                mSp_body.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            int position = getAdapterPosition();
                            CartItem product = list.get(position);
                            if (VAL_BODY[i].equalsIgnoreCase("Top")) {
                                mRadioGroup1.setVisibility(View.VISIBLE);
                                mRadioGroup2.setVisibility(View.VISIBLE);

                                mRadioGroup1.clearCheck();
                                mRadioGroup2.clearCheck();
                                mLl_front.setVisibility(View.GONE);
                                mLl_qty_cut.setVisibility(View.VISIBLE);

                            } else {
                                product.setFabric_FrontQty(0);
                                product.setFabric_FrontCut(0);
                                product.setFabric_BackQty(0);
                                product.setFabric_BackCut(0);
                                product.setFabric_BajuQty(0);
                                product.setFabric_BajuCut(0);
                                product.setFabric_ExtraQty(0);
                                product.setFabric_ExtraCut(0);

                                mRadioGroup1.setVisibility(View.GONE);
                                mRadioGroup2.setVisibility(View.GONE);
                                mRadioGroup1.clearCheck();
                                mRadioGroup2.clearCheck();

                                mLl_front.setVisibility(View.VISIBLE);
                                mLl_qty_cut.setVisibility(View.GONE);
                            }
                            if (!VAL_BODY[i].contains("SELECT") &&
                                    !VAL_BODY[i].equalsIgnoreCase(list.get(position).getBodyPart())) {
                                list.get(position).setBodyPart(VAL_BODY[i]);

                                APIs.Update_Cart_Fabric(activity, CartAdapterBuyer.this, product.getCartId(), product.getCartQuantity() + "", product.getMinOrderQuantity() + "", product.getFabricCuts() + "", product.getBodyPart(),
                                        product.getFabric_FrontQty() + "", product.getFabric_FrontCut() + "", product.getFabric_BackQty() + "",
                                        product.getFabric_BackCut() + "", product.getFabric_BajuQty() + "", product.getFabric_BajuCut() + ""
                                        , product.getFabric_ExtraQty() + "", product.getFabric_ExtraCut() + "");

                            } else {
                                mBtn_update_fabric.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            isChanging = true;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            try {
                CartItem item = list.get(getAdapterPosition());
                String edited = editable.toString().trim();
                if (isChanging && !edited.isEmpty() && parseInt(edited) != item.getCartQuantity()) {
                    mBtn_update.setVisibility(View.VISIBLE);
                } else if (item.getSuitFbricId() == 1) {
                    mBtn_update.setVisibility(View.INVISIBLE);
                }
                isChanging = false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    if (checkedId != -1) {
                        mLl_front.setVisibility(View.VISIBLE);
                        mRadioGroup2.setOnCheckedChangeListener(null);
                        mRadioGroup2.clearCheck();
                        mRadioGroup2.setOnCheckedChangeListener(listener2);

                        int position = getAdapterPosition();
                        CartItem item = list.get(position);

                        if (checkedId == R.id.radioFront) {
                            setChecked(1, position, item);
                        } else {
                            setChecked(2, position, item);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    if (checkedId != -1) {
                        mLl_front.setVisibility(View.VISIBLE);
                        mRadioGroup1.setOnCheckedChangeListener(null);
                        mRadioGroup1.clearCheck();
                        mRadioGroup1.setOnCheckedChangeListener(listener1);
                        mBtn_update_fabric.setVisibility(View.VISIBLE);

                        int position = getAdapterPosition();
                        CartItem item = list.get(position);

                        if (checkedId == R.id.radioBaju) {
                            setChecked(3, position, item);
                        } else {
                            setChecked(4, position, item);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        private void setChecked(int i, int position, CartItem item) {
            mTv_qty_fabric.setVisibility(View.GONE);
            mTv_cut_fabric.setVisibility(View.GONE);
            mTv_qty_fabric_back.setVisibility(View.GONE);
            mTv_cut_fabric_back.setVisibility(View.GONE);
            mTv_qty_fabric_baju.setVisibility(View.GONE);
            mTv_cut_fabric_baju.setVisibility(View.GONE);
            mTv_qty_fabric_extra.setVisibility(View.GONE);
            mTv_cut_fabric_extra.setVisibility(View.GONE);

            if (i == 1) {
                mTv_qty_fabric.setText(item.getFabric_FrontQty() + "");
                setCutEdit(item.getFabric_FrontCut(), mTv_cut_fabric);
                mTv_qty_fabric.setVisibility(View.VISIBLE);
                mTv_cut_fabric.setVisibility(View.VISIBLE);
            } else if (i == 2) {
                mTv_qty_fabric_back.setText(item.getFabric_BackQty() + "");
                setCutEdit(item.getFabric_BackCut(), mTv_cut_fabric_back);
                mTv_qty_fabric_back.setVisibility(View.VISIBLE);
                mTv_cut_fabric_back.setVisibility(View.VISIBLE);
            } else if (i == 3) {
                mTv_qty_fabric_baju.setText(item.getFabric_BajuQty() + "");
                setCutEdit(item.getFabric_BajuCut(), mTv_cut_fabric_baju);
                mTv_qty_fabric_baju.setVisibility(View.VISIBLE);
                mTv_cut_fabric_baju.setVisibility(View.VISIBLE);
            } else {
                mTv_qty_fabric_extra.setText(item.getFabric_ExtraQty() + "");
                setCutEdit(item.getFabric_ExtraCut(), mTv_cut_fabric_extra);
                mTv_qty_fabric_extra.setVisibility(View.VISIBLE);
                mTv_cut_fabric_extra.setVisibility(View.VISIBLE);
            }
        }

        private void setCutEdit(double c, EditText mTv_cut_fabric_back) {
            String cut = c + "";
            if (cut.endsWith(".0"))
                cut = cut.substring(0, cut.length() - 2);
            mTv_cut_fabric_back.setText(cut);
        }

        @Override
        public void onClick(View view) {
            try {
                posit = getAdapterPosition();
                CartItem product = list.get(posit);
                if (view == mIv_product_image) {
                    if (product.getSuitFbricId() == 1) {//suit
                        showPopup(list.get(getAdapterPosition()).getImageName(), product.getProductId());
                    } else
                        showPopupFabric(list.get(getAdapterPosition()).getImageName(), product.getProductId());
                } else if (view == mIv_delete) {
                    AlertDialogManager.showDialogYesNo(activity, "Do you want to delete this product?", "Yes", () -> APIs.RemoveCartProduct(activity, CartAdapterBuyer.this, product.getProductId()));
                } else if (view == mBtn_update) {
                    String qty = mTv_qty.getText().toString().trim();
                    if (parseInt(qty) == 0) {
                        AlertDialogManager.showDialog(activity, "Quantity can't be 0", null);
                    } else if (parseInt(qty) < product.getMinOrderQuantity()) {
                        AlertDialogManager.showDialog(activity, "Minimum " + product.getMinOrderQuantity() + " quantity required", null);
                    } else {
                        APIs.Update_Cart_Suit(activity, CartAdapterBuyer.this, product.getCartId(), qty, product.getMinOrderQuantity() + "");
                    }
                } else if (view == mBtn_update_fabric) {
                    String fabric_FrontQty = mTv_qty_fabric.getText().toString().trim(),
                            fabric_BackQty = mTv_qty_fabric_back.getText().toString().trim(),
                            fabric_BajuQty = mTv_qty_fabric_baju.getText().toString().trim(),
                            fabric_ExtraQty = mTv_qty_fabric_extra.getText().toString().trim(),
                            fabric_FrontCut = mTv_cut_fabric.getText().toString().trim(),
                            fabric_BackCut = mTv_cut_fabric_back.getText().toString().trim(),
                            fabric_BajuCut = mTv_cut_fabric_baju.getText().toString().trim(),
                            fabric_ExtraCut = mTv_cut_fabric_extra.getText().toString().trim();

                    int i = 0;
                    if (fabric_FrontQty.isEmpty()) {
                        fabric_FrontQty = "0";
                        i++;
                    }
                    if (fabric_FrontCut.isEmpty()) {
                        fabric_FrontCut = "0";
                        i++;
                    }
                    if (fabric_BackQty.isEmpty()) {
                        fabric_BackQty = "0";
                        i++;
                    }
                    if (fabric_BackCut.isEmpty()) {
                        fabric_BackCut = "0";
                        i++;
                    }
                    if (fabric_BajuQty.isEmpty()) {
                        fabric_BajuQty = "0";
                        i++;
                    }
                    if (fabric_BajuCut.isEmpty()) {
                        fabric_BajuCut = "0";
                        i++;
                    }
                    if (fabric_ExtraQty.isEmpty()) {
                        fabric_ExtraQty = "0";
                        i++;
                    }
                    if (fabric_ExtraCut.isEmpty()) {
                        fabric_ExtraCut = "0";
                        i++;
                    }
                    if (i != 8) {
                        int qtyFront = Integer.parseInt(fabric_FrontQty), qtyBack =
                                Integer.parseInt(fabric_BackQty),
                                qtyBaju = Integer.parseInt(fabric_BajuQty),
                                qtyExtra = Integer.parseInt(fabric_ExtraQty);

                        int qty = qtyFront + qtyBack + qtyBaju + qtyExtra;

                        double cutFront = parseDouble(fabric_FrontCut),
                                cutBack = Double.parseDouble(fabric_BackCut),
                                cutBaju = Double.parseDouble(fabric_BajuCut),
                                cutExtra = Double.parseDouble(fabric_ExtraCut);

                        double cut = cutFront + cutBack + cutBaju + cutExtra;
                        boolean showError = false;
                        if (mTv_cut_fabric.getVisibility() == View.VISIBLE && (qtyFront == 0 || cutFront == 0))
                            showError = true;
                        else if (mTv_cut_fabric_back.getVisibility() == View.VISIBLE && (qtyBack == 0 || cutBack == 0))
                            showError = true;
                        else if (mTv_cut_fabric_baju.getVisibility() == View.VISIBLE && (qtyBaju == 0 || cutBaju == 0))
                            showError = true;
                        else if (mTv_cut_fabric_extra.getVisibility() == View.VISIBLE && (qtyExtra == 0 || cutExtra == 0))
                            showError = true;
                        if (showError) {
                            AlertDialogManager.showDialog(activity, "Qty/Cut can't be 0", null);
                        } else {
                            APIs.Update_Cart_Fabric(activity, CartAdapterBuyer.this, product.getCartId(),
                                    qty + "", product.getMinOrderQuantity() + "", cut + "",
                                    product.getBodyPart(),
                                    fabric_FrontQty, fabric_FrontCut, fabric_BackQty,
                                    fabric_BackCut, fabric_BajuQty, fabric_BajuCut
                                    , fabric_ExtraQty, fabric_ExtraCut);
                        }
                    } else {
                        AlertDialogManager.showDialog(activity, "Qty/Cut can't be 0", null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_row_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CartItem item = list.get(position);
        try {
            holder.mTv_product_code.setSelected(true);
            holder.mTv_product_code.setText(WordUtils.capitalize(item.getProductCode() + " (" + item.getBrandName() + ")"));

            if (item.getSuitFbricId() == 1) {
                holder.mTv_qty.setText(item.getCartQuantity() + "");
                holder.mLl_suit.setVisibility(View.VISIBLE);
                holder.mLl_fabric.setVisibility(View.GONE);
                holder.mLl_qty_cut.setVisibility(View.GONE);
                holder.mTv_price.setText(commonVariables.strCurrency_name + " " + item.getOfferPrice());
                holder.mTv_total.setText(commonVariables.strCurrency_name + " " + item.getTotalAmount());
            } else {
                holder.mLl_qty_cut.setVisibility(View.VISIBLE);
                holder.mLl_suit.setVisibility(View.GONE);
                holder.mLl_fabric.setVisibility(View.VISIBLE);
                holder.mTv_total_qty.setText(item.getCartQuantity() + "");
                holder.mTv_total_cut.setText(item.getFabricCuts() + "");
                holder.mTv_price_fabric.setText(commonVariables.strCurrency_name + " " + item.getOfferPrice());
                holder.mTv_total_fabric.setText(commonVariables.strCurrency_name + " " + item.getTotalAmount());
                String cut = item.getFabricCuts() + "";
                if (cut.endsWith(".0"))
                    cut = cut.substring(0, cut.length() - 2);
                holder.mTv_cut_fabric.setText(cut);
                holder.mTv_qty_fabric.setText(item.getCartQuantity() + "");

                try {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.adapter_row_dropdown_item, SP_BODY);
                    holder.mSp_body.setAdapter(arrayAdapter);
                    holder.mSp_body.clearFocus();
                    String BodyPart = list.get(position).getBodyPart();
                    if (!BodyPart.isEmpty()) {
                        holder.mSp_body.setSelection(getIndexOf(BodyPart));
                        if (BodyPart.equalsIgnoreCase("Top")) {
                            holder.mLl_front.setVisibility(View.GONE);
                            holder.mLl_qty_cut.setVisibility(View.VISIBLE);
                        } else {
                            holder.mLl_front.setVisibility(View.VISIBLE);
                            holder.mLl_qty_cut.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String strImageURL = item.getImageName();
            if ((strImageURL != null) && (!strImageURL.equals(""))) {
                Glide.with(activity).load(strImageURL)
                        .asBitmap().approximate().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f)
                        .error(R.drawable.no_img).into(holder.mIv_product_image);
            }

            holder.horizontalAdapter.setData(item.getFabric_Colors()); // List of Strings
            holder.horizontalAdapter.setRowIndex(position);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getIndexOf(String bodyPart) {
        int pos = 0;
        try {
            for (int i = 0; i < VAL_BODY.length; i++) {
                if (VAL_BODY[i].equalsIgnoreCase(bodyPart)) {
                    pos = i;
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pos;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        if (jobjWhole != null) {
            try {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetProductDetail_Suit_Seller")) {
                    JSONObject job = jobjWhole.optJSONObject("resData");
                    mTv_Product_Code.setText(job.optString("ProductCode"));
                    mTv_Brand.setText(job.optString("BrandName"));
                    mTv_Category.setText(job.optString("CategoryName"));
                    mTv_Type.setText(job.optString("FabricType"));
                    mTv_Price.setText(commonVariables.strCurrency_name + " " + job.optInt("OfferPrice"));
                    mTv_Min_Qty.setText("" + job.optInt("MinOrderQty"));
                    if (job.optBoolean("IsAllOver")) {
                        mLL_Fabrics.setVisibility(View.GONE);
                        mEdt_All_Fabrics.setVisibility(View.VISIBLE);
                        mEdt_Dupatta.setVisibility(View.GONE);
                        mTv_All_Fabrics.setText(job.optString("FabricName"));
                    } else {
                        mLL_Fabrics.setVisibility(View.VISIBLE);
                        mEdt_All_Fabrics.setVisibility(View.GONE);
                        mEdt_Dupatta.setVisibility(View.VISIBLE);
                        mTv_Top_Fabrics.setText(job.optString("TopFabricName"));
                        mTv_Bottom_Fabrics.setText(job.optString("BottomFabricName"));
                        mTv_Dupatta.setText(job.optString("DupattaFabricName"));
                    }
                } else if (strApiName.equalsIgnoreCase("GetProductDetail_Fabric")) {
                    JSONObject job = jobjWhole.optJSONObject("resData");
                    mTv_Product_Code.setText(job.optString("ProductCode"));
                    mTv_Brand.setText(job.optString("BrandName"));
                    mTv_Category.setText(job.optString("CategoryName"));
                    mTv_Type.setText(job.optString("FabricType"));
                    mTv_Price.setText(commonVariables.strCurrency_name + " " + job.optString("OfferPrice"));
                    mTv_Min_Qty.setText("" + job.optInt("MinOrderQty"));
                } else if (strApiName.equalsIgnoreCase("RemoveCartProduct")) {
                    int strresId = jobjWhole.optInt("resInt");

                    if (strresId == 1) {
                        list.remove(posit);
                        AppPreferences.getPrefs().edit().putInt(commonVariables.CART_COUNT, list.size()).apply();
                        if (list.size() == 0) {
                            ((CartActivityBuyer) activity).setListAdapter(list, SP_BODY, VAL_BODY);
                        } else
                            notifyDataSetChanged();
                    } else {
                        AlertDialogManager.showDialog(activity, jobjWhole.optString("res"), null);
                    }

                } else if (strApiName.equalsIgnoreCase("Update_Cart_Suit")) {
                    int strresId = jobjWhole.optInt("resInt");

                    if (strresId == 1) {
                        list.get(posit).setCartQuantity(jobjWhole.optJSONObject("resData").optInt("CartQuantity"));
                        list.get(posit).setTotalAmount(jobjWhole.optJSONObject("resData").optString("TotalPrice"));
                        notifyDataSetChanged();
                    } else {
                        AlertDialogManager.showDialog(activity, jobjWhole.optString("res"), null);
                    }

                } else if (strApiName.equalsIgnoreCase("Update_Cart_Fabric")) {
                    int strresId = jobjWhole.optInt("resInt");
                    if (strresId == 1) {
                        CartItem item = list.get(posit);
                        JSONObject jo = jobjWhole.optJSONObject("resData");
                        item.setCartQuantity(jo.optInt("FabricQty"));
                        item.setTotalAmount(jo.optString("TotalPrice"));
                        item.setFabricCuts(jo.optDouble("FabricCuts"));

                        item.setFabric_FrontCut(jo.optDouble("Fabric_FrontCut"));
                        item.setFabric_FrontQty(jo.optInt("Fabric_FrontQty"));
                        item.setFabric_BackQty(jo.optInt("Fabric_BackQty"));
                        item.setFabric_BackCut(jo.optDouble("Fabric_BackCut"));
                        item.setFabric_BajuQty(jo.optInt("Fabric_BajuQty"));
                        item.setFabric_BajuCut(jo.optDouble("Fabric_BajuCut"));
                        item.setFabric_ExtraCut(jo.optDouble("Fabric_ExtraCut"));
                        item.setFabric_ExtraQty(jo.optInt("Fabric_ExtraQty"));

                        item.setCartQuantity(jo.optInt("FabricQty"));
                        item.setFabricCuts(jo.optDouble("FabricCuts"));

                        list.set(posit, item);
                        notifyItemChanged(posit);
                    } else {
                        AlertDialogManager.showDialog(activity, jobjWhole.optString("res"), null);
                    }

                }
                APIs.GetOrderSummary_Suit(null, (CartActivityBuyer) activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showPopup(String imageName, String productId) {
        try {
            dialog = new Dialog(
                    activity, R.style.popupTheme);
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_popup_product_detail, null);


            dialog.setContentView(view); // your custom view.
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();
            ImageView close = (ImageView) view.findViewById(R.id.iv_close);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_gallery);
            mTv_Brand = (EditText) view.findViewById(R.id.tv_brand_name);
            mTv_Product_Code = (EditText) view.findViewById(R.id.tv_product_code);
            mTv_Top_Fabrics = (EditText) view.findViewById(R.id.tv_top_fabrics);
            mTv_Bottom_Fabrics = (EditText) view.findViewById(R.id.tv_bottom_fabrics);
            mTv_Dupatta = (EditText) view.findViewById(R.id.tv_dupatta);
            mEdt_Dupatta = (TextInputLayout) view.findViewById(R.id.edt_dupatta);
            mTv_All_Fabrics = (EditText) view.findViewById(R.id.tv_all_fabrics);
            mEdt_All_Fabrics = (TextInputLayout) view.findViewById(R.id.edt_all_fabrics);
            mTv_Category = (EditText) view.findViewById(R.id.tv_category);
            mTv_Type = (EditText) view.findViewById(R.id.tv_type);
            mTv_Price = (EditText) view.findViewById(R.id.tv_price);
            mTv_Min_Qty = (EditText) view.findViewById(R.id.tv_min_qty);
            mLL_Fabrics = (LinearLayout) view.findViewById(R.id.ll_top_bottom_fab);
            String[] Images = {imageName};
            Glide.with(activity).load(imageName).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f).error(R.drawable.no_img_big).into(imageView);
            APIs.GetProductDetail_Suit_Seller(activity, this, productId);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window
                    dialog.dismiss();

                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ViewPagerActivity.class);
                    i.putExtra(commonVariables.INTENT_EXTRA_LIST_IMAGE_ARRAY, Images);
                    i.putExtra(commonVariables.INTENT_EXTRA_POSITION, 0);
                    i.putExtra(commonVariables.KEY_IS_LANDSCAPE, false);
                    activity.startActivity(i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showPopupFabric(String imageName, String productId) {
        try {
            dialog = new Dialog(
                    activity, R.style.popupTheme);
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_popup_product_detail, null);


            dialog.setContentView(view);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();
            ImageView close = (ImageView) view.findViewById(R.id.iv_close);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_gallery);
            mTv_Brand = (EditText) view.findViewById(R.id.tv_brand_name);
            mTv_Product_Code = (EditText) view.findViewById(R.id.tv_product_code);
            mTv_Category = (EditText) view.findViewById(R.id.tv_category);
            mTv_Type = (EditText) view.findViewById(R.id.tv_type);
            mTv_Price = (EditText) view.findViewById(R.id.tv_price);
            mTv_Min_Qty = (EditText) view.findViewById(R.id.tv_min_qty);
            mLl_fabric = (LinearLayout) view.findViewById(R.id.ll_fabric);
            mLl_fabric.setVisibility(View.GONE);
            String[] Images = {imageName};
            Glide.with(activity).load(imageName).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f).error(R.drawable.no_img_big).into(imageView);
            APIs.GetProductDetail_Fabric(activity, this, productId);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ViewPagerActivity.class);
                    i.putExtra(commonVariables.INTENT_EXTRA_LIST_IMAGE_ARRAY, Images);
                    i.putExtra(commonVariables.INTENT_EXTRA_POSITION, posit);
                    i.putExtra(commonVariables.KEY_IS_LANDSCAPE, false);
                    activity.startActivity(i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}