package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import view.ChamNgonListActivity;
import view.DetailActivity;
import view.ShareActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chamngon.R;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

public class ChamNgonListAdapter extends ArrayAdapter<String>{

	private LayoutInflater inflater;
	private int mViewResId;
	private ArrayList<ChamNgon> chamNgonList;
	private ChamNgonListActivity chamngonList_activity;
	private SQLiteHelper sqliteHelper;	

	private enum PendingAction {
		NONE,
		POST_PHOTO,
		POST_STATUS_UPDATE
	}
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private PendingAction pendingAction = PendingAction.NONE;


	public ChamNgonListAdapter(Context context, 
			int textViewResourceId,
			ArrayList<ChamNgon> chamNgonList, 
			ChamNgonListActivity chamngonList_activity, 
			SQLiteHelper sqliteHelper) {
		super(context, textViewResourceId);
		inflater 					= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		this.mViewResId 			= textViewResourceId;
		this.chamNgonList 			= chamNgonList;
		this.chamngonList_activity 	= chamngonList_activity;
		this.sqliteHelper 			= sqliteHelper;
	}

	@Override
	public int getCount() {
		return chamNgonList.size();
	}

	@Override
	public String getItem(int position) {
		String result;
		if (chamNgonList.get(position).getnDungAnh() != "") {
			result = chamNgonList.get(position).getnDungViet() + "\n" + 
					chamNgonList.get(position).getnDungAnh()  + "\n" +
					chamNgonList.get(position).getTacGia();
		} else {
			result = chamNgonList.get(position).getnDungViet() + "\n" + 
					chamNgonList.get(position).getTacGia(); 
		}
		return result;
	}

	@Override
	public long getItemId(int position) {
		return chamNgonList.get(position).getChid();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		System.out.println("get view cham ngon");
		convertView=inflater.inflate(mViewResId, null);

		TextView viet 	= (TextView) convertView.findViewById(R.id.viet);
		TextView anh 	= (TextView) convertView.findViewById(R.id.anh1);
		TextView tacgia = (TextView) convertView.findViewById(R.id.tacgia);
		final ImageButton buttonLove = (ImageButton)convertView.findViewById(R.id.buttonLove);
		final ImageButton buttonShare = (ImageButton)convertView.findViewById(R.id.buttonShare);
		final ImageButton buttonOtherShare = (ImageButton)convertView.findViewById(R.id.buttonOtherShare);
		

		viet.setText(chamNgonList.get(position).getnDungViet());
		anh.setText(chamNgonList.get(position).getnDungAnh());
		tacgia.setText("- " + chamNgonList.get(position).getTacGia() + " -");

		anh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mintent= new Intent(chamngonList_activity.getApplicationContext(),DetailActivity.class);
				mintent.putExtra("chid", chamNgonList.get(position).getChid());
				chamngonList_activity.startActivity(mintent);

			}
		});

		viet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mintent= new Intent(chamngonList_activity.getApplicationContext(),DetailActivity.class);
				mintent.putExtra("chid", chamNgonList.get(position).getChid());
				chamngonList_activity.startActivity(mintent);

			}
		});
		
		if (chamNgonList.get(position).getYeuthich() == 0) {
			buttonLove.setBackgroundResource(R.drawable.favorites_add);
		} else {
			buttonLove.setBackgroundResource(R.drawable.gnome_favorites);
		}
		
		buttonLove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (chamNgonList.get(position).getYeuthich() == 0) {
					chamNgonList.get(position).setYeuthich(1);
					buttonLove.setBackgroundResource(R.drawable.gnome_favorites);
				} else {
					chamNgonList.get(position).setYeuthich(0);
					buttonLove.setBackgroundResource(R.drawable.favorites_add);
				}
				sqliteHelper.updateChamNgon(chamNgonList.get(position));
			}
		});


		buttonShare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickShare(position);
			}
		});
		Session session = Session.getActiveSession();
		boolean available = session != null && session.isOpened();
		buttonShare.setEnabled(available);
		if (!available) {
			buttonShare.setVisibility(View.INVISIBLE);
		} else {
			buttonShare.setVisibility(View.VISIBLE);
		}
		
		buttonOtherShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				System.out.println("share intent");
				//shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT ,chamNgonList.get(position).getnDungViet()+ "\n" + 
						chamNgonList.get(position).getnDungAnh() + "\n"+ chamNgonList.get(position).getTacGia() );
				chamngonList_activity.startActivity(Intent.createChooser(shareIntent, "Select an application to share: " ));
			}
		});

		return convertView;

	}

	private void onClickShare(int nIdx) {
		final int nIndex = nIdx;
		AlertDialog.Builder builder = new AlertDialog.Builder(chamngonList_activity);
		builder.setTitle(R.string.alert_confirm_title)
			   .setMessage(R.string.alert_comfirm_message)
			   .setPositiveButton(R.string.alert_comfirm_positiveButton, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				performPublish(PendingAction.POST_STATUS_UPDATE, nIndex);
			}
		})
		.setNegativeButton(R.string.alert_confirm_negative, null)
		.show();
	}

	private void showAlert(String title, String message) {
		new AlertDialog.Builder(chamngonList_activity)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(R.string.ok, null)
		.show();
	}

	private void postStatusUpdate(int nIdx) {
		if (hasPublishPermission()) {
			final String message = chamNgonList.get(nIdx).getnDungViet() + "\n\n" + 
					chamNgonList.get(nIdx).getnDungAnh() + "\n\n" + "- " + 
					chamNgonList.get(nIdx).getTacGia() + " -";
			Request request = Request
					.newStatusUpdateRequest(Session.getActiveSession(), message, new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							showPublishResult(response.getError());
						}
					});
			request.executeAsync();
		} else {
			pendingAction = PendingAction.POST_STATUS_UPDATE;
		}
	}

	private void showPublishResult(FacebookRequestError error) {
		String title = null;
		String alertMessage = null;
		if (error == null) {
			title = chamngonList_activity.getString(R.string.success);
			alertMessage = chamngonList_activity.getString(R.string.successfully_posted_post);
		} else {
			title = chamngonList_activity.getString(R.string.error);
			alertMessage = error.getErrorMessage();
		}

		showAlert(title, alertMessage);

	}

	// not use yet
	private void postPhoto(int nIdx) {

	}

	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction(int nIdx) {
		PendingAction previouslyPendingAction = pendingAction;
		// These action s may re-set pendingAction if they are still pending, but we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;
		switch (previouslyPendingAction) {
		case POST_PHOTO:
			postPhoto(nIdx);
			break;
		case POST_STATUS_UPDATE:
			postStatusUpdate(nIdx);
			break;
		}
	}

	private boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		return session != null && session.getPermissions().contains("publish_actions");
	}

	private void performPublish(PendingAction action, int nIdx) {
		Session session = Session.getActiveSession();
		if (session != null) {
			pendingAction = action;
			if (hasPublishPermission()) {
				// We can do the action right away.
				handlePendingAction(nIdx);
			} else {
				// We need to get new permissions, then complete the action when we get called back.
				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(chamngonList_activity, PERMISSIONS));
			}
		}
	}



}