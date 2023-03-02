package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.view.ViewCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    ImageView oreo;
    int numOfGrandmas = 0, numOfFarms = 0;
    int grandmaCost = 5, farmCost = 50;
    TextView grandmaPrice, farmPrice;
    float touchX;
    float touchY;
    int numForViewAdding = 0;
    boolean canBuyGrandma = false;
    TextView scoreText;
    LinearLayout subLayout;
    ConstraintLayout mainLayout;
    ImageView grandma;
    ImageView farm;
    boolean isGrandmaAdded = false;
    boolean isFarmAdded = false;
    AtomicInteger score = new AtomicInteger(0);

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

        //OTHER
        subLayout = findViewById(R.id.subLayout);
        mainLayout = findViewById(R.id.mainLayout);
        oreo = findViewById(R.id.imageView);
        scoreText = findViewById(R.id.textView_score);
        //

        //end of Views

        //RUN BACKGROUND ANIMATION

        //


        //Constraints
        ConstraintLayout.LayoutParams lp = new Constraints.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        ConstraintSet cs = new ConstraintSet();
        ConstraintSet cs2 = new ConstraintSet();


        cs.clone(mainLayout);
        cs2.clone(mainLayout);

        grandma.setLayoutParams(lp);
        grandma.setAdjustViewBounds(true);
        grandma.setMaxHeight(300);
        grandma.setMaxWidth(300);
        // Grandma constraints
        cs.connect(grandma.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 200);
        cs.connect(grandma.getId(), ConstraintSet.LEFT, mainLayout.getId(), ConstraintSet.LEFT, 200);
        cs.connect(grandma.getId(), ConstraintSet.RIGHT, mainLayout.getId(), ConstraintSet.RIGHT);
        cs.setVerticalBias(grandma.getId(), .2f);
        cs.setHorizontalBias(grandma.getId(), .1f);
        cs.applyTo(mainLayout);

        // Farm constraints
        farm.setLayoutParams(lp);
        farm.setAdjustViewBounds(true);
        farm.setMaxHeight(300);
        farm.setMaxWidth(300);

        // Grandma constraints
        cs2.connect(farm.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 200);
        cs2.connect(farm.getId(), ConstraintSet.LEFT, mainLayout.getId(), ConstraintSet.LEFT, 800);
        cs2.connect(farm.getId(), ConstraintSet.RIGHT, mainLayout.getId(), ConstraintSet.RIGHT);
        cs2.setVerticalBias(farm.getId(), .4f);
        cs2.setHorizontalBias(farm.getId(), .4f);
        //
        cs2.applyTo(mainLayout);


        //Animation For OREO
        final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.8f,1.0f,0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation1.setDuration(40);

        final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.6f,1.0f,1.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(120);

        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        oreo.startAnimation(rotateAnimation);

        //


        //OREO ON CLICK
        oreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeFloater();

                score.incrementAndGet();

                v.startAnimation(scaleAnimation1);
                if(scaleAnimation1.hasEnded()){
                    v.startAnimation(scaleAnimation);
                }
            }
        });
        //


        //Grandma OnClick
        grandma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("grandma", "it clicked!");
                final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.1f,1.0f,0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation1.setDuration(500);
                final ScaleAnimation fadeIn = new ScaleAnimation(0.1f,1.0f,0.1f,1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                fadeIn.setDuration(500);
                v.startAnimation(scaleAnimation1);

                numOfGrandmas++;
                    score.set(score.get() - 5);

                producerThread myThread = new producerThread(3000);
                    myThread.start();
                mainLayout.removeView(grandma);

                ImageView temp = new ImageView(MainActivity.this);
                temp.setImageResource(R.drawable.grandma);
                temp.setAdjustViewBounds(true);
                temp.setMaxHeight(100);
                temp.setMaxWidth(100);

                subLayout.addView(temp);
                temp.startAnimation(fadeIn);
                grandmaCost = 1+ (grandmaCost + (2 * numOfGrandmas)) ;
                grandmaPrice.setText("Grandma: " + String.valueOf(grandmaCost));
                isGrandmaAdded = false;

            }

        });
        //

        //Farm OnClick
        farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numOfFarms++;
                final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.1f,1.0f,0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation1.setDuration(500);
                final ScaleAnimation fadeIn = new ScaleAnimation(0.1f,1.0f,0.1f,1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                fadeIn.setDuration(500);
                v.startAnimation(scaleAnimation1);
                //  final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.1f,1.0f,0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                //  scaleAnimation1.setDuration(500);
                score.set(score.get() - 50);
                mainLayout.removeView(farm);
                producerThread myFarmThread = new producerThread(500);
                myFarmThread.start();
                ImageView temp = new ImageView(MainActivity.this);
                temp.setImageResource(R.drawable.farm);
                temp.setAdjustViewBounds(true);

                temp.setMaxHeight(100);
                temp.setMaxWidth(100);
                subLayout.addView(temp);
                temp.startAnimation(fadeIn);
                farmCost = farmCost +  (2 * numOfFarms) ;
                farmPrice.setText("Farm" + String.valueOf(farmCost));
                isFarmAdded = false;
            }
        });
        //

        //SCORE COUNTER | Runs Continuously
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                scoreText.setText(String.valueOf(score));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (score.get() >= grandmaCost){
                            addGrandma();
                        }
                        if (score.get() >= farmCost){
                            addFarm();
                        }
                    }
                });

            }
        };
        t.scheduleAtFixedRate(tt,0,50);

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
        back.scheduleAtFixedRate(backgroundRunner,0,300);
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

            final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.5f,1.0f,0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation1.setDuration(80);
            final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.7f,1.0f,1.7f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation.setDuration(160);
            mainLayout.addView(grandma);
            grandma.startAnimation(scaleAnimation1);
            if(scaleAnimation1.hasEnded()){
                grandma.startAnimation(scaleAnimation);
            }
            isGrandmaAdded = true;
        }

    }
    //

    //Adds Farm to Top Of Screen And Plays Animation
    public void addFarm(){
        if(!isFarmAdded) {
            farm.setAdjustViewBounds(true);

            final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f,0.8f,1.0f,0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation1.setDuration(80);
            final ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.6f,1.0f,1.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            scaleAnimation.setDuration(160);
            mainLayout.addView(farm);
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
        private int durration;

        public producerThread(int durration) {
            this.durration = durration;
        }
        @Override
        public void run() {
            while (isRunning) {

                try {
                    Thread.sleep(durration);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getXPrecision();
        touchY = event.getYPrecision();
        return super.onTouchEvent(event);
    }

    //Makes element that shows up onClick of OREO
    public void makeFloater(){
        final ImageView imageView = new ImageView(this);
        imageView.setId(View.generateViewId());
        imageView.setImageResource(R.drawable.star);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);

        mainLayout.addView(imageView);

        final ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);

        constraintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, mainLayout.getId(), constraintSet.BOTTOM);
        constraintSet.connect(imageView.getId(), ConstraintSet.TOP, mainLayout.getId(), constraintSet.TOP);
        constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, mainLayout.getId(), constraintSet.LEFT);
        constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, mainLayout.getId(), constraintSet.RIGHT);

        constraintSet.setHorizontalBias(imageView.getId(), touchX);

        constraintSet.applyTo(mainLayout);
        float randomFloat = (float) (Math.random());
        TranslateAnimation translateAnimation = new TranslateAnimation(touchX+80,touchX + 90/*+ randomFloat*/,touchY,touchY+ -50);
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
    //TODO: fix Grandma and Farm Constraints
    //TODO:Fix Grandma and Farm sizes in Bottom Linear Layout
    //TODO:Add One More Addon
    //TODO:Fix Oreo Animation so it works while spinning

}