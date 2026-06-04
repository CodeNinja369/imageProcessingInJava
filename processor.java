import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;
public class processor {
    private String fileName;
    private String fileOutName;
    private int xSize;
    private int ySize;
    private BufferedImage image;
    private pixel[] pArray;
    private int[] fArray;
    public processor(String fname, String fOut, int x,int y, BufferedImage e){
        this.fileName = fname;
        this.fileOutName = fOut;
        this.xSize = x;
        this.ySize = y;
        this.image = e;
        read();
        this.fArray = getFarray();
        this.pArray = getPArray();
    }
    public void read(){
        try {
            File demo = new File(this.fileName);
            this.image = new BufferedImage(this.xSize, this.ySize, BufferedImage.TYPE_INT_RGB);
            this.image = ImageIO.read(demo);

        } catch (Exception e) {
            System.err.print(e);
        }
    }
    public void write(){
        try {
            File output = new File(this.fileOutName);
            ImageIO.write(this.image, "jpg", output);
        } catch (Exception e) {
            System.err.print(e);
        }
    }
    //sorting by rgb base 255 value
    public void sort1(){
        int e = 0;
        Arrays.sort(this.fArray);
        
        for(int x =0; x<this.xSize; x++){
            for(int y = 0; y<this.ySize; y++){
                this.image.setRGB(x, y, this.fArray[e]);
                e++;
            }
        }
    }
    //sorting by brightness calculated for differences in human vision
    public void sort2(){
        int n = this.pArray.length;
        for (int i = 1; i < n; ++i) {
            pixel key = this.pArray[i];
            int j = i - 1;
            while (j >= 0 && this.pArray[j].pixelB > key.pixelB) {
                this.pArray[j + 1] = this.pArray[j];
                j = j - 1;
            }
            this.pArray[j + 1] = key;
        }
        int e = 0;
        for(int x =0; x<this.xSize; x++){
            
            for(int y = 0; y<this.ySize; y++){
                this.image.setRGB(x, y, this.pArray[e].pixelC);
                e++;
            }
        }
    }


//sorted by red value
    public void sort2_2(){
        int n = this.pArray.length;
        for (int i = 1; i < n; ++i) {
            pixel key = this.pArray[i];
            int j = i - 1;
            while (j >= 0 && this.pArray[j].red > key.red) {
                this.pArray[j + 1] = this.pArray[j];
                j = j - 1;
            }
            this.pArray[j + 1] = key;
        }
        int e = 0;
        for(int x =0; x<this.xSize; x++){
            
            for(int y = 0; y<this.ySize; y++){
                this.image.setRGB(x, y, this.pArray[e].pixelC);
                e++;
            }
        }
    }
    //sorted by green
    public void sort2_3(){
        int n = this.pArray.length;
        for (int i = 1; i < n; ++i) {
            pixel key = this.pArray[i];
            int j = i - 1;
            while (j >= 0 && this.pArray[j].green > key.blue) {
                this.pArray[j + 1] = this.pArray[j];
                j = j - 1;
            }
            this.pArray[j + 1] = key;
        }
        int e = 0;
        for(int x =0; x<this.xSize; x++){
            
            for(int y = 0; y<this.ySize; y++){
                this.image.setRGB(x, y, this.pArray[e].pixelC);
                e++;
            }
        }
    }
    //sorted by blue
    public void sort2_4(){
        int n = this.pArray.length;
        for (int i = 1; i < n; ++i) {
            pixel key = this.pArray[i];
            int j = i - 1;
            while (j >= 0 && this.pArray[j].blue > key.blue) {
                this.pArray[j + 1] = this.pArray[j];
                j = j - 1;
            }
            this.pArray[j + 1] = key;
        }
        int e = 0;
        for(int x =0; x<this.xSize; x++){
            
            for(int y = 0; y<this.ySize; y++){
                this.image.setRGB(x, y, this.pArray[e].pixelC);
                e++;
            }
        }
    }
//create array of pixel objects in an image
    private pixel[] getPArray(){
        int d = 0;
        pixel[] out = new pixel[this.xSize*this.ySize];
        for(int i = 0; i<this.fArray.length; i++){
            out[i] = new pixel(this.fArray[i]);
        }
        
        return out;
    }
    //created flattened array
    private int[] getFarray(){
        int d = 0;
        int[] out = new int[this.xSize*this.ySize];
        for(int x =0; x<this.xSize; x++){
            for(int y = 0; y<this.ySize; y++){
                out[d]= this.image.getRGB(x, y);
                d++;
            }
        }
        return out;
    }
    //sorts into checkerboard-like pattern of alternating pixels with maximised contrast
    public void sort3(){
        for(int index = 0; index < pArray.length - 1; index++){
            pixel c1 = pArray[index];
            pixel pstore = c1;
            int ppos = 0; 
            double contstore = 0;
            for(int i = index+1; i < pArray.length; i++){
                double contrast = 0;
                if(c1.pixelB > pArray[i].pixelB){
                    contrast = (c1.pixelB + 0.05) / (pArray[i].pixelB + 0.05);
                }
                if(pArray[i].pixelB > c1.pixelB){
                    contrast = (pArray[i].pixelB + 0.05) / (c1.pixelB + 0.05);
                }
                if(contstore < contrast){
                    contstore = contrast;
                    pstore = pArray[i];
                    ppos = i;
                }
            }
            pixel np = pArray[index+1];
            pArray[index+1] = pstore;
            pArray[ppos] = np;
        }
        int e = 0;
        for(int x =0; x<this.xSize; x++){
                
            for(int y = 0; y<this.ySize; y++){
               this.image.setRGB(x, y, this.pArray[e].pixelC);
                    e++;
            }
        }
    }
    //creates brightness map of image
    public int[] createMap() {
        int[] map = new int[pArray.length];
    
        for (int i = 0; i < pArray.length; i++) {
            int rank = 0;
            for (int j = 0; j < pArray.length-1; j++) {
                if (pArray[j].pixelB <= pArray[i].pixelB) {
                    rank++;
                }
            }
            map[i] = rank;
        }
    
        return map;
    }
    //recolours image using brightness map, and image of equal size sorted by brightness
    public void obamaAlg(processor pp){
        int[] m = createMap();
        for(int i =0; i<pArray.length-1; i++){
            this.pArray[i] = pp.pArray[m[i]];
        }
        int e = 0;
        for(int x =0; x<this.xSize; x++){
            
            for(int y = 0; y<this.ySize; y++){
                this.image.setRGB(x, y, this.pArray[e].pixelC);
                e++;
            }
        }
    }

    
}
