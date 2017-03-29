package com.hyperchain.controller.base;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * Created by martin on 2017/3/9.
 */
public final class NIOWriter {

    private int bufferSize = 10 * 1024 * 1024;
    private String filePath;

    private File file;
    private FileChannel fileChannel;
    private ByteBuffer byteBuffer;


    /**
     * NIO写文件（默认缓存大小为10MB）
     *
     * @param filePath 文件路径
     * @throws IOException
     */
    public NIOWriter(String filePath) throws IOException {
        this.filePath = filePath;
        init();
    }

    /**
     * NIO写文件
     *
     * @param bufferSize 缓存大小，必须大于一次写入的数据量
     * @param filePath   文件路径
     * @throws IOException
     */
    public NIOWriter(int bufferSize, String filePath) throws IOException {
        this.bufferSize = bufferSize;
        this.filePath = filePath;
        init();
    }

    private void init() throws IOException {
        // 新建一个文件
        file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        // 获取文件通道
        fileChannel = new RandomAccessFile(file, "rws").getChannel();
        // 新开一个缓存
        byteBuffer = ByteBuffer.allocateDirect(bufferSize);
    }

    /**
     * 清空文件
     *
     * @throws IOException
     */
    public void cleanFile() throws IOException {
        // 清空文件（防止文件已经存在）
        fileChannel.truncate(0);
    }

    /**
     * 写数据。本条数据不会直接写入文件，而是先放在缓存中，直到缓存满。
     *
     * @param line 要写入的字符串
     */
    public void write(String line) throws IOException {
        byte[] bytes = line.getBytes();
        int remainSpace = bufferSize - byteBuffer.position();
        // 如果缓存已经放不下新数据
        while (bytes.length > remainSpace) {
            // 截取
            byteBuffer.put(Arrays.copyOfRange(bytes, 0, remainSpace));
            // 翻转缓存
            byteBuffer.flip();
            // 将缓存写入文件
            fileChannel.write(byteBuffer);
            // 清空缓存
            byteBuffer.clear();
            // 剩下未放入缓存的数据将进入下一个循环
            bytes = Arrays.copyOfRange(bytes, remainSpace, bytes.length);
            // 重置缓存剩余空间
            remainSpace = bufferSize;
        }
        // 将一行数据放入缓存
        byteBuffer.put(bytes);
    }

    /**
     * 在文件末尾追加写
     *
     * @param line 要写入的字符串
     * @throws IOException
     */
    public void append(String line) throws IOException {
        fileChannel.position(fileChannel.size());
        this.write(line);
    }

    /**
     * 写完后的收尾工作，此方法必须被调用，否则可能丢失数据
     */
    public void writeDone() throws IOException {
        // 翻转缓存
        byteBuffer.flip();
        // 将缓存写入文件
        fileChannel.write(byteBuffer);
        // 清空缓存
        byteBuffer.clear();
        // 关闭文件通道
        fileChannel.close();
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
