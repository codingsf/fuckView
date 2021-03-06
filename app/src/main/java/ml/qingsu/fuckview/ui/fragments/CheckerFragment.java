package ml.qingsu.fuckview.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ml.qingsu.fuckview.R;
import ml.qingsu.fuckview.ui.activities.MainActivity;
import ml.qingsu.fuckview.ui.popups.DumpViewerPopupView;
import ml.qingsu.fuckview.utils.ShellUtils;
import ml.qingsu.fuckview.utils.dumper.DumperService;

/**
 * Created by w568w on 18-2-7.
 */

public class CheckerFragment extends Fragment {
    ListView mList;
    FrameLayout mStatusContainer;
    ImageView mStatusIcon;
    TextView mStatusText;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.checker_fragment, null);
        mList = (ListView) layout.findViewById(R.id.checker_list);
        mStatusContainer = (FrameLayout) layout.findViewById(R.id.checker_status_container);
        mStatusIcon = (ImageView) layout.findViewById(R.id.checker_status_icon);
        mStatusText = (TextView) layout.findViewById(R.id.checker_module_status);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
        HashMap<String, String> row = new HashMap<>();
        row.put("item", getString(R.string.check_item_module_enabled));
        row.put("status", bool2Str(MainActivity.isModuleActive()));
        dataList.add(row);

        row = new HashMap<>(2);
        row.put("item", getString(R.string.check_item_floating_permission));
        row.put("status", bool2Str(canShowFloatingWindow()));
        dataList.add(row);


        row = new HashMap<>(2);
        row.put("item", getString(R.string.check_item_service_running));
        row.put("status", bool2Str(DumperService.getInstance() != null && DumperService.getInstance().getRootInActiveWindow() != null));
        dataList.add(row);


        row = new HashMap<>(2);
        row.put("item", getString(R.string.check_item_root));
        row.put("status", bool2Str(ShellUtils.checkRootPermission()));
        dataList.add(row);

        mList.setAdapter(new SimpleAdapter(getContext(), dataList, R.layout.pairs, new String[]{"item", "status"}, new int[]{R.id.pairs_textView1, R.id.pairs_textView2}));

        int colorResId = MainActivity.isModuleActive() ? R.color.darker_green : R.color.warning;
        int iconResId = MainActivity.isModuleActive() ? R.drawable.ic_check_circle : R.drawable.ic_error;
        int textResId = MainActivity.isModuleActive() ? R.string.module_active : R.string.module_not_active;
        mStatusText.setText(textResId);
        mStatusText.setTextColor(getResources().getColor(colorResId));
        mStatusIcon.setImageResource(iconResId);
        mStatusContainer.setBackgroundColor(getResources().getColor(colorResId));
    }

    private static String bool2Str(boolean b) {
        return b ? "OK" : "Failed";
    }

    private boolean canShowFloatingWindow() {
        try {
            DumpViewerPopupView dumpViewerPopupView = new DumpViewerPopupView(getActivity(), "");
            dumpViewerPopupView.show();
            dumpViewerPopupView.hide();
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
}
