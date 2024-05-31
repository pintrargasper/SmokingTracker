package eu.mister3551.smokingtracker.ui.settings;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import eu.mister3551.smokingtracker.MainActivity;
import eu.mister3551.smokingtracker.R;
import eu.mister3551.smokingtracker.Utils;
import eu.mister3551.smokingtracker.adapter.DropdownAdapter;
import eu.mister3551.smokingtracker.database.Manager;
import eu.mister3551.smokingtracker.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private Manager manager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        manager = Utils.getManager().get();

        TextView text_view_version = root.findViewById(R.id.text_view_version);
        Button button_select_language = root.findViewById(R.id.button_select_language);
        Button button_select_color = root.findViewById(R.id.button_select_color);
        Button button_select_point_color = root.findViewById(R.id.button_select_point_color);
        Button button_select_line_color = root.findViewById(R.id.button_select_line_color);
        Button button_select_paint_style = root.findViewById(R.id.button_select_paint_style);

        button_select_language.setOnClickListener(view -> showPopup(root.getContext(), "Language"));
        button_select_color.setOnClickListener(view -> showPopup(root.getContext(), "Graph color"));
        button_select_point_color.setOnClickListener(view -> showPopup(root.getContext(), "Point color"));
        button_select_line_color.setOnClickListener(view -> showPopup(root.getContext(), "Line color"));
        button_select_paint_style.setOnClickListener(view -> showPopup(root.getContext(), "Paint style"));

        String version = text_view_version.getText() + " " + getAppVersion(root.getContext());
        text_view_version.setText(version);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showPopup(Context context, String title) {
        switch (title) {
            case "Language" -> showLanguagePopup(context);
            case "Graph color", "Line color", "Point color" -> showColorPopup(context, title);
            case "Paint style" -> showPaintStylePopup(context, title);
        }
    }

    private void showLanguagePopup(Context context) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.fragment_settings_popup_language, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        Button buttonConfirm = bottomSheetView.findViewById(R.id.button_confirm);
        Button buttonClose = bottomSheetView.findViewById(R.id.button_close);
        Spinner spinnerLanguage = bottomSheetView.findViewById(R.id.spinner_language);

        CharSequence[] languages = context.getResources().getTextArray(R.array.language_array);

        DropdownAdapter adapter = new DropdownAdapter(context, R.layout.fragment_settings_dropdown_language, languages);
        adapter.setDropDownViewResource(R.layout.fragment_settings_dropdown_language);
        spinnerLanguage.setAdapter(adapter);
        spinnerLanguage.setSelection(getSelectedLanguageIndex());

        buttonConfirm.setOnClickListener(view -> {
            Utils.getSettings().setLanguage(getLanguageCode(spinnerLanguage.getSelectedItemPosition()));
            MainActivity.setLanguage(context);
            requireActivity().recreate();
            manager.update(getLanguageCode(spinnerLanguage.getSelectedItemPosition()));
            bottomSheetDialog.dismiss();
        });

        buttonClose.setOnClickListener(view -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    private void showColorPopup(Context context, String title) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.fragment_settings_popup_colors, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView textViewSettingsMessage = bottomSheetView.findViewById(R.id.text_view_settings_message);
        textViewSettingsMessage.setText(title);

        Map<Integer, CheckBox> colorCheckBoxMap = initializeColorCheckBoxMap(bottomSheetView);

        setupColorCheckBoxListeners(title, colorCheckBoxMap);

        Button buttonConfirm = bottomSheetView.findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(view -> {
            applySelectedColors(title, colorCheckBoxMap);
            bottomSheetDialog.dismiss();
        });

        Button buttonClose = bottomSheetView.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    private void showPaintStylePopup(Context context, String title) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.fragment_settings_popup_paint_style, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView textViewSettingsMessage = bottomSheetView.findViewById(R.id.text_view_settings_message);
        textViewSettingsMessage.setText(title);

        Map<Paint.Style, CheckBox> paintStyleCheckBoxMap = initializePaintStyleCheckBoxMap(bottomSheetView);

        setupSingleSelectionListener(paintStyleCheckBoxMap, Utils.getSettings().getGraph().getPaintStyle());

        Button buttonConfirm = bottomSheetView.findViewById(R.id.button_confirm);
        Button buttonClose = bottomSheetView.findViewById(R.id.button_close);

        buttonConfirm.setOnClickListener(view -> {
            Paint.Style selectedStyle = getSelectedPaintStyle(paintStyleCheckBoxMap);
            Utils.getSettings().getGraph().setPaintStyle(selectedStyle);
            manager.update(selectedStyle);
            bottomSheetDialog.dismiss();
        });

        buttonClose.setOnClickListener(view -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    private Paint.Style getSelectedPaintStyle(Map<Paint.Style, CheckBox> paintStyleCheckBoxMap) {
        for (Map.Entry<Paint.Style, CheckBox> entry : paintStyleCheckBoxMap.entrySet()) {
            if (entry.getValue().isChecked()) {
                return entry.getKey();
            }
        }
        return Paint.Style.FILL;
    }

    private Map<Integer, CheckBox> initializeColorCheckBoxMap(View bottomSheetView) {
        Map<Integer, CheckBox> colorCheckBoxMap = new HashMap<>();
        colorCheckBoxMap.put(Color.GRAY, bottomSheetView.findViewById(R.id.check_box_grey));
        colorCheckBoxMap.put(Color.BLUE, bottomSheetView.findViewById(R.id.check_box_blue));
        colorCheckBoxMap.put(Color.RED, bottomSheetView.findViewById(R.id.check_box_red));
        colorCheckBoxMap.put(Color.YELLOW, bottomSheetView.findViewById(R.id.check_box_yellow));
        return colorCheckBoxMap;
    }

    private Map<Paint.Style, CheckBox> initializePaintStyleCheckBoxMap(View bottomSheetView) {
        Map<Paint.Style, CheckBox> colorCheckBoxMap = new HashMap<>();
        colorCheckBoxMap.put(Paint.Style.FILL, bottomSheetView.findViewById(R.id.check_box_fill));
        colorCheckBoxMap.put(Paint.Style.STROKE, bottomSheetView.findViewById(R.id.check_box_stroke));
        colorCheckBoxMap.put(Paint.Style.FILL_AND_STROKE, bottomSheetView.findViewById(R.id.check_box_fill_and_stroke));
        return colorCheckBoxMap;
    }

    private void setupColorCheckBoxListeners(String title, Map<Integer, CheckBox> colorCheckBoxMap) {
        switch (title) {
            case "Graph color" -> setupSingleSelectionListeners(colorCheckBoxMap, Utils.getSettings().getGraph().getColor());
            case "Point color" -> setupMultiSelectionListeners(colorCheckBoxMap, Utils.getSettings().getGraph().getPointColors());
            case "Line color" -> setupMultiSelectionListeners(colorCheckBoxMap, Utils.getSettings().getGraph().getLineColors());
            default -> throw new IllegalArgumentException("Unknown title: " + title);
        }
    }

    private void setupSingleSelectionListeners(Map<Integer, CheckBox> colorCheckBoxMap, int currentColor) {
        for (Map.Entry<Integer, CheckBox> entry : colorCheckBoxMap.entrySet()) {
            CheckBox checkBox = entry.getValue();
            int color = entry.getKey();
            checkBox.setChecked(color == currentColor);

            checkBox.setOnClickListener(view -> {
                if (checkBox.isChecked()) {
                    colorCheckBoxMap.values().forEach(otherCheckBox -> {
                        if (otherCheckBox != checkBox) {
                            otherCheckBox.setChecked(false);
                        }
                    });
                } else if (colorCheckBoxMap.values().stream().noneMatch(CheckBox::isChecked)) {
                    Objects.requireNonNull(colorCheckBoxMap.get(Color.GRAY)).setChecked(true);
                }
            });
        }
    }

    private void setupSingleSelectionListener(Map<Paint.Style, CheckBox> colorCheckBoxMap, Paint.Style currentStyle) {
        for (Map.Entry<Paint.Style, CheckBox> entry : colorCheckBoxMap.entrySet()) {
            CheckBox checkBox = entry.getValue();
            Paint.Style paintStyle = entry.getKey();
            checkBox.setChecked(paintStyle == currentStyle);

            checkBox.setOnClickListener(view -> {
                if (checkBox.isChecked()) {
                    colorCheckBoxMap.values().forEach(otherCheckBox -> {
                        if (otherCheckBox != checkBox) {
                            otherCheckBox.setChecked(false);
                        }
                    });
                } else if (colorCheckBoxMap.values().stream().noneMatch(CheckBox::isChecked)) {
                    Objects.requireNonNull(colorCheckBoxMap.get(Paint.Style.FILL)).setChecked(true);
                }
            });
        }
    }

    private void setupMultiSelectionListeners(Map<Integer, CheckBox> colorCheckBoxMap, List<Integer> currentColors) {
        for (Map.Entry<Integer, CheckBox> entry : colorCheckBoxMap.entrySet()) {
            CheckBox checkBox = entry.getValue();
            int color = entry.getKey();
            checkBox.setChecked(currentColors.contains(color));

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (!isChecked) {
                    for (CheckBox otherCheckBox : colorCheckBoxMap.values()) {
                        if (otherCheckBox.isChecked()) {
                            return;
                        }
                    }
                    Objects.requireNonNull(colorCheckBoxMap.get(Color.GRAY)).setChecked(true);
                }
            });
        }
    }

    private void applySelectedColors(String title, Map<Integer, CheckBox> colorCheckBoxMap) {
        List<Integer> selectedColors = new ArrayList<>();
        for (Map.Entry<Integer, CheckBox> entry : colorCheckBoxMap.entrySet()) {
            if (entry.getValue().isChecked()) {
                selectedColors.add(entry.getKey());
            }
        }
        switch (title) {
            case "Graph color" -> Utils.getSettings().getGraph().setColor(selectedColors.get(0));
            case "Point color" -> Utils.getSettings().getGraph().setPointColors(selectedColors);
            case "Line color" -> Utils.getSettings().getGraph().setLineColors(selectedColors);
            default -> {
                return;
            }
        }
        manager.update(Utils.getSettings().getGraph().getColor(), convertPointColors(Utils.getSettings().getGraph().getPointColors()), convertPointColors(Utils.getSettings().getGraph().getLineColors()));
    }

    private int getSelectedLanguageIndex() {
        return switch (Utils.getSettings().getLanguage()) {
            case "Sl" -> 0;
            case "DE" -> 2;
            case "FR" -> 3;
            default -> 1;
        };
    }

    private String getLanguageCode(int index) {
        return switch (index) {
            case 0 -> "Sl";
            case 2 -> "DE";
            case 3 -> "FR";
            default -> "EN";
        };
    }

    private String convertPointColors(List<Integer> pointColors) {
        return pointColors.stream()
                .map(Object::toString)
                .collect(Collectors.joining(";"));
    }

    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }
}