package com.example.bursel.databaseapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    public static DairysDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新增日记
                Intent intent=new Intent(MainActivity.this,EditDairyActivity.class);
                startActivity(intent);
            }
        });

        //为ListView注册上下文菜单
        ListView list = (ListView) findViewById(R.id.lstWords);
        registerForContextMenu(list);


        //创建SQLiteOpenHelper对象，注意第一次运行时，此时数据库并没有被创建
        mDbHelper = new DairysDBHelper(this);

        //在列表显示全部单词
        ArrayList<Map<String, String>> items=getAll();
        setDairysListView(items);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_insert:
                //新增日记
//                InsertDialog();
                Intent intent=new Intent(MainActivity.this,EditDairyActivity.class);
                startActivity(intent);
                return true;
        }

        ArrayList<Map<String, String>> items=MainActivity.getAll();
        setDairysListView(items);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contextmenu_wordslistview, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {     //长按之后出现的操作
        TextView textId=null;
        TextView textWord=null;
        TextView textMeaning=null;
        TextView textSample=null;

        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;

        switch (item.getItemId()){
            case R.id.action_delete:
                //删除日记
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.textId);
                if(textId!=null){
                    String strId=textId.getText().toString();
                    DeleteDialog(strId);
                }
                break;
            case R.id.action_update:
                //修改日记
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                Bundle bundle=new Bundle();
                Intent intent=new Intent(MainActivity.this,EditDairyActivity.class);
                TextView title=(TextView)itemView.findViewById(R.id.textViewTitle);
                TextView body=(TextView)itemView.findViewById(R.id.textViewBody);
                TextView id=(TextView)itemView.findViewById(R.id.textId);
                bundle.putString("title",title.getText().toString());
                bundle.putString("body",body.getText().toString());
                bundle.putString("id",id.getText().toString());
                intent.putExtra("dairy",bundle);
                startActivity(intent);

                break;
        }
        return true;
    }

    //设置适配器，在列表中显示单词
    private void setDairysListView(ArrayList<Map<String, String>> items){
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item,
                new String[]{Dairys.Dairy._ID,Dairys.Dairy.COLUMN_NAME_TITLE, Dairys.Dairy.COLUMN_NAME_BODY},
                new int[]{R.id.textId,R.id.textViewTitle, R.id.textViewBody});

        ListView list = (ListView) findViewById(R.id.lstWords);

        list.setAdapter(adapter);
    }




    public static ArrayList<Map<String, String>> getAll() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Dairys.Dairy._ID,
                Dairys.Dairy.COLUMN_NAME_TITLE,
                Dairys.Dairy.COLUMN_NAME_BODY
        };

        //排序
        String sortOrder =
                Dairys.Dairy.COLUMN_NAME_TITLE + " DESC";


        Cursor c = db.query(
                Dairys.Dairy.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return ConvertCursor2List(c);
    }



    public static ArrayList<Map<String, String>> ConvertCursor2List(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Dairys.Dairy._ID, String.valueOf(cursor.getInt(0)));
            map.put(Dairys.Dairy.COLUMN_NAME_TITLE, cursor.getString(1));
            map.put(Dairys.Dairy.COLUMN_NAME_BODY, cursor.getString(2));
            result.add(map);
        }
        return result;
    }

    //使用Sql语句插入单词
    private void InsertUserSql(String strTitle, String strBody){
        String sql="insert into dairy(title,body) values(?,?)";

        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql,new String[]{strTitle,strBody});
    }

    //使用insert方法增加日记
    public static void Insert(String strTitle, String strBody) {

        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Dairys.Dairy.COLUMN_NAME_TITLE, strTitle);
        values.put(Dairys.Dairy.COLUMN_NAME_BODY, strBody);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                Dairys.Dairy.TABLE_NAME,
                null,
                values);
    }


    //新增对话框
    private void InsertDialog() {
       final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        new AlertDialog.Builder(this)
                .setTitle("新增单词")//标题
                .setView(tableLayout)//设置视图
                        //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strTitle=((EditText)tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strBody=((EditText)tableLayout.findViewById(R.id.txtMeaning)).getText().toString();

                        //既可以使用Sql语句插入，也可以使用使用insert方法插入
                       // InsertUserSql(strWord, strMeaning, strSample);
                        Insert(strTitle, strBody);

                        ArrayList<Map<String, String>> items=getAll();
                        setDairysListView(items);

                    }
                })
                        //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框


    }
    //使用Sql语句删除日记
    private void DeleteUseSql(String strId) {
        String sql="delete from dairy where _id='"+strId+"'";

        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL(sql);
    }

    //删除日记
    private void Delete(String strId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // 定义where子句
        String selection = Dairys.Dairy._ID + " = ?";

        // 指定占位符对应的实际参数
        String[] selectionArgs = {strId};

        // Issue SQL statement.
        db.delete(Dairys.Dairy.TABLE_NAME, selection, selectionArgs);
    }


    //删除对话框
    private void DeleteDialog(final String strId){
        new AlertDialog.Builder(this).setTitle("删除日记").setMessage("是否真的删除该日记?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //既可以使用Sql语句删除，也可以使用使用delete方法删除
                DeleteUseSql(strId);
                //Delete(strId);
                setDairysListView(getAll());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    //使用Sql语句更新单词
    private void UpdateUseSql(String strId,String strTitle, String strBody) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="update dairy set title=?,body=? where _id=?";
        db.execSQL(sql, new String[]{strTitle, strBody,strId});
    }

    //使用方法更新
    public static void Update(String strId,String strTitle, String strBody) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        // New value for one column
        ContentValues values = new ContentValues();
        values.put(Dairys.Dairy.COLUMN_NAME_TITLE, strTitle);
        values.put(Dairys.Dairy.COLUMN_NAME_BODY, strBody);

        String selection = Dairys.Dairy._ID + " = ?";
        String[] selectionArgs = {strId};

        int count = db.update(
                Dairys.Dairy.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


    //使用Sql语句查找
    private ArrayList<Map<String, String>> SearchUseSql(String strDairySearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql="select * from dairy where title like ? order by title desc";
        Cursor c=db.rawQuery(sql,new String[]{"%"+strDairySearch+"%"});

        return ConvertCursor2List(c);
    }

    //使用query方法查找
    private ArrayList<Map<String, String>> Search(String strDairySearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Dairys.Dairy._ID,
                Dairys.Dairy.COLUMN_NAME_TITLE,
                Dairys.Dairy.COLUMN_NAME_BODY
        };

        String sortOrder =
                Dairys.Dairy.COLUMN_NAME_TITLE + " DESC";

        String selection = Dairys.Dairy.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = {"%"+strDairySearch+"%"};

        Cursor c = db.query(
                Dairys.Dairy.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return ConvertCursor2List(c);
    }

    //查找对话框
//    private void SearchDialog() {
//        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.searchterm, null);
//        new AlertDialog.Builder(this)
//                .setTitle("查找单词")//标题
//                .setView(tableLayout)//设置视图
//                        //确定按钮及其动作
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String txtSearchWord=((EditText)tableLayout.findViewById(R.id.txtSearchWord)).getText().toString();
//
//                        ArrayList<Map<String, String>> items=null;
//                        //既可以使用Sql语句查询，也可以使用方法查询
//                        items=SearchUseSql(txtSearchWord);
//                       // items=Search(txtSearchWord);
//
//                        if(items.size()>0) {
//                            Bundle bundle=new Bundle();
//                            bundle.putSerializable("result",items);
//                            Intent intent=new Intent(MainActivity.this,SearchActivity.class);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                        }else
//                            Toast.makeText(MainActivity.this,"没有找到",Toast.LENGTH_LONG).show();
//
//
//                    }
//                })
//                        //取消按钮及其动作
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .create()//创建对话框
//                .show();//显示对话框
//
//    }
}