package com.app.sharingscreen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.opengl.GLSurfaceView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.opentok.android.BaseVideoRenderer
import com.opentok.android.OpentokError
import com.opentok.android.Publisher
import com.opentok.android.PublisherKit
import com.opentok.android.Session
import com.opentok.android.Stream



class ShareScreen(var myActivity: Activity,var context: Context,val contentView: View,var API_KEY:String,var SESSION_ID:String,var TOKEN:String) {

     var session: Session? = null
     var publisher: Publisher? = null


    val isValid: Boolean
        get() = !(TextUtils.isEmpty(API_KEY) || TextUtils.isEmpty(SESSION_ID) || TextUtils.isEmpty(TOKEN))

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            if (!isValid) {
                finishWithMessage("Invalid OpenTokConfig. \"\"\"\n" +
                        "               OpenTokConfig:\n" +
                        "               API_KEY: $API_KEY\n" +
                        "               SESSION_ID: $SESSION_ID\n" +
                        "               TOKEN: $TOKEN\n" +
                        "               \"\"\".trimIndent()")
                return
            }
            initializeSession(API_KEY, SESSION_ID, TOKEN)
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(
                context,
                "Permission Denied\n$deniedPermissions",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    data class Builder(
        var myActivity: Activity? = null,
        var context: Context? = null,
        var contentView: View? = null,
        var API_KEY: String? = null,
        var SESSION_ID: String? = null,
        var TOKEN: String? = null,

    )

    {

        fun activity(myActivity: Activity) = apply { this.myActivity = myActivity }
        fun context(context: Context) = apply { this.context = context }
        fun contentView(contentView: View) = apply { this.contentView = contentView }
        fun API_KEY(API_KEY: String) = apply { this.API_KEY = API_KEY }
        fun SESSION_ID(SESSION_ID: String) = apply { this.SESSION_ID = SESSION_ID }
        fun TOKEN(TOKEN: String) = apply { this.TOKEN = TOKEN }

        fun build() = ShareScreen(myActivity!!, context!!, contentView!!,API_KEY!!,SESSION_ID!!,TOKEN!!)
    }

    private val publisherListener: PublisherKit.PublisherListener = object :
        PublisherKit.PublisherListener {
        override fun onStreamCreated(publisherKit: PublisherKit, stream: Stream) {
            Log.d("TAG", "onStreamCreated: Publisher Stream Created. Own stream ${stream.streamId}")
        }

        override fun onStreamDestroyed(publisherKit: PublisherKit, stream: Stream) {
            Log.d("TAG", "onStreamDestroyed: Publisher Stream Destroyed. Own stream ${stream.streamId}")
        }

        override fun onError(publisherKit: PublisherKit, opentokError: OpentokError) {
            finishWithMessage("PublisherKit onError: ${opentokError.message}")
        }
    }
    private val sessionListener: Session.SessionListener = object : Session.SessionListener {
        override fun onConnected(session: Session) {
            Log.d("TAG", "onConnected: Connected to session: ${session.sessionId}")

            val screenSharingCapturer = ScreenSharingCapturer(contentView)

            publisher = Publisher.Builder(myActivity).capturer(screenSharingCapturer).build()
            publisher?.setPublisherListener(publisherListener)
            publisher?.publisherVideoType = PublisherKit.PublisherKitVideoType.PublisherKitVideoTypeScreen



            publisher?.renderer?.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL)

            if (publisher?.view is GLSurfaceView) {
                (publisher?.view as GLSurfaceView).setZOrderOnTop(true)
            }
            session.publish(publisher)
        }

        override fun onDisconnected(session: Session) {
            Log.d("TAG", "onDisconnected: Disconnected from session: ${session.sessionId}")
        }

        override fun onStreamReceived(session: Session, stream: Stream) {
            Log.d("TAG", "onStreamReceived: New Stream Received ${stream.streamId} in session: ${session.sessionId}")
        }

        override fun onStreamDropped(session: Session, stream: Stream) {
            Log.d("TAG", "onStreamDropped: Stream Dropped: ${stream.streamId} in session: ${session.sessionId}")
        }

        override fun onError(session: Session, opentokError: OpentokError) {
            finishWithMessage("Session error: ${opentokError.message}")
        }
    }

    fun initializeShareScreen(){
        requestPermissions()
    }
    private fun requestPermissions() {

        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            .check();

    }
    private fun finishWithMessage(message: String) {
        Log.e("TAG", message)
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()

    }

    private fun initializeSession(apiKey: String, sessionId: String, token: String) {
        Log.i("TAG", "apiKey: $apiKey")
        Log.i("TAG", "sessionId: $sessionId")
        Log.i("TAG", "token: $token")

        /*
        The context used depends on the specific use case, but usually, it is desired for the session to
        live outside of the Activity e.g: live between activities. For a production applications,
        it's convenient to use Application context instead of Activity context.
         */
        session = Session.Builder(context, apiKey, sessionId).build().also {
            it.setSessionListener(sessionListener)
            it.connect(token)

        }
        session!!.onResume()


    }

    fun connectSession(){
        if(session != null){
            if (!isValid) {
                finishWithMessage("Invalid OpenTokConfig. \"\"\"\n" +
                        "               OpenTokConfig:\n" +
                        "               API_KEY: $API_KEY\n" +
                        "               SESSION_ID: $SESSION_ID\n" +
                        "               TOKEN: $TOKEN\n" +
                        "               \"\"\".trimIndent()")
                return
            }
            initializeSession(API_KEY, SESSION_ID, TOKEN)
        }
    }

     fun disconnectSession() {
        if (session == null) {
            return
        }
        if (publisher != null) {
            session!!.unpublish(publisher)
            publisher = null
        }
        session!!.disconnect()
    }
}