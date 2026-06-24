import java.awt.image.BufferedImage;
public class m{
    public static void main(String[]args){
        int w = 205;
        int h = 246;
        BufferedImage demoIm = null;
        BufferedImage mapIm = null;
        processor demo = new processor("demo1.jpg", "demo1Out3.jpg", w, h, demoIm);

        demo.read();

        demo.sort3();
        demo.write();
        
    }
    

}