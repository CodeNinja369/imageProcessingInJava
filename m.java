import java.awt.image.BufferedImage;
public class m{
    public static void main(String[]args){
        int w = 643;
        int h = 360;
        BufferedImage demoIm = null;
        BufferedImage mapIm = null;
        processor demo = new processor("demo2.jpg", "demo2Out3.jpg", w, h, demoIm);

        demo.read();

        demo.sort3();
        demo.write();
        
    }
    

}