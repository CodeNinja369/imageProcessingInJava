import java.awt.Color;
class pixel{
    public int pixelC;
    public int[] pixelA;
    public double pixelB;
    public int red;
    public int green;
    public int blue;
    public int px;
    public int py;
    public pixel(int pixelC, int x, int y){
        this.pixelC = pixelC;
        this.pixelA = getRGBArray();
        this.pixelB = getBrightness();
        this.red = pixelA[0];
        this.green = pixelA[1];
        this.blue = pixelA[2];
        this.px = x;
        this.py = y;
        
    }
    private int[] getRGBArray(){
        Color clr = new Color(this.pixelC);
        int[] val = {clr.getRed(), clr.getGreen(), clr.getBlue()};
        return val;
    }
    public void updateC(){
        Color clr = new Color(this.red, this.green, this.blue);
        this.pixelC = clr.getRGB();
    }
    public void updateCA(){
        this.red = this.pixelA[0];
        this.green = this.pixelA[1];
        this.blue = this.pixelA[2];
        this.updateC();
    }
    private double getBrightness(){
        int[] rgb = this.pixelA;
        return (0.299*rgb[0] + 0.587*rgb[1] + 0.114*rgb[2]);
    }
}