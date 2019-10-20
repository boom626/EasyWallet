package com.example.easywallet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.easywallet.DB.LedgerItem;
import com.example.easywallet.DB.LedgerRepository;

public class insertActivity extends AppCompatActivity {

    private int mLedgerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        Intent intent =getIntent();
        mLedgerType = intent.getIntExtra("type",0);

        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle(mLedgerType==0? "บันทึกรายรับ" : "บันทึกรายจ่าย"); //ถ้าใช่ทำ "บันทึกรายรับ" ถ้าไม่ใช่ "บันทึกรายจ่าย" ถ้าไม่ทำแบบนี้ไปทำif elseที่ class LedgerRecyclerViewAdapter
        }
        ImageView ledgerTypeImageView =findViewById(R.id.ledger_type_image_View);
        ledgerTypeImageView.setImageResource(
                mLedgerType==0? R.drawable.ic_income : R.drawable.ic_expense
        );

        Button saveButton =findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText descriptionEditText =findViewById(R.id.description_edit_text);
                String description =descriptionEditText.getText().toString();

                EditText amountEditText =findViewById(R.id.amount_edit_text);
                int amount =Integer.parseInt(amountEditText.getText().toString());

                LedgerItem item=new LedgerItem(0,description,amount*(mLedgerType==1? -1 : 1));

                LedgerRepository repo =new LedgerRepository(insertActivity.this);
                repo.insertLedger(item, new LedgerRepository.InsertCallback() {
                    @Override
                    public void onInsertSuccess() {
                        //reloadData();
                        finish();
                    }
                });
            }
        });
    }
}
