package com.tian.rsamaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tian.rsamaster.utils.RSAEncrypt;
import com.tian.rsamaster.utils.RSASignature;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String rsaPublicKey;
    private String rsaPrivateKey;

    private TextView tvGenPublic;
    private TextView tvGenPrivate;
    private TextView tvRsaEncode1;
    private TextView tvRsaDecode1;
    private EditText etSource;
    private TextView tvRsaEncode2;
    private TextView tvRsaDecode2;
    private TextView tvSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvGenPublic = (TextView) findViewById(R.id.tv_rsa_public_key);
        tvGenPrivate = (TextView) findViewById(R.id.tv_rsa_private_key);
        Button btGen = (Button) findViewById(R.id.bt_gen_rsa);
        btGen.setOnClickListener(this);

        etSource = (EditText) findViewById(R.id.et_encode_by_public);
        tvRsaEncode1 = (TextView) findViewById(R.id.tv_encode_string);
        tvRsaDecode1 = (TextView) findViewById(R.id.tv_decode_string);
        Button btEncodePublic = (Button) findViewById(R.id.bt_encode_by_public);
        btEncodePublic.setOnClickListener(this);
        Button btDecodePrivate = (Button) findViewById(R.id.bt_decode_by_private);
        btDecodePrivate.setOnClickListener(this);

        tvRsaEncode2 = (TextView) findViewById(R.id.tv_encode_private_string);
        tvRsaDecode2 = (TextView) findViewById(R.id.tv_decode_public_string);
        Button btEncodePrivate = (Button) findViewById(R.id.bt_encode_by_private);
        btEncodePrivate.setOnClickListener(this);
        Button btDecodePublic = (Button) findViewById(R.id.bt_decode_by_public);
        btDecodePublic.setOnClickListener(this);

        tvSign = (TextView) findViewById(R.id.tv_sign_string);
        Button btSigh = (Button) findViewById(R.id.bt_sign_by_private);
        btSigh.setOnClickListener(this);
        Button btCheck = (Button) findViewById(R.id.bt_docheck_by_public);
        btCheck.setOnClickListener(this);
        genRsaKey();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_gen_rsa:
                genRsaKey();
                break;
            case R.id.bt_encode_by_public:
                encodeByPublic();
                break;
            case R.id.bt_decode_by_private:
                decodeByPrivate();
                break;
            case R.id.bt_encode_by_private:
                encodeByPrivate();
                break;
            case R.id.bt_decode_by_public:
                decodeByPublic();
                break;
            case R.id.bt_sign_by_private:
                signByPrivate();
                break;
            case R.id.bt_docheck_by_public:
                doCheckByPublic();
                break;
        }
    }

    /**
     * 验签
     */
    private void doCheckByPublic() {
        String source = tvSign.getText().toString();
        if (TextUtils.isEmpty(source)) {
            Toast.makeText(this, "请先使用私钥签名", Toast.LENGTH_SHORT).show();
            return;
        }
        String signContent = etSource.getText().toString();
        if (RSASignature.doCheck(signContent, source, rsaPublicKey)) {
            Toast.makeText(this, "验签成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "验签失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 签名
     */
    private void signByPrivate() {
        String signContent = etSource.getText().toString();
        if (TextUtils.isEmpty(signContent)) {
            Toast.makeText(this, "请输入签名内容", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String signstr = RSASignature.sign(signContent, rsaPrivateKey);
            tvSign.setText(signstr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用公钥解密
     */
    private void decodeByPublic() {
        String source = tvRsaEncode2.getText().toString();
        if (TextUtils.isEmpty(source)) {
            Toast.makeText(this, "请先使用私钥加密", Toast.LENGTH_SHORT).show();
            return;
        }

        //公钥解密
        try {
            String decodeStr = RSAEncrypt.decryptByPublicKey(rsaPublicKey, source);
            tvRsaDecode2.setText(decodeStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用私钥加密
     */
    private void encodeByPrivate() {
        String source = etSource.getText().toString();
        if (TextUtils.isEmpty(source)) {
            return;
        }
        //私钥加密
        try {
            String cipher = RSAEncrypt.encryptByPrivateKey(rsaPrivateKey, source);
            tvRsaEncode2.setText(cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decodeByPrivate() {
        String source = tvRsaEncode1.getText().toString();
        if (TextUtils.isEmpty(source)) {
            Toast.makeText(this, "请先使用公钥加密", Toast.LENGTH_SHORT).show();
            return;
        }

        //私钥解密
        try {
            String decodeStr = RSAEncrypt.decryptByPrivateKey(rsaPrivateKey, source);
            tvRsaDecode1.setText(decodeStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encodeByPublic() {
        String source = etSource.getText().toString();
        if (TextUtils.isEmpty(source)) {
            return;
        }
        //公钥加密
        try {
            tvRsaEncode1.setText(RSAEncrypt.encryptByPublicKey(rsaPublicKey, source));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 生成rsa公私钥
     */
    private void genRsaKey() {

        //生成私钥公钥对
        String[] rsaArray = RSAEncrypt.genKeyPair();
        if (rsaArray == null) {
            return;
        }
        rsaPublicKey = rsaArray[0];
        rsaPrivateKey = rsaArray[1];
        tvGenPublic.setText(rsaPublicKey);
        tvGenPrivate.setText(rsaPrivateKey);
    }
}
