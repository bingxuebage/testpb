package com.jeson.testpb;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    EditText id_et, name_et, sex_et;
    Button submit_bt;
    ListView list_lv;

    // ***************************Variables**********************
    Context context;
    List<StudentInfo> list;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        id_et = (EditText) findViewById(R.id.id_et);
        name_et = (EditText) findViewById(R.id.name_et);
        sex_et = (EditText) findViewById(R.id.sex_et);
        submit_bt = (Button) findViewById(R.id.submit_bt);
        list_lv = (ListView) findViewById(R.id.list_lv);

        submit_bt.setOnClickListener(new MyOnClickListener());
        list = new ArrayList<StudentInfo>();
        adapter = new MyAdapter(context, list);
        list_lv.setAdapter(adapter);
    }

    class MyOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = Integer.parseInt(id_et.getText().toString());
            String name = name_et.getText().toString();
            String sex = sex_et.getText().toString();
            InfoHelper.saveStudentIntoFile(id, name, sex);
            list = InfoHelper.getStudentsFromFile();
            adapter.setList(list);
            id_et.setText("");
            name_et.setText("");
            sex_et.setText("");
            id_et.requestFocus();
        }

    }

    class MyAdapter extends BaseAdapter {

        Context context;
        List<StudentInfo> list;

        public MyAdapter(Context context, List<StudentInfo> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<StudentInfo> list) {
            this.list = list;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;

            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.adapter_item, null, false);
                holder = new ViewHolder();
                holder.id_tv = (TextView) convertView.findViewById(R.id.id_tv);
                holder.name_tv = (TextView) convertView
                        .findViewById(R.id.name_tv);
                holder.sex_tv = (TextView) convertView
                        .findViewById(R.id.sex_tv);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            StudentInfo student = (StudentInfo) this.getItem(position);
            holder.id_tv.setText(student.getId() + "");
            holder.name_tv.setText(student.getName());
            holder.sex_tv.setText(student.getSex());

            return convertView;
        }

        class ViewHolder {
            TextView id_tv, name_tv, sex_tv;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
