package playground.nio;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {

    public static void main(String[] args) throws IOException {
        readFile("./README.md");
    }

    public static void readFile(String filename) throws IOException {
        FileChannel channel = FileChannel.open(Path.of(filename),
                StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        for(int status = 0; status != -1; status = channel.read(buffer)) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.println(buffer.get());
            }
            buffer.compact();
        }
        channel.close();
    }

    public static void socketClient() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("localhost", 8086));
    }

    public static void socketServer() throws IOException {
        ServerSocket socket = new ServerSocket(8086);
        
    }
}
