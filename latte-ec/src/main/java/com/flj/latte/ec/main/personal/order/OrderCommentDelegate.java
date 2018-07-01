package com.flj.latte.ec.main.personal.order;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.ec.common.util.ToastUtil;
import com.flj.latte.ec.utils.Utils;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IFailure;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.widget.AutoPhotoLayout;
import com.flj.latte.ui.widget.StarBar;
import com.flj.latte.util.callback.CallbackManager;
import com.flj.latte.util.callback.CallbackType;
import com.flj.latte.util.callback.IGlobalCallback;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;


/**
 * Created by yb
 */

public class OrderCommentDelegate extends LatteDelegate {

    private StarBar mStarLayout = null;
    private AutoPhotoLayout mAutoPhotoLayout = null;
    private AppCompatEditText comment;
    int idValue;
    int commentType;
    int position;
    android.support.v7.widget.Toolbar tb_shop_cart;

    void onClickSubmit(String fileId) {
        final String goodesdetailUrl = API.Config.getDomain() + API.INSERT_COMMENT;
        LatteLogger.d("IUDHAS", goodesdetailUrl);
        final WeakHashMap<String, Object> goodesComment = new WeakHashMap<>();
        final String userName = LattePreference.getCustomAppProfile("name");
        final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        //mGoodsId=96;
        goodesComment.put("idValue", "" + idValue);
        //获取商品评论固定0
        goodesComment.put("commentType", commentType);
        goodesComment.put("userName", userName);
        goodesComment.put("content", comment.getText().toString().trim());
        goodesComment.put("ipAddress", Utils.getIpString());
        goodesComment.put("parentId", 1);
        goodesComment.put("commentRank", mStarLayout.getStarMark());
        goodesComment.put("fileId", StringUtils.isEmpty(fileId) ? "" : fileId);
        goodesComment.put("userId", mUserId);
        final String jsonString = JSON.toJSONString(goodesComment);
        LogUtils.e("jsonString", jsonString);
        RestClient.builder()
                .url(goodesdetailUrl)
                .loader(getContext())
                .raw(jsonString)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.json("goodesdetail", response);
                        LogUtils.e("response", response);
                        // getSupportDelegate().pop();
                        if (StringUtils.isEmpty(response)) return;
                        int code = JSON.parseObject(response).getInteger("code");
                        String msg = JSON.parseObject(response).getString("msg");
                        if (0 == code) {
                            if (msg.contains("成功") || msg.contains("success")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("position", position);
                                setFragmentResult(Activity.RESULT_OK, bundle);
                                getSupportDelegate().pop();
                            }
                            ToastUtil.showToast(getContext(), msg);

                        }
                    }

                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        ToastUtil.show(getContext(), "服务器忙，请稍后再试");
                    }
                })
                .build()
                .post();


        // Toast.makeText(getContext(), "评分： " + mStarLayout.getStarCount(), Toast.LENGTH_LONG).show();
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_comment;
    }

    public static OrderCommentDelegate create(int idValue, int commentType, int position) {
        final Bundle args = new Bundle();
        args.putInt("idValue", idValue);
        args.putInt("commentType", commentType);
        args.putInt("position", position);
        final OrderCommentDelegate delegate = new OrderCommentDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final Bundle args = getArguments();
        if (args != null) {
            idValue = args.getInt("idValue");
            commentType = args.getInt("commentType");
            position = args.getInt("position");
        }
        mStarLayout = $(R.id.custom_star_layout);
        mStarLayout.setIntegerMark(true);
        mStarLayout.setOnClick(true);
        mAutoPhotoLayout = $(R.id.custom_auto_photo_layout);
        tb_shop_cart = getProxyActivity().findViewById(R.id.tb_shop_cart);
        tb_shop_cart.setBackgroundColor(Color.parseColor("#ff9999"));
        comment = $(R.id.et_order_comment);

        $(R.id.top_tv_comment_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                if (null == imgUrls || imgUrls.size() == 0) {
                    onClickSubmit(null);
                } else {
                    uploadImage(imgUrls);
                }
            }
        });
        mAutoPhotoLayout.setDelegate(this);
        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void executeCallback(@Nullable Uri args) {
                        imgUrls.add(args.getPath());
                        mAutoPhotoLayout.onCropTarget(args);
                    }
                });
    }

    private List<String> imgUrls = new ArrayList<>();

    private void uploadImage(List<String> path) {
        final String goodesdetailUrl = API.Config.getDomain() + API.UPLOAD_COMMENT_IMG;
        LatteLogger.d("upload", goodesdetailUrl);
        WeakHashMap<String, File> params = new WeakHashMap<>();
        //new File(path.trim()
        //params.put("file", new File(path.trim()));
        List<File> files = new ArrayList<>();
        for (String str : path) {
            files.add(new File(str.trim()));
        }
        RestClient.builder()
                .url(goodesdetailUrl)
                //.params("goods_id", params)
                .file(files)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.json("goodesdetail", response);
                        LogUtils.e("response", response);
                        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("data");
                        final String fileId = profileJson.getString("fileId");
                        onClickSubmit(fileId);
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        ToastUtil.show(getContext(), "服务器忙，请稍后再试");
                    }
                })
                .build()
                .uploads();
    }
}
