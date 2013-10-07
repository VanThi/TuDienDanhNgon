package model;

import java.util.ArrayList;

import view.ChamNgonListActivity;
import view.MainActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContentListAdapter extends ArrayAdapter<String>{
	
	private LayoutInflater inflater;
	private int mViewResId;
	private ArrayList<Content> contentList;
	private MainActivity mainAct;
	private SQLiteHelper sh;
	private  Typeface tf;
	
	public ContentListAdapter(Context context, int viewResourceId, 
			ArrayList<Content> contentList, MainActivity mainAct) {
		super(context, viewResourceId);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.contentList = contentList;
		this.mainAct = mainAct;
		this.mViewResId=viewResourceId;
		this.sh = new SQLiteHelper(context);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return contentList.size();
	}
	
	@Override
	public String getItem(int position) {
		return contentList.get(position).getTen();
	}
	
	@Override
	public long getItemId(int position) {
		return contentList.get(position).getCid();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(mViewResId,null);
		TextView ten= (TextView) convertView.findViewById(cyber.app.chamngon.R.id.ten);
		/*tf=Typeface.createFromAsset(getContext().getAssets(),
                "VNICLOIS.PFM");
		ten.setTypeface(tf);*/
		if(contentList.get(position).getCid()!=9){
			int numberChamNgon=sh.getNumberChamNgonByContent(contentList.get(position).getCid());
			ten.setText("    "+contentList.get(position).getTen()+"("+numberChamNgon+")");
		}else{
			int numberChamNgon=sh.getNumberChamNgonYeuThich();
			ten.setText("    "+contentList.get(position).getTen()+"("+numberChamNgon+")");
		}
		// chuyen acti
		ten.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(mainAct,ChamNgonListActivity.class);
				mIntent.putExtra("cid", contentList.get(position).getCid());
				mainAct.startActivity(mIntent);
				
			}
		});
		return convertView;
	}
	
	
	
}
