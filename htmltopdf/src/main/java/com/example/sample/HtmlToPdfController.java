package com.example.sample;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HtmlToPdfController {

	@GetMapping("/")
	public String index() {
		return "editor";
	}

	@PostMapping("/generatePdf")
	public void generatePdf(@RequestParam("content") String content, HttpServletResponse response)
			throws DocumentException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(content);
		renderer.layout();
		renderer.createPDF(baos);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=document.pdf");
		response.setContentLength(baos.size());

		ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
		org.apache.commons.io.IOUtils.copy(bis, response.getOutputStream());
		response.flushBuffer();
	}
}