import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadedServer implements Runnable{

    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected String       status = "<i>test</i>";
    protected Device      myDevice = null;

    public SingleThreadedServer(int port, Device d){
        this.serverPort = port;
        this.myDevice = d;
    }

    public void pullStatus(String s){
        this.status = s;
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();

        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            try {
                processClientRequest(clientSocket);
            } catch (IOException e) {
                //log exception and go on to next request.
            }
        }

        System.out.println("Server Stopped.");
    }

    private void processClientRequest(Socket clientSocket) throws IOException {
        InputStream  input  = clientSocket.getInputStream();
        OutputStream output = clientSocket.getOutputStream();
        long time = System.currentTimeMillis();
        String InputParams="";

        //IoUtils.copy(input, System.out);
        InputParams = IoUtils.getString(input);
        //System.out.println("InputParams: " + InputParams);
        switch (InputParams) {
        case "addcoin":
            System.out.println("action: eatCoin();");
            if (myDevice instanceof TollSim) ((TollSim)myDevice).eatCoin();
            break;
        case "":
            System.out.println("display();");
            break;
        default:
            System.out.println("unknown action");
        }

        output.write(("HTTP/1.1 200 OK\n\n"+
                      "<html>"+
                      "<head><title>TollSimJAVA</title>"+
                      "</head><body><pre>" +
                      this.status +
                      "</pre></body></html>").getBytes());

        input.close();
        output.close();
        //String myString = IOUtils.toString(input, "UTF-8");
        //System.out.println("Request = " + myString);
        //System.out.println("Request processed: " + time);
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }
}