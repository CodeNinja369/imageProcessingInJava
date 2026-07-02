import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import javax.imageio.ImageIO;


public class processor {
    private String fileName;
    private String fileOutName;
    private int xSize;
    private int ySize;
    private BufferedImage image;
    private pixel[][] pArray;
    private int[] cArray;
    public processor(String fname, String fOut, int x,int y, BufferedImage e){
        this.fileName = fname;
        this.fileOutName = fOut;
        this.xSize = x;
        this.ySize = y;
        this.image = e;
        read();
        this.cArray = getCarray();
        this.pArray = getParray();
    }
    //read and write to image files
    public void read() {
        try {
            image = ImageIO.read(new File(this.fileName));
        } catch (Exception e) {
            System.err.println("read: " + e.getMessage());
        }
    }

    
    public void write() {
        try {
            File output = new File(this.fileOutName);
            ImageIO.write(this.image, "jpg", output);
        } catch (Exception e) {
            System.err.println("write: " + e.getMessage());
        }
    }
    //create array of pixel objects in an image
    private pixel[][] getParray(){
        int d = 0;
        pixel[][] out = new pixel[this.xSize][this.ySize];
        for(int x =0; x<this.xSize; x++){
            for(int y = 0; y<this.ySize; y++){
                out[x][y] = new pixel(this.image.getRGB(x, y), x, y);
                d++;
            }
        }
        return out;
    }
    //created flattened array
    private int [] getCarray(){
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
    //quicksort
    private int partition(Field f, int low, int high) {
        int i = low - 1;
        try {
            double pivot = Double.parseDouble(f.get(getPixel(high)).toString());

            for (int j = low; j <= high - 1; j++) {
                if (Double.parseDouble(f.get(getPixel(j)).toString()) < pivot) {
                    i++;
                    swap(i, j);
                }
            }
            swap(i + 1, high);

        } catch (IllegalAccessException e) {
            System.err.println("partition: cannot access field — " + e.getMessage());
        }
        return i + 1;
    }

    // translate a flat index into the 2D array
    private pixel getPixel(int index) {
        int x = index % xSize;
        int y = index / xSize;
        return pArray[x][y];
    }

    private void setPixel(int index, pixel p) {
        int x = index % xSize;
        int y = index / xSize;
        pArray[x][y] = p;
    }

    private void swap(int i, int j) {
        pixel temp = getPixel(i);
        setPixel(i, getPixel(j));
        setPixel(j, temp);
    }

    public void quickSort(String att, int low, int high) {
        try {
            Field field = pixel.class.getField(att);
            quickSortInternal(field, low, high);
        } catch (NoSuchFieldException e) {
            System.err.println("quickSort: no field named '" + att + "' on pixel");
        }
    }

    private void quickSortInternal(Field field, int low, int high) {
        if (low < high) {
            int pi = partition(field, low, high);
            quickSortInternal(field, low, pi - 1);
            quickSortInternal(field, pi + 1, high);
        }
    }
    //update image file
    private void update(){
        for (int x = 0; x < this.xSize; x++) {
            for (int y = 0; y < this.ySize; y++) {
                this.image.setRGB(x, y, this.pArray[x][y].pixelC);

            }
        }
    }
    //sorts base 255 colour values using quicksort
    public void sort1() {
        quickSort("pixelC", 0, xSize*ySize - 1);
        update();
    }
    //sorting by brightness calculated for differences in human vision
    public void sort2(){
        quickSort("pixelB", 0, xSize*ySize-1);
        update();
    }


//sorted by red value
    public void sort2_2(){
        quickSort("red", 0, xSize*ySize-1);
        update();
    }
    //sorted by green
    public void sort2_3(){
        quickSort("green", 0, xSize*ySize-1);
        update();
    }
    //sorted by blue
    public void sort2_4(){
        quickSort("blue", 0, xSize*ySize-1);
        update();
    }

    //sorts into checkerboard-like pattern of alternating pixels with maximised contrast
    public void sort3(){
        int len = xSize * ySize;
        for(int index = 0; index < len - 1; index++){
            pixel c1 = pArray[index % xSize][index / xSize];
            pixel pstore = c1;
            int ppos = 0; 
            double contstore = 0;
            for(int i = index+1; i < len; i++){
                double contrast = 0;
                if(c1.pixelB > pArray[i % xSize][i / xSize].pixelB){
                    contrast = (c1.pixelB + 0.05) / (pArray[i % xSize][i / xSize].pixelB + 0.05);
                }
                if(pArray[i % xSize][i / xSize].pixelB > c1.pixelB){
                    contrast = (pArray[i % xSize][i / xSize].pixelB + 0.05) / (c1.pixelB + 0.05);
                }
                if(contstore < contrast){
                    contstore = contrast;
                    pstore = pArray[i % xSize][i / xSize];
                    ppos = i;
                }
            }
            pixel np = pArray[(index+1) % xSize][(index+1) / xSize];
            pArray[(index+1) % xSize][(index+1) / xSize] = pstore;
            pArray[ppos % xSize][ppos / xSize] = np;
        }
        int e = 0;
        update();
    }
    //creates brightness map of image
    public int[] createMap() {
        int rows = pArray.length;
        int cols = pArray[0].length;
        int total = rows * cols;
        int[] map = new int[total];

        for (int i = 0; i < total; i++) {
            int ri = i / cols, ci = i % cols;
            int rank = 0;
            for (int j = 0; j < total - 1; j++) {
                int rj = j / cols, cj = j % cols;
                if (pArray[rj][cj].pixelB <= pArray[ri][ci].pixelB) {
                    rank++;
                }
            }
            map[i] = rank;
        }

        return map;
}

    //recolours image using brightness map, and image of equal size sorted by brightness
    public void obamaAlg(processor pp) {
        int[] m = createMap();
        int rows = pArray.length;
        int cols = pArray[0].length;
        int total = rows * cols;

        for (int i = 0; i < total - 1; i++) {
            int ri = i / cols, ci = i % cols;
            int rank = m[i];
            int rr = rank / cols, rc = rank % cols;
            this.pArray[ri][ci] = pp.pArray[rr][rc];
        }
        update();
    }
    //remove Red, Green or Blue, or any combination of two
    public void onlyRed(){
        for(int i =0; i<this.xSize; i++){
            for(int j =0; j<this.ySize; j++){
                pArray[i][j].green=0;
                pArray[i][j].blue=0;
                pArray[i][j].updateC();
            }
        }
        update();
    }
    public void onlyBlue(){
        for(int i =0; i<pArray.length; i++){
            for(int j =0; j<this.ySize; j++){
                pArray[i][j].red=0;
                pArray[i][j].green=0;
                pArray[i][j].updateC();
            }
        }
        update();
    }
    public void onlyGreen(){
        for(int i =0; i<pArray.length; i++){
            for(int j =0; j<this.ySize; j++){
                pArray[i][j].red=0;
                pArray[i][j].blue=0;
                pArray[i][j].updateC();
            }
        }
        update();
    }
    public void noRed(){
        for(int i =0; i<pArray.length; i++){
            for(int j =0; j<this.ySize; j++){
                pArray[i][j].green=0;
                pArray[i][j].blue=0;
                pArray[i][j].updateC();
            }
        }
        update();
    }
    public void noGreen(){
        for(int i =0; i<pArray.length; i++){
            for(int j =0; j<this.ySize; j++){
                pArray[i][j].green=0;
                pArray[i][j].updateC();
            }
        }
        update();
    }
    public void noBlue(){
        for(int i =0; i<pArray.length; i++){
            for(int j =0; j<this.ySize; j++){
                pArray[i][j].blue=0;
                pArray[i][j].updateC();
            }
        }
        update();
    }
    public void noWhite1(){
        for(int i = 0; i < xSize; i++){
            for(int j = 0; j<ySize; j++){
                int min = Math.min(pArray[i][j].red, Math.min(pArray[i][j].green, pArray[i][j].blue));
                if(min>0){
                    pArray[i][j].red   = pArray[i][j].red%min;
                    pArray[i][j].green = pArray[i][j].green%min;
                    pArray[i][j].blue  = pArray[i][j].blue%min;
                    
                }
                pArray[i][j].updateC();
            }
        }
        update();
    }
    public void noWhite2(){
        for(int i = 0; i < xSize; i++){
            for(int j = 0; j<ySize; j++){
                int min = Math.min(pArray[i][j].red, Math.min(pArray[i][j].green, pArray[i][j].blue));
                if(min>0){
                    pArray[i][j].red   = pArray[i][j].red-min;
                    pArray[i][j].green = pArray[i][j].green-min;
                    pArray[i][j].blue  = pArray[i][j].blue-min;
                    
                }
                pArray[i][j].updateC();
            }
        }
        update();
    }
    public void white(){
        for(int i = 0; i < xSize; i++){
            for(int j = 0; j<ySize; j++){
                int max = Math.max(pArray[i][j].red, Math.max(pArray[i][j].green, pArray[i][j].blue));
                pArray[i][j].red   = max;
                pArray[i][j].green = max;
                pArray[i][j].blue  = max;
                pArray[i][j].updateC();
            }
        }
        update();
    }
    public void black(){
        for(int i = 0; i < xSize; i++){
            for(int j = 0; j<ySize; j++){
                int min = Math.min(pArray[i][j].red, Math.min(pArray[i][j].green, pArray[i][j].blue));
                pArray[i][j].red   = min;
                pArray[i][j].green = min;
                pArray[i][j].blue  = min;
                pArray[i][j].updateC();
            }
        }
        update();
    }

    //changes a pixel's colour value to an average value based on position
    public void average1(){
        int[] rowAv = new int[3];
        int[][] rAvs = new int[this.ySize][3];

        for(int i =0; i<this.ySize; i++){
            for(int j = 0; j<this.xSize; j++){
                rowAv[0]+=pArray[i][j].red;
                rowAv[1]+=pArray[i][j].green;
                rowAv[2]+=pArray[i][j].blue;
           
            }
            rAvs[i][0] = rowAv[0]/this.xSize;
            rAvs[i][1] = rowAv[1]/this.xSize;
            rAvs[i][2] = rowAv[2]/this.xSize;
            rowAv = new int[3];
        }
  
        for(int i =0; i<this.ySize; i++){
            for(int j = 0; j<this.xSize; j++){
                pArray[i][j].red = rAvs[i][0];
                pArray[i][j].green = rAvs[i][1];
                pArray[i][j].blue = rAvs[i][2];
                pArray[i][j].updateC();

            }
        }
        update();
    }
   
}
