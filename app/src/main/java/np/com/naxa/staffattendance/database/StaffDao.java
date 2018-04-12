package np.com.naxa.staffattendance.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import np.com.naxa.staffattendance.pojo.Staff;

/**
 * Created by samir on 4/1/2018.
 */

public class StaffDao {

    private long saveStaff(ContentValues contentValues) {
        return DatabaseHelper.getDatabaseHelper().getWritableDatabase().insert(DatabaseHelper.TABLE_STAFF, null, contentValues);

    }

    public long saveStaff(Staff staff) {
        return saveStaff(getContentValuesFronSaff(staff));
    }

    public ContentValues getContentValuesFronSaff(Staff staff) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KEY_STAFF_FULL_NAME, staff.getName());
        contentValues.put(DatabaseHelper.KEY_STAFF_TYPE, staff.getStaffType());
        return contentValues;
    }


    public ArrayList<Staff> getStaffFromCursor(Cursor cursor) {
        ArrayList<Staff> staffs = new ArrayList<>();

        if (cursor == null || cursor.getCount() <= 0) {
            return staffs;
        }

        while (cursor.moveToNext()) {
            String id = DatabaseHelper.getStringFromCursor(cursor, DatabaseHelper.KEY_STAFF_TYPE);
            String name = DatabaseHelper.getStringFromCursor(cursor, DatabaseHelper.KEY_STAFF_FULL_NAME);
            Staff staff = new Staff(id, name);
            staffs.add(staff);
        }

        return staffs;
    }

}
