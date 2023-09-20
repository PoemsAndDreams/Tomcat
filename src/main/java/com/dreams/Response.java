package com.dreams;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Response implements HttpServletResponse {
    private int status = 200;
    private String message = "OK";
    private Map<String,String> headers = new HashMap<>();

    //常量，空格
    private byte SP = ' ';
    //常量，回车
    private byte CR = '\r';
    //常量，换行
    private byte LF = '\n';
    //每个response对应一个request
    private Request request;
    private OutputStream socketOutputStream;

    //对于一个响应体只要一个响应体
    private ResponseServletOutputStream responseServletOutputStream = new ResponseServletOutputStream();

    public Response(Request request) {
        this.request = request;
        try {
            this.socketOutputStream = request.getSocket().getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public boolean containsHeader(String name) {
        return false;
    }

    @Override
    public String encodeURL(String url) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String url) {
        return null;
    }

    @Override
    public String encodeUrl(String url) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return null;
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {

    }

    @Override
    public void sendError(int sc) throws IOException {

    }

    @Override
    public void sendRedirect(String location) throws IOException {

    }

    @Override
    public void setDateHeader(String name, long date) {

    }

    @Override
    public void addDateHeader(String name, long date) {

    }

    @Override
    public void setHeader(String name, String value) {

    }

    public void addHeader(String s1, String s2) {
        headers.put(s1,s2);
    }

    @Override
    public void setIntHeader(String name, int value) {

    }

    @Override
    public void addIntHeader(String name, int value) {

    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStatus(int status,String message) {
        status = status;
        message = message;
    }


    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    //响应
    public ResponseServletOutputStream getOutputStream() {
        return responseServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return null;
    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }


    public void send(){
        //发送响应

        try {
            //响应行
            sendResponseLine();
            //响应头
            sendResponseHeader();
            //响应体
            sendResponseBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResponseBody() throws IOException {
        socketOutputStream.write(getOutputStream().getBytes());
    }

    private void sendResponseHeader() throws IOException {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            socketOutputStream.write(key.getBytes());
            socketOutputStream.write(":".getBytes());
            socketOutputStream.write(value.getBytes());
            socketOutputStream.write(CR);
            socketOutputStream.write(LF);
        }
        socketOutputStream.write(CR);
        socketOutputStream.write(LF);
    }

    private void sendResponseLine() throws IOException {
        socketOutputStream.write(request.getProtocol().getBytes());
        socketOutputStream.write(SP);
        socketOutputStream.write(status);
        socketOutputStream.write(SP);
        socketOutputStream.write(message.getBytes());
        socketOutputStream.write(CR);
        socketOutputStream.write(LF);
    }
}
