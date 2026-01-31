import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * TCP通信の基本的な流れを学ぶためのサンプルクラス。
 *
 * <p>このクラスでは、JavaでTCPコネクションを確立し、
 * データを送受信して切断するまでの一連の流れを確認できる。</p>
 *
 * <h3>TCP通信の流れ（Javaコードとの対応）</h3>
 * <ol>
 *   <li><b>名前解決</b><br>
 *       接続先のホスト名をIPアドレスに変換する。
 *       {@link InetAddress#getByName(String)}</li>
 *
 *   <li><b>ソケット生成</b><br>
 *       TCP通信用のソケットを作成する（まだ未接続）。
 *       {@code new Socket()}</li>
 *
 *   <li><b>TCPコネクション確立</b><br>
 *       {@link Socket#connect(SocketAddress, int)} を呼び出すと、
 *       OS内部でローカルポートの割り当てや
 *       3-wayハンドシェイクが行われ、接続が確立される。</li>
 *
 *   <li><b>データ送受信</b><br>
 *       接続が確立した後、
 *       {@link Socket#getOutputStream()} で送信（write）、
 *       {@link Socket#getInputStream()} で受信（read）を行う。</li>
 *
 *   <li><b>切断</b><br>
 *       {@link Socket#close()} によりTCPコネクションを切断する。
 *       このサンプルでは try-with-resources により自動的に close される。</li>
 * </ol>
 *
 * <p>
 * ポイント：
 * TCPの詳細な制御（ハンドシェイクなど）はJavaではなくOSが担当しており、
 * Javaコードでは主に「どのタイミングで connect / write / read / close を呼ぶか」
 * を理解することが重要。
 * </p>
 */
public class TcpFlowClient {

    public static void main(String[] args) {
        String host = args.length >= 1 ? args[0] : "localhost";
        int port = args.length >= 2 ? Integer.parseInt(args[1]) : 5000;

        String message = "Hello TCP!\n";

        try {
            // 1) 名前解決（DNS / hosts）
            InetAddress address = InetAddress.getByName(host);
            System.out.println("[DNS] host=" + host + " -> ip=" + address.getHostAddress());

            // 2) ソケット生成（未接続）
            try (Socket socket = new Socket()) {
                System.out.println("[SOCKET] created (not connected yet)");

                // read() の待ち時間（学習用）
                socket.setSoTimeout((int) Duration.ofSeconds(5).toMillis());

                // 3) 接続先（IP + ポート）を指定
                SocketAddress remote = new InetSocketAddress(address, port);

                // 4) TCP確立（connect）
                System.out.println("[CONNECT] connecting to " + remote + " ...");
                socket.connect(remote, (int) Duration.ofSeconds(5).toMillis());
                System.out.println("[CONNECT] connected! local=" + socket.getLocalSocketAddress()
                        + ", remote=" + socket.getRemoteSocketAddress());

                // 5) 送信（write）
                OutputStream out = socket.getOutputStream();
                byte[] sendBytes = message.getBytes(StandardCharsets.UTF_8);
                out.write(sendBytes);
                out.flush();
                System.out.println("[WRITE] sent " + sendBytes.length + " bytes");

                // 6) 受信（read）※相手が何も返さない場合はタイムアウト
                InputStream in = socket.getInputStream();
                byte[] buf = new byte[1024];

                System.out.println("[READ] waiting ...");
                int n = in.read(buf);
                if (n == -1) {
                    System.out.println("[READ] peer closed connection (EOF)");
                } else {
                    String received = new String(buf, 0, n, StandardCharsets.UTF_8);
                    System.out.println("[READ] received " + n + " bytes: " + received.replace("\n", "\\n"));
                }

                // 7) 切断（close）は try-with-resources により自動実行
                System.out.println("[CLOSE] done (socket will be closed automatically)");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
