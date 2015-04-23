package net.appz.waterwaves;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

/**
 * GraphView creates a scaled line or bar graph with x and y axis labels. 
 * @author Arno den Hond
 *
 */
public class GraphView extends View {

    private static final String TAG = GraphView.class.getSimpleName();
    public static boolean BAR = true;
	public static boolean LINE = false;

	private Paint paint;
    Water water;
    Rock rock;
    Point displaySize;
	private boolean type;

	public GraphView(Context context, Water water, Rock rock, Point displaySize, boolean type) {
		super(context);
		if (water == null)
            new IllegalArgumentException("Water is null!!");
        this.water = water;

        this.rock = rock;
        this.displaySize = displaySize;

		this.type = type;
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float border = 0;
		float horstart = border * 2;
		float height = getHeight();
		float width = getWidth();
		float max = getMax();
		float min = getMin();
		float diff = max - min;
		float graphheight = height - (2 * border);
		float graphwidth = width - (2 * border);

		if (max != min) {
			paint.setColor(Color.parseColor("#249ae6"));
			if (type == BAR) {
				float datalength = water.size();
				float colwidth = (width - (2 * border)) / datalength;
				for (int i = 0; i < water.size(); i++) {
					float val = water.getColumn(i).getHeight() - min;
					float rat = val / diff;
					float h = graphheight * rat;
					canvas.drawRect((i * colwidth), - h + graphheight, (i * colwidth) + colwidth, height, paint);
				}
			} else {
				float datalength = water.size();
				float colwidth = (width - (2 * border)) / datalength;
				float halfcol = colwidth / 2;
				float lasth = 0;
				for (int i = 0; i < water.size(); i++) {
					float val = water.getColumn(i).getHeight() - min;
					float rat = val / diff;
					float h = graphheight * rat;
					if (i > 0)
						canvas.drawLine(((i - 1) * colwidth) + (horstart + 1) + halfcol, (border - lasth) + graphheight, (i * colwidth) + (horstart + 1) + halfcol, (border - h) + graphheight, paint);
					lasth = h;
				}
			}
		}

        if (rock != null) {
            paint.setColor(Color.LTGRAY);
            canvas.drawCircle(rock.Position.x, rock.Position.y, 10, paint);
            Log.i(TAG, "Rock> " + rock.Position);
        }

        //Log.i(">" , water.columns + " : " + Arrays.toString(water.columns));
	}

	private float getMax() {
        return displaySize.y;
	}

	private float getMin() {
        return 0;
    }

    public void setRock(Rock rock) {
        this.rock = rock;
    }

    public Rock getRock() {
        return rock;
    }
}
