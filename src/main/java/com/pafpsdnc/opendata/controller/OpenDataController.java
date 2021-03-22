package com.pafpsdnc.opendata.controller;

import com.pafpsdnc.opendata.model.OpenData;
import com.pafpsdnc.opendata.repository.OpenDataRepository;
import com.pafpsdnc.opendata.exception.OpenDataNotFound;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/opendata")
public class OpenDataController {
    @Autowired
    private OpenDataRepository openDataRepository;

    @Autowired
    private DataSource dataSource;

    private final Logger logger = LoggerFactory.getLogger(OpenDataController.class);

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OpenData findById(@PathVariable Integer id) throws OpenDataNotFound {
        OpenData openData = openDataRepository.findById(id).orElseThrow(OpenDataNotFound::new);
        String log = "Show : " + openData.toString();
        logger.trace(log);

        return openData;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<OpenData> findOpenData() {
        String log = "Json with all opendata";
        logger.trace(log);

        return openDataRepository.findAll();
    }

    @PostMapping("/pdf")
    @ResponseStatus(HttpStatus.OK)
    public void openDataPdf(HttpServletResponse response, @RequestBody Map<String, Object> responseData) throws JRException, SQLException, IOException {
        InputStream openDataStream = new ClassPathResource("opendata.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(openDataStream);

        String log = "PDF with all opendata";
        if (responseData.containsKey("ids")) {
            ArrayList<Integer> idsList = (ArrayList<Integer>) responseData.get("ids");
            if (!idsList.isEmpty()) {
                String ids = idsList.stream().distinct().sorted().map(String::valueOf).collect(Collectors.joining(","));
                log = "PDF of opendata : " + ids;
            }
        }

        logger.trace(log);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, responseData, dataSource.getConnection());
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment; filename=\"opendata.pdf\"");
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public OpenData createOpenData(@RequestBody Map<String, Object> responseData) {
        OpenData openData = new OpenData();
        openData.setValue((String) responseData.get("value"));
        openData.setUniqId((Integer) responseData.get("uniqId"));
        openData.setName((String) responseData.get("name"));

        openDataRepository.save(openData);

        String log = "Create : " + openData.toString();
        logger.trace(log);

        return openData;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OpenData updateOpenData(@PathVariable("id") Integer id, @RequestBody Map<String, Object> responseData) throws OpenDataNotFound {
        OpenData openData = openDataRepository.findById(id).orElseThrow(OpenDataNotFound::new);

        openData.setValue((String) responseData.get("value"));
        openData.setUniqId((Integer) responseData.get("uniqId"));
        openData.setName((String) responseData.get("name"));
        openDataRepository.save(openData);

        String log = "Update : " + openData.toString();
        logger.trace(log);

        return openData;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteOpenData(@PathVariable("id") Integer id) throws OpenDataNotFound {
        OpenData openData = openDataRepository.findById(id).orElseThrow(OpenDataNotFound::new);
        openDataRepository.delete(openData);

        String log = "Delete : " + openData.toString();
        logger.trace(log);

        return "Les données avec l'id " + openData.getUniqId() + " ont bien été supprimées";
    }
}
