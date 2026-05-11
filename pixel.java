import java.awt.Color;
class pixel{
    int pixelC;
    int[] pixelA;
    double pixelB;
    public pixel(int pixelC){
        this.pixelC = pixelC;
        this.pixelA = getRGBArray();
        this.pixelB = getBrightness();
    }
    private int[] getRGBArray(){
        Color clr = new Color(this.pixelC);
        int[] val = {clr.getRed(), clr.getGreen(), clr.getBlue()};
        return val;
    }
    private double getBrightness(){
        int[] rgb = this.pixelA;
        return (0.299*rgb[0] + 0.587*rgb[1] + 0.114*rgb[2]);
    }
}