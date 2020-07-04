package com.kkrepo.blog.configuration.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WangRuofei
 * @create 2020-06-30 6:10 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    public CustomHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            InputStream inputStream = request.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer)) > 0) {
                    baos.write(buffer,0,read);
                }
                body = baos.toByteArray();
            }
        } catch (IOException ex) {
            log.error("Error reading the request body...");
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

        ServletInputStream inputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return inputStream;
    }

    // 对外提供读取流的方法
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
