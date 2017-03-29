package com.hyperchain.controller.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Created by martin on 2017/3/10.
 */
public class NIOReader {
    /**
     * 缓存大小，必须大于一次写入的数据量
     */
    private int bufferSize;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 换行符
     */
    private String entryString = "\n";

    private File file;
    private FileChannel fileChannel;

    private String bufferString;
    private boolean isRead = false;
    private boolean isReadDone = false;
    private boolean isBufferStringEmpty = true;
    private int fromIndex = 0;
    private int endIndex = 0;
    private String remainString = "";

    /**
     * 构造函数
     *
     * @param bufferSize 缓存大小
     * @param filePath   文件路径
     */
    public NIOReader(int bufferSize, String filePath) throws FileNotFoundException {
        this.bufferSize = bufferSize;
        this.filePath = filePath;
        file = new File(filePath);
        fileChannel = new RandomAccessFile(file, "rw").getChannel();

    }


    public String nextLine() throws IOException {
        endIndex = bufferString.indexOf(entryString, fromIndex);
        if (endIndex != -1) {
            String line = bufferString.substring(fromIndex, endIndex);
            bufferString = bufferString.substring(endIndex+1, bufferString.length());
            line = remainString + line;
            remainString = "";
            fromIndex = endIndex + 1;
            return line;
        } else {
            remainString += bufferString;
            bufferString = "";
            if (this.hasNextLine()) {
                fromIndex = 0;
                return nextLine();
            } else {
                return "";
            }
        }
    }

    public boolean hasNextLine() throws IOException {
        // 如果没有读过到缓存，则读入到缓存
        if (!isRead) {
            bufferStringRead();
            isRead = true;
        }
        isBufferStringEmpty = bufferString.isEmpty();
        // 如果文件读完且缓存为空，则没有数据可读了
        if (isReadDone && isBufferStringEmpty) {
            return false;
        }
        // 如果缓存空了，但是下一次文件还没读
        if (isBufferStringEmpty) {
            bufferStringRead();
            return this.hasNextLine();
        }
        // 只要缓存没有空
        return true;
    }

    private void bufferStringRead() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
        isReadDone = fileChannel.read(byteBuffer) == -1;
        byteBuffer.rewind();
        CharBuffer charBuffer = Charset.forName("UTF-8").decode(byteBuffer);
        bufferString = charBuffer.toString();
        byteBuffer.clear();
    }

    public String getEntryString() {
        return entryString;
    }

    public void setEntryString(String entryString) {
        this.entryString = entryString;
    }

}
