import java.awt.image.BufferedImage;
public class m{
    public static void main(String[]args){
        int w = 643;
        int h = 360;
        BufferedImage demo1Im = null;
        processor demo1 = new processor("demo2.jpg", "demo2Out2.jpg", w, h, demo1Im);
        demo1.read();
        demo1.sort2();
        demo1.write();
        
    }
    

}