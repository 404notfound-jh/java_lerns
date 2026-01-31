# TCP connection

## 目的
TCP接続の基本的な流れと挙動を小さく検証する。

## 何がわかるか
- TCPクライアントが接続できる条件
- 送受信の基本的な流れ
- 例外発生時の挙動（接続失敗・切断など）

## 構成
- `src/TcpFlowClient.java`: TCPクライアントのサンプル
- `scripts/remoteProc.ps1`: 接続先/検証用の補助スクリプト

## 実行方法（例）
```bash
# 例: javacでコンパイルして実行
javac src/TcpFlowClient.java
java -cp src TcpFlowClient
```

## 使い方メモ
- 接続先: `host=localhost`, `port=5000`（引数で上書き可）
  - 例: `java -cp src TcpFlowClient 127.0.0.1 5000`
- タイムアウト: connect 5秒 / read 5秒
- 入出力の形式: UTF-8 テキスト
  - 送信: `Hello TCP!\n`
  - 受信: 1回の `read` で取得した分を表示

## 観察ログ
- ___
- ___

## 参考
- ___
