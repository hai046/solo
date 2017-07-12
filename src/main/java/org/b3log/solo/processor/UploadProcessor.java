package org.b3log.solo.processor;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.JSONRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import jodd.upload.FileUpload;
import jodd.upload.MultipartStreamParser;
import jodd.upload.impl.MemoryFileUploadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by haizhu on 2017/7/12.
 * <p>
 * haizhu12345@gmail.com
 */
@RequestProcessor
public class UploadProcessor {

    private static MemoryFileUploadFactory memoryFileUploadFactory = new MemoryFileUploadFactory();

    private final static int MAX_SIZE = 5 * 1024 * 1024;//5M

    private static String BASE_DIR = "images/upload";

    private static Logger logger = LoggerFactory.getLogger(UploadProcessor.class);

    @RequestProcessing(value = "/upload", method = HTTPRequestMethod.POST)
    public void upload(final HTTPRequestContext context) {
        final HttpServletRequest request = context.getRequest();

        final JSONRenderer renderer = new JSONRenderer();

        context.setRenderer(renderer);
        final JSONObject jsonObject = new JSONObject();

        request.getSession().getServletContext();
        String contentType = request.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) try {
            ServletInputStream inputStream = request.getInputStream();

            memoryFileUploadFactory.setMaxFileSize(MAX_SIZE);
            MultipartStreamParser multipartStreamParser = new MultipartStreamParser(
                    memoryFileUploadFactory);
            multipartStreamParser.parseRequestStream(inputStream, "UTF-8");
            String key = multipartStreamParser.getParameter("key");
            System.out.println(key);
            //            JSONObject result = new JSONObject();
            //            result.put("key", key);
            JSONArray files = new JSONArray();
            jsonObject.put("files", files);
            Iterator<String> iterator = multipartStreamParser.getFileParameterNames().iterator();
            while (iterator.hasNext()) {
                String name = iterator.next();
                FileUpload file = multipartStreamParser.getFile(name);
                File saveFile = new File(BASE_DIR, key);
                FileUtils.writeByteArrayToFile(saveFile, file.getFileContent());
                files.put(name);

                jsonObject.put("key", saveFile.getPath());

                logger.info("save {}", saveFile.getAbsolutePath());
                break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //        if (request instanceof MultipartRequest) {
        //            MultipartFile file = ((MultipartRequest) request).getMultiFileMap().getFirst(param);
        //            if (file != null && !file.isEmpty()) {
        //                try {
        //                    imageId = imageService.createImage(JiemoRequestContext.getUserId(),
        //                            file.getBytes(), needPngThumb);
        //                } catch (Exception e) {
        //                    logger.error("图片上传失败", e);
        //                }
        //            }
        //        }

        renderer.setJSONObject(jsonObject);

    }
}
