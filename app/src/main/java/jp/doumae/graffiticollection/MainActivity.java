//package jp.doumae.graffiticollection;
//import android.app.DialogFragment;
//import android.app.WallpaperManager;
//import android.content.ClipData;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.content.Intent;
//import android.support.v4.view.PagerAdapter;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.ViewPager;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;
//
//public class MainActivity extends FragmentActivity {
//
//    ViewPager vp;
//    PagerAdapter pa;
//    View menuView = null;
//
//    public static List<String> images = new ArrayList<String>();
//    private static int changeNumber;
//    WallpaperManager wm;
//
//    private static final int REQUEST_CODE = 1001;
//    private static final int REQUEST_PAINT = 1002;
//    private static final int REQUEST_BOOKMENU = 1003;
//
//    DialogStationery ds;
//    DialogText dt;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // 隠しエリアメニュー
//        // 一件削除ボタン
//        Button sdBtn = (Button)findViewById(R.id.singleDelete);
//        sdBtn.setOnClickListener(deleteImage);
//        // 全件削除ボタン
//        Button alBtn = (Button)findViewById(R.id.allDelete);
//        alBtn.setOnClickListener(deleteImage);
//        // 壁紙変更
//        Button wallpaperBtn = (Button)findViewById(R.id.wallpaper);
//        wallpaperBtn.setOnClickListener(wallpaper);
//
//        // メニューボタンエリア
//        // 写真ボタン
//        ImageButton slideBtn = (ImageButton)findViewById(R.id.imageButton);
//        slideBtn.setOnClickListener(galleryOpen);
//        // 写真編集ボタン
//        ImageButton editBtn = (ImageButton)findViewById(R.id.drawButton);
//        editBtn.setOnClickListener(edit);
//        // 削除ボタン
//        ImageButton deleteBtn = (ImageButton)findViewById(R.id.deleteButton);
//        deleteBtn.setOnClickListener(deleteMenuOpen);
//        // その他ボタン
//        ImageButton otherwiseBtn = (ImageButton)findViewById(R.id.otherwiseButton);
//        otherwiseBtn.setOnClickListener(otherwiseMenuOpen);
//
//        wm = WallpaperManager.getInstance(this);
//        ds = new DialogStationery();
//        dt = new DialogText();
//    }
//
//    public void setItem(int item){
//        vp.setCurrentItem(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if(id == R.id.action_settings){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    // 削除メニュー表示
//    public View.OnClickListener otherwiseMenuOpen = (new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            menuOpen(findViewById(R.id.otherwise));
//        }
//    });
//
//    // 削除メニュー表示
//    public View.OnClickListener deleteMenuOpen = (new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            menuOpen(findViewById(R.id.Delete));
//        }
//    });
//    // メニューアニメーション
//    private void menuOpen(View view){
//        MenuAnimation mAnim = new MenuAnimation(getApplicationContext());
//        // メニューが開いていない
//        if(menuView == null){
//            mAnim.openAnimation(view);
//            menuView = view;
//        }
//        // 違うメニューを開く
//        else if(menuView != view){
//            mAnim.closeAnimation(menuView);
//            mAnim.openAnimation(view);
//            menuView = view;
//        }
//        // メニューを全て閉じる
//        else if(menuView == view){
//            mAnim.closeAnimation(view);
//            menuView = null;
//        }
//    }
//
//    // ギャラリー呼び出し
//    public View.OnClickListener galleryOpen = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            // 新規作成
//            Intent intent = new Intent(getApplication(),BookMenuActivity.class);
//            startActivityForResult(intent,REQUEST_BOOKMENU);
//        }
//    };
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // ギャラリーからのResult
//        if(resultCode == REQUEST_BOOKMENU && data != null){
//
//            images = data.getStringArrayListExtra("images");
////
////            for(String img : list){
////                images.add(img);
////            }
//
//            adapterSet();
//            vp.setCurrentItem(0);
//        }
//
//        // PaintActivityからResult
//        else if(requestCode == REQUEST_PAINT){
//            adapterSet();
//            vp.setCurrentItem(changeNumber);
//        }
//
//    }
//
//    // 写真編集
//    public View.OnClickListener edit = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            if(!images.isEmpty()) {
//                Intent intent = new Intent(getApplication(), PaintActivity.class);
//                // 編集画像
//                intent.putExtra("editImage", images.get(vp.getCurrentItem()));
//                // 編集画像List番号
//                intent.putExtra("editImageNumber",vp.getCurrentItem());
//                changeNumber = vp.getCurrentItem();
//                startActivityForResult(intent,REQUEST_PAINT);
//            }
//            // 写真List無し
//            else {
//                ds.setArguments(dt.dialogMessage("写真がセットされていません"));
//                ds.show(getFragmentManager(),"");
//                return;
//            }
//        }
//    };
//
//    // 壁紙変更
//    public View.OnClickListener wallpaper = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            if(!images.isEmpty()) {
//                Uri uri = Uri.parse(images.get(vp.getCurrentItem()));
//                ds.setArguments(dt.dialogMessage("壁紙を変更しました"));
//                ds.show(getFragmentManager(),"");
//                try {
//                    wm.setBitmap(Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), wm.getDesiredMinimumWidth(), wm.getDesiredMinimumHeight(), true));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            // 写真List無し
//            else {
//                // メッセージ表示
//                ds.setArguments(dt.dialogMessage("写真がセットされていません"));
//                ds.show(getFragmentManager(),"");
//                return;
//            }
//        }
//    };
//
//    // 写真1件削除
//    public View.OnClickListener deleteImage = (new View.OnClickListener() {
//
//        @Override
//        public void onClick(View view) {
//            // 一件削除
//            if(images.size() != 0 && view.getId() == R.id.singleDelete) {
//                int fragmentPosition = vp.getCurrentItem();
//                images.remove(fragmentPosition);
//                adapterSet();
//                ds.setArguments(dt.dialogMessage("写真を削除しました"));
//                ds.show(getFragmentManager(),"");
//                vp.setCurrentItem(fragmentPosition-1);
//            }
//            // 全削除
//            else if(images.size() != 0 && view.getId() == R.id.allDelete) {
//                images.clear();
//                adapterSet();
//                ds.setArguments(dt.dialogMessage("全ての写真を削除しました"));
//                ds.show(getFragmentManager(),"");
//            }
//            else {
//                // メッセージ表示
//                ds.setArguments(dt.dialogMessage("写真がセットされていません"));
//                ds.show(getFragmentManager(),"");
//                return;
//            }
//        }
//    });
//
//
//    // アダプターセット
//    public void adapterSet(){
//        pa = new jp.doumae.graffiticollection.PagerAdapter(getSupportFragmentManager(), images);
//        vp = (ViewPager) findViewById(R.id.pager);
//        vp.setAdapter(pa);
//
//
//
//        for(String list : images){
//            System.out.println("aaa"+list);
//        }
//    }
//}

