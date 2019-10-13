package hello;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }


    @GetMapping("/poi")
    public ResponseEntity<InputStreamResource> poi() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet 1") ;
        //iterating r number of rows
        for (int r=0;r < 5; r++ )
        {
            Row row = sheet.createRow(r);
            //iterating c number of columns
            for (int c=0;c < 5; c++ )
            {
                Cell cell = row.createCell(c);
                cell.setCellValue("Cell "+r+" "+c);
            }
        }

        return getResponseEntityForWorkbook("worksheet.xlsx", workbook);
    }

    private static ResponseEntity<InputStreamResource> getResponseEntityForWorkbook(String filename, Workbook workbook) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream( outputStream.toByteArray() ));

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", filename);
        return ResponseEntity.ok().contentLength(outputStream.size()).contentType(MediaType.APPLICATION_OCTET_STREAM).header(headerKey, headerValue).body(inputStreamResource);
    }

    @GetMapping( "/headshot")
    public ResponseEntity headshot() throws IOException {
        String filePath = "static/headshot.jpg";

        InputStream inputStream = ClassLoader.getSystemResourceAsStream(filePath);
//        InputStream inputStream = new FileInputStream(new File(filePath));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentLength(Files.size(Paths.get(filePath));
//        return new ResponseEntity(inputStreamResource, headers, HttpStatus.OK);
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", "headshot.jpg");
//        response.setHeader(headerKey, headerValue);
        return ResponseEntity.ok().contentLength(inputStream.available()).contentType(MediaType.APPLICATION_OCTET_STREAM).header(headerKey, headerValue).body(inputStreamResource);
    }
}
