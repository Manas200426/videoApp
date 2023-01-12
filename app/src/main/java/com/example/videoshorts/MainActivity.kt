package com.example.videoshorts

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.videoshorts.databinding.ActivityMainBinding
import com.example.videoshorts.databinding.ListVideoBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: VideoAdapter
    private val videos = ArrayList<Video>()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        videos.add(
            Video(
                "Video1",
            "http://commondataStorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            )
        )
        videos.add(
            Video(
                "Video2",
                "http://commondataStorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"

            )
        )
        videos.add(
            Video(
                "Video3",
                "http://commondataStorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
            )
        )
        adapter = VideoAdapter(this,videos,object: VideoAdapter.OnVideoPreparedListener{
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)

            }
        } )

        binding.viewpager2.adapter = adapter
        binding.viewpager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                if(previousIndex != -1){
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }
                val newIndex = exoPlayerItems.indexOfFirst { it.position ==  position}
                if (newIndex != -1){
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        } )
    }

    override fun onPause() {
        super.onPause()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewpager2.currentItem}
        if(index != -1){
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewpager2.currentItem}
        if(index != -1){
            val player = exoPlayerItems[index].exoPlayer

            player.playWhenReady = false
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(exoPlayerItems.isNotEmpty()){
            for(item in exoPlayerItems){
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }
    }
