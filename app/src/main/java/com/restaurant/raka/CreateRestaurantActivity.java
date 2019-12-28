package com.restaurant.raka;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.restaurant.raka.data.Constans;
import com.restaurant.raka.data.Session;
import com.restaurant.raka.model.RestoranResponse;
import com.restaurant.raka.utils.DialogUtils;

public class CreateRestaurantActivity extends AppCompatActivity {

    Session session;
    EditText namarm, kategori, link_foto, alamat;
    Button create_restaurant;
    ProgressDialog progressDialog;
    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        userId = getIntent().getStringExtra("userId");
        initBinding();
        initClick();
    }

    private void initBinding() {
        namarm = findViewById(R.id.et_namarm);
        kategori = findViewById(R.id.et_kategori);
        link_foto = findViewById(R.id.et_link_foto);
        alamat = findViewById(R.id.et_alamat);
        create_restaurant = findViewById(R.id.btn_create_restaurant);
    }

    private void initClick() {
        create_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namarm.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "Isi dengan Nama Restaurant", Toast.LENGTH_SHORT).show();
                }else if(kategori.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "Masukan Kategori Restaurant", Toast.LENGTH_SHORT).show();
                }else if(link_foto.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "Masukan url Foto", Toast.LENGTH_SHORT).show();
                }else if(alamat.getText().toString().isEmpty()){
                    Toast.makeText(CreateRestaurantActivity.this, "Isi dengan Alamat Restaurant ", Toast.LENGTH_SHORT).show();
                } else {
                    createRestaurant();
                }
            }
        });
    }
    public void createRestaurant() {
        DialogUtils.openDialog(this);
        AndroidNetworking.post(Constans.CREATE_RESTAURANT)
                .addBodyParameter("userid", userId)
                .addBodyParameter("namarm", namarm.getText().toString())
                .addBodyParameter("kategori", kategori.getText().toString())
                .addBodyParameter("link_foto", link_foto.getText().toString())
                .addBodyParameter("alamat", alamat.getText().toString())
                .build()
                .getAsObject(RestoranResponse.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        if (response instanceof RestoranResponse) {
                            RestoranResponse res = (RestoranResponse) response;
                            if (res.getStatus().equals("success")) {
                                Toast.makeText(CreateRestaurantActivity.this,"Restaurant Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(CreateRestaurantActivity.this,"Restaurant Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                            }
                        }
                        DialogUtils.closeDialog();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(CreateRestaurantActivity.this, "Eror API", Toast.LENGTH_SHORT).show();
                        Toast.makeText(CreateRestaurantActivity.this, "Eror API : "+anError.getCause().toString(), Toast.LENGTH_SHORT).show();
                        DialogUtils.closeDialog();
                    }
                });
    }

}
