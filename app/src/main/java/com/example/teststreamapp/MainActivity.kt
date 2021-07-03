package com.example.teststreamapp

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pedro.rtmp.utils.ConnectCheckerRtmp
import com.pedro.rtplibrary.rtmp.RtmpCamera1


class MainActivity : AppCompatActivity(),ConnectCheckerRtmp,SurfaceHolder.Callback,View.OnClickListener {

    lateinit var  startStream:Button
    lateinit var  endStream:Button
    lateinit var rtmpCamera1: RtmpCamera1

    val rtmpLink = "rtmp://184.72.239.149/vod"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startStream = findViewById(R.id.start_stream)
        endStream = findViewById(R.id.end_stream)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        startStream.setOnClickListener(View.OnClickListener {
            rtmpCamera1 = RtmpCamera1(surfaceView, this@MainActivity)
//start stream
//start stream
            if (rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                rtmpCamera1.startStream("rtmp://yourEndPoint")
            } else {
                /**This device cant init encoders, this could be for 2 reasons: The encoder selected doesnt support any configuration setted or your device hasnt a H264 or AAC encoder (in this case you can see log error valid encoder not found) */
            }
//stop stream
//stop stream
            rtmpCamera1.stopStream()
        })
    }

    override fun onAuthErrorRtmp() {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "Auth error", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onAuthSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "Auth success", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onConnectionFailedRtmp(reason: String) {
        runOnUiThread {
            if (rtmpCamera1.reTry(5000, reason)) {
                Toast.makeText(this@MainActivity, "Retry", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Connection failed. $reason",
                    Toast.LENGTH_SHORT
                )
                    .show()
                rtmpCamera1.stopStream()
            }
        }
    }

    override fun onConnectionStartedRtmp(rtmpUrl: String) {
    }


    override fun onConnectionSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(
                this@MainActivity,
                "Connection success",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDisconnectRtmp() {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "Disconnected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        rtmpCamera1.startPreview();
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        if (rtmpCamera1.isStreaming()) {
            rtmpCamera1.stopStream();
        }
        rtmpCamera1.stopPreview();
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.start_stream -> {
                if(!rtmpCamera1.isStreaming){
                    rtmpCamera1.startStream(rtmpLink);
                }
                else {
                    Toast.makeText(this@MainActivity, "Already streaming",
                        Toast.LENGTH_SHORT).show();
                }
            }
            R.id.end_stream -> {
                if(rtmpCamera1.isStreaming){
                    rtmpCamera1.stopStream()
                }
                else {
                    Toast.makeText(this@MainActivity, "Already streaming",
                        Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


}