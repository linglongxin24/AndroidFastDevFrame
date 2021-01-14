package com.bjtsh.dylan.selectphoto

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.bjtsn.dylan.requestpermission.RequestPermission
import com.bjtsn.dylan.startactivityforresult.StartActivityForResult
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

class SelectPhoto {
    private var callBack: CallBack? = null
    private var mFragment: Fragment? = null
    private var mActivity: FragmentActivity? = null
    private var mContext: Context


    constructor(mActivity: FragmentActivity) {
        this.mActivity = mActivity
        this.mContext = mActivity
    }

    constructor(fragment: Fragment) {
        this.mFragment = fragment
        this.mContext = fragment.requireContext()
    }

    fun selectPhoto() :SelectPhoto{
        if (mActivity != null) {
            RequestPermission(mActivity!!).requestPermission(Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .setPermissionCheckCallBack(object : RequestPermission.PermissionCheckCallBack {
                        override fun onReject(permission: Array<out String>) {
                            Toast.makeText(mContext, "您拒绝了存储和拍照权限无法进行拍照！", Toast.LENGTH_SHORT).show()
                        }

                        override fun onRejectAndNoAsk(permission: Array<out String>) {
                            Toast.makeText(mContext, "您拒绝了存储和拍照权限无法进行拍照！", Toast.LENGTH_SHORT).show()
                        }

                        override fun onSucceed() {

                            showDialog()
                        }

                    })

        }
        return this
    }

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    /**
     * 弹出选择对话框
     */
    private fun showDialog() {
        val localView = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_add_picture, null)
        val tv_camera = localView.findViewById(R.id.tv_camera) as TextView
        val tv_gallery = localView.findViewById(R.id.tv_gallery) as TextView
        val tv_cancel = localView.findViewById(R.id.tv_cancel) as TextView
        val dialog = Dialog(mContext, R.style.custom_dialog)
        dialog.setContentView(localView)
        val window = dialog.window
        if (window != null) {
            window.setGravity(Gravity.BOTTOM)
            // 设置全屏
            val windowManager: WindowManager = window.windowManager
            val display = windowManager.defaultDisplay
            val lp: WindowManager.LayoutParams = window.attributes
            lp.width = display.width // 设置宽度
            dialog.window?.attributes = lp
            dialog.show()
        }
        tv_cancel.setOnClickListener { dialog.dismiss() }
        tv_camera.setOnClickListener {
            dialog.dismiss()
            /** 拍照 */
            camera()
        }
        tv_gallery.setOnClickListener {
            dialog.dismiss()
            /** 从系统相册选取照片 */
            gallery()
        }
    }

    /**
     * 拍照
     */
    fun camera() :SelectPhoto{
        /**判断存储卡是否可以用，可用进行存储 */
        if (hasSdcard()) {
            var dir: File? = null
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                dir = mContext.externalCacheDir
                if (!dir!!.exists()) {
                    dir.mkdirs()
                }
            }
            if (dir == null || !dir.exists()) {
                dir = mContext.cacheDir
                if (!dir.exists()) {
                    dir.mkdirs()
                }
            }
            val avatarFolder = File(dir, "avatar")
            if (!avatarFolder.exists()) {
                avatarFolder.mkdirs()
            }
            val tempFile = File(avatarFolder, System.currentTimeMillis().toString() + "_temp_photo.jpg")

            /**从文件中创建uri */
            val uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Uri.fromFile(tempFile)
            } else {
                FileProvider.getUriForFile(mContext, mContext.applicationInfo.packageName+ ".provider", tempFile)
            }
            val intent = Intent()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            /**开启一个带有返回值的Activity，PHOTO_REQUEST_CAMERA */
            val callBack = object : StartActivityForResult.CallBack {
                override fun onActivityResult(resultCode: Int, data: Intent?) {
                    /**从相机返回的数据**/
                    if (resultCode == Activity.RESULT_OK && hasSdcard()) {
                        var file = File(tempFile.path)
                        val fileUri = Uri.fromFile(file)
                        compressWithLuban(file)
                    } else {
                        Toast.makeText(mContext, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (mActivity != null) {
                StartActivityForResult(mActivity!!).startActivityForResult(intent).setOnActivityResultCallBack(callBack)
            } else if (mFragment != null) {
                StartActivityForResult(mFragment!!).startActivityForResult(intent).setOnActivityResultCallBack(callBack)

            }
        } else {
            Toast.makeText(mContext, "未找到存储卡，无法拍照！", Toast.LENGTH_SHORT).show()
        }
        return this
    }

    /**
     * 从相册获取
     */
    fun gallery() :SelectPhoto{
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val callBack = object : StartActivityForResult.CallBack {
            override fun onActivityResult(resultCode: Int, data: Intent?) {
                /** 从相册返回的数据**/
                if (resultCode == Activity.RESULT_OK && data != null) {
                    /**得到图片的全路径 */
                    val uri = data.data
                    val proj = arrayOf(MediaStore.Images.Media.DATA)

                    /**好像是android多媒体数据库的封装接口，具体的看Android文档 */
                    var cursor: Cursor? = null
                    if (mActivity != null) {
                        cursor = mActivity!!.managedQuery(uri, proj, null, null, null)
                    } else if (mFragment != null) {
                        cursor = mFragment!!.requireActivity().managedQuery(uri, proj, null, null, null)
                    }
                    if (cursor != null) {
                        //按我个人理解 这个是获得用户选择的图片的索引值
                        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst()
                        //最后根据索引值获取图片路径
                        val path = cursor.getString(column_index)
                        val file = File(path)
                        val fileUri = Uri.fromFile(file)
                        compressWithLuban(file)
                    }
                }

            }
        }
        if (mActivity != null) {
            StartActivityForResult(mActivity!!).startActivityForResult(intent).setOnActivityResultCallBack(callBack)
        } else if (mFragment != null) {
            StartActivityForResult(mFragment!!).startActivityForResult(intent).setOnActivityResultCallBack(callBack)

        }
        return this
    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private fun compressWithLuban(file: File) {
        Luban.with(mContext)
                .load(file)
                .setCompressListener(object : OnCompressListener {
                    override fun onStart() {

                    }

                    override fun onSuccess(file: File) {
                        callBack?.onSelectPhoto(file)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                }).launch()
    }

    /**
     * 判断sdcard是否被挂载
     */
    private fun hasSdcard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    interface CallBack {
        fun onSelectPhoto(file: File)
    }

}