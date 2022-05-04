import java.awt.*;
import java.util.List;
import java.awt.geom.Path2D;
import java.util.*;
import javax.swing.*;
import static java.lang.Math.*;
import static java.util.stream.Collectors.toList;

public class editing  extends JPanel {
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

    static final double G = sqrt(2); //
    static final double H = 1+sqrt(2);
    static final double T = toRadians(36); // theta

    List<Tile> tiles = new ArrayList<>();
    public editing() {
        int w = 700, h = 450;
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.white);

        tiles = deflateTiles(setupPrototiles(w, h), 3);
    }

    List<Tile> setupPrototiles(int w, int h) {
        List<Tile> proto = new ArrayList<>();

        // sun
        //proto.add(new Tile(Type.Thin, w / 2, h / 2, PI/4, w / 10));
        //proto.add(new Tile(Type.Fat, w / 2, h / 2, PI/2, w / 10));
        //proto.add(new Tile(Type.Fat, w / 2, h / 2, PI/3, w / 10));
        //proto.add(new Tile(Type.Thin, w / 2, h / 2, 0, w / 10));
        proto.add(new Tile(Type.Thin_2, w / 2, h / 2, PI/3, w / 10));
        //proto.add(new Tile(Type.Thin_2, w / 2, h / 2, 0, w / 10));
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

        }

        // remove duplicates
        tls = next.stream().distinct().collect(toList());

        return deflateTiles(tls, generation - 1);
    }

    void drawTiles(Graphics2D g) {
        //double[][] dist = {{G, 1, G}, {-G, -2*G*cos(T), -G}};
        for (Tile tile : tiles) {
            //double angle = tile.angle - T;
            double CHEK;
            CHEK = tile.angle;
            Path2D path = new Path2D.Double();
            path.moveTo(tile.x, tile.y);

            int ord = tile.type.ordinal();
            if ( ord==0) {//fat
                double x1 = tile.x + 1 * tile.size * cos(CHEK);
                double y1 = tile.y + 1 * tile.size * sin(CHEK);
                path.lineTo(x1, y1);
                double x2 = tile.x + 1 * tile.size * cos(CHEK)-(G-1)*tile.size * sin(CHEK);
                double y2 = tile.y + 1 * tile.size * sin(CHEK)+(G-1)*tile.size * cos(CHEK);
                path.lineTo(x2, y2);
                double x3 = tile.x + 1 * tile.size * cos(CHEK)-(G-1)*tile.size * sin(CHEK)+(G-1)*tile.size * cos(CHEK);
                double y3 = tile.y + 1 * tile.size * sin(CHEK)+(G-1)*tile.size * cos(CHEK)+(G-1)*tile.size * sin(CHEK);
                path.lineTo(x3, y3);
                double x4 = tile.x + (1 * tile.size * cos(CHEK)-(G-1)*tile.size * sin(CHEK)+(G-1)*tile.size * cos(CHEK)-1 * tile.size * sin(CHEK));
                double y4 = tile.y + (1 * tile.size * sin(CHEK)+(G-1)*tile.size * cos(CHEK)+(G-1)*tile.size * sin(CHEK)+1 * tile.size * cos(CHEK));
                path.lineTo(x4, y4);
                double x5 = tile.x + (1 * tile.size * cos(CHEK)-(G-1)*tile.size * sin(CHEK)+(G-1)*tile.size * cos(CHEK)-1 * tile.size * sin(CHEK))-1 * tile.size * cos(CHEK);
                double y5 = tile.y + (1 * tile.size * sin(CHEK)+(G-1)*tile.size * cos(CHEK)+(G-1)*tile.size * sin(CHEK)+1 * tile.size * cos(CHEK))-1 * tile.size * sin(CHEK);
                path.lineTo(x5, y5);
                double x6 = tile.x + (1 * tile.size * cos(CHEK)-(G-1)*tile.size * sin(CHEK)+(G-1)*tile.size * cos(CHEK)-1 * tile.size * sin(CHEK))-1 * tile.size * cos(CHEK)+(G-1)*tile.size * sin(CHEK);
                double y6 = tile.y + (1 * tile.size * sin(CHEK)+(G-1)*tile.size * cos(CHEK)+(G-1)*tile.size * sin(CHEK)+1 * tile.size * cos(CHEK))-1 * tile.size * sin(CHEK)-(G-1)*tile.size * cos(CHEK);
                path.lineTo(x6, y6);
                double x7 = tile.x + (1 * tile.size * cos(CHEK)-(G-1)*tile.size * sin(CHEK)+(G-1)*tile.size * cos(CHEK)-1 * tile.size * sin(CHEK))-1 * tile.size * cos(CHEK)+(G-1)*tile.size * sin(CHEK)-(G-1)*tile.size * cos(CHEK);
                double y7 = tile.y + (1 * tile.size * sin(CHEK)+(G-1)*tile.size * cos(CHEK)+(G-1)*tile.size * sin(CHEK)+1 * tile.size * cos(CHEK))-1 * tile.size * sin(CHEK)-(G-1)*tile.size * cos(CHEK)-(G-1)*tile.size * sin(CHEK);
                path.lineTo(x7, y7);
            }
            if ( ord==1) {//thin


                double x1 = tile.x + 1 * tile.size * cos(CHEK);
                double y1 = tile.y + 1 * tile.size * sin(CHEK);
                path.lineTo(x1, y1);
                double x2 = tile.x + 1 * tile.size * cos(CHEK)-G*tile.size * sin(CHEK);
                double y2 = tile.y + 1 * tile.size * sin(CHEK)+G*tile.size * cos(CHEK);
                path.lineTo(x2, y2);
                double x3 = tile.x + 1 * tile.size * cos(CHEK)-G*tile.size * sin(CHEK)-(G-1)*tile.size * cos(CHEK);
                double y3 = tile.y + 1 * tile.size * sin(CHEK)+G*tile.size * cos(CHEK)-(G-1)*tile.size * sin(CHEK);
                path.lineTo(x3, y3);
                double x4 = tile.x + 1 * tile.size * cos(CHEK)-G*tile.size * sin(CHEK)-(G-1)*tile.size * cos(CHEK)+(G-1)*tile.size * sin(CHEK);
                double y4 = tile.y + 1 * tile.size * sin(CHEK)+G*tile.size * cos(CHEK)-(G-1)*tile.size * sin(CHEK)-(G-1)*tile.size * cos(CHEK);
                path.lineTo(x4, y4);
                double x5 = tile.x + 1 * tile.size * cos(CHEK)-G*tile.size * sin(CHEK)-(G-1)*tile.size * cos(CHEK)+(G-1)*tile.size * sin(CHEK)-(2-G)*tile.size * cos(CHEK);
                double y5 = tile.y + 1 * tile.size * sin(CHEK)+G*tile.size * cos(CHEK)-(G-1)*tile.size * sin(CHEK)-(G-1)*tile.size * cos(CHEK)-(2-G)*tile.size * sin(CHEK);
                path.lineTo(x5, y5);
                }
            if ( ord==2) {//thin_2-longper
                double x1 = tile.x - 1 * tile.size * cos(-CHEK);
                double y1 = tile.y + 1 * tile.size * sin(-CHEK);
                path.lineTo(x1, y1);
                double x2 = tile.x - (1 * tile.size * cos(-CHEK)-G*tile.size * sin(-CHEK));
                double y2 = tile.y + 1 * tile.size * sin(-CHEK)+G*tile.size * cos(-CHEK);
                path.lineTo(x2, y2);
                double x3 = tile.x - (1 * tile.size * cos(-CHEK)-G*tile.size * sin(-CHEK)-(G-1)*tile.size * cos(-CHEK));
                double y3 = tile.y + 1 * tile.size * sin(-CHEK)+G*tile.size * cos(-CHEK)-(G-1)*tile.size * sin(-CHEK);
                path.lineTo(x3, y3);
                double x4 = tile.x - (1 * tile.size * cos(-CHEK)-G*tile.size * sin(-CHEK)-(G-1)*tile.size * cos(-CHEK)+(G-1)*tile.size * sin(-CHEK));
                double y4 = tile.y + 1 * tile.size * sin(-CHEK)+G*tile.size * cos(-CHEK)-(G-1)*tile.size * sin(-CHEK)-(G-1)*tile.size * cos(-CHEK);
                path.lineTo(x4, y4);
                double x5 = tile.x - (1 * tile.size * cos(-CHEK)-G*tile.size * sin(-CHEK)-(G-1)*tile.size * cos(-CHEK)+(G-1)*tile.size * sin(-CHEK)-(2-G)*tile.size * cos(-CHEK));
                double y5 = tile.y + 1 * tile.size * sin(-CHEK)+G*tile.size * cos(-CHEK)-(G-1)*tile.size * sin(-CHEK)-(G-1)*tile.size * cos(-CHEK)-(2-G)*tile.size * sin(-CHEK);
                path.lineTo(x5, y5);

            }
            path.closePath();
            g.setColor(ord == 0 ? Color.red : Color.white );
            g.fill(path);
            g.setColor(Color.darkGray);
            g.draw(path);
        }
    }

    @Override
    public void paintComponent(Graphics og) {
        super.paintComponent(og);
        Graphics2D g = (Graphics2D) og;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(-500,-500);
        g.scale(6,6);

        drawTiles(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Penrose Tiling");
            f.setResizable(true);
            f.add(new editing(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}

