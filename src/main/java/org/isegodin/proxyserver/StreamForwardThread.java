package org.isegodin.proxyserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author i.segodin
 */
public class StreamForwardThread extends Thread {

    private Executor executor;

    private InputStream source;
    private OutputStream target;

    public StreamForwardThread(Executor executor, InputStream source, OutputStream target) {
        this.executor = executor;
        this.source = source;
        this.target = target;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[65536];
        int size;
        try {
            read: {
                while ((size = source.read(buffer)) != -1) {
                    target.write(buffer, 0, size);
                }
                target.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.finished();
        }
    }
}