package jp.doumae.graffiticollection;
import android.app.DialogFragment;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;

public class MainActivity extends FragmentActivity {

    ViewPager vp;
    PagerAdapter pa;
    View menuView = null;

    public static List<String> images = new ArrayList<String>();
    private static int changeNumber;
    WallpaperManager wm;

    private static final int REQUEST_CODE = 1001;
    private static final int REQUEST_PAINT = 1002;

    DialogStationery ds;
    DialogText dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 隠しエリアメニュー
        // 写真List新規作成ボタン
        Button newBtn = (Button)findViewById(R.id.newImageSet);
        newBtn.setOnClickListener(galleryOpen);
        // 写真追加ボタン
        Button addBtn = (Button)findViewById(R.id.imageAdd);
        addBtn.setOnClickListener(galleryOpen);
        // 一件削除ボタン
        Button sdBtn = (Button)findViewById(R.id.singleDelete);
        sdBtn.setOnClickListener(deleteImage);
        // 全件削除ボタン
        Button alBtn = (Button)findViewById(R.id.allDelete);
        alBtn.setOnClickListener(deleteImage);
        // 壁紙変更
        Button wallpaperBtn = (Button)findViewById(R.id.wallpaper);
        wallpaperBtn.setOnClickListener(wallpaper);

        // メニューボタンエリア
        // 写真ボタン
        ImageButton slideBtn = (ImageButton)findViewById(R.id.imageButton);
        slideBtn.setOnClickListener(ImageSetMenuOpen);
        // 写真編集ボタン
        ImageButton editBtn = (ImageButton)findViewById(R.id.drawButton);
        editBtn.setOnClickListener(edit);
        // 削除ボタン
        ImageButton deleteBtn = (ImageButton)findViewById(R.id.deleteButton);
        deleteBtn.setOnClickListener(deleteMenuOpen);
        // その他ボタン
        ImageButton otherwiseBtn = (ImageButton)findViewById(R.id.otherwiseButton);
        otherwiseBtn.setOnClickListener(otherwiseMenuOpen);

