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
statetransition.table.state_event_table_name = state_event
``` 
state_eventの部分に任意のテーブル名を記述してください  
文字列の前後にダブルクオートはつけないでください！

### テーブルの作成
上記で定義したテーブルを作成してください
#### テーブル定義例:sqlite
```sql
CREATE TABLE state_event (
    state_event_id TEXT NOT NULL UNIQUE,
    id TEXT, 
    state_type TEXT NOT NULL, 
    state TEXT NOT NULL,  
    state_event_date_time DATE NOT NULL,
    event_date_time DATE NOT NULL,
    is_latest NUMBER
);
create index i_state_event_id on state_event(state_event_id);
create index i_id on state_event(id);
create index i_state_type on state_event(state_type);
create index i_state_event_date_time on state_event(state_event_date_time);
create index i_event_date_time on state_event(event_date_time);
create index i_is_latest on state_event(is_latest);

```
