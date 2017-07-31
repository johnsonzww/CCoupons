package com.example.administrator.ccoupons.AddCoupon;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.example.administrator.ccoupons.CustomEditText.ClearableEditText;
import com.example.administrator.ccoupons.R;
import com.example.administrator.ccoupons.Tools.DataBase.ImageDiskCache;
import com.example.administrator.ccoupons.Tools.ImageManager;
import com.example.administrator.ccoupons.Tools.PixelUtils;
import com.example.administrator.ccoupons.Tools.TakePhotoUtil;
import com.example.administrator.ccoupons.User.UserPortraitActivity;
import com.jph.takephoto.model.TResult;

import java.util.ArrayList;
import java.util.List;

import static com.mob.MobSDK.getContext;

public class FillFormActivity extends AppCompatActivity {

    private EditText categoryEditText;
    private ClearableEditText productNameText,
            brandNameText,
            discountText;
    private TextView nextButton;
    private RecyclerView recyclerView;
    private ConstraintsAdapter adapter;
    private NestedScrollView scrollView;
    private ArrayList<String> constraintList;
    private LinearLayout addpicture;
    private ImageView couponPicture;
    private TakePhotoUtil takePhotoUtil;
    private String path;
    private ImageDiskCache imageDiskCache = ImageDiskCache.getInstance(getContext());

    private void bindViews() {
        categoryEditText = (EditText) findViewById(R.id.form_category_edittext);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.arrow);
        drawable.setBounds(0, 0, 40, 40);
        categoryEditText.setCompoundDrawables(null, null, drawable, null);
        scrollView = (NestedScrollView) findViewById(R.id.form_scrollview);
        nextButton = (TextView) findViewById(R.id.form_next_button);
        productNameText = (ClearableEditText) findViewById(R.id.form_product_edittext);
        brandNameText = (ClearableEditText) findViewById(R.id.form_brand_edittext);
        discountText = (ClearableEditText) findViewById(R.id.form_discount_edittext);
        recyclerView = (RecyclerView) findViewById(R.id.form_constraints_recyclerview);
        addpicture = (LinearLayout) findViewById(R.id.coupon_add_picture);
        couponPicture = (ImageView) findViewById(R.id.coupon_picture);
        constraintList = new ArrayList<>();
        constraintList.add("first");
        constraintList.add("second");
        constraintList.add("third");
        //TODO: adapter

        adapter = new ConstraintsAdapter(constraintList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ImageView addButton = (ImageView) findViewById(R.id.add_constraint_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = constraintList.size();
                System.out.println("Current size = " + size);
                constraintList.add("new added");
                adapter.notifyItemInserted(size);
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    }
                });
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> NConstraintList = new ArrayList<String>();
                for (String str : constraintList) {
                    if (str.length() > 0)
                        NConstraintList.add(str);
                }
                //   Intent intent = new Intent(FillFormActivity.this, AddCouponActivity.class);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);

        bindViews();
        takePhotoUtil = new TakePhotoUtil(this);
        takePhotoUtil.onCreate(savedInstanceState);
        setAddpicture();
    }

    private void setAddpicture() {
        addpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.portrait_bottom_dialog, null);
                TextView tv_account = (TextView) view.findViewById(R.id.tv_take_photo);
                TextView tv_compare = (TextView) view.findViewById(R.id.tv_from_album);
                final Dialog mBottomSheetDialog = new Dialog(FillFormActivity.this, R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView(view);
                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                mBottomSheetDialog.show();
                tv_account.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FillFormActivity.this, "拍照", Toast.LENGTH_SHORT).show();
                        takePhotoUtil.takePhoto(TakePhotoUtil.Select_type.PICK_BY_TAKE_NOT_CROP, new TakePhotoUtil.SimpleTakePhotoListener() {
                            @Override
                            public void takeSuccess(TResult result) {
                                path = result.getImage().getCompressPath();
                                System.out.println("success:" + path);
                                imageDiskCache.writeToDiskCache(path, BitmapFactory.decodeFile(path));
                                System.out.println("success");
                                updatePic();
                            }
                        });
                        mBottomSheetDialog.dismiss();
                    }
                });
                tv_compare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FillFormActivity.this, "从相册中选择", Toast.LENGTH_SHORT).show();
                        takePhotoUtil.takePhoto(TakePhotoUtil.Select_type.PICK_BY_SELECT_NOT_CROP, new TakePhotoUtil.SimpleTakePhotoListener() {
                            @Override
                            public void takeSuccess(TResult result) {
                                path = result.getImage().getCompressPath();
                                System.out.println("success:" + path);
                                imageDiskCache.writeToDiskCache(path, BitmapFactory.decodeFile(path));
                                System.out.println("success");
                                updatePic();
                            }
                        });
                        mBottomSheetDialog.dismiss();
                    }
                });
            }
        });
    }

    private void updatePic() {
        ImageManager.GlideImage(path, couponPicture, getContext());
    }


    public class ConstraintsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private ArrayList<String> constraintList;

        public ConstraintsAdapter(ArrayList<String> cList) {
            this.constraintList = cList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mContext == null) {
                mContext = parent.getContext();
            }
            View view = LayoutInflater.from(mContext).inflate(R.layout.constraint_item, parent, false);
            return new ConstraintsViewHolder(view);

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final String constraint = constraintList.get(position);
            final ConstraintsViewHolder viewHolder = (ConstraintsViewHolder) holder;
            viewHolder.editText.setText(constraintList.get(position));
            viewHolder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Delete index " + position);
                    constraintList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(0, constraintList.size());
                    System.out.println("Current size = " + constraintList.size());
                }
            });
        }

        @Override
        public int getItemCount() {
            return constraintList.size();
        }


        public class ConstraintsViewHolder extends RecyclerView.ViewHolder {
            LinearLayout rootView;
            EditText editText;
            ImageView deleteButton;

            public ConstraintsViewHolder(View view) {
                super(view);
                rootView = (LinearLayout) view;
                editText = (EditText) view.findViewById(R.id.form_constraint_edittext);
                deleteButton = (ImageView) view.findViewById(R.id.delete_constraint_button);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        takePhotoUtil.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        takePhotoUtil.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        takePhotoUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}