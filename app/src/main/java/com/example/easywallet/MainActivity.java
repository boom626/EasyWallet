package com.example.easywallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.easywallet.DB.AppDatabase;
import com.example.easywallet.DB.LedgerDao;
import com.example.easywallet.DB.LedgerItem;
import com.example.easywallet.DB.LedgerRepository;
import com.example.easywallet.adapter.LedgerRecyclerViewAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //reloadData();

        Button incomeButton=findViewById(R.id.income_button);
        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,insertActivity.class);
                intent.putExtra("type",0);  //เรากำหนด 0=รายรับ
                startActivity(intent);
//                LedgerItem item=new LedgerItem(0,"ถูกหวย",2000);
////        id เป็น autoGenerate ใส่่อะไรก็ได้เดี่ยวมันgen code ให้เอง
//
////                InsertTask task = new InsertTask(MainActivity.this, new InsertCallback() {
////                    @Override
////                    public void onInsertSuccess() {
////                        reloadData();
////                    }
////                });
////                //task.execute(item,item,item,item);
////                task.execute(item);
//                LedgerRepository repo =new LedgerRepository(MainActivity.this);
//                repo.insertLedger(item, new LedgerRepository.InsertCallback() {
//                    @Override
//                    public void onInsertSuccess() {
//                        reloadData();
//                    }
//                });
            }
        });
//        AppDatabase db =AppDatabase.getInstance(MainActivity.this);
//        List<LedgerItem> itemList =db.ledgerDao().getAll();
        Button expenseButton=findViewById(R.id.expense_button);
        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,insertActivity.class);
                intent.putExtra("type",1); //เรากำหนด 1=รายจ่าย
                startActivity(intent);
//                LedgerItem item=new LedgerItem(0,"จ่ายค่าหอ",-1000);
//
//                LedgerRepository repo =new LedgerRepository(MainActivity.this);
//                repo.insertLedger(item, new LedgerRepository.InsertCallback() {
//                    @Override
//                    public void onInsertSuccess() {
//                        reloadData();
//                    }
//                });
            }
        });
    }

    @Override
    protected void onResume() {  //ต้องใส่reloadData()ไว้ในonResume() ถึงอัตโนมัติทั้งตอนแรก และ กลับมา
        super.onResume();
        reloadData();
    }

    //-------------------------------------
//    private void reloadData() {
//        GetTask getTask=new GetTask(MainActivity.this, new Callback() {
//            @Override
//            public void onGetLedger(List<LedgerItem> itemList) {
//                RecyclerView recyclerView=findViewById(R.id.ledger_recycler_view);
//                LedgerRecyclerViewAdapter adapter =new LedgerRecyclerViewAdapter(
//                        MainActivity.this,
//                        R.layout.item_ledger,
//                        itemList
//                );
//                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this)); //ให้แสดงผลเปนแนวตั้ง(แต่ตัวitemเป็นแนวนอน) หรือ แนวนอน
//                recyclerView.setAdapter(adapter);
//            }
//        });
//        getTask.execute();  }
    private void reloadData() {
        LedgerRepository repo =new LedgerRepository(MainActivity.this);
        repo.getLedger(new LedgerRepository.Callback() {
            @Override
            public void onGetLedger(List<LedgerItem> itemList) {

                int totalAmount =0;

                for(LedgerItem item: itemList){
                    totalAmount+=item.amount;
                }

                TextView balanceTextView=findViewById(R.id.balance_text_view);
                balanceTextView.setText("คงเหลือ ".concat(String.valueOf(totalAmount)).concat(" บาท"));

                RecyclerView recyclerView=findViewById(R.id.ledger_recycler_view);
                LedgerRecyclerViewAdapter adapter =new LedgerRecyclerViewAdapter(
                        MainActivity.this,
                        R.layout.item_ledger,
                        itemList
                );
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this)); //ให้แสดงผลเปนแนวตั้ง(แต่ตัวitemเป็นแนวนอน) หรือ แนวนอน
                recyclerView.setAdapter(adapter);
            }
        });
    }
    //-------------------------------------move on class LedgerRepository--------------------------------------
//    private  static class GetTask extends AsyncTask<Void,Void,List<LedgerItem>>{
//
//        private  Context mContext;
//        private Callback mCallback;
//
//        public GetTask(Context context,Callback callback){
//            this.mContext=context;
//            this.mCallback=callback;
//        }
//
//        @Override
//        protected List<LedgerItem> doInBackground(Void... voids) {
//            AppDatabase db = AppDatabase.getInstance(mContext);
//            List<LedgerItem> itemList =db.ledgerDao().getAll();
//            return itemList;
//        }
//
//        @Override
//        protected void onPostExecute(List<LedgerItem> ledgerItemsList) {
//            super.onPostExecute(ledgerItemsList);
//
//            mCallback.onGetLedger(ledgerItemsList);
//        }
//    }
//    //-------------------------------------
//    private interface Callback{
//        void onGetLedger(List<LedgerItem> itemList);
//    }
//    //-------------------------------------
//    private static  class InsertTask extends AsyncTask<LedgerItem,Void, Void>{
//        private Context mContext;
//        private InsertCallback mCallback;
//
//        public InsertTask(Context context,InsertCallback callback) {
//            this.mContext=context;
//            this.mCallback=callback;
//        }
//
//        @Override
//        protected Void doInBackground(LedgerItem... ledgerItem) {
//            AppDatabase db=AppDatabase.getInstance(mContext);
//            db.ledgerDao().insert(ledgerItem[0]); //ถ้าหลายitem วนloop
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            mCallback.onInsertSuccess();
//        }
//    }
//    //-------------------------------------
//    private interface InsertCallback{
//        void onInsertSuccess();
//    }
}
