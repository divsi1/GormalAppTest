package com.example.gormalapptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.gormalapptest.Room.DatabaseClient;
import com.example.gormalapptest.Room.ProductEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button addProductButton;
    Button syncProductToCloudButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addProductButton=(Button)findViewById(R.id.btn_add);
        syncProductToCloudButton=(Button)findViewById(R.id.btn_sync);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddProductsActivity.class));
            }
        });
        syncProductToCloudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncData();
            }
        });
    }

    private void syncData() {
        class syncProducts extends AsyncTask<Void, Void, List<ProductEntity>> {

            @Override
            protected List<ProductEntity> doInBackground(Void... voids) {
                List<ProductEntity> productEntityList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .productDao()
                        .getAll();
                return productEntityList;
            }

            @Override
            protected void onPostExecute(List<ProductEntity> productEntities) {
                super.onPostExecute(productEntities);
                if (productEntities.size() != 0) {
                    Log.d("divya", productEntities.toString());
                    for(int i=0;i<productEntities.size();i++)
                    {
                        callToApi(productEntities.get(i));
                    }

                } else {
                    Log.d("divya", "no produsts available");
                }
            }
        }

        syncProducts su = new syncProducts();
        su.execute();
    }

    public void callToApi(final ProductEntity productEntity) {

        RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        StringRequest sr = new StringRequest(Request.Method.POST,"http://206.189.128.26/api/addNewProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("divya_api_response",response);
                onResponseReceived.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("divya_api_error",error.toString());
                onResponseReceived.onFailure();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
//                to store product information online:
//                http://206.189.128.26/api/addNewProduct
//                Request Parameters:
//                1. product_name (max 30 characters)
//                2. product_desc (max 100 characters)
//                3. product_quantity (max 4 digits)
//                4. product_price (max 5 digits)
//                5. user_mobile_no (Please send your 10 digits registered mobile number here. Do not
//                        show this field in add product form)
                params.put("product_name",productEntity.getProductName());
                params.put("product_desc",productEntity.getProductDesc());
                params.put("product_quantity", productEntity.getProductQuantity());
                params.put("product_price",productEntity.getProductPrice());
                params.put("user_mobile_no","9999999999");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    OnResponseReceived onResponseReceived=new OnResponseReceived() {
        @Override
        public void onSuccess() {
            Toast.makeText(getApplicationContext(),
                    "result success",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure() {
            Toast.makeText(getApplicationContext(),
                    "result failure",
                    Toast.LENGTH_SHORT).show();
        }
    };

}