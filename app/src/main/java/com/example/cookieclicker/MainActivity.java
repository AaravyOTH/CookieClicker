package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    ImageView oreo, grandma, farm, milkPortal;
    int amountForAscending = 1000;
    int numOfGrandmas = 0, numOfFarms = 0, numOfMilkPortals = 0;
    int grandmaCost = 5, farmCost = 50,  milkPortalCost = 200;
    TextView grandmaPrice, farmPrice, milkPortalPrice, scoreText, ascendText;
    boolean farmTemp = true;
    float touchX;
    float touchY;
    int timesAscended = 0;
AtomicInteger clickAmount = new AtomicInteger(1);
    LinearLayout subLayout, topLayout;
    ConstraintLayout mainLayout;

    Button ascend;
    boolean touchInsideOreo = false;
    boolean isGrandmaAdded = false;
    boolean isFarmAdded = false;
    int amountClicked = 10000;
    boolean isMilkPortalAdded = false;
    AtomicInteger score = new AtomicInteger(0);
    boolean temp = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets Views

        //Grandma
        grandma = new ImageView(this);
        grandma.setId(View.generateViewId());
        grandma.setImageResource(R.drawable.grandma);
        grandmaPrice = findViewById(R.id.textView_grandmaCost);

        //farm
        farm = new ImageView(this);
        farm.setId(View.generateViewId());
        farm.setImageResource(R.drawable.farm);
        farmPrice = findViewById(R.id.textView_farmCost);

        //

        //Milk Portal
        milkPortal = new ImageView(this);
        milkPortal.setId(View.generateViewId());
        milkPortal.setImageResource(R.drawable.milkportal);
        milkPortalPrice = findViewById(R.id.textView_milkPortal);

        //Layouts
        subLayout = findViewById(R.id.subLayout);
        mainLayout = findViewById(R.id.mainLayout);
        topLayout = findViewById(R.id.topSubLayout);
        //

        //Other
        oreo = findViewById(R.id.imageView);

        scoreText = findViewById(R.id.textView_score);

        ascend = findViewById(R.id.Button_reset);
        ascendText = findViewById(R.id.textView_Ascend);
        //

        //end of Views

        ascendText.setText("Cookies  for  Click  Upgrade:  " + amountForAscending);

        //

        List<producerThread> threadList = new ArrayList<>();

        final ScaleAnimation animationOut = new ScaleAnimation(1.0f,0.1f,1.0f,0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        animationOut.setDuration(500);
        //Constraints

        //Animation For OREO
        final ScaleAnimation oreoAnimation1 = new ScaleAnimation(1.0f,0.8f,1.0f,0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        oreoAnimation1.setDuration(40);

        final ScaleAnimation oreoAnimation = new ScaleAnimation(1.0f,1.6f,1.0f,1.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        oreoAnimation.setDuration(120);

        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        oreo.startAnimation(rotateAnimation);

        //
        Animation rotatefast = AnimationUtils.loadAnimation(this, R.anim.rotatefast);




        ascend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score.get() > amountForAscending-1){
                    timesAscended++;
                    clickAmount.incrementAndGet();
                    amountForAscending = amountForAscending + timesAscended * 500;
                }
                for (producerThread thread : threadList) {
                    thread.stopThread();
                    score.set(0);
                    scoreText.setText("0");
                }
                Toast toast = Toast.makeText(getApplicationContext(), "Ascending...", Toast.LENGTH_LONG);
                toast.show();
                threadList.clear();
                score.set(0);
                scoreText.setText("0");
                subLayout.removeAllViewsInLayout();
                grandmaCost = 5;
                farmCost = 50;
                milkPortalCost = 200;
                ascendText.setText("Cookies for Click Upgrade: " + amountForAscending);
                isGrandmaAdded = false;
                isFarmAdded = false;
                isMilkPortalAdded = false;
                numOfGrandmas = 0;
                numOfMilkPortals = 0;
                numOfFarms = 0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        score.set(0);

                    }
                }, 3500);
            }

        });
        //OREO ON CLICK
        oreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeFloater();
                score.addAndGet(clickAmount.get());

                v.startAnimation(oreoAnimation1);
                if(oreoAnimation1.hasEnded()){
                    v.startAnimation(oreoAnimation);
                }

            }
        });
        //


        //Grandma OnClick
        grandma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.1f,1.0f,0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation1.setDuration(500);
                final ScaleAnimation fadeIn = new ScaleAnimation(0.1f,1.0f,0.1f,1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                fadeIn.setDuration(500);
                v.startAnimation(scaleAnimation1);
                //

                numOfGrandmas++;
                    score.set(score.get() - grandmaCost);
                producerThread newThread = new producerThread(3000);
                threadList.add(newThread);
                newThread.start();


                topLayout.removeView(grandma);

                ImageView temp = new ImageView(MainActivity.this);
                temp.setImageResource(R.drawable.grandma);
                temp.setAdjustViewBounds(true);
                temp.setMaxWidth(100);
                temp.setMaxHeight(100);



                subLayout.addView(temp);
                temp.startAnimation(fadeIn);
                grandmaCost = 1+ (grandmaCost + (3 * numOfGrandmas)) ;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isGrandmaAdded = false;
                    }
                }, 1500);

            }

        });
        //

        //Farm OnClick
        farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numOfFarms++;

                score.set(score.get() - farmCost);

                producerThread newThread = new producerThread(500);
                threadList.add(newThread);
                newThread.start();

                final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.1f,1.0f,0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation1.setDuration(500);

                final ScaleAnimation fadeIn = new ScaleAnimation(0.1f,1.0f,0.1f,1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                fadeIn.setDuration(500);

                v.startAnimation(scaleAnimation1);

                topLayout.removeView(farm);

                ImageView temp = new ImageView(MainActivity.this);
                temp.setImageResource(R.drawable.farm);
                temp.setAdjustViewBounds(true);
                temp.setMaxWidth(100);
                temp.setMaxHeight(100);

                subLayout.addView(temp);
                temp.startAnimation(fadeIn);

                farmCost = farmCost +  (4 * numOfFarms) ;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFarmAdded = false;
                    }
                }, 1500);
            }
        });
        //
        milkPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.1f,1.0f,0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation1.setDuration(500);
                final ScaleAnimation fadeIn = new ScaleAnimation(0.1f,1.0f,0.1f,1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                fadeIn.setDuration(500);
                v.startAnimation(scaleAnimation1);
                //

                numOfMilkPortals++;
                score.set(score.get() - milkPortalCost);


                producerThread newThread = new producerThread(75);
                threadList.add(newThread);
                newThread.start();
                topLayout.removeView(milkPortal);

                ImageView temp = new ImageView(MainActivity.this);
                temp.setImageResource(R.drawable.milkportal);
                temp.setAdjustViewBounds(true);
                temp.setMaxWidth(100);
                temp.setMaxHeight(100);



                subLayout.addView(temp);
                temp.startAnimation(fadeIn);
                milkPortalCost = 1+ (milkPortalCost + (5 * numOfMilkPortals)) ;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isMilkPortalAdded = false;
                    }
                }, 1500);
            }
        });
        //SCORE COUNTER | Runs Continuously
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void run() {

                        scoreText.setText(String.valueOf(score));
                        grandmaPrice.setText("Grandma : " + grandmaCost);
                        farmPrice.setText("Farm : " + farmCost);
                        milkPortalPrice.setText("Milk Portal : " + milkPortalCost);
                        if(score.get() < grandmaCost){
                            try {
                                topLayout.removeView(grandma);
                                isGrandmaAdded = false;

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        if(score.get() < farmCost) {
                            try {
                                //if(farmTemp == true){
                                //topLayout.findViewById(R.drawable.farm).startAnimation(oreoAnimation1);
                                // farmTemp = false;
                                //   }
                                //  if(farm.getAnimation().hasEnded()) {
                                topLayout.removeView(farm);
                                isFarmAdded = false;

                                //farmTemp = true;



                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        if(score.get() < milkPortalCost){
                            try{
                                topLayout.removeView(milkPortal);
                                isMilkPortalAdded = false;
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                                   }
                        if (score.get() >= grandmaCost){
                            addGrandma();
                        }
                        if (score.get() >= farmCost){
                            addFarm();
                        }
                        if(score.get() >= milkPortalCost){
                            addMilkPortal();
                        }

                    }
                });
            }



        };
        t.scheduleAtFixedRate(tt,0,100);

        //Runs Animated Background
        TimerTask backgroundRunner = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        background();
                    }
                });

            }
        };
        back.scheduleAtFixedRate(backgroundRunner,0,150);
        //
    }//END of OnCreate



