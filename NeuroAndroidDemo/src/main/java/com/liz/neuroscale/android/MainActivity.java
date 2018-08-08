package com.liz.neuroscale.android;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseManager;
import com.liz.neuroscale.android.activities.ConfigureActivity;
import com.liz.neuroscale.android.activities.PublishActivity;
import com.liz.neuroscale.android.activities.SubscribeActivity;
import com.mikepenz.fastadapter.utils.RecyclerViewCacheUtil;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import ExtraUtils.Configuration;
import ExtraUtils.DataStructure.NeuroPipeLine;
import ExtraUtils.HTTP.HttpManager;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<NeuroPipeLine> mPipeline;// = new ArrayList<>();
    public static ArrayList<Muse> mMuseDevice;
    private AccountHeader headerResult = null;
    private Drawer result;

    private Toolbar mToolbar;
//    private FragmentTabHost mTabHost = null;;
    private TabHost mTabHost = null;
    private LocalActivityManager mLocalActivityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**init fragment tabhost */
//        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
//        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
//
//        mTabHost.addTab(mTabHost.newTabSpec("0")
//                .setIndicator(getString(R.string.drawer_neuroscale_configure)),
//                ConfigureFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("1")
//                        .setIndicator(getString(R.string.drawer_neuroscale_publish)),
//                PublishFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("2")
//                        .setIndicator(getString(R.string.drawer_neuroscale_subscribe)),
//                SubscribeFragment.class, null);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        /**init tabhost */
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(mLocalActivityManager);
        /**add tabs*/
        mTabHost.addTab(mTabHost.newTabSpec("0")
                .setIndicator(getString(R.string.drawer_neuroscale_configure))
                .setContent(new Intent(this, ConfigureActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("1")
                .setIndicator(getString(R.string.drawer_neuroscale_publish))
                .setContent(new Intent(this, PublishActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("2")
                .setIndicator(getString(R.string.drawer_neuroscale_subscribe))
                .setContent(new Intent(this, SubscribeActivity.class)));
        /**array list*/
        mPipeline = new ArrayList<>();
        mMuseDevice = new ArrayList<>();
        /**Handle Toolbar*/
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.drawer_neuroscale_main_activity_title);
        /**save instance state*/
        if (savedInstanceState == null) {
            mTabHost.setCurrentTab(0);
        }
        /**init navigation window*/
        this.InitSlidingWindow(savedInstanceState);
        /**update all the devices*/
        updateBackground();
    }
    /**
     * init navigation window
     *
     * @param savedInstanceState
     */
    private void InitSlidingWindow(Bundle savedInstanceState) {
        final IProfile profile = new ProfileDrawerItem()
                .withName("Qusp")
                .withEmail("Qusp@qusp.io")
                .withIcon(R.drawable.logo_qusp)
                .withIdentifier(100);
        /**Create the AccountHeader*/
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.logo_glassbrain)
                .addProfiles(
                        profile
//                        new ProfileSettingDrawerItem()
//                                .withName("Manage Account")
//                                .withIcon(GoogleMaterial.Icon.gmd_settings)
//                                .withIdentifier(101)
                )
                .withSavedInstance(savedInstanceState)
                .build();
        /**add the drawer to show the navigation menu*/
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_neuroscale_configure)
                                .withDescription(R.string.drawer_neuroscale_configure_desc)
                                .withIcon(R.drawable.ic_settings)
                                .withIdentifier(Configuration.ID_Configure)
                                .withSelectable(false),
                        new SectionDrawerItem().withName(R.string.drawer_neuroscale_section_operations),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_neuroscale_publish)
                                .withDescription(R.string.drawer_neuroscale_publish_drawer_desc)
                                .withIcon(R.drawable.ic_publish)
                                .withIdentifier(Configuration.ID_Publish)
                                .withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_neuroscale_subscribe)
                                .withDescription(R.string.drawer_neuroscale_subscribe_desc)
                                .withIcon(R.drawable.ic_subscribe)
                                .withIdentifier(Configuration.ID_Subscribe)
                                .withSelectable(false)
//                        new DividerDrawerItem(),
//                        new PrimaryDrawerItem()
//                                .withName(R.string.drawer_neuroscale_setting)
//                                .withDescription(R.string.drawer_neuroscale_setting_desc)
//                                .withIcon(GoogleMaterial.Icon.gmd_adb)
//                                .withIdentifier(Configuration.ID_AdvancedSetting)
//                                .withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    /**
                     * if this item is been clicked
                     */
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            result.setSelection(drawerItem.getIdentifier(), false);
                            if (drawerItem.getIdentifier() == Configuration.ID_Configure) {
                                mTabHost.setCurrentTab(0);
////                                Configuration.PageIdentifer = Configuration.ID_Configure;
                                getSupportActionBar().setTitle(R.string.drawer_neuroscale_configure);
                                Toast.makeText(MainActivity.this, getString(R.string.drawer_neuroscale_configure), Toast.LENGTH_SHORT).show();
                            } else if (drawerItem.getIdentifier() == Configuration.ID_Publish) {
                                mTabHost.setCurrentTab(1);
////                                Configuration.PageIdentifer = Configuration.ID_Publish;
                                getSupportActionBar().setTitle(R.string.drawer_neuroscale_publish);
                                Toast.makeText(MainActivity.this, getString(R.string.drawer_neuroscale_publish), Toast.LENGTH_SHORT).show();
                            } else if (drawerItem.getIdentifier() == Configuration.ID_Subscribe) {
                                mTabHost.setCurrentTab(2);
////                                Configuration.PageIdentifer = Configuration.ID_Subscribe;
                                getSupportActionBar().setTitle(R.string.drawer_neuroscale_subscribe);
                                Toast.makeText(MainActivity.this, getString(R.string.drawer_neuroscale_subscribe), Toast.LENGTH_SHORT).show();
                            } else if (drawerItem.getIdentifier() == Configuration.ID_AdvancedSetting) {
////                                Configuration.PageIdentifer = Configuration.ID_AdvancedSetting;
                                getSupportActionBar().setTitle(R.string.drawer_neuroscale_setting);
                                Toast.makeText(MainActivity.this, getString(R.string.drawer_neuroscale_setting), Toast.LENGTH_SHORT).show();
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();
        //if you have many different types of DrawerItems you can magically pre-cache those items to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first clear the cache to make sure no old elements are in
        //RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);
        new RecyclerViewCacheUtil<IDrawerItem>()
                .withCacheSize(5).apply(result.getRecyclerView(), result.getDrawerItems());

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(Configuration.ID_Configure, false);
            //set the active profile
            headerResult.setActiveProfile(profile);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case R.id.menu_neuroscale_configure:
//                getSupportActionBar().setTitle(R.string.drawer_neuroscale_configure);
//                Fragment f = ConfigureFragment.newInstance(getString(R.string.drawer_neuroscale_configure));
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            moveTaskToBack(true);
            super.onBackPressed();
        }
    }

    /**
     * find pipelines and muse
     */
    private void updateBackground()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**clear all the pipelines in current list*/
                mPipeline.clear();
                ArrayList<NeuroPipeLine> pipeLines = HttpManager.GetPipelines();
                if (pipeLines.size()>0){
                    //add pipelines
                    for (NeuroPipeLine pipeLine : pipeLines) {
                        mPipeline.add(pipeLine);
                    }
                }
                /**find all paired muse device*/
                MuseManager.refreshPairedMuses();
                List<Muse> muses = MuseManager.getPairedMuses();
                for (Muse muse : muses) {
                    mMuseDevice.add(muse);   //add all the paired muse
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLocalActivityManager.dispatchResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(isFinishing());
    }
}
