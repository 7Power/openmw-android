package ui.activity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import ui.files.ParseJson;
import ui.files.ParseJson.FilesData;
import ui.files.PluginReader;

import com.libopenmw.openmw.R;
import com.mobeta.android.dslv.DragSortListView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class PluginsView extends Activity {

	public List<FilesData> Plugins;
	private Adapter adapter;
	private int deletePos = -1;
	private TextView pluginInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);

		pluginInfo = (TextView) findViewById(R.id.pluginsInfo);
		try {

			Plugins = ParseJson.loadFile();
			if (Plugins == null)
				Plugins = new ArrayList<ParseJson.FilesData>();

			File yourDir = new File(MainActivity.dataPath);

			checkFilesDeleted(yourDir);

			addNewFiles(yourDir);

			DragSortListView listView = (DragSortListView) findViewById(R.id.listView1);

			adapter = new Adapter();
			listView.setAdapter(adapter);

			listView.setDropListener(onDrop);
			listView.setRemoveListener(onRemove);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "data files not found",
					Toast.LENGTH_LONG).show();
			finish();
		}

	}

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {

			deletePos = which;
			showDialod();
		}
	};

	private void showDialod() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Do you want to delete " + Plugins.get(deletePos).name
				+ " ?");

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				deletePlugin();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						adapter.notifyDataSetChanged();
						dialog.cancel();
					}
				});

		alert.show();
	}

	private void deletePlugin() {
		File inputfile = new File(MainActivity.dataPath + "/"
				+ Plugins.get(deletePos).name);
		if (inputfile.exists())
			inputfile.delete();
		Plugins.remove(deletePos);
		savePluginsData();
		adapter.notifyDataSetChanged();

	}

	public void savePlugins(View v) throws IOException {

		try {

			FileWriter writer = new FileWriter(MainActivity.configsPath
					+ "/openmw/openmw.cfg");

			int i = 0;
			while (i < Plugins.size()) {

				if (Plugins.get(i).enabled == 1) {
					writer.write("content= " + Plugins.get(i).name + "\n");

					if (checkBsaExists(MainActivity.dataPath + "/"
							+ Plugins.get(i).nameBsa))
						writer.write("fallback-archive= "
								+ Plugins.get(i).nameBsa + "\n");

					writer.flush();
				}
				i++;

			}
			writer.close();
			Toast toast = Toast.makeText(getApplicationContext(),
					"Saving done", Toast.LENGTH_LONG);
			toast.show();

		} catch (Exception e) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"config file openmw.cfg not found", Toast.LENGTH_LONG);
			toast.show();
			e.printStackTrace();
		}
	}

	private boolean checkBsaExists(String path) {
		File inputfile = new File(path);
		if (inputfile.exists())
			return true;
		else
			return false;

	}

	private void checkFilesDeleted(File yourDir) throws JSONException,
			IOException {
		int deletedFilesCount = 0;
		int i = 0;
		List<FilesData> tmp = ParseJson.loadFile();
		for (i = 0; i < tmp.size(); i++) {
			boolean fileDeleted = true;

			for (File f : yourDir.listFiles()) {

				if (f.isFile() && f.getName().endsWith(tmp.get(i).name)) {

					fileDeleted = false;
					break;

				} else
					fileDeleted = true;

			}

			if (fileDeleted) {
				Plugins.remove(i - deletedFilesCount);
				deletedFilesCount++;
			}

		}
		if (Plugins.size() < i)
			savePluginsData();

	}

	private int loadingPos(int place) {
		int countPlace = 0;
		int[] loadingPlaces = new int[Plugins.size()];
		for (int i = 0; i < Plugins.size(); i++)
			if (Plugins.get(i).enabled == 1) {
				loadingPlaces[i] = countPlace;
				countPlace++;
			}

		return loadingPlaces[place];
	}

	private void addNewFiles(File yourDir) throws JSONException, IOException {
		int lastEsmPos = 0;

		for (int i = 0; i < Plugins.size(); i++) {
			if (Plugins.get(i).name.endsWith(".esm")
					|| Plugins.get(i).name.endsWith(".ESM"))
				lastEsmPos = i;
			else
				break;
		}

		for (File f : yourDir.listFiles()) {

			boolean newPlugin = true;
			for (FilesData data : Plugins) {
				if (f.isFile() && f.getName().endsWith(data.name)) {

					newPlugin = false;
					break;

				} else
					newPlugin = true;

			}
			if (newPlugin) {
				FilesData pluginData = new FilesData();

				pluginData.name = f.getName();
				pluginData.nameBsa = f.getName().split("\\.")[0] + ".bsa";
				if (f.getName().endsWith(".esm")
						|| f.getName().endsWith(".ESM")) {
					Plugins.add(lastEsmPos, pluginData);
					lastEsmPos++;
				} else if (f.getName().endsWith(".esp")
						|| f.getName().endsWith(".ESP")
						|| f.getName().endsWith(".omwgame")
						|| f.getName().endsWith(".omwaddon")) {
					Plugins.add(pluginData);
				}

			}

		}

		savePluginsData();
	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			FilesData item = Plugins.get(from);

			Log.d("DEBUG", "from" + from + "to" + to + "   "
					+ Plugins.get(0).name);
			Plugins.remove(from);

			Plugins.add(to, item);

			try {
				pluginInfo.setText(PluginReader.read(MainActivity.dataPath
						+ "/" + item.name));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			adapter.notifyDataSetChanged();
			savePluginsData();
		}
	};

	private void savePluginsData() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ParseJson.saveFile(Plugins);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	public class Adapter extends BaseAdapter

	{

		public View rowView;

		@Override
		public boolean isEmpty() {

			// TODO Auto-generated method stub

			return false;

		}

		@Override
		public boolean hasStableIds() {

			// TODO Auto-generated method stub

			return false;

		}

		@Override
		public int getViewTypeCount() {

			// TODO Auto-generated method stub

			return 1;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) PluginsView.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			rowView = inflater.inflate(R.layout.rowlistview, parent, false);

			TextView data = (TextView) rowView.findViewById(R.id.textView1);
			TextView enabled = (TextView) rowView
					.findViewById(R.id.textViewenabled);

			final CheckBox Box = (CheckBox) rowView
					.findViewById(R.id.checkBoxenable);
			final TextView loadingPlace = (TextView) rowView
					.findViewById(R.id.loadingPlace);
		
			enabled.setText(String.valueOf(Plugins.get(position).enabled));

			if (Plugins.get(position).enabled == 1){
				Box.setChecked(true);
				loadingPlace.setText("" + loadingPos(position));

			}
			Box.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (Box.isChecked()) {

						Plugins.get(position).enabled = 1;
						loadingPlace.setText("" + loadingPos(position));
						adapter.notifyDataSetChanged();


						savePluginsData();
					} else {

						Plugins.get(position).enabled = 0;
						loadingPlace.setText("");
						adapter.notifyDataSetChanged();

						savePluginsData();

					}
				}

			});

			data.setText(Plugins.get(position).name);
			return rowView;

		}

		@Override
		public int getItemViewType(int position) {

			// TODO Auto-generated method stub

			return 0;

		}

		@Override
		public long getItemId(int position) {

			// TODO Auto-generated method stub

			return 0;

		}

		@Override
		public Object getItem(int position) {

			// TODO Auto-generated method stub

			return null;

		}

		@Override
		public int getCount() {

			return Plugins.size();

		}

		@Override
		public boolean isEnabled(int position) {

			// TODO Auto-generated method stub

			return false;

		}

		@Override
		public boolean areAllItemsEnabled() {

			// TODO Auto-generated method stub

			return false;
		}

	}

}
