package co.edu.udea.compumovil.gr06_20172.lab1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //declaracion de variables globes
    DbHelper dbHelper;
    SQLiteDatabase db;
    Fragment info = new InformationFragment();
    Fragment edit = new EditFragment();
    FragmentTransaction manager = getSupportFragmentManager().beginTransaction();

    //FloatingActionButton fab;
    ///////////////

    private boolean controlSelect=false;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper =new DbHelper(this);//nueva base de datos

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create Navigation drawer and inflate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        InformationFragment fragment = new InformationFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();

        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);
                        // TODO: handle navigation
                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()){


                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.info:
                                //Toast.makeText(getApplicationContext(),"Info Selected",Toast.LENGTH_SHORT).show();
                                InformationFragment fragment = new InformationFragment();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame,fragment);
                                fragmentTransaction.commit();
                                return true;

                            case R.id.edit:
                                //Toast.makeText(getApplicationContext(),"Edit Selected",Toast.LENGTH_SHORT).show();
                                EditFragment fragment1 = new EditFragment();
                                android.support.v4.app.FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction1.replace(R.id.frame,fragment1);
                                fragmentTransaction1.commit();
                                return true;

                            // For rest of the options we just show a toast on click

                            case R.id.action_exit:
                                db = dbHelper.getWritableDatabase();
                                db.execSQL("delete from " + StatusContract.TABLE_LOGIN);
                                db.close();
                                //Toast.makeText(getApplicationContext(),"Exit Selected",Toast.LENGTH_SHORT).show();
                                Intent newActivity = new Intent(MainActivity.this, Login.class);
                                startActivity(newActivity);
                                finish();
                                return true;

                            default:
                                Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                                return true;

                        }
                    }
                });
        //manager.replace(R.id.fragment_container, info);
        //manager.commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            db = dbHelper.getWritableDatabase();
            db.execSQL("delete from " + StatusContract.TABLE_LOGIN);
            db.close();
            Intent newActivity = new Intent(this, Login.class);
            startActivity(newActivity);
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

}
