package top.toptimus.common;

import com.google.zxing.*;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Random;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class QRCodeUtil {

    // 设置二维码编码格式
    private static final String CHARSET = "utf-8";
    // 保存的二维码格式
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 200;
    // LOGO宽度
    private static final int LOGO_WIDTH = 80;
    // LOGO高度
    private static final int LOGO_HEIGHT = 80;

    /**
     * @param content      二维码内容
     * @param logoPath     logo图片地址
     * @param needCompress 是否压缩logo图片大小
     * @return BufferedImage 返回类型
     * @throws Exception 参数说明
     * @ApiNote : 将二维码内容创建到Image流
     */
    private static BufferedImage createImage(String content, String logoPath, boolean needCompress) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>() {{
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            put(EncodeHintType.CHARACTER_SET, CHARSET);
            put(EncodeHintType.MARGIN, 1);
        }};

        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (logoPath == null || "".equals(logoPath)) {
            return image;
        }
        // 插入logo
        QRCodeUtil.insertImage(image, logoPath, needCompress);
        return image;
    }

    /**
     * @param source       二维码Image流
     * @param logoPath     logo地址
     * @param needCompress 是否压缩大小
     * @throws Exception 参数说明
     * @ApiNote : 将logo插入到二维码中
     */
    private static void insertImage(BufferedImage source, String logoPath, boolean needCompress) throws Exception {
        File file = new File(logoPath);
        if (!file.exists()) {
            System.err.println("" + logoPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(logoPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > LOGO_WIDTH) {
                width = LOGO_WIDTH;
            }
            if (height > LOGO_HEIGHT) {
                height = LOGO_HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * @param destPath 文件夹地址
     * @return void 返回类型
     * @ApiNote : 创建文件夹
     */
    private static boolean mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
            return true;
        }

        return false;
    }

    /**
     * @param content      二维码内容
     * @param logoPath     logo图片地址
     * @param destPath     目标保存地址
     * @param needCompress 是否压缩logo图片大小
     * @return void 返回类型
     * @throws Exception 参数说明
     * @ApiNote : 生成二维码
     */
    private static String encode(String content, String logoPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoPath, needCompress);
        String file = "";
        if (mkdirs(destPath)) {
            file = new Random().nextInt(99999999) + ".jpg";
            ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
        }
        return file;
    }

    /**
     * @param content      二维码内容
     * @param logoPath     logo图片地址
     * @param destPath     目标保存地址
     * @param needCompress 是否压缩logo图片大小
     * @throws Exception 参数说明
     * @ApiNote : 生成二维码
     */
    private static void encode(String content, String logoPath, String destPath, String file, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
    }

    /**
     * @param content  二维码内容
     * @param destPath 目标保存地址
     * @return void 返回类型
     * @throws Exception 参数说明
     * @ApiNote : 生成二维码
     */
    public static String encode(String content, String destPath) throws Exception {
        return QRCodeUtil.encode(content, null, destPath, false);
    }

    public static void encode(String content, String destPath, String file) throws Exception {
        QRCodeUtil.encode(content, null, destPath, file, false);
    }

    /**
     * @param content      二维码内容
     * @param logoPath     logo图片地址
     * @param output       输出流
     * @param needCompress 是否压缩logo图片大小
     * @throws Exception 参数说明
     * @ApiNote : 生成二维码
     */
    public static void encode(String content, String logoPath, OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * @param content 二维码内容
     * @param output  输出流
     * @throws Exception 参数说明
     * @ApiNote : 生成二维码
     */
    public static void encode(String content, OutputStream output) throws Exception {
        QRCodeUtil.encode(content, null, output, false);
    }

    /**
     * @param file 文件对象
     * @return String 返回类型
     * @throws Exception 参数说明
     * @ApiNote : 对二维码解码
     */
    private static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>() {{
            put(DecodeHintType.CHARACTER_SET, CHARSET);
        }};
        return new MultiFormatReader().decode(bitmap, hints).getText();
    }

    /**
     * @param path 文件路径
     * @return String 返回类型
     * @throws Exception 参数说明
     * @ApiNote : 对二维码解码
     */
    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }


    /**
     * 条形码编码
     *
     * @param contents 条码内容
     * @param width    宽度
     * @param height   高度
     * @param imgPath  图片路径
     */
    public static void encode(String contents, int width, int height, String imgPath) {
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.EAN_13, codeWidth, height, null);
            MatrixToImageWriter.writeToFile(bitMatrix, "png", new File(imgPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 条形码解码
     *
     * @param imgPath 图片路径
     * @return String
     */
    public static String decodeBar(String imgPath) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(new File(imgPath));
            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            result = new MultiFormatReader().decode(bitmap, null);
            return result.getText();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除
     *
     * @param fileName 文件名
     * @return 删除是否成功
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            Boolean succeedDelete = file.delete();
            if (succeedDelete) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return true;
            }
        } else {
            System.out.println("删除单个文件" + fileName + "失败！");
            return false;
        }
    }


}
