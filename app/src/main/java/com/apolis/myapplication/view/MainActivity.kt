package com.apolis.myapplication.view

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.apolis.myapplication.broadcastReceiver.MyBroadcastReceiver
import com.apolis.myapplication.metadata.User
import com.apolis.myapplication.ui.theme.MyApplicationTheme
import com.apolis.myapplication.viewmodel.MainViewModel
import com.apolis.myapplication.workManager.MyForeGroundWorker
import com.apolis.myapplication.workManager.MyForeGroundWorker.Companion.ARG_PROGRESS
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    @Inject
    lateinit var myBroadcastReceiver: MyBroadcastReceiver
    @Inject
    lateinit var targetIntentFilter: IntentFilter
    @Inject
    lateinit var workManager:WorkManager


    private lateinit var enableWorkManagerButton : LiveData<WorkInfo>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doSomethingWithBroadcastReceiver()
        setContent {
            setWorkManagerForeGroundService()
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                 UserList(userLivedataList = mainViewModel.userListFlow,
                     { startClick() },
                     enableWorkManagerButton
                 )
                }
            }
        }
    }
    /** Normally set foreground Service
     * <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
     *
     *
    val pendingIntent: PendingIntent =
    Intent(this, ExampleActivity::class.java).let { notificationIntent ->
    PendingIntent.getActivity(this, 0, notificationIntent, 0)
    }
    val notification: Notification = Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
    .setContentTitle(getText(R.string.notification_title))
    .setContentText(getText(R.string.notification_message))
    .setSmallIcon(R.drawable.icon)
    .setContentIntent(pendingIntent)
    .setTicker(getText(R.string.ticker_text))
    .build()
    // Notification ID cannot be 0.
    startForeground(ONGOING_NOTIFICATION_ID, notification)

     */



    private fun startClick(){

        setWorkManagerForeGroundService()
    }
    private fun setWorkManagerForeGroundService() {
        val workRequest = OneTimeWorkRequest.from(MyForeGroundWorker::class.java)
        // observe progress
//            .observe(this) {
//                    workInfo: WorkInfo? ->
//                if (workInfo != null) {
//                    val progress = workInfo.progress
//
//                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
//                        binding.start.isEnabled = true
//                    }
//                }
//            }
        // run the worker
        workManager.enqueue(workRequest)
        enableWorkManagerButton = workManager.getWorkInfoByIdLiveData(workRequest.id)

    }


    //context-registered receiver
   // To stop receiving broadcasts, call unregisterReceiver(android.content.BroadcastReceiver). Be sure to unregister the receiver when you no longer need it or the context is no longer valid.
    private fun doSomethingWithBroadcastReceiver(){
        registerReceiver(myBroadcastReceiver,targetIntentFilter)
    }

    override fun onDestroy() {
        unregisterReceiver(myBroadcastReceiver)
        super.onDestroy()
    }
    /**
     *   /Broadcast Receiver has security issue, so you can try with permission
     *   step 1 Sending with permissions
     *   sendBroadcast(Intent, String) or sendOrderedBroadcast(Intent, String, BroadcastReceiver, Handler, int, String, Bundle)
     */
    private fun sendBroadcastWithPermission(){
        sendBroadcast(Intent("send with permission"), Manifest.permission.SEND_SMS)
        //To receive the broadcast, the receiving app must request the permission as shown below:
        //<uses-permission android:name="android.permission.SEND_SMS"/>
        // can use custom permission
    }

    /**
     * receiving with permission:
     * manifest-register
     * <receiver android:name=".MyBroadcastReceiver"
    android:permission="android.permission.SEND_SMS">
    <intent-filter>
    <action android:name="android.intent.action.AIRPLANE_MODE"/>
    </intent-filter>
    </receiver>
     *
     * context-register
     * var filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
    registerReceiver(receiver, filter, Manifest.permission.SEND_SMS, null )
     */
    //if no need external app to receive, please use localBroadcast receiverer
    //better use context-register receiver cuz good for performance and user experience
    //also several intent are restrict to use context-register receiver, eg. connection-action


}


@Composable
fun UserList(userLivedataList: LiveData<List<User>>,
floatActionClick:()->Unit,
workInfoLivedata:LiveData<WorkInfo>) {
    val userList = userLivedataList.observeAsState()
    val workInfo by workInfoLivedata.observeAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = floatActionClick,
                backgroundColor = if(workInfo?.state == WorkInfo.State.SUCCEEDED) Color.Blue
            else Color.DarkGray) {

            }
        }
    ) {
        val progress = workInfo?.progress

        Column() {
            Slider(value = progress?.getFloat(ARG_PROGRESS, 0f)?:0f, onValueChange = {})
            LazyColumn(modifier = Modifier.weight(1f)){
                userList.value?.forEachIndexed { index, user ->
                    item {
                        Card {
                            Text(text = "$index ${user.name}")
                        }
                    }
                }
            }
        }

    }


}

