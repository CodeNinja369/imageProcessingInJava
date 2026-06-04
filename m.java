import java.awt.image.BufferedImage;
public class m{
    public static void main(String[]args){
        int w = 205;
        int h = 246;
        BufferedImage demoIm = null;
        BufferedImage mapIm = null;
        processor demo = new processor("demo3.jpg", "demo1Out4.jpg", w, h, demoIm);
        processor mappa = new processor("demo1.jpg", "demo1Out4.jpg", w, h, mapIm);
        demo.read();
        mappa.read();
        demo.sort2();
        mappa.obamaAlg(demo);
        mappa.write();
        
    }
    

}