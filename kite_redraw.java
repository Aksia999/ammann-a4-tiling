package myfirstjava;
/*
 * Increase the number in line 60 to generate more tiles. In line 217 change the number to change the size ofhe tiles.
 *          */

import java.awt.*;
import java.util.List;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;


import static java.lang.Math.*;
import static java.util.stream.Collectors.toList;

public class kite_redraw extends JPanel {
    // ignores missing hash code
    class Tile {
        double x, y, angle, size;
        Type type;

        Tile(Type t, double x, double y, double a, double s) {
            type = t;
            this.x = x;
            this.y = y;
            angle = a;
            size = s;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Tile) {
                Tile t = (Tile) o;
                return type == t.type && x == t.x && y == t.y && angle == t.angle;
            }
            return false;
        }
    }

    enum Type {
        Fat, Thin ,Thin_2
    }

    static final double G = sqrt(2);
    static final double H = 1 + sqrt(2) ; // exchange ratio
    static final double T = toRadians(36); // theta

    List<Tile> tiles = new ArrayList<>();

    public kite_redraw() {
        int w = 700, h = 450;
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.white);

        tiles = deflateTiles(setupPrototiles(w, h), 3); //increase the number to increase the number of times the program expands and generates the tiles



    }

    List<Tile> setupPrototiles(int w, int h) {
        List<Tile> proto = new ArrayList<>();

        // sun
        // for (double a = PI / 2 + T; a < 415 * PI/180; a += 2 * T)
           // proto.add(new Tile(Type.Kite, w / 2, h / 2, a, w / 2.5));
    
        //proto.add(new Tile(Type.Thin_2, w / 2, h / 2, 0, w / 2.5));
       // proto.add(new Tile(Type.Thin_2, w / 2, h / 2, PI/2, w / 2.5));
      //  proto.add(new Tile(Type.Thin_2, w / 2, h / 2, -PI/2, w / 2.5));
        //proto.add(new Tile(Type.Thin_2, w / 2, h / 2, PI/2, w / 2.5));
       // proto.add(new Tile(Type.Thin_2, w / 2, h / 2, PI, w / 2.5));
        //proto.add(new Tile(Type.Thin_2, w / 2, h / 2, 0, w / 2.5));
       // proto.add(new Tile(Type.Thin, w / 2, h / 2, PI/2, w / 2.5));
    //  proto.add(new Tile(Type.Thin, w / 2, h / 2, PI, w / 2.5));
       // proto.add(new Tile(Type.Thin, w / 2, h / 2, PI/3, w / 2.5));
        //proto.add(new Tile(Type.Thin, w / 2, h / 2, -PI/2, w / 2.5));
       // proto.add(new Tile(Type.Fat, w / 2, h / 2, PI, w / 2.5));
        //proto.add(new Tile(Type.Fat, w / 2, h / 2, PI/2, w / 2.5));
       // proto.add(new Tile(Type.Fat, w / 2, h / 2, -PI/2, w / 2.5));
        proto.add(new Tile(Type.Fat, w / 2, h / 2, 0, w / 2.5));

        return proto;
    }

    List<Tile> deflateTiles(List<Tile> tls, int generation) {
        if (generation <= 0)
            return tls;

        List<Tile> next = new ArrayList<>();

        for (Tile tile : tls) {
            double x = tile.x, y = tile.y, a = tile.angle, nx, ny ;
            double size = tile.size/H ;

            if (tile.type == Type.Fat) { //square one

                //next.add(tile);
                next.add(new Tile(Type.Fat, x, y, 0*T+a, size));
                double x1=x+(1+G)*tile.size*cos(a)/H-1*tile.size*sin(a)/H;
                double y1=y+(1+G)*tile.size*sin(a)/H+1*tile.size*cos(a)/H;
                next.add(new Tile(Type.Fat, x1, y1, a+PI/2,size));
                next.add(new Tile(Type.Thin_2, x1, y1, a+PI/2,size));
                next.add(new Tile(Type.Thin, x1, y1, a,size));
                double x2=x-(1+G)*tile.size*sin(a)/H+1*tile.size*cos(a)/H;
                double y2=y+(1+G)*tile.size*cos(a)/H+1*tile.size*sin(a)/H;
                next.add(new Tile(Type.Thin, x2, y2, a+PI,size));
                next.add(new Tile(Type.Thin_2, x2, y2, a-PI/2,size));
                double x3=x+(2*G+2)*tile.size*cos(a+PI/4)/H;
                double y3=y+(2*G+2)*tile.size*sin(a+PI/4)/H;
                next.add(new Tile(Type.Fat, x3, y3, a+PI,size));
                //next.add(tile);



            } else if(tile.type == Type.Thin){//longer

                next.add(new Tile(Type.Fat, x, y, a, size));
                double x1=x+(1+G)*tile.size*cos(a)/H-1*tile.size*sin(a)/H;
                double y1=y+(1+G)*tile.size*sin(a)/H+1*tile.size*cos(a)/H;
                next.add(new Tile(Type.Fat, x1, y1, a+PI/2,size));
                next.add(new Tile(Type.Thin_2, x1, y1, a+PI/2,size));
                double x2=x-(1+G)*tile.size*sin(a)/H+1*tile.size*cos(a)/H;
                double y2=y+(1+G)*tile.size*cos(a)/H+1*tile.size*sin(a)/H;
                next.add(new Tile(Type.Thin, x2, y2, a+PI,size));
                double x3=x1-(1+G)*tile.size*sin(a)/H-1*tile.size*cos(a)/H;
                double y3=y1+(1+G)*tile.size*cos(a)/H-1*tile.size*sin(a)/H;
                next.add(new Tile(Type.Thin_2, x3, y3, a+PI,size));


            }
            // next.add(tile);
            else if(tile.type == Type.Thin_2){//longer-2

                next.add(new Tile(Type.Fat, x, y, a+PI/2, size));
                double x1=x-(1+G)*tile.size*cos(a)/H-1*tile.size*sin(a)/H;
                double y1=y-(1+G)*tile.size*sin(a)/H+1*tile.size*cos(a)/H;
                next.add(new Tile(Type.Fat, x1, y1, a,size));
                next.add(new Tile(Type.Thin, x1, y1, a-PI/2,size));
                double x2=x-(1+G)*tile.size*sin(a)/H-1*tile.size*cos(a)/H;
                double y2=y+(1+G)*tile.size*cos(a)/H-1*tile.size*sin(a)/H;
                next.add(new Tile(Type.Thin_2, x2, y2, a+PI,size));
                double x3=x1-(1+G)*tile.size*sin(a)/H+1*tile.size*cos(a)/H;
                double y3=y1+(1+G)*tile.size*cos(a)/H+1*tile.size*sin(a)/H;
                next.add(new Tile(Type.Thin, x3, y3, a+PI,size));




            }
            //next.add(tile);
        }

        // remove duplicates
        tls = next.stream().distinct().collect(toList());

        return deflateTiles(tls, generation - 1);
    }
    void drawTiles(Graphics2D g) {
        double[][] dist = {{G, G, G}, {-G, -1, -G},{G, G, G}};
        BufferedImage BIkite=new BufferedImage(1,1,1);
        BufferedImage BIdart=new BufferedImage(1,1,1);
        BufferedImage BIthin=new BufferedImage(1,1,1);
        try {
            BIkite = ImageIO.read(this.getClass().getResource("fat.png"));
            BIdart = ImageIO.read(this.getClass().getResource("thin.png"));
            BIthin = ImageIO.read(this.getClass().getResource("thintwo.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        for (Tile tile : tiles) {
            double angle = tile.angle - T;
            Path2D path = new Path2D.Double();
            path.moveTo(tile.x, tile.y);

          int ord = tile.type.ordinal();
            /*  for (int i = 0; i < 3; i++) {
                double x = tile.x + dist[ord][i] * tile.size * cos(angle);
                double y = tile.y - dist[ord][i] * tile.size * sin(angle);
                path.lineTo(x, y);
                angle += T;

            }//dist
              /*
            path.closePath();
            g.setColor(ord == 0 ? Color.orange : Color.yellow);
            g.fill(path);
            g.setColor(Color.darkGray);
            g.draw(path);
      */
          double size=0.0323;
          double transl=0.34;
            if(ord==0) {      //kite
                // create the transform, note that the transformations happen
                // in reversed order (so check them backwards)
                AffineTransform at = new AffineTransform();

                // 4. translate it to the center of the component
                at.translate(tile.x+transl*tile.size*Math.cos(tile.angle),tile.y+transl*tile.size*Math.sin(tile.angle));

                // 3. do the actual rotation
                at.rotate(tile.angle);

                // 2. just a scale because this image is big
                at.scale(tile.size*size, tile.size*size);


                // 1. translate the object so that you rotate it around the
                //    center (easier :))
                at.translate(-BIkite.getWidth()/2, -BIkite.getHeight()/2);

                g.drawImage(BIkite, at ,null);
            }
            else if(ord==1)//
            {

                // create the transform, note that the transformations happen
                // in reversed order (so check them backwards)
                AffineTransform at = new AffineTransform();

                // 4. translate it to the center of the component
                at.translate(tile.x+transl*tile.size*Math.cos(tile.angle),tile.y+transl*tile.size*Math.sin(tile.angle));

                // 3. do the actual rotation
                at.rotate(tile.angle);

                // 2. just a scale because this image is big
                at.scale(tile.size*size, tile.size*size);


                // 1. translate the object so that you rotate it around the
                //    center (easier :))
                at.translate(-BIdart.getWidth()/2, -BIdart.getHeight()/2);

                g.drawImage(BIdart, at ,null);


            }
            else if(ord==2)// Thin2
            {

                // create the transform, note that the transformations happen
                // in reversed order (so check them backwards)
                AffineTransform at = new AffineTransform();

                // 4. translate it to the center of the component
                at.translate(tile.x+transl*tile.size*Math.cos(tile.angle),tile.y+transl*tile.size*Math.sin(tile.angle));

                // 3. do the actual rotation
                at.rotate(tile.angle);

                // 2. just a scale because this image is big
                at.scale(tile.size*size, tile.size*size);


                // 1. translate the object so that you rotate it around the
                //    center (easier :))
                at.translate(-BIthin.getWidth()/2, -BIthin.getHeight()/2);

                g.drawImage(BIthin, at ,null);


            }

            /*
             BufferedImage BIkite=new BufferedImage(1,1,1);
        	try {
        		BIkite = ImageIO.read(this.getClass().getResource("Kite.png"));
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
            g.drawImage(BIkite,10,100,null);
            g.drawImage(BIkite,0,0,null);
            */
        }



    }



    @Override
    public void paintComponent(Graphics og) {
        super.paintComponent(og);
        Graphics2D g = (Graphics2D) og;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(200, 170);//change this number to move image around
        g.scale(0.8,0.8); //change this number to change the size of the image
        drawTiles(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Penrose Tiling");
            f.setResizable(true);

            f.add(new kite_redraw(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);

            f.setVisible(true);
        });
    }
}

