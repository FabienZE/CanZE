package lu.fisch.canze.widgets;


import lu.fisch.awt.Color;
import lu.fisch.awt.Graphics;
import lu.fisch.canze.interfaces.DrawSurfaceInterface;

/**
 *
 * @author robertfisch
 */
public class BatteryBar extends Drawable {

    public BatteryBar() {
        super();
    }

    public BatteryBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // test
    }

    public BatteryBar(DrawSurfaceInterface drawSurface, int x, int y, int width, int height) {
        this.drawSurface=drawSurface;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {

        // black border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        // calculate fill height
        int fillHeight = (int) ((value-min)/(double)(max-min)*(height-1));
        int barWidth = width-Math.max(g.stringWidth(min+""),g.stringWidth(max+""))-10-10-g.stringHeight(title)-4;

        // draw the filled part
        g.drawRect(x + width - barWidth, y, barWidth, height);
        if( value< -20 ) {g.setColor(Color.BLACK);}
        else if ( value< 0 ) { g.setColor(Color.BLUE);}
        else if ( value< 15 ) { g.setColor(Color.GRAY);}
        else if ( value< 22 ) { g.setColor(Color.GREEN);}
        else if ( value< 25 ) { g.setColor(Color.YELLOW);}
        else  { g.setColor(Color.RED);}
        if(inverted)
            g.fillRect(x+1+width-barWidth, y+1, barWidth-1, fillHeight);
        else
            g.fillRect(x+1+width-barWidth, y+height-fillHeight, barWidth-1, fillHeight);

        // draw the ticks
        if(minorTicks>0 || majorTicks>0)
        {
            g.setColor(Color.GRAY_DARK);
            int toTicks = minorTicks;
            if(toTicks==0) toTicks=majorTicks;
            double accel = (double)height/((max-min)/(double)toTicks);
            double ax,ay,bx=0,by=0;
            int actual = min;
            int sum = 0;
            for(double i=height; i>=0; i-=accel)
            {
                if(minorTicks>0)
                {
                    ax = x+width-barWidth-5;
                    ay = y+i;
                    bx = x+width-barWidth;
                    by = y+i;
                    g.drawLine((int)ax, (int)ay, (int)bx, (int)by);
                }
                // draw majorTicks
                if(majorTicks!=0 && sum % majorTicks == 0) {
                    if(majorTicks>0)
                    {
                        ax = x+width-barWidth-10;
                        ay = y+i;
                        bx = x+width-barWidth;
                        by = y+i;
                        g.drawLine((int)ax, (int)ay, (int)bx, (int)by);

                        if(ay!=y+height && (int)i!=0)
                        {
                            g.setColor(Color.WHITE);
                            g.drawLine(x+1+width-barWidth, (int)ay, x+width-1, (int)by);
                            g.setColor(Color.GRAY_DARK);
                        }
                    }

                    // draw String
                    if(showLabels)
                    {
                        String text = (actual)+"";
                        double sw = g.stringWidth(text);
                        bx = width-barWidth-16-sw;
                        by = y+i;
                        g.drawString(text, (int)(bx), (int)(by+g.stringHeight(text)*(1-i/height)));
                    }

                    actual+=majorTicks;
                }
                sum+=minorTicks;
            }
        }
        // draw the value
        if(showValue)
        {
            g.setTextSize(Math.min(width/7,40));
            String text = String.format("%." + (String.valueOf(field.getDecimals()).length() - 1) + "f", field.getValue());
            int tw = g.stringWidth(text);
            int th = g.stringHeight(text);
            int tx = x+width-barWidth/2-tw/2;
            int ty = y+height/2;
            g.setColor(Color.GREEN_DARK);
            g.drawString(text, tx, ty);
        }
        // draw the title
        if(title!=null && !title.equals(""))
        {
            g.setColor(Color.BLUE);
            g.setTextSize(16);
            int tw = g.stringWidth(title);
            int th = g.stringHeight(title);
            int tx = x; //x+width-barWidth/2-tw/2;
            int ty = y+height; //getY()+getHeight()-8;
            g.rotate(-90, tx, ty);
            g.drawString(title,tx+4,ty+th-2);
            g.rotate(90,tx,ty);
        }

    }
}
