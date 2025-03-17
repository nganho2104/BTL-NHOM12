package com.example.btl_comicapp.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.btl_comicapp.object.TruyenTranh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "DbHelper";
    private static final String DATABASE_NAME = "COMIC.db";
    private static final int DATABASE_VERSION = 1;

    // Đường dẫn đầy đủ đến file /data/data/[package]/databases/COMIC.db
    private final String DB_PATH;
    private final Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        // Cách khuyến nghị: dùng getDatabasePath() thay vì thủ công
        DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath();

        // Nếu database chưa tồn tại, copy từ assets
        if (!checkDatabaseExists()) {
            // Tạo database rỗng để ghi đè
            SQLiteDatabase db = super.getReadableDatabase();
            db.close();

            // Copy file COMIC.db từ assets
            copyDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Nếu bạn đã có bảng 'truyen', 'theloai', 'linktruyen' trong COMIC.db,
        // KHÔNG cần tạo lại ở đây. Để tránh ghi đè.
        //
        // Ví dụ: Nếu muốn tạo bảng mới (chưa có trong COMIC.db), thì mới thêm lệnh:
        // db.execSQL("CREATE TABLE IF NOT EXISTS linktruyen (Id TEXT, Chap TEXT, Link TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nếu cần nâng cấp database, bạn có thể viết logic xóa bảng hoặc migrate ở đây.
        // Ví dụ:
        // db.execSQL("DROP TABLE IF EXISTS truyen");
        // onCreate(db);
    }

    /**
     * Copy file COMIC.db từ assets vào /data/data/[package]/databases/
     */
    private void copyDatabase() {
        try {
            // Mở file .db trong thư mục assets
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);

            // Đảm bảo thư mục databases/ tồn tại
            File dbFile = new File(DB_PATH);
            File dbDir = dbFile.getParentFile();
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            // Tạo outputStream ghi vào DB_PATH
            OutputStream outputStream = new FileOutputStream(DB_PATH);

            // Ghi dữ liệu từng khối 1024 byte
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            Log.d(TAG, "Database sao chép thành công!");
        } catch (IOException e) {
            Log.e(TAG, "Lỗi sao chép database: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra xem file COMIC.db đã tồn tại ở /data/data/[package]/databases/ chưa
     */
    private boolean checkDatabaseExists() {
        File dbFile = new File(DB_PATH);
        return dbFile.exists();
    }

    // ================================
    //     HÀM LẤY DỮ LIỆU MẪU
    // ================================
    public ArrayList<TruyenTranh> getAllTruyen() {
        ArrayList<TruyenTranh> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM truyen", null);
        Log.d(TAG, "Tìm thấy số truyện: " + cursor.getCount());

        while (cursor.moveToNext()) {
            @SuppressLint("Range") TruyenTranh truyen = new TruyenTranh(
                    cursor.getString(cursor.getColumnIndex("Id")),
                    cursor.getString(cursor.getColumnIndex("Name")),
                    cursor.getString(cursor.getColumnIndex("Author")),
                    cursor.getString(cursor.getColumnIndex("Description")),
                    cursor.getString(cursor.getColumnIndex("imageLink")),
                    cursor.getString(cursor.getColumnIndex("IdCategory"))
            );
            list.add(truyen);
        }
        cursor.close();
        db.close();

        Log.d(TAG, "Danh sách truyện lấy ra: " + list.size());
        return list;
    }

    public TruyenTranh getTruyenById(String id) {
        SQLiteDatabase db = getReadableDatabase();
        TruyenTranh truyenTranh = null;

        Cursor cursor = db.rawQuery("SELECT * FROM truyen WHERE Id = ?", new String[]{id});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String tenTruyen = cursor.getString(cursor.getColumnIndex("Name"));
            @SuppressLint("Range") String tacGia = cursor.getString(cursor.getColumnIndex("Author"));
            @SuppressLint("Range") String moTa = cursor.getString(cursor.getColumnIndex("Description"));
            @SuppressLint("Range") String linkAnh = cursor.getString(cursor.getColumnIndex("imageLink"));
            @SuppressLint("Range") String idCategory = cursor.getString(cursor.getColumnIndex("IdCategory"));

            truyenTranh = new TruyenTranh(id, tenTruyen, tacGia, moTa, linkAnh, idCategory);
        }
        cursor.close();
        db.close();

        return truyenTranh;
    }

    public String getCategoryNameById(String idCategory) {
        SQLiteDatabase db = getReadableDatabase();
        String categoryName = "Không xác định"; // Giá trị mặc định

        Cursor cursor = db.rawQuery("SELECT NameCategory FROM TheLoai WHERE IdCategory = ?",
                new String[]{idCategory});
        if (cursor.moveToFirst()) {
            categoryName = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return categoryName;
    }
}
