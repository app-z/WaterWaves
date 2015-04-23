package net.appz.waterwaves;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.View;

/**
 * Created by App-z.net on 22.04.15.
 */
public class GraphView extends View {

    private static final String TAG = GraphView.class.getSimpleName();

    public enum Type{BAR, LINE, POLY}

	private Paint paint;
    private Water water;
    private Rock rock;
    private Point displaySize;
	private Type type;

	public GraphView(Context context, Water water, Rock rock, Point displaySize, Type type) {
		super(context);
		if (water == null)
            throw new IllegalArgumentException("Water is null!!");
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
		//float graphwidth = width - (2 * border);
        Point[] points = new Point[4];
        for (int i = 0; i < points.length; i++){
            points[i] = new Point();
        }
        int colorWaves = Color.parseColor("#249ae6");

		if (max != min) {
			paint.setColor(colorWaves);
			if (type == Type.POLY) {
				float datalength = water.size();
				float colwidth = (width - (2 * border)) / datalength;
				for (int i = 0; i < water.size(); i++) {
					float val = water.getColumn(i).getHeight() - min;
					float rat = val / diff;
					float h = graphheight * rat;

                    float h1 = h;
                    if( i < water.size() - 1 ) {
                        float val1 = water.getColumn(i + 1).getHeight() - min;
                        float rat1 = val1 / diff;
                        h1 = graphheight * rat1;
                    }
                    points[0].set((int) (i * colwidth), (int) (- h + graphheight));
                    points[1].set((int) ((i * colwidth) + colwidth), (int) (- h1 + graphheight));
                    points[2].set((int) ((i * colwidth) + colwidth), (int) height);
                    points[3].set((int) (i * colwidth), (int)height);
                    drawPoly(canvas, colorWaves , points);
				}
            } else if (type == Type.BAR) {
                float datalength = water.size();
                float colwidth = (width - (2 * border)) / datalength;
                for (int i = 0; i < water.size(); i++) {
                    float val = water.getColumn(i).getHeight() - min;
                    float rat = val / diff;
                    float h = graphheight * rat;
                    canvas.drawRect((i * colwidth), -h + graphheight, (i * colwidth) + colwidth, height, paint);
                }
            }else {
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
            //Log.i(TAG, "Rock> " + rock.Position);
        }
	}

    /**
     * Draw polygon
     *
     * @param canvas The canvas to draw on
     * @param color  Integer representing a fill color (see http://developer.android.com/reference/android/graphics/Color.html)
     * @param points Polygon corner points
     */
    private void drawPoly(Canvas canvas, int color, Point[] points) {
        // line at minimum...
        if (points.length < 2) {
            return;
        }

        // paint
        Paint polyPaint = new Paint();
        polyPaint.setColor(color);
        polyPaint.setStyle(Paint.Style.FILL);

        // path
        Path polyPath = new Path();
        polyPath.moveTo(points[0].x, points[0].y);
        int i, len;
        len = points.length;
        for (i = 0; i < len; i++) {
            polyPath.lineTo(points[i].x, points[i].y);
        }
        polyPath.lineTo(points[0].x, points[0].y);

        // draw
        canvas.drawPath(polyPath, polyPaint);
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

}
