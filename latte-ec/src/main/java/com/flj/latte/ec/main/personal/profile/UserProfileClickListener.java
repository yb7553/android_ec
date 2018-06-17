package com.flj.latte.ec.main.personal.profile;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;
import com.flj.latte.ec.common.http.api.API;
import com.flj.latte.ec.main.personal.list.ListBean;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IError;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.date.DateDialogUtil;
import com.flj.latte.util.callback.CallbackManager;
import com.flj.latte.util.callback.CallbackType;
import com.flj.latte.util.callback.IGlobalCallback;
import com.flj.latte.util.file.FileUtil;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.util.storage.LattePreference;

import java.io.File;
import java.util.WeakHashMap;

/**
 * Created by yb
 */

public class UserProfileClickListener extends SimpleClickListener {

    private final UserProfileDelegate DELEGATE;

    private String[] mGenders = new String[]{"男", "女"};

    public UserProfileClickListener(UserProfileDelegate DELEGATE) {
        this.DELEGATE = DELEGATE;
    }

    @Override
    public void onItemClick(final BaseQuickAdapter adapter, final View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);
        final int id = bean.getId();
        switch (id) {
            case 1:
                //开始照相机或选择图片
                CallbackManager.getInstance()
                        .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                            @Override
                            public void executeCallback(@Nullable Uri args) {
                                LatteLogger.d("ON_CROP", args);
                                final ImageView avatar = (ImageView) view.findViewById(R.id.img_arrow_avatar);
                                Glide.with(DELEGATE)
                                        .load(args)
                                        .into(avatar);
                                final String upUrl=UploadConfig.UPLOAD_IMG;
                                //final File realFile = FileUtils.getFileByPath(FileUtil.getRealFilePath(DELEGATE.getContext(), args));
                                LatteLogger.d("ON_CROP2", args.getPath());
                                RestClient.builder()
                                        .url(upUrl)
                                        .loader(DELEGATE.getContext())
                                        .file(args.getPath())
                                        .success(new ISuccess() {
                                            @Override
                                            public void onSuccess(String response) {
                                                LatteLogger.d("ON_CROP_UPLOAD", response);
                                                final String path = JSON.parseObject(response).getJSONArray("data")
                                                        .getJSONObject(0).getString("path");

                                                String url = API.Config.getDomain() + API.USER_AVATAR_UPDATE;
                                                LatteLogger.d("url",url);
                                                final WeakHashMap<String, Object> weakHashMap = new WeakHashMap<>();
                                                final Long mUserId=LattePreference.getCustomAppProfileLong("userId");
                                                weakHashMap.put("userId",mUserId);
                                                weakHashMap.put("avatar",path);
                                                final String jsonString = JSON.toJSONString(weakHashMap);

                                                //通知服务器更新信息
                                                RestClient.builder()

                                                        .url(url)
                                                        //.params("avatar", path)
                                                        .loader(DELEGATE.getContext())
                                                        .raw(jsonString)
                                                        .success(new ISuccess() {
                                                            @Override
                                                            public void onSuccess(String response) {
                                                                //获取更新后的用户信息，然后更新本地数据库
                                                                //没有本地数据的APP，每次打开APP都请求API，获取信息
                                                                LatteLogger.d("ON_CROP3", response);
                                                                LattePreference.addCustomAppProfile("avatar", path);
                                                            }
                                                        })
                                                        .build()
                                                        .post();
                                            }
                                        })
                                        .error(new IError() {
                                            @Override
                                            public void onError(int code, String msg) {
                                                LatteLogger.d("err_CROP_UPLOAD", code);
                                                LatteLogger.d("err_CROP_UPLOAD", msg);
                                            }
                                        })
                                        .build()
                                        .upload();
                            }
                        });
                DELEGATE.startCameraWithCheck();
                break;
            case 2:
                final LatteDelegate nameDelegate = bean.getDelegate();
                DELEGATE.getSupportDelegate().start(nameDelegate);

                break;
            case 3:
                getGenderDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final TextView textView = (TextView) view.findViewById(R.id.tv_arrow_value);
                        textView.setText(mGenders[which]);
                        dialog.cancel();
                    }
                });
                break;
            case 4:
                final DateDialogUtil dateDialogUtil = new DateDialogUtil();
                dateDialogUtil.setDateListener(new DateDialogUtil.IDateListener() {
                    @Override
                    public void onDateChange(String date) {
                        final TextView textView = (TextView) view.findViewById(R.id.tv_arrow_value);
                        textView.setText(date);
                    }
                });
                dateDialogUtil.showDialog(DELEGATE.getContext());
                break;
            default:
                break;
        }
    }

    private void getGenderDialog(DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DELEGATE.getContext());
        builder.setSingleChoiceItems(mGenders, 0, listener);
        builder.show();
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
