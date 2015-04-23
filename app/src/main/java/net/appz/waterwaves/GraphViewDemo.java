package net.appz.waterwaves;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;

/**
 * GraphViewDemo creates some dummy data to demonstrate the GraphView component.
 * @author Arno den Hond
 *
 */

// http://gamedevelopment.tutsplus.com/tutorials/make-a-splash-with-2d-water-effects--gamedev-236
// http://habrahabr.ru/post/254287/
public class GraphViewDemo extends Activity {

    private final static int TIMER_INTERVAL = 35;
    private static final String TAG = GraphViewDemo.class.getSimpleName();
    private int timer_interval;

    GraphView graphView;

    Water water;
    Rock rock;

    Point displaySize = new Point();

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(displaySize);

        water = new Water(this, displaySize);

        graphView = new GraphView(this, water, rock, displaySize, GraphView.Type.POLY, Color.parseColor("#249ae6"));
		setContentView(graphView);
        timer_interval = TIMER_INTERVAL;
	}




    float oldX, oldY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float newX;
        float newY;
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            oldX = event.getX();
            oldY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            newX = event.getX();
            newY = event.getY();
            rock = new Rock(newX, newY, (newX - oldX) / 5f, (newY - oldY) / 5f);
            graphView.setRock(rock);

            Log.i(TAG, "oldX = " + oldX + " : oldY = " + oldY + " : x = " + newX + " : y = " + newY);
        }
        return false;
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (rock!=null) {
                if (rock.Position.y < getDisplayH()/2f && rock.Position.y + rock.Velocity.y >= getDisplayH()/2f)
                    water.Splash(rock.Position.x, rock.Velocity.y * rock.Velocity.y * 5f);

                rock.Update(water);

                if (rock.Position.y > getDisplayH()) {
                    rock = null;
                    graphView.setRock(null);
                }

            }
            water.Update();
            graphView.invalidate();
            timerHandler.postDelayed(this, timer_interval);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        timerHandler.postDelayed(timerRunnable, timer_interval);
    }

    int getDisplayW(){
        return displaySize.x;
    }

    int getDisplayH(){
        return displaySize.y;
    }

}