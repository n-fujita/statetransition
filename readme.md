# statetransition

状態遷移をDBで管理するオープンソースライブラリ

## 目的
ドメインごとに状態遷移を作ると時間がかかるので、1箇所で管理できるようにしたい

## コンセプト
- DBの種類に依存しない
  - 対象: oracle, mysql, sqlite (文字コードはUTF-8)
  - DBの定義は使う側に記述してもらう
- 堅牢に状態遷移する
  - 遷移できない状態への遷移をエラーにする
  - javaのコード側で堅牢にする。テーブルが汎用的なため
- Spring, Mybatisが前提

## セットアップ

### gradle.build
TODO

### 管理するテーブル名を定義する

resources/application.properties
```
statetransition.table.state_table_name = state
statetransition.table.state_event_table_name = state_event
``` 
state, state_eventの部分に任意のテーブル名を記述してください  
文字列の前後にダブルクオートはつけないでください！

### テーブルの作成
上記で定義したテーブルを作成してください
#### テーブル定義例:sqlite
```sql
CREATE TABLE state (
  state_event_id TEXT UNIQUE,
  id TEXT, 
  state_type TEXT, 
  state TEXT,  
  state_event_date_time DATE, 
  event_date_time DATE
);

CREATE TABLE state_event (
  state_event_id TEXT UNIQUE,
  id TEXT, 
  state_type TEXT, 
  state TEXT,  
  state_event_date_time DATE, 
  event_date_time DATE,
  UNIQUE(id, state_type)
);



```
