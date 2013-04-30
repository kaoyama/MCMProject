package app.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.ListView;
import app.localization.R;

/**
 * Android list preference with multi-select checkboxes. This code was taken
 * from http://blog.350nice.com/wp/archives/240,
 * "MultiChoice Preference Widget for Android."
 */
public class ListPreferenceMultiSelect extends ListPreference {
	private String separator;
	private static final String DEFAULT_SEPARATOR = ", ";
	private String checkAllKey = null;
	private boolean[] mClickedDialogEntryIndices;

	/**
	 * Constructor for list view with multi-select
	 * 
	 * @param context
	 *            Application context
	 * @param attrs
	 *            Attribute sets
	 */
	public ListPreferenceMultiSelect(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ListPreferenceMultiSelect);
		checkAllKey = a
				.getString(R.styleable.ListPreferenceMultiSelect_checkAll);
		String s = a.getString(R.styleable.ListPreferenceMultiSelect_separator);
		if (s != null) {
			separator = s;
		} else {
			separator = DEFAULT_SEPARATOR;
		}

		// Initialize the array of boolean to the same size as number of entries
		mClickedDialogEntryIndices = new boolean[getEntries().length];
	}

	/**
	 * Another constructor for list view with multi-select
	 * 
	 * @param context
	 */
	public ListPreferenceMultiSelect(Context context) {
		this(context, null);
	}

	/**
	 * Initialize the entry array
	 * 
	 * @param entries
	 *            List of entries to be initialize
	 */
	@Override
	public void setEntries(CharSequence[] entries) {
		super.setEntries(entries);
		// Initialize the array of boolean to the same size as number of entries
		mClickedDialogEntryIndices = new boolean[entries.length];
	}

	/**
	 * Initialize entries and their values
	 * 
	 * @param builder
	 *            Builder is initialized with the entry values
	 */
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		CharSequence[] entries = getEntries();
		CharSequence[] entryValues = getEntryValues();
		if (entries == null || entryValues == null
				|| entries.length != entryValues.length) {
			throw new IllegalStateException(
					"ListPreference requires an entries array and an entryValues "
							+ "array which are both the same length");
		}

		restoreCheckedEntries();
		builder.setMultiChoiceItems(entries, mClickedDialogEntryIndices,
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which,
							boolean val) {
						if (isCheckAllValue(which) == true) {
							checkAll(dialog, val);
						}
						mClickedDialogEntryIndices[which] = val;
					}
				});
	}

	/**
	 * Returns whether or not the list should all be checked
	 * 
	 * @param which
	 *            Index of entryValues to check for check
	 * @return Returns true if the value of entryValues at specified Index is
	 *         equal to the checkAllKey
	 */
	private boolean isCheckAllValue(int which) {
		final CharSequence[] entryValues = getEntryValues();
		if (checkAllKey != null) {
			return entryValues[which].equals(checkAllKey);
		}
		return false;
	}

	/**
	 * Check all items in the list
	 * 
	 * @param dialog
	 *            Dialog that contains the listview
	 * @param val
	 *            Boolean value to indicate if the item should be all checked
	 */
	private void checkAll(DialogInterface dialog, boolean val) {
		ListView lv = ((AlertDialog) dialog).getListView();
		int size = lv.getCount();
		for (int i = 0; i < size; i++) {
			lv.setItemChecked(i, val);
			mClickedDialogEntryIndices[i] = val;
		}
	}

	/**
	 * Parse the specified value
	 * 
	 * @param val
	 *            Value to parse
	 * @return Return parsed value
	 */
	public String[] parseStoredValue(CharSequence val) {
		if ("".equals(val)) {
			return null;
		} else {
			return ((String) val).split(separator);
		}
	}

	/**
	 * Restore previously saved checked entries
	 */
	private void restoreCheckedEntries() {
		CharSequence[] entryValues = getEntryValues();

		// Explode the string read in sharedpreferences
		String[] vals = parseStoredValue(getValue());

		if (vals != null) {
			List<String> valuesList = Arrays.asList(vals);
			for (int i = 0; i < entryValues.length; i++) {
				CharSequence entry = entryValues[i];
				if (valuesList.contains(entry)) {
					mClickedDialogEntryIndices[i] = true;
				}
			}
		}
	}

	/**
	 * Function that is called when the dialog is closed.
	 * 
	 * @param positiveResult
	 *            Boolean on whether or not to execute closing
	 */
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// super.onDialogClosed(positiveResult);
		ArrayList<String> values = new ArrayList<String>();

		CharSequence[] entryValues = getEntryValues();
		if (positiveResult && entryValues != null) {
			for (int i = 0; i < entryValues.length; i++) {
				if (mClickedDialogEntryIndices[i] == true) {
					// Don't save the state of check all option - if any
					String val = (String) entryValues[i];
					if (checkAllKey == null
							|| (val.equals(checkAllKey) == false)) {
						values.add(val);
					}
				}
			}

			if (callChangeListener(values)) {
				setValue(join(values, separator));
			}
		}
	}

	/**
	 * Join items as strings. Credits to kurellajunior.
	 * 
	 * @param pColl
	 *            Iterable list containing list items
	 * @param separator
	 *            Separator of each items in the string
	 * @return String with all items joined together
	 */
	protected static String join(Iterable<? extends Object> pColl,
			String separator) {
		Iterator<? extends Object> oIter;
		if (pColl == null || (!(oIter = pColl.iterator()).hasNext()))
			return "";
		StringBuilder oBuilder = new StringBuilder(String.valueOf(oIter.next()));
		while (oIter.hasNext())
			oBuilder.append(separator).append(oIter.next());
		return oBuilder.toString();
	}

	/**
	 * Checks to see if the string to be found is contained in the raw string.
	 * 
	 * @param straw
	 *            String to be found
	 * @param haystack
	 *            Raw string that can be read direct from preferences
	 * @param separator
	 *            Separator string. If null, static default separator will be
	 *            used
	 * @return boolean True if the straw was found in the haystack
	 */
	public static boolean contains(String straw, String haystack,
			String separator) {
		if (separator == null) {
			separator = DEFAULT_SEPARATOR;
		}
		String[] vals = haystack.split(separator);
		for (int i = 0; i < vals.length; i++) {
			if (vals[i].equals(straw)) {
				return true;
			}
		}
		return false;
	}
}
