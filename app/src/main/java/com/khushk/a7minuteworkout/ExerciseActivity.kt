package com.khushk.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.khushk.a7minuteworkout.databinding.ActivityExerciseBinding
import java.util.*
import kotlin.collections.ArrayList



class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityExerciseBinding

    private var restTimer: CountDownTimer? = null
    private  var restProgress = 0
    private var restTimerDuration : Long = 10

    private var exerciseTimer: CountDownTimer? = null
    private  var exerciseProgress = 0
    private var exerciseTimerDuration: Long = 30

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarExerciseActivity)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbarExerciseActivity.setNavigationOnClickListener {
          customDialogForBackButton()
        }
        tts = TextToSpeech(this, this)

        exerciseList = Constants.defaultExerciseList()
        setupRestView()

        setupExerciseStatusRecyclerView()

    }

    override fun onDestroy() {

        if (restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        if (tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if (player!= null){
            player!!.stop()
        }

        super.onDestroy()
    }
    private fun setRestProgressBar(){

       binding.progressBar.progress = restProgress
        restTimer = object : CountDownTimer(restTimerDuration * 10000, 1000){
            override fun onTick(millisUntilFinished: Long) {
             restProgress++
                binding.progressBar.progress = 10 - restProgress
               binding.tvTimer.text = (10-restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)

                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
            }

        }.start()
    }
    private fun setExerciseProgressBar(){
        binding.progressBarExercise.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding.progressBarExercise.progress = exerciseTimerDuration.toInt() - exerciseProgress
                binding.tvExerciseTimer.text = (exerciseTimerDuration.toInt()-exerciseProgress).toString()
            }

            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!! -1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsComppleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }

        }.start()
    }
    private fun setupExerciseView(){
        binding.llRestView.visibility = View.GONE
        binding.llExerciseView.visibility = View.VISIBLE
        if (exerciseTimer!= null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0

        }
        speakOut(exerciseList!![currentExercisePosition].getName())
        setExerciseProgressBar()

        binding.ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding.tvExerciseName.text = exerciseList!![currentExercisePosition].getName()


    }
 private fun setupRestView(){
     try {
        // val soundURI = Uri.parse("android:resource://com.khushk.a7minuteworkout/" + R.raw.press_start)
         player = MediaPlayer.create(applicationContext,R.raw.press_start)
         player!!.isLooping = false
         player!!.start()

     }catch (e: Exception){
         e.printStackTrace()
     }



     binding.llRestView.visibility = View.VISIBLE
     binding.llExerciseView.visibility = View.GONE


     if (restTimer!= null){
         restTimer!!.cancel()
         restProgress = 0

     }
     binding.tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition +1].getName()
     setRestProgressBar()
 }

    override fun onInit(status: Int) {
       if (status == TextToSpeech.SUCCESS){
           val result = tts!!.setLanguage(Locale.ENGLISH)
           if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
               Log.e("TTS", "The Language specified is not supported")
       }else{
           Log.e("TTS","Initialization Failed!")
       }
    }
    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun setupExerciseStatusRecyclerView(){
        binding.rvExerciseStatus.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)
         exerciseAdapter = ExerciseStatusAdapter(exerciseList!!,this)
        binding.rvExerciseStatus.adapter = exerciseAdapter
    }
    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
        customDialog.findViewById<View>(R.id.tvYes).setOnClickListener {
            finish()
            customDialog.dismiss() // Dialog will be dismissed
        }
        customDialog.findViewById<View>(R.id.tvNo).setOnClickListener {
            customDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        customDialog.show()
    }
}