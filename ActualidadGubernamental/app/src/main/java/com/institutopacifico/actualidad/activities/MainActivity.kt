package com.institutopacifico.actualidad.activities

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.TextView
import com.institutopacifico.actualidad.R
import com.institutopacifico.actualidad.adapters.MainActivityDashboardRichObjectAdapter
import com.institutopacifico.actualidad.application.ApplicationClass
import com.institutopacifico.actualidad.objects.UserDataSingleton
import com.institutopacifico.actualidad.objects.UserObject
import com.institutopacifico.actualidad.utils.FragmentArrayGeneratorHelper
import com.institutopacifico.actualidad.utils.FragmentTransactionHelper
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.menu_left_drawer.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit internal var SlidingRoot: SlidingRootNav
    var doubleBackToExitPressedOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //SetContentView
        setContentView(R.layout.activity_main)

        //Set Toolbar
        setSupportActionBar(toolbar)

        //DrawerLayout
        var toggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        SlidingRoot = SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(true)
                .withDragDistance(metrics.widthPixels) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.9f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
                .withRootViewYTranslation(4) //C
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject()
        //main_menu = findViewById(R.id.main_menu) as LinearLayout

        //  fragmentManager = supportFragmentManager

        //Set NavigationBar listView
        setNavigationBarListView()

        //Set transaction Constants
        FragmentTransactionHelper.setTransactionData(this, SlidingRoot, supportFragmentManager)

        //Fill TextViews
        FillTextViews(text_view_main_activity_comment, text_view_main_activity_name)

        EventBus.getDefault().register(this)
    }

    private fun setNavigationBarListView() {
        navigation_bar_listview.layoutManager = LinearLayoutManager(this)
        navigation_bar_listview.adapter = MainActivityDashboardRichObjectAdapter(FragmentArrayGeneratorHelper().getFragmentArray(), toolbar)
    }

    override fun onBackPressed() {
        //   FragmentTransactionHelper.goToPreviousFragment()

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        //    var count: Int = FragmentTransactionHelper.fragmentManager.backStackEntryCount;
        if (FragmentTransactionHelper.fragmentManager.backStackEntryCount > 1) {
            FragmentTransactionHelper.fragmentManager.popBackStack()
            toolbar.title = getString(R.string.toolbar_title_onckackpressed)
            //  var a= PrimalBoletinFragment()
            //   a.FRAGMENT_TITLE=""

        } else {
            SlidingRoot.openMenu()
        }

        this.doubleBackToExitPressedOnce = true
        //  Toast.makeText(this, "Presione atras de nuevo para salir.", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 500)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    @Subscribe
    fun RefreshTextViews(UserObject_userObject: UserObject) {
        FillTextViews(text_view_main_activity_comment, text_view_main_activity_name)
    }

    public override fun onDestroy() {
        super.onDestroy()

        val refWatcher = ApplicationClass.getRefWatcher(this)
        refWatcher!!.watch(this)
        EventBus.getDefault().unregister(this)
    }

    private fun FillTextViews(comment: TextView?, name: TextView?) {
        name?.text = UserDataSingleton.userData.stringInstitutoPacificoAboutUserName
        comment?.text = UserDataSingleton.userData.stringInstitutoPacificoAboutMembership
    }
}
