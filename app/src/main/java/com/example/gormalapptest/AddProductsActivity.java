package com.example.gormalapptest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gormalapptest.Room.DatabaseClient;
import com.example.gormalapptest.Room.ProductEntity;

public class AddProductsActivity extends AppCompatActivity {

    Button saveProductButton;
    EditText etProductName;
    EditText etProductDescription;
    EditText etProductPrice;
    EditText etProductQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_details);
        saveProductButton=(Button)findViewById(R.id.btn_save);
        etProductName=(EditText)findViewById(R.id.edt_product_name);
        etProductDescription=(EditText)findViewById(R.id.edt_product_description);
        etProductPrice=(EditText)findViewById(R.id.edt_product_price);
        etProductQuantity=(EditText)findViewById(R.id.edt_product_quantity);

        saveProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save data to db

                if(dataValidity())
                {
                    ProductEntity productEntity=new ProductEntity();
                    productEntity.setProductName(etProductName.getText().toString());
                    productEntity.setProductDesc(etProductDescription.getText().toString());
                    productEntity.setProductPrice(etProductPrice.getText().toString());
                    productEntity.setProductQuantity(etProductQuantity.getText().toString());
                    insertDataToDb(productEntity);
                }
            }
        });

    }

    boolean dataValidity() {
        if(etProductName.getText().toString()==null||
        etProductQuantity.getText().toString()==null||
        etProductDescription.getText().toString()==null||
        etProductPrice.getText().toString()==null)
        {
            Toast.makeText(this,"data cant be null",Toast.LENGTH_SHORT).show();
            return false;
        }
        if( etProductName.getText().toString().length()>20)
        {
            Toast.makeText(this,
                    "data inside product name cant be greater than 20 words",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if( etProductDescription.getText().toString().length()>100)
        {
            Toast.makeText(this,
                    "data inside product description cant be greater than 100 words",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if( etProductPrice.getText().toString().length()>5)
        {
            Toast.makeText(this,
                    "data inside product price cant be greater than 99999",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if( etProductQuantity.getText().toString().length()>4)
        {
            Toast.makeText(this,
                    "data inside product price cant be greater than 9999",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    void insertDataToDb(ProductEntity productEntity) {
        class insertDBData extends AsyncTask<Void, Void, Void> {
            Context context;
            ProductEntity productEntity;

            insertDBData(Context context, ProductEntity productEntity) {
                this.context = context;
                this.productEntity = productEntity;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .productDao()
                        .insert(productEntity);
                finish();
                return null;
            }


        }

        insertDBData su = new insertDBData(this, productEntity);
        su.execute();
    }


}