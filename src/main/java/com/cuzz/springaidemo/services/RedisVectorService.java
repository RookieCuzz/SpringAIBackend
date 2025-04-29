package com.cuzz.springaidemo.services;

import com.cuzz.springaidemo.models.dto.DocumentDTO;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RedisVectorService {


    @Value("classpath:/prompt/data.txt")
    private Resource textResource;

    @jakarta.annotation.Resource
    VectorStore vectorStoreRedis;


    /**
     *
     * @param url 文件的缓存路径
     * @param name 知识库名称
     * @return
     */

    public boolean addDocument2Store(String url,String name){
        Resource resource = new FileSystemResource(url);

        String string = readContent(resource);
        string = "这是你的"+name+"知识库  "+string;
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression filterExpression = b.eq("knowledge", name).build();
        Document document=new Document(string,Map.of("knowledge",name));
        List<Document> documents = new ArrayList<>();
        System.out.println(documents);
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        documents.add(document);
        vectorStoreRedis.add(tokenTextSplitter.apply(documents));
        return true;
    }
//    public String readContent(Resource resource) {
//        try (InputStream inputStream = resource.getInputStream();
//             InputStreamReader reader = new InputStreamReader(inputStream);
//             BufferedReader bufferedReader = new BufferedReader(reader)) {
//            // 将所有行读取并以换行符拼接成一个字符串
//            return bufferedReader.lines().collect(Collectors.joining("\n"));
//        } catch (IOException e) {
//            throw new UncheckedIOException("读取资源内容失败", e);
//        }
//    }

    public String readContent(Resource resource) {
        try {
            String filename = resource.getFilename();
            if (filename == null) {
                throw new IllegalArgumentException("资源没有文件名");
            }

            if (filename.endsWith(".txt")) {
                return readTextFile(resource);
            } else if (filename.endsWith(".docx")) {
                return readDocxFile(resource);
            } else if (filename.endsWith(".md")) {
                return readMarkdownFile(resource);
            } else if (filename.endsWith(".yml") || filename.endsWith(".yaml")) {
                return readYamlFile(resource);
            } else if (filename.endsWith(".pdf")) {
                return readPdfFile(resource);
            } else if (filename.endsWith(".xlsx") || filename.endsWith(".xls")) {
                return readExcelFile(resource);
            } else {
                throw new UnsupportedOperationException("不支持的文件格式: " + filename);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("读取资源内容失败", e);
        }
    }

    // 读取 .txt 文件
    private String readTextFile(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        }
    }

    // 读取 .docx 文件
    private String readDocxFile(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream();
             XWPFDocument document = new XWPFDocument(inputStream)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            return paragraphs.stream()
                    .map(XWPFParagraph::getText)
                    .collect(Collectors.joining("\n"));
        }
    }

    // 读取 .md 文件
    private String readMarkdownFile(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String markdown = bufferedReader.lines().collect(Collectors.joining("\n"));
            return markdown;
        }
    }

    // Markdown 转纯文本
    private String convertMarkdownToPlainText(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        return FlexmarkHtmlConverter.builder().build().convert(document.toString());
    }
    // 读取 .yml / .yaml 文件
    private String readYamlFile(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            Yaml yaml = new Yaml();
            Map<String, Object> yamlData = yaml.load(inputStream);
            return yamlData.toString();
        }
    }

    // 读取 .pdf 文件
    private String readPdfFile(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    // 读取 .xls / .xlsx 文件
    private String readExcelFile(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream();
             Workbook workbook = resource.getFilename().endsWith(".xlsx") ?
                     new XSSFWorkbook(inputStream) : new HSSFWorkbook(inputStream)) {

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        result.append(getCellValue(cell)).append("\t");
                    }
                    result.append("\n");
                }
            }
            return result.toString();
        }
    }

    // 获取 Excel 单元格内容
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public DocumentDTO getDocumentDTOByFile(String url,String name){
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDocumentName(name);
        Resource resource = new FileSystemResource(url);
        System.out.println("输出"+readContent(resource));
        documentDTO.setContent(readContent(resource));
        return documentDTO;
    }
    public DocumentDTO getDocumentDTOByFile(String url,Long id){
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(Math.toIntExact(id));
        Resource resource = new FileSystemResource(url);
        documentDTO.setContent(readContent(resource));
        return documentDTO;
    }

    /**
     *
     * @param documentDTO 需要存储的知识库内容
     * @return
     */

    public boolean addDocument2Store(DocumentDTO documentDTO){
        List<Document> documents = new ArrayList<>();
        Document document = new Document(documentDTO.getContent(), Map.of("knowledge",documentDTO.getDocumentName()));
        documents.add(document);
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        List<Document> mDocuments = tokenTextSplitter.apply(documents);
        mDocuments.forEach(item->{
            System.out.println(item.getId());
            System.out.println(item.getMetadata().get("knowledge"));
        });
        vectorStoreRedis.add(mDocuments);
        return true;
    }


    public boolean deleteDocumentByName(String name){

        // 使用 FilterExpressionBuilder 构建过滤表达式
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        Filter.Expression filterExpression = b.eq("knowledge", name).build();
        System.out.println("尝试删除"+name);
        vectorStoreRedis.delete(filterExpression);
        return true;

    }


}
