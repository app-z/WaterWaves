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
    private int colorWaves;
    private Point displaySize;
	private Type type;

    private Point[] pointsPoly = new Point[4];

	public GraphView(Context context, Water water, Rock rock, Point displaySize, Type type, int colorWaves) {
		super(context);
		if (water == null)
            throw new IllegalArgumentException("Water is null!!");
        this.water = water;

        this.colorWaves = colorWaves;
        this.rock = rock;
        this.displaySize = displaySize;

		this.type = type;
		paint = new Paint();

        for (int i = 0; i < pointsPoly.length; i++){
            pointsPoly[i] = new Point();
        }
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
                    pointsPoly[0].set((int) (i * colwidth), (int) (- h + graphheight));
                    pointsPoly[1].set((int) ((i * colwidth) + colwidth), (int) (- h1 + graphheight));
                    pointsPoly[2].set((int) ((i * colwidth) + colwidth), (int) height);
                    pointsPoly[3].set((int) (i * colwidth), (int)height);
                    drawPoly(canvas, pointsPoly, paint);
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
     * @param points Polygon corner points
     * @param paint Paint
     */
    private void drawPoly(Canvas canvas, Point[] points, Paint paint) {
        // line at minimum...
        if (points.length < 2) {
            return;
        }

        // paint style
        paint.setStyle(Paint.Style.FILL);

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
        canvas.drawPath(polyPath, paint);
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
