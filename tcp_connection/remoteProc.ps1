$listener = [System.Net.Sockets.TcpListener]::new([System.Net.IPAddress]::Any, 5000)
$listener.Start()
"Listening on 0.0.0.0:5000"

while ($true) {
  $client = $listener.AcceptTcpClient()
  "Client connected: $($client.Client.RemoteEndPoint)"

  $stream = $client.GetStream()
  $reader = New-Object System.IO.StreamReader($stream)
  $writer = New-Object System.IO.StreamWriter($stream)
  $writer.AutoFlush = $true

  # 1行受け取って、そのまま返す（エコー）
  $line = $reader.ReadLine()
  "Received: $line"
  $writer.WriteLine("echo: $line")

  $client.Close()
}