package com.example.a97557.movie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 97557 on 2017/4/7.
 */

public class EmployeeActivity extends AppCompatActivity {
    private ImageView imageView, ponitView;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private List<Employee> employees = new ArrayList<>();
    private SQLiteDatabase db;
    private PopupWindow popupWindow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_employee);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/employee.db3", null);
        CloseActivityClass.activityList.add(this);
        imageView = (ImageView) findViewById(R.id.back_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ponitView = (ImageView) findViewById(R.id.three_points);

        ponitView.setOnClickListener(popClick);

        listView = (ListView)findViewById(R.id.employee_list);

//        employees = getEmployees();
        new Thread(getAllEmployees).start();
        setListViewEvent();
    }

    class ListViewAdapter extends BaseAdapter {

        private Context context;
        private List<Employee> employees;

        //构造方法
        public ListViewAdapter(Context context,List<Employee> employees){
            this.context = context;
            this.employees = employees;
        }

        //获取list的item的个数
        @Override
        public int getCount() {
            return employees.size();
        }

        //获取position位置的item的数据
        @Override
        public Object getItem(int position) {
            return employees.get(position);
        }
        //获取position位置的item
        @Override
        public long getItemId(int position) {
            return position;
        }

        //设置item的视图
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                //获取item的资源
                convertView = LayoutInflater.from(context).inflate(R.layout.employees_info, null);
                viewHolder = new ViewHolder();
                //id的绑定
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.employee_image);
                viewHolder.textView = (TextView)convertView.findViewById(R.id.employee_name);
                viewHolder.subscriptView = (TextView)convertView.findViewById(R.id.subscript);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //设置item控件数据的绑定
            viewHolder.imageView.setImageResource(employees.get(position).getImage());
            viewHolder.textView.setText(employees.get(position).getName());
            viewHolder.subscriptView.setText(employees.get(position).getEmployeeNumber() + "");
            return convertView;
        }
        public class ViewHolder {
            private ImageView imageView;
            private TextView textView;
            private TextView subscriptView;
        }
    }

    View.OnClickListener popClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getPopupWindow();
            popupWindow.showAsDropDown(v);
        }
    };
    /**
     * 创建PopupWindow
     */
    protected void initPopuptWindow() {
        View popupWindow_view = getLayoutInflater().inflate(R.layout.employee_pop, null,
                false);
        popupWindow = new PopupWindow(popupWindow_view, 300, 240, true);
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
        Button insertButton = (Button) popupWindow_view.findViewById(R.id.insert);
        Button searchButton = (Button) popupWindow_view.findViewById(R.id.search);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EmployeeActivity.this, InsertEmployeeActivity.class);
                startActivity(in);
                popupWindow.dismiss();
            }
        });
// 保存
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EmployeeActivity.this, "搜索",Toast.LENGTH_SHORT).show();

                finish();
                popupWindow.dismiss();
            }
        });
    }

    public List<Employee> getEmployees() {

        new Thread(getAllEmployees).start();

        List<Employee> lists = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from employees", null);
        while (true) {
            if (cursor.moveToNext() == false) {
                break;
            }
            Employee employee = new Employee();
            employee.setId(cursor.getInt(0));
            employee.setImage(R.drawable.head);
            employee.setName(cursor.getString(1).toString());
            employee.setEmployeeNumber(cursor.getString(2).toString());
            employee.setEmployeePosition(cursor.getString(5).toString());
            employee.setAddress(cursor.getString(7).toString());
            employee.setEmployeeSex(cursor.getString(3).toString());
            employee.setEmployeeAge(25);
            employee.setTelNumber(cursor.getString(6).toString());
            employee.setPassword(cursor.getString(4).toString());
            lists.add(employee);
        }

        return lists;
    }

    public void setListViewEvent() {
        listViewAdapter = new ListViewAdapter(this, employees);

        listView.setAdapter(listViewAdapter);
        //ListView的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EmployeeActivity.this, EmployeeInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image", employees.get(position).getImage());
                bundle.putInt("id", employees.get(position).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    Runnable getAllEmployees = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            try {
                String path = "http://115.159.82.119:8080/Movie/employee/EmployeeQueryAll";
                HttpGet httpRequest = new HttpGet(path);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(httpRequest);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    Bundle data = new Bundle();
                    data.putString("value", strResult);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
                Log.i("返回", "zhengqu");
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            JSONArray array = null;
            List<Employee> lists = new ArrayList<>();

            try {
                array = new JSONArray(val);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    Employee employee = new Employee();
                    employee.setId(Integer.parseInt(object.getString("emp_id")));
                    employee.setImage(R.drawable.head);
                    employee.setName(object.getString("emp_name"));
                    employee.setEmployeeNumber(object.getString("emp_id"));
                    employee.setEmployeePosition(object.getString("emp_position"));
                    employee.setAddress(object.getString("emp_addr"));
                    employee.setEmployeeSex(object.getString("emp_sex"));
                    employee.setEmployeeAge(25);
                    employee.setTelNumber(object.getString("emp_tel_num"));
                    employee.setPassword(object.getString("emp_password"));
                    lists.add(employee);
                }
                employees = lists;
                setListViewEvent();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("所有员工", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };

    private void getPopupWindow() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    protected void onRestart() {
        super.onRestart();
//        employees = getEmployees();
        new Thread(getAllEmployees).start();
        setListViewEvent();
    }
}
