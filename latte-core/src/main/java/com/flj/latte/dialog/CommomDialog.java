package com.flj.latte.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.diabin.latte.R;


/**
 * Created by TQ on 2017/11/16.
 */

public class  CommomDialog extends Dialog implements View.OnClickListener{
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private View mLine;

    private Context mContext;
    private String content;
    private onSubmitListener submitListener;
    private onCancelListener cancelListener;
    private String positiveName;
    private String negativeName;
    private String title;
    private boolean isSimply;

    public CommomDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommomDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public CommomDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CommomDialog(Context context, int themeResId, String content, onSubmitListener submitListener, onCancelListener cancelListener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.submitListener = submitListener;
        this.cancelListener = cancelListener;
    }

    protected CommomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CommomDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public CommomDialog setContent(String content){
        this.content = content;
        return this;
    }

    public CommomDialog setPositiveButton(String name, onSubmitListener submitListener){
        this.positiveName = name;
        this.submitListener = submitListener;
        return this;
    }

    public CommomDialog setNegativeButton(String name, onCancelListener cancelListener){
        this.negativeName = name;
        this.cancelListener = cancelListener;
        return this;
    }

    public CommomDialog setSimply(boolean simply) {
        isSimply = simply;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commom);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        contentTxt = (TextView)findViewById(R.id.content);
        titleTxt = (TextView)findViewById(R.id.title);
        submitTxt = (TextView)findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        mLine = findViewById(R.id.bottom_divided_line);
        cancelTxt = (TextView)findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);

        contentTxt.setText(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }

        if(!TextUtils.isEmpty(title)){
            titleTxt.setText(title);
        }

        if (isSimply){
            titleTxt.setVisibility(View.GONE);
            cancelTxt.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel) {
            if (cancelListener != null) {
                cancelListener.onClickCancel(this);
            }
            this.dismiss();

        } else if (i == R.id.submit) {
            if (submitListener != null) {
                submitListener.onClickSubmit(this);
            }

        }
    }


    public interface onSubmitListener{
        void onClickSubmit(Dialog dialog);
    }

    public interface onCancelListener{
        void onClickCancel(Dialog dialog);
    }
}
