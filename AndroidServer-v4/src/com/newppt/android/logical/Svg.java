package com.newppt.android.logical;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.newppt.android.entity.MyPath;

public class Svg {

	private String fileName = null; 			// PPT文件名
	private String Path = null;					 // PPT路径
	public int currentPage, page;				 // 当前页和总页数
	private Slide slide[]; 						// 存放PPT的Slides
	private XSLFSlide slidePPTx[]; 				// 存放PPTX的Slides
	private SlideShow ppt;
	private XMLSlideShow pptx;
	public Document doc; 						// 存放PPT或pptx的XML数据结构
	private SVGGraphics2D graphics = null;
	private Dimension pgsize = null;			 // PPT页面尺寸
	
	private byte[] imageBuffer;
	private byte[] fileBuffer; 

	public Svg(String fileName, String Path) {
		// TODO Auto-generated constructor stub
		this.fileName = fileName;
		this.Path = Path;
		getPPTSlides();
	}

	/**
	 * 提取文件后缀
	 * 
	 * @return String
	 */
	private String getExt() {
		File f = new File(Path);
		String fileName = f.getName();
		String fileExtension = fileName
				.substring(fileName.lastIndexOf(".") + 1);
		System.out.println(fileExtension);
		return fileExtension;
	}

	/**
	 * 获取PPT文件Slides
	 * 
	 */
	protected void getPPTSlides() {
		// getPath();
		currentPage = 1;
		String prefix = getExt();

		// 判断
		if (prefix.equalsIgnoreCase("ppt")) {
			try {
				System.out.println(prefix);
				FileInputStream is = new FileInputStream(Path);
				FileInputStream inputStream = new FileInputStream(Path);
				
				fileToBytes(inputStream);// File To bytes
				
				ppt = new SlideShow(is);
				is.close();
				pgsize = ppt.getPageSize();
				slide = ppt.getSlides();
				page = slide.length;

				for (int i = 0; i < slide.length; i++) {
					TextRun[] truns = slide[i].getTextRuns();  //得到PPT中文字信息
					for (int k = 0; k < truns.length; k++) {
						RichTextRun[] rtruns = truns[k].getRichTextRuns();  //得到文字的格式
						for (int l = 0; l < rtruns.length; l++) {
							rtruns[l].setFontIndex(1);
							rtruns[l].setFontName("宋体");
						}
					}
				}

			} catch (Exception e) {
				e.getStackTrace();
			}
		} else {
			try {
				System.out.println(prefix);
				FileInputStream is = new FileInputStream(Path);
				
                FileInputStream inputStream = new FileInputStream(Path);
				
				fileToBytes(inputStream);// File To bytes
				
				pptx = new XMLSlideShow(is);
				is.close();
				pgsize = pptx.getPageSize();
				slidePPTx = pptx.getSlides();
				page = slidePPTx.length;

				for (int i = 0; i < slidePPTx.length; i++) {
					setFont(slidePPTx[i]);

				}

			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}

	private void fileToBytes(FileInputStream is) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] byffer = new byte[1024];
		int length = -1;
//		System.out.println("------1");
		while ((length = is.read(byffer)) != -1) {
			byteArrayOutputStream.write(byffer, 0, length);
//			System.out.println("------4");
		}
//		System.out.println("------2");

		byteArrayOutputStream.close();
		
//		System.out.println("------3");
		
		fileBuffer = byteArrayOutputStream.toByteArray();
		System.out.println("lol"+fileBuffer.length);
	}
	
	public byte[] getFileBytes() {
		return fileBuffer;
	}

	/**
	 * 设置pptx字体
	 * 
	 * @param slide
	 */
	private void setFont(XSLFSlide slide) {
		for (XSLFShape shape : slide.getShapes()) {
			if (shape instanceof XSLFTextShape) {
				for (XSLFTextParagraph paragraph : ((XSLFTextShape) shape)) {
					List<XSLFTextRun> truns = paragraph.getTextRuns();
					for (XSLFTextRun trun : truns) {
						trun.setFontFamily("宋体");
					}
				}
			}

		}
	}

	/**
	 * 设置画笔
	 * 
	 * @return
	 */
	protected SVGGraphics2D setGraphics() {
		DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
		doc = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);

		graphics = new SVGGraphics2D(doc);
		graphics.setSVGCanvasSize( pgsize);
		graphics.setPaint(Color.white);
		return graphics;
	}

	/**
	 * 设置以及返回doc
	 * 
	 * @param cur
	 * @return
	 */
	public Document getAndSetDoc(int cur) {
		this.currentPage = cur;
		String prefix = getExt();
		setGraphics();
		Element svgRoot;
		if (prefix.equalsIgnoreCase("ppt")) {
			slide[currentPage - 1].draw(graphics);
			svgRoot = doc.getDocumentElement();
			graphics.getRoot(svgRoot);
//			createPPTJpg(doc);
			createPPTJpg2(currentPage - 1);
	
		} else {
			slidePPTx[currentPage - 1].draw(graphics);
			svgRoot = doc.getDocumentElement();
			graphics.getRoot(svgRoot);
			
			createPPTXJpg(currentPage - 1);
			
		}
		return doc;
	}

	/**
	 * 创建pptx当前页转化为jpg
	 * 
	 * @param cur
	 *            当前页
	 */
	public void createPPTXJpg(int cur) {

		MyPath mypath = new MyPath();
		File file = new File(mypath.returnRootPath());
		if (!file.exists() && !file.isDirectory()) {
			try {
				file.mkdir();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		BufferedImage img = new BufferedImage(pgsize.width, pgsize.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D jpgGraphics = img.createGraphics();
		// clear the drawing area
		jpgGraphics.setPaint(Color.white);
		jpgGraphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

		// render
		slidePPTx[cur].draw(jpgGraphics);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			javax.imageio.ImageIO.write(img, "jpg", out);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		imageBuffer = out.toByteArray();
		System.out.println("lolo"+imageBuffer.length);

	}
	
	public void createPPTJpg2(int cur) {
		MyPath mypath = new MyPath();
		File file = new File(mypath.returnRootPath());
		if (!file.exists() && !file.isDirectory()) {
			try {
				file.mkdir();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		BufferedImage img = new BufferedImage(pgsize.width, pgsize.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D jpgGraphics = img.createGraphics();
		// clear the drawing area
		jpgGraphics.setPaint(Color.white);
		jpgGraphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

		// render
		slide[cur].draw(jpgGraphics);
//		FileOutputStream out;
//		try {
//			out = new FileOutputStream(mypath.returnJpgPath());
//			javax.imageio.ImageIO.write(img, "jpg", out);
//			out.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
      ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			javax.imageio.ImageIO.write(img, "jpg", out);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		imageBuffer = out.toByteArray();
		System.out.println("lolo"+imageBuffer.length);
	}
	
	public byte[] getImageBytes() {
		return imageBuffer;
	}

	/**
	 * 创建ppt当前页转化为jpg
	 * 
	 * @param document
	 */
	public void createPPTJpg(Document document) {
		// Create a JPEGTranscoder and set its quality hint.
        MyPath myPath = new MyPath();
		File file = new File(myPath.returnRootPath());
		if (!file.exists() && !file.isDirectory()) {
			try {
				file.mkdir();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JPEGTranscoder t = new JPEGTranscoder();
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(1.0));

		try {
			TranscoderInput input = new TranscoderInput(document);
			OutputStream ostream = new FileOutputStream(myPath.returnJpgPath());
			TranscoderOutput output = new TranscoderOutput(ostream);

			// Perform the transcoding.
			t.transcode(input, output);
			ostream.flush();

			ostream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