//Timer for Score Counter
    Timer t = new Timer();
    Timer back = new Timer();

//

    //Adds Grandma to Top Of Screen And Plays Animation
    public void addGrandma(){
        if(!isGrandmaAdded) {
            grandma.setAdjustViewBounds(true);
            getConstraints(grandma, 200, 100);
            final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.5f,1.0f,0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation1.setDuration(80);
            final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.7f,1.0f,1.7f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation.setDuration(160);
            topLayout.addView(grandma);
            grandma.startAnimation(scaleAnimation1);
            if(scaleAnimation1.hasEnded()){
                grandma.startAnimation(scaleAnimation);
            }
            isGrandmaAdded = true;
        }
    }
    //
    public void addMilkPortal(){
        if(!isMilkPortalAdded) {
            milkPortal.setAdjustViewBounds(true);
            getConstraints(milkPortal, 200, 500);//DONT THINK I NEED THIS

            final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.8f,1.0f,0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation1.setDuration(80);
            final ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.6f,1.0f,1.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation.setDuration(160);
            topLayout.addView(milkPortal);
            milkPortal.startAnimation(scaleAnimation1);
            if(scaleAnimation1.hasEnded()){
                milkPortal.startAnimation(scaleAnimation);
            }
            isMilkPortalAdded = true;
        }

    }
    //Adds Farm to Top Of Screen And Plays Animation
    public void addFarm(){
        if(!isFarmAdded) {
            farm.setAdjustViewBounds(true);

            getConstraints(farm, 200, 500);

            final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.8f,1.0f,0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation1.setDuration(80);
            final ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.6f,1.0f,1.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation.setDuration(160);
            topLayout.addView(farm);
            farm.startAnimation(scaleAnimation1);
            if(scaleAnimation1.hasEnded()){
                farm.startAnimation(scaleAnimation);
            }
            isFarmAdded = true;
        }

    }
    //

    //Thread that Safely adds cookies that production Units have created
    public class producerThread extends Thread{
        private boolean isRunning = true;
        private int duration;

        public producerThread(int duration) {
            this.duration = duration;
        }
        @Override
        public void run() {
            while (isRunning) {

                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                score.incrementAndGet();
            }
        }

        public void stopThread() {
            isRunning = false;
        }
    }


    //Makes element that shows up onClick of OREO
    public void makeFloater(){
        final ImageView imageView = new ImageView(this);
        imageView.setId(View.generateViewId());
        int randomNum = (int) (Math.random() * 8) + 1;
        switch (randomNum) {
            case 1:
                imageView.setImageResource(R.drawable.oreo1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.oreo2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.oreo3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.oreo4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.oreo5);
                break;
            case 6:
                imageView.setImageResource(R.drawable.oreo6);
                break;
            case 7:
                imageView.setImageResource(R.drawable.oreo7);
                break;
            case 8:
                imageView.setImageResource(R.drawable.oreo8);
                break;
            default:
                imageView.setImageResource(R.drawable.oreo1);
                break;
        }


        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setAdjustViewBounds(true);
        imageView.setMaxHeight(100);
        imageView.setMaxWidth(200);
        mainLayout.addView(imageView);

        final ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);

        constraintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, mainLayout.getId(), constraintSet.BOTTOM);
        constraintSet.connect(imageView.getId(), ConstraintSet.TOP, mainLayout.getId(), constraintSet.TOP);
        constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, mainLayout.getId(), constraintSet.LEFT);
        constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, mainLayout.getId(), constraintSet.RIGHT);

        constraintSet.setHorizontalBias(imageView.getId(), touchX / mainLayout.getWidth());

        constraintSet.applyTo(mainLayout);
        int randomXStart = (int) (Math.random() * 201) - 100;
        TranslateAnimation translateAnimation = new TranslateAnimation(425 + randomXStart,425 - randomXStart,300,-425);
        translateAnimation.setDuration((int)(Math.random()*1000)+500);
        imageView.startAnimation(translateAnimation);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainLayout.removeView(imageView);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void background(){
        final ImageView imageView = new ImageView(this);
        imageView.setId(View.generateViewId());
        imageView.setImageResource(R.drawable.oreo);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setAdjustViewBounds(true);
        imageView.setMaxHeight(200);
        imageView.setMaxWidth(200);
        mainLayout.addView(imageView);
        ViewCompat.setZ(imageView, -1);

        final ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);

        constraintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, mainLayout.getId(), constraintSet.BOTTOM);
        constraintSet.connect(imageView.getId(), ConstraintSet.TOP, mainLayout.getId(), constraintSet.TOP);
        constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, mainLayout.getId(), constraintSet.LEFT);
        constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, mainLayout.getId(), constraintSet.RIGHT);

        constraintSet.setHorizontalBias(imageView.getId(), touchX);

        constraintSet.setHorizontalBias(imageView.getId(), (float)(Math.random()*2));
        constraintSet.applyTo(mainLayout);
        TranslateAnimation translateAnimation = new TranslateAnimation(imageView.getX(),imageView.getX(),-1200,1200);
        translateAnimation.setDuration((int)(Math.random()*4000)+2000);
        imageView.startAnimation(translateAnimation);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainLayout.removeView(imageView);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void removeView(ImageView imageView){
        final ScaleAnimation animationForRemoveView = new ScaleAnimation(1.0f,1.5f,1.0f,1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        animationForRemoveView.setDuration(200);

    //     imageView.startAnimation(scaleAnimation1);
    //    while(scaleAnimation1.hasEnded()!= true){

        //}

            topLayout.removeView(imageView);
    //    }catch (Exception e){
     //       e.printStackTrace();
     //   }
    }
    public void getConstraints(ImageView imageView, int marginTop, int marginLeft){
        ConstraintLayout.LayoutParams lp = new Constraints.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        ConstraintSet cs = new ConstraintSet();
        imageView.setAdjustViewBounds(true);

        cs.clone(mainLayout);

        imageView.setLayoutParams(lp);
        //imageView.setMaxHeight(300);
        //imageView.setMaxWidth(300);
        cs.connect(imageView.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, marginTop);
        cs.connect(imageView.getId(), ConstraintSet.LEFT, mainLayout.getId(), ConstraintSet.LEFT, marginLeft);
        cs.connect(imageView.getId(), ConstraintSet.RIGHT, mainLayout.getId(), ConstraintSet.RIGHT);
        cs.connect(imageView.getId(), ConstraintSet.BOTTOM, mainLayout.getId(), ConstraintSet.BOTTOM);

        cs.setHorizontalBias(imageView.getId(), .5f);
        cs.setVerticalBias(imageView.getId(), .5f);

        cs.applyTo(mainLayout);

    }
    //TODO:Fix Oreo Animation so it works while spinning
}