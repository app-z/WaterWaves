package net.appz.waterwaves;

import android.content.Context;
import android.graphics.Point;

/**
 * Created by App-z.net on 22.04.15.
 */
public class Water {

    private final static int COLUMN_COUNT = 301;

    private Context context;
    private Point displaySize;

    class WaterColumn{
            public float TargetHeight;
            public float Height;
            public float Speed;

        public WaterColumn(float TargetHeight,
                float Height,
                float Speed){
            this.TargetHeight = TargetHeight;
            this.Height = Height;
            this.Speed = Speed;
        }

        public void Update(float dampening, float tension)
        {
            float x = this.TargetHeight - this.Height;
            this.Speed += tension * x - this.Speed * dampening;
            this.Height += this.Speed;
        }

        public float getHeight(){
            return Height;
        }

        @Override
        public String toString() {
            return "TargetHeight = " + TargetHeight + " : Height = " + Height + " : Speed = " + Speed;
        }
    }


    WaterColumn[] columns = new WaterColumn[COLUMN_COUNT];

    public float Tension = 0.025f;
    public float Dampening = 0.025f;
    public float Spread = 0.25f;

    public Water(Context context, Point displaySize)
    {
        this.context = context;
        this.displaySize = displaySize;
        for (int i = 0; i < columns.length; i++){
            columns[i] = new WaterColumn(getDisplayH()/2.f, getDisplayH()/2.f, 0f);
        }
    }

    public WaterColumn getColumn(int index){
        return columns[index];
    }

    public int size(){
        return columns.length;
    }

//    float SCREEN_W = 800f;


    public static float clamp(float value, float min, float max) {
        value = (value > max) ? max : value;
        value = (value < min) ? min : value;
        return value;
    }

    public void Splash(float xPosition, float speed)
    {

        int index = (int)clamp(xPosition / getScale(), 0, columns.length - 1);
        for (int i = Math.max(0, index - 0); i < Math.min(columns.length - 1, index + 1); i++)
            columns[index].Speed = speed;
    }

    int getDisplayW(){
        return displaySize.x;
    }

    int getDisplayH(){
        return displaySize.y;
    }

    float getScale(){
        return getDisplayW() / (columns.length - 1f);
    }
    // Returns the height of the water at a given x coordinate.
    public float GetHeight(float x)
    {

       if (x < 0 || x > getDisplayW())
            return getDisplayH()/2f;

        int index = (int)(x / getScale());

        return columns[index].Height;
    }

    public void Update()
    {
        for (int i = 0; i < columns.length; i++)
            columns[i].Update(Dampening, Tension);

        float[] lDeltas = new float[columns.length];
        float[] rDeltas = new float[columns.length];

        // do some passes where columns pull on their neighbours
        for (int j = 0; j < 8; j++)
        {
            for (int i = 0; i < columns.length; i++)
            {
                if (i > 0)
                {
                    lDeltas[i] = Spread * (columns[i].Height - columns[i - 1].Height);
                    columns[i - 1].Speed += lDeltas[i];
                }
                if (i < columns.length - 1)
                {
                    rDeltas[i] = Spread * (columns[i].Height - columns[i + 1].Height);
                    columns[i + 1].Speed += rDeltas[i];
                }
            }

            for (int i = 0; i < columns.length; i++)
            {
                if (i > 0)
                    columns[i - 1].Height += lDeltas[i];
                if (i < columns.length - 1)
                    columns[i + 1].Height += rDeltas[i];
            }
        }

        //Log.i(">>>", columns + " : " + Arrays.toString(columns));

    }


}