        wm = WallpaperManager.getInstance(this);
        ds = new DialogStationery();
        dt = new DialogText();
    }

    public void setItem(int item){
        vp.setCurrentItem(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 写真メニュー表示
    public View.OnClickListener ImageSetMenuOpen = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            menuOpen(findViewById(R.id.imageSet));
        }
    });

    // 削除メニュー表示
    public View.OnClickListener otherwiseMenuOpen = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            menuOpen(findViewById(R.id.otherwise));
        }
    });

    // 削除メニュー表示
    public View.OnClickListener deleteMenuOpen = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            menuOpen(findViewById(R.id.Delete));
        }
    });
    // メニューアニメーション
    private void menuOpen(View view){
        MenuAnimation mAnim = new MenuAnimation(getApplicationContext());
        // メニューが開いていない
        if(menuView == null){
            mAnim.openAnimation(view);
            menuView = view;
        }
        // 違うメニューを開く
        else if(menuView != view){
            mAnim.closeAnimation(menuView);
            mAnim.openAnimation(view);
            menuView = view;
        }
        // メニューを全て閉じる
        else if(menuView == view){
            mAnim.closeAnimation(view);
            menuView = null;
        }
    }

    // ギャラリー呼び出し
    public View.OnClickListener galleryOpen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 新規作成
            if(view.getId() == R.id.newImageSet){
                // 画像List初期化
                images.clear();
                adapterSet();
            }
            // 画像リスト無し
            else if(view.getId() == R.id.imageAdd && images.isEmpty()){
                ds.setArguments(dt.dialogMessage("写真がセットされていません"));
                ds.show(getFragmentManager(),"");
                return;
            }
            // ギャラリー呼び出し
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);

            MenuAnimation mAnim = new MenuAnimation(getApplicationContext());
            mAnim.closeAnimation(menuView);
            menuView = null;
        }
    };

    // 写真List作成
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ギャラリーからのResult
        if(requestCode == REQUEST_CODE && resultCode ==RESULT_OK){

            // 複数選択
            if(data.getClipData() != null){
                int itemCount = data.getClipData().getItemCount();
                // 画像List作成
                for (int i = 0; i < itemCount; i++) {
                    ClipData.Item cUri = data.getClipData().getItemAt(i);
                    images.add(cUri.getUri().toString());
                }
            }
            // 一枚選択
            else {
                Uri uri = data.getData();
                images.add(uri.toString());
            }
            adapterSet();
            vp.setCurrentItem(0);
        }

        // PaintActivityからResult
        else if(requestCode == REQUEST_PAINT){
            adapterSet();
            vp.setCurrentItem(changeNumber);
        }

    }

    // 写真編集
    public View.OnClickListener edit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!images.isEmpty()) {
                Intent intent = new Intent(getApplication(), PaintActivity.class);
                // 編集画像
                intent.putExtra("editImage", images.get(vp.getCurrentItem()));
                // 編集画像List番号
                intent.putExtra("editImageNumber",vp.getCurrentItem());
                changeNumber = vp.getCurrentItem();
                startActivityForResult(intent,REQUEST_PAINT);
            }
            // 写真List無し
            else {
                ds.setArguments(dt.dialogMessage("写真がセットされていません"));
                ds.show(getFragmentManager(),"");
                return;
            }
        }
    };

    // 壁紙変更
    public View.OnClickListener wallpaper = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!images.isEmpty()) {
                Uri uri = Uri.parse(images.get(vp.getCurrentItem()));
                ds.setArguments(dt.dialogMessage("壁紙を変更しました"));
                ds.show(getFragmentManager(),"");
                try {
                    wm.setBitmap(Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), wm.getDesiredMinimumWidth(), wm.getDesiredMinimumHeight(), true));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 写真List無し
            else {
                // メッセージ表示
                ds.setArguments(dt.dialogMessage("写真がセットされていません"));
                ds.show(getFragmentManager(),"");
                return;
            }
        }
    };

    // 写真1件削除
    public View.OnClickListener deleteImage = (new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            // 一件削除
            if(images.size() != 0 && view.getId() == R.id.singleDelete) {
                int fragmentPosition = vp.getCurrentItem();
                images.remove(fragmentPosition);
                adapterSet();
                ds.setArguments(dt.dialogMessage("写真を削除しました"));
                ds.show(getFragmentManager(),"");
                vp.setCurrentItem(fragmentPosition-1);
            }
            // 全削除
            else if(images.size() != 0 && view.getId() == R.id.allDelete) {
                images.clear();
                adapterSet();
                ds.setArguments(dt.dialogMessage("全ての写真を削除しました"));
                ds.show(getFragmentManager(),"");
            }
            else {
                // メッセージ表示
                ds.setArguments(dt.dialogMessage("写真がセットされていません"));
                ds.show(getFragmentManager(),"");
                return;
            }
        }
    });


    // アダプターセット
    public void adapterSet(){
        pa = new jp.doumae.graffiticollection.PagerAdapter(getSupportFragmentManager(), images);
        vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(pa);
    }
}