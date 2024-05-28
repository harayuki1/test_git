package com.example.sample;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HtmlToPdfController {

	int count = 1;

	@GetMapping("/")
	public String index() {
		return "editor";
	}

	@PostMapping("/generatePdf")
	public void generatePdf(
			@RequestParam("content1") String content1,
			@RequestParam("content2") String content2,
			@RequestParam("content3") String content3,
			HttpServletResponse response) throws DocumentException, IOException {

		String fullContent = "<!DOCTYPE html>\n"
				+ "<html lang=\"ja\">\n"
				+ "\n"
				+ "<head>\n"
				+ "    <meta charset=\"UTF-8\"/>\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n"
				+ "    <title>testtemplate</title>\n"
				+ "    <style>\n"
				+ "        table {\n"
				+ "            width: 100%;\n"
				+ "            border-collapse: collapse;\n"
				+ "            border: 1px solid black; /* テーブルの枠線 */\n"
				+ "        }\n"
				+ "\n"
				+ "        table td {\n"
				+ "            padding: 10px; /* セルの内側の余白 */\n"
				+ "            vertical-align: top; /* 垂直方向の上揃え */\n"
				+ "            border: 1px solid black; /* セルの枠線 */\n"
				+ "        }\n"
				+ "\n"
				+ "        .textarea {\n"
				+ "            width: 100%;\n"
				+ "            font-size: 16px;\n"
				+ "            font-family: IPAexゴシック, sans-serif;\n"
				+ "        }\n"
				+ "\n"
				+ "        form {\n"
				+ "            margin-top: 20px;\n"
				+ "        }\n"
				+ "    </style>\n"
				+ "</head>\n"
				+ "\n"
				+ "<body>\n"
				+ "    <h1>test template</h1>\n"
				+ "    <form>\n"
				+ "        <table>\n"
				+ "            <tr>\n"
				+ "                <td>question1</td>\n"
				+ "                <td>" + content1 + "</td>\n"
				+ "            </tr>\n"
				+ "            <tr>\n"
				+ "                <td>question2</td>\n"
				+ "                <td>" + content2 + "</td>\n"
				+ "            </tr>\n"
				+ "            <tr>\n"
				+ "                <td>question3</td>\n"
				+ "                <td>" + content3 + "</td>\n"
				+ "            </tr>\n"
				+ "        </table>\n"
				+ "    </form>\n"
				+ "</body>\n"
				+ "\n"
				+ "</html>";

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();

		// フォント設定
		ITextFontResolver fontResolver = renderer.getFontResolver();
		fontResolver.addFont("src/main/resources/fonts/ipaexg.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

		renderer.setDocumentFromString(fullContent);
		renderer.layout();
		renderer.createPDF(baos);

		String document = "doc" + count + ".pdf";
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=" + document);
		response.setContentLength(baos.size());

		ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
		org.apache.commons.io.IOUtils.copy(bis, response.getOutputStream());
		response.flushBuffer();
		count++;

		System.out.println("PDF生成が完了しました。");
		//		@PostMapping("/generatePdf")
		//		public void generatePdf(
		//				@RequestParam("content") String[][] content,
		//				HttpServletResponse response) throws DocumentException, IOException {
		//
		//			StringBuilder fullContent = new StringBuilder();
		//			fullContent.append("<!DOCTYPE html>\n");
		//			fullContent.append("<html lang=\"ja\">\n");
		//			fullContent.append("<head>\n");
		//			fullContent.append("<meta charset=\"UTF-8\"/>\n");
		//			fullContent.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n");
		//			fullContent.append("<title>PDF生成結果</title>\n");
		//			fullContent.append("<style>\n");
		//			fullContent.append("body {\n");
		//			fullContent.append("    font-family: IPAexゴシック, sans-serif;\n");
		//			fullContent.append("    margin: 20px;\n");
		//			fullContent.append("}\n");
		//			fullContent.append(".question {\n");
		//			fullContent.append("    margin-bottom: 20px;\n");
		//			fullContent.append("}\n");
		//			fullContent.append(".question label {\n");
		//			fullContent.append("    font-weight: bold;\n");
		//			fullContent.append("}\n");
		//			fullContent.append(".submit-btn {\n");
		//			fullContent.append("    margin-top: 20px;\n");
		//			fullContent.append("}\n");
		//			fullContent.append("</style>\n");
		//			fullContent.append("</head>\n");
		//			fullContent.append("<body>\n");
		//			fullContent.append("<h1>PDF生成結果</h1>\n");
		//			fullContent.append("<form>\n");
		//
		//			for (int i = 1; i < content.length; i++) {
		//				fullContent.append("<div class=\"question\">\n");
		//				fullContent.append("<label for=\"question").append(i).append("\">大問").append(i).append(":</label>\n");
		//
		//				for (int j = 1; j < content[i].length; j++) {
		//					fullContent.append("<div class=\"sub-question\">\n");
		//					fullContent.append("<label for=\"question").append(i).append("_").append(j).append("\">問題").append(j)
		//							.append(":</label>\n");
		//					fullContent.append("<input type=\"text\" id=\"question").append(i).append("_").append(j)
		//							.append("\" name=\"question").append(i).append("_").append(j).append("\" value=\"")
		//							.append(content[i][j]).append("\">\n");
		//					fullContent.append("</div>\n");
		//				}
		//
		//				fullContent.append("</div>\n");
		//			}
		//
		//			fullContent.append("</form>\n");
		//			fullContent.append("</body>\n");
		//			fullContent.append("</html>");
		//
		//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//			ITextRenderer renderer = new ITextRenderer();
		//
		//			// フォント設定
		//			ITextFontResolver fontResolver = renderer.getFontResolver();
		//			fontResolver.addFont("src/main/resources/fonts/ipaexg.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		//
		//			renderer.setDocumentFromString(fullContent.toString());
		//			renderer.layout();
		//			renderer.createPDF(baos);
		//
		//			String document = "doc" + count + ".pdf";
		//			response.setContentType("application/pdf");
		//			response.setHeader("Content-Disposition", "attachment; filename=" + document);
		//			response.setContentLength(baos.size());
		//
		//			ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
		//			org.apache.commons.io.IOUtils.copy(bis, response.getOutputStream());
		//			response.flushBuffer();
		//			count++;
		//
		//			System.out.println("PDF生成が完了しました。");
		//		}
	}
}
