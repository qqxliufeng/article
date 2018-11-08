package com.android.ql.lf.article.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import com.android.ql.lf.article.R
import com.android.ql.lf.article.ui.fragments.start.StartCustomTypeFragment
import com.android.ql.lf.baselibaray.ui.activity.BaseSplashActivity
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import kotlinx.android.synthetic.main.activity_splash_layout.*
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.AppSettingsDialog

class SplashActivity : BaseSplashActivity(),EasyPermissions.PermissionCallbacks,EasyPermissions.RationaleCallbacks {

    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    override fun getLayoutId() = R.layout.activity_splash_layout

    override fun initView() {
        super.initView()
        if (hasPermission()) {
            //延时三秒，进入主页
            mIvSplash.postDelayed({
//                FragmentContainerActivity.from(this).setClazz(StartCustomTypeFragment::class.java).setNeedNetWorking(true).setHiddenToolBar(true).start()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            },2500)
        } else {
            //请求权限
            requestPermission()
        }
    }

    private fun hasPermission() = EasyPermissions.hasPermissions(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)

    private fun requestPermission(){
        EasyPermissions.requestPermissions(this,"应用程序需要以下权限才能运行，请先设置",0,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //处理请求同意结果
        if (hasPermission()) {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        //处理请求失败结果
        if (EasyPermissions.somePermissionPermanentlyDenied(this,permissions.toList())){
            val builder = AlertDialog.Builder(this)
            builder.setPositiveButton("确定"){_,_->
                val packageURI = Uri.parse("package:$packageName")
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                startActivityForResult(intent, AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE)
            }
            builder.setNegativeButton("取消"){_,_->
                finish()
            }
            builder.setTitle("程序需要以下权限")
            builder.setItems(arrayOf("存储权限","相机权限"),null)
            builder.create().show()
        }else{
            requestPermission()
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {
    }

    override fun onRationaleDenied(requestCode: Int) {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasPermission()) {
//                FragmentContainerActivity.from(this).setClazz(StartCustomTypeFragment::class.java).setNeedNetWorking(true).setHiddenToolBar(true).start()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            } else {
                requestPermission()
            }
        }
    }
}