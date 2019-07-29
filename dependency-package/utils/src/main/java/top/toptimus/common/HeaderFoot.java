package top.toptimus.common;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class HeaderFoot extends PdfPageEventHelper {
    private String header;
    private String watermark; //水印页眉内容
    private PdfTemplate template;
    private Font font;
    private BaseFont baseFont;

    /**
     * 构造方法
     *
     * @param baseFont
     * @param font
     * @param waterMark
     */
    public HeaderFoot(BaseFont baseFont, Font font, String waterMark) {
        this.baseFont = baseFont;
        this.font = font;
        this.watermark = waterMark;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        template = writer.getDirectContent().createTemplate(30, 16);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        Rectangle rect = writer.getBoxSize("art");
        /*
         * 1.添加页眉
         */
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(watermark, font), rect.getLeft(200), rect.getTop() + 20, 0);

        /*
         * 2.添加水印
         */
        PdfContentByte waterMar = writer.getDirectContentUnder();
        // 开始设置水印
        waterMar.beginText();
        // 设置水印透明度
        PdfGState gs = new PdfGState();
        // 设置填充字体不透明度为0.4f
        gs.setFillOpacity(0.4f);

        try {
            // 设置水印字体参数及大小
            waterMar.setFontAndSize(baseFont, 60);
            // 设置透明度
            waterMar.setGState(gs);
            // 设置水印对齐方式 水印内容 X坐标 Y坐标 旋转角度
            waterMar.showTextAligned(Element.ALIGN_RIGHT, watermark, rect.getWidth(), rect.getHeight() / 2, 45);
            // 设置水印颜色
            waterMar.setColorFill(BaseColor.GRAY);
            waterMar.endText();
            waterMar.stroke();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * 3.添加页码
         */
        try {
            //添加页码
            PdfPTable table = new PdfPTable(3);
            //设置表格的宽度有两种方法，分别如下
            table.setTotalWidth(new float[]{80, 12, 40});
            //将宽度锁定
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(12);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            PdfPCell cell = new PdfPCell(new Paragraph("第 " + writer.getPageNumber() + " 页/共 ", font));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            PdfPCell cell1 = new PdfPCell(Image.getInstance(template));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell1);
            PdfPCell cell2 = new PdfPCell(new Paragraph("页", font));
            cell2.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell2);
            table.writeSelectedRows(0, -1, 235, 50, writer.getDirectContent());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(template, Element.PARAGRAPH, new Phrase(String.valueOf(writer.getPageNumber())), 2, 2, 0);
    }
}
