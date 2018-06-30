package com.flj.latte.ec.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.ec.common.util.ToastUtil;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IFailure;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;

import java.util.List;
import java.util.WeakHashMap;

/**
 * 商品评论
 */

public class GoodsCommentDelegate extends LatteDelegate implements View.OnClickListener {

    private static final String ARG_GOODS_DATA = "ARG_GOODS_DATA";
    private JSONObject mData = null;
    private RecyclerView rv_comment;
   // private AppCompatTextView tv_goods_comment_count;
    private int comment_count = 0;
    GoodsCommentAdapter adapter;
    String goodsData;
    @Override
    public Object setLayout() {
        return R.layout.delegate_goods_comment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            goodsData = args.getString(ARG_GOODS_DATA);
            mData = JSON.parseObject(goodsData);
            try {
                comment_count = mData.getInteger("comment_count");
            } catch (Exception e) {
                comment_count = 0;
            }
            getCommentData(mData.getInteger("id"));
        }
    }

    //获取评论内容
    private void getCommentData(int goodsData) {
        final String goodesdetailUrl = API.Config.getDomain() + API.GET_COMMENT_DETAIL;
        LatteLogger.d("IUDHAS", goodesdetailUrl);
        final WeakHashMap<String, Object> goodesComment = new WeakHashMap<>();
        final Long mUserId = LattePreference.getCustomAppProfileLong("userId");
        //mGoodsId=96;
        goodesComment.put("idValue", goodsData);
        //获取商品评论固定0
        goodesComment.put("commentType", 0);
        goodesComment.put("page", 1);
        goodesComment.put("per_page", 3);
        goodesComment.put("userId", mUserId);

        final String jsonString = JSON.toJSONString(goodesComment);
        LogUtils.e("jsonString", jsonString);
        RestClient.builder()
                .url(goodesdetailUrl)
                //.params("goods_id", mGoodsId)
                .loader(getContext())
                .raw(jsonString)
                // .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        LatteLogger.json("goodesdetail", response);
                        LogUtils.e("goodesdetail", response);
                        List<MultipleItemEntity> list = new CommentDataConverter().setJsonData(response).
                                convert();
                        if (null == list)
                            return;
                        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        rv_comment.setLayoutManager(manager);
                        adapter = new GoodsCommentAdapter(list, getParentDelegate());
                        rv_comment.setAdapter(adapter);
                    }

                })
                . failure(new IFailure() {
                            @Override
                            public void onFailure() {
                                ToastUtil.show(getContext(), "服务器忙，请稍后再试");
                            }
                        })
                . build()
                .post();

    }

    private void initView() {
      //  $(R.id.tv__more_comment_tip).setOnClickListener(this);
     //   $(R.id.tv_more_comment_arrow).setOnClickListener(this);
    //    tv_goods_comment_count = $(R.id.tv_goods_comment_count);
        rv_comment = $(R.id.rv_comment);
    }

    public static GoodsCommentDelegate create(String goodsData) {
        final Bundle args = new Bundle();
        args.putString(ARG_GOODS_DATA, goodsData);
        final GoodsCommentDelegate delegate = new GoodsCommentDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initView();
        int id = mData.getInteger("id");
        try {
            comment_count = mData.getInteger("comment_count");
        } catch (Exception e) {
            comment_count = 0;
        }
        //tv_goods_comment_count.setText("评论(" + comment_count + ")");


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv__more_comment_tip
                || view.getId() == R.id.tv_more_comment_arrow) {
            //跳转更多评论
            if(null==adapter ||null==adapter.getData()||adapter.getData().size()==0){
                ToastUtil.showToast(getContext(),"暂无评论信息");
                return;
            }
            getSupportDelegate().start(new MoreGoosCommentDelegate().create(goodsData));

        }
    }
}
