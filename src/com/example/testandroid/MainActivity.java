package com.example.testandroid;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {
	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB db;
	private List<String> dataList=new ArrayList<String>();
	private List<Province> provinceList;
	private List<City> cityList;
	private Province selectedProvince;
	private City selectedCity;
	private int currentLevel;//默认初始化值为0（LEVEL_PROVINCE）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      
       db=CoolWeatherDB.getInstance(this);
       Province province=new Province();
       
       province.setProvince("福建省");
        db.SaveProvince(province);
        City city=new City();
        city.setProvince("福建省");
        city.setCity("南平市");
        city.setCounty("南平");
        city.setEnName("nanping");
        city.setSEnName("np");
        db.SaveCity(city);
        currentLevel=LEVEL_CITY;
        queryFromServer(null,"province");

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
    protected void queryProvinces() {
		// TODO Auto-generated method stub
		
		provinceList=db.loadProvince();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province province:provinceList){
				dataList.add(province.getProvince());
				adapter.notifyDataSetChanged();
				listView.setSelection(0);
				titleText.setText("中国");
				currentLevel=LEVEL_PROVINCE;
			}
		}else{
		queryFromServer(null,"province");
	}}
    
	private void queryFromServer(final String code, final String type) {
		// TODO Auto-generated method stub
		String address;
		if(currentLevel==LEVEL_PROVINCE){
			address="http://10.0.2.2/all_city.json";}else{
				address="http://10.0.2.2/province.json";
		}
		
		HttpUtil.sendHttpRequest(address,new HttpCallbackListener(){
			@Override
			public void onFinish(String response){
				boolean result=false;
				if("province".equals(type)){
					result=Utility.handleProvinceResponse(db,response);
				}else if("city".equals(type)){
					result=Utility.handleCityResponse(db,response);
				}
				
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
			});
	}}