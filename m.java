import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;
public class m{
    public static void main(String[]args){
        int w = 205;
        int h = 246;
        BufferedImage im = null;
        im = read(w, h, im, "demo1.jpg");
        int[]flatArr = new int[w*h];
        int d = 0;
        for(int x =0; x<w; x++){
            for(int y = 0; y<h; y++){
                flatArr[d]= im.getRGB(x, y);
                d++;
            }
        }
        int e = 0;
        Arrays.sort(flatArr);
        
        for(int x =0; x<w; x++){
            for(int y = 0; y<h; y++){
                im.setRGB(x, y, flatArr[e]);
                e++;
            }
        }

        write(im, "demo1Out.jpg");
    }
    private static BufferedImage read(int height, int width, BufferedImage image, String fName){
        try {
            File demo = new File(fName);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            image = ImageIO.read(demo);


        } catch (Exception e) {
            System.err.print(e);
        }
        return image;
    }
    private static void write(BufferedImage image, String fName){
        try {
            File output = new File(fName);
            ImageIO.write(image, "jpg", output);
        } catch (Exception e) {
            System.err.print(e);
        }
    }
}