package com.ipinpar.app.view;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.ipinpar.app.R;
import com.ipinpar.app.entity.CityModel;
import com.ipinpar.app.entity.DistrictModel;
import com.ipinpar.app.entity.ProvinceModel;
import com.ipinpar.app.manager.XmlParserHandler;

public class AddressSelectVIew extends PopupWindow  implements OnClickListener, OnWheelChangedListener {

	private Context context;
	private onSelectListener onSelectListener;
	private View mainView;
	
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private Button mBtnConfirm;
	
	/**
	 * ����ʡ
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - ʡ value - ��
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - �� values - ��
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	
	/**
	 * key - �� values - �ʱ�
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 

	/**
	 * ��ǰʡ������
	 */
	protected String mCurrentProviceName;
	/**
	 * ��ǰ�е�����
	 */
	protected String mCurrentCityName;
	/**
	 * ��ǰ��������
	 */
	protected String mCurrentDistrictName ="";
	
	protected String mCurrentZipCode ="";
	
	public AddressSelectVIew(Context context, onSelectListener marketSelectListener) {
		super(context);
		this.context = context;
		this.onSelectListener = marketSelectListener;
			initView();
	}
	
	public static interface onSelectListener{
		public void onSelect(String address1,String address2,String address3);
	}
	
	private void initView(){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mainView = inflater.inflate(R.layout.view_select_address, null);
			setContentView(mainView);
			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			mViewProvince = (WheelView) mainView.findViewById(R.id.id_province);
			mViewCity = (WheelView) mainView.findViewById(R.id.id_city);
			mViewDistrict = (WheelView) mainView.findViewById(R.id.id_district);
			mBtnConfirm = (Button) mainView.findViewById(R.id.btn_confirm);
			
			mViewProvince.addChangingListener(this);
	    	mViewCity.addChangingListener(this);
	    	mViewDistrict.addChangingListener(this);
	    	mBtnConfirm.setOnClickListener(this);
	    	
	    	mViewProvince.addChangingListener(this);
	    	mViewCity.addChangingListener(this);
	    	mViewDistrict.addChangingListener(this);
	    	mBtnConfirm.setOnClickListener(this);
	    	
	    	initProvinceDatas();
			mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
			mViewProvince.setVisibleItems(7);
			mViewCity.setVisibleItems(7);
			mViewDistrict.setVisibleItems(7);
			updateCities();
			updateAreas();
	}
	
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}
	
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
		mViewDistrict.setCurrentItem(0);
	}

	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
	
	 protected void initProvinceDatas()
		{
			List<ProvinceModel> provinceList = null;
	    	AssetManager asset = context.getAssets();
	        try {
	            InputStream input = asset.open("province_data.xml");
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser parser = spf.newSAXParser();
				XmlParserHandler handler = new XmlParserHandler();
				parser.parse(input, handler);
				input.close();
				provinceList = handler.getDataList();
				if (provinceList!= null && !provinceList.isEmpty()) {
					mCurrentProviceName = provinceList.get(0).getName();
					List<CityModel> cityList = provinceList.get(0).getCityList();
					if (cityList!= null && !cityList.isEmpty()) {
						mCurrentCityName = cityList.get(0).getName();
						List<DistrictModel> districtList = cityList.get(0).getDistrictList();
						mCurrentDistrictName = districtList.get(0).getName();
						mCurrentZipCode = districtList.get(0).getZipcode();
					}
				}
				//*/
				mProvinceDatas = new String[provinceList.size()];
	        	for (int i=0; i< provinceList.size(); i++) {
	        		mProvinceDatas[i] = provinceList.get(i).getName();
	        		List<CityModel> cityList = provinceList.get(i).getCityList();
	        		String[] cityNames = new String[cityList.size()];
	        		for (int j=0; j< cityList.size(); j++) {
	        			cityNames[j] = cityList.get(j).getName();
	        			List<DistrictModel> districtList = cityList.get(j).getDistrictList();
	        			String[] distrinctNameArray = new String[districtList.size()];
	        			DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
	        			for (int k=0; k<districtList.size(); k++) {
	        				DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
	        				mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
	        				distrinctArray[k] = districtModel;
	        				distrinctNameArray[k] = districtModel.getName();
	        			}
	        			mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
	        		}
	        		mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
	        	}
	        } catch (Throwable e) {  
	            e.printStackTrace();  
	        } finally {
	        	
	        } 
		}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			onSelectListener.onSelect(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName);
			dismiss();
			break;
		default:
			break;
		}
	}
	
}
